package com.asgab.service.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.asgab.core.pagination.Page;
import com.asgab.entity.BusinessOpportunity;
import com.asgab.entity.Client;
import com.asgab.entity.ClientContact;
import com.asgab.entity.ClientShare;
import com.asgab.entity.Log;
import com.asgab.entity.MergeTable;
import com.asgab.repository.AgencyMapper;
import com.asgab.repository.BusinessOpportunityMapper;
import com.asgab.repository.ClientContactMapper;
import com.asgab.repository.ClientMapper;
import com.asgab.repository.ClientShareMapper;
import com.asgab.service.account.AccountService;
import com.asgab.service.account.ShiroDbRealm.ShiroUser;
import com.asgab.service.log.LogService;
import com.asgab.service.management.CurrencyTypeService;
import com.asgab.service.management.IndustryTypeService;
import com.asgab.util.CommonUtil;
import com.asgab.util.JsonMapper;
import com.asgab.util.MapCompareUtil;
import com.asgab.util.Operate;
import com.asgab.util.SelectMapper;

@Component
@Transactional
public class ClientService {

  @Autowired
  private ClientMapper clientMapper;
  @Autowired
  private ClientContactMapper clientContactMapper;
  @Autowired
  private ClientShareMapper clientShareMapper;
  @Autowired
  private CurrencyTypeService currencyTypeService;
  @Autowired
  private IndustryTypeService industryTypeService;
  @Autowired
  private BusinessOpportunityMapper businessOpportunityMapper;
  @Autowired
  private AgencyMapper agencyService;
  @Autowired
  private AccountService accountService;

  // Json工具类
  private final static JsonMapper jsonMapper = new JsonMapper();

  // JSON比较key
  private final static String COMPARE_KEY_ZH = "log.client.review.keys_zh";

  private final static String COMPARE_KEY_EN = "log.client.review.keys_en";

  @Autowired
  private LogService logService;

  public Page<Client> search(Page<Client> page) {
    List<Client> clients = clientMapper.search(page.getSearchMap(), page.getRowBounds());
    int count = clientMapper.count(page.getSearchMap());
    page.setContent(clients);
    page.setTotal(count);
    return page;
  }

  public Page<Client> searchClients(Page<Client> page) {
    List<Client> clients = clientMapper.searchClients(page.getSearchMap(), page.getRowBounds());
    int count = clientMapper.countClients(page.getSearchMap());
    page.setContent(clients);
    page.setTotal(count);
    return page;
  }

  public Client get(Long id) {
    return clientMapper.get(id);
  }

  public void save(Client client) {

    client.setState("cli_saved");
    // 保存广告主
    clientMapper.save(client);

    // 添加广告主联系人
    addClientContacts(client);

    // 添加销售人员
    addClientShares(client.getSaleIds(), client.getId());

    clientMapper.update(client);
  }

  public void update(Client client) {

    if (0 == client.getWhether_channel()) {
      client.setChannel(null);
    }
    // 先删除销售人员,物理删除
    clientShareMapper.deleteByClientId(client.getId());
    // 再添加销售人员
    addClientShares(client.getSaleIds(), client.getId());
    // 先逻辑删除客户联系人
    clientContactMapper.deleteByClientId(client.getId());
    // 再添加客户联系人
    addClientContacts(client);
    // 修改广告主
    clientMapper.update(client);

  }

  public List<Client> getAdvertisers() {
    Map<String, Object> parameters = new HashMap<String, Object>();
    Subject subject = SecurityUtils.getSubject();
    if (!subject.hasRole("admin") && !subject.hasRole("superAdmin")) {
      Long id = ((ShiroUser) SecurityUtils.getSubject().getPrincipal()).id;
      // 如果是代理.
      if (((ShiroUser) SecurityUtils.getSubject().getPrincipal()).master_id != null) {
        id = ((ShiroUser) SecurityUtils.getSubject().getPrincipal()).master_id;
      }
      parameters.put("user_id", id);
    }
    return clientMapper.getAdvertisers(parameters);
  }

  public List<SelectMapper> getAdvertiserMappers() {
    return getAdvertiserMappers(null);
  }

  public List<SelectMapper> getAdvertiserMappers(Long clientId) {
    List<Client> clients = getAdvertisers();
    if (clientId != null) {
      clients.add(get(clientId));
    }
    List<SelectMapper> mappers = new ArrayList<SelectMapper>();
    for (int i = 0; i < clients.size(); i++) {
      mappers.add(new SelectMapper(clients.get(i).getId() + "", clients.get(i).getClientname(), String.valueOf(clients.get(i).getCurrency_id())));
    }
    return mappers;
  }

  /***
   * 添加广告主联系人
   * 
   * @param ccs 多个联系人信息
   * @param client_id 广告主ID
   */
  private void addClientContacts(Client saveClient) {

    if (saveClient.getContacts() != null && saveClient.getContacts().size() > 0) {
      boolean saved_first = false;
      for (int i = 0; i < saveClient.getContacts().size(); i++) {
        ClientContact thisCC = saveClient.getContacts().get(i);
        if (thisCC.getContact_person() != null) {
          // index[0]存放在clients中
          if (!saved_first) {
            saved_first = true;
            saveClient.setContact_person(thisCC.getContact_person());
            saveClient.setEmail(thisCC.getEmail());
            saveClient.setPosition(thisCC.getPosition());
            saveClient.setPhone(thisCC.getPhone());
          } else {
            thisCC.setClient_id(saveClient.getId());
            thisCC.setCreated_at(new Date());
            clientContactMapper.save(thisCC);
          }
        }
      }
    }

  }

  /***
   * 添加userId和clientId的关系
   * 
   * @param userIds 销售人员IDs
   * @param client_id 广告主ID
   */
  private void addClientShares(String userIds, long client_id) {
    ClientShare cs = null;
    if (StringUtils.isNotBlank(userIds)) {
      for (String share_id : userIds.split(",")) {
        if (!"0".equals(share_id)) {
          cs = new ClientShare();
          cs.setClient_id(client_id);
          cs.setShare_id(Long.parseLong(share_id));
          clientShareMapper.save(cs);
        }
      }
    }
  }

  public void delete(Long id) {
    clientMapper.delete(id);
  }

  public List<Client> getClientsByIdList(List<Long> idList) {
    return clientMapper.getClientsByIdList(idList);
  }

  public void setSelect(HttpServletRequest request) {
    setSelect(request, new Client());
  }

  public void setSelect(HttpServletRequest request, Client client) {
    LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
    String lang = localeResolver.resolveLocale(request).getLanguage();
    Map<String, Object> searchMap = new HashMap<String, Object>();
    searchMap.put("lang", lang);
    request.setAttribute("currencyTypes", currencyTypeService.getOptions(null));
    request.setAttribute("industryTypes", industryTypeService.getOptions(searchMap));
    request.setAttribute("agencys", agencyService.search(null));
    request.setAttribute("users", accountService.getAllXMOUser());
    request.setAttribute("states", "EN".equalsIgnoreCase(lang) ? client.getSTATES_EN() : client.getSTATES_ZH());
  }

  public void resetClientContact(Client client) {
    ClientContact first = new ClientContact();
    first.setContact_person(client.getContact_person());
    first.setPhone(client.getPhone());
    first.setEmail(client.getEmail());
    first.setPosition(client.getPosition());
    client.getContacts().add(first);
    Map<String, Object> searchMap = new HashMap<String, Object>();
    searchMap.put("client_id", client.getId());
    client.getContacts().addAll(clientContactMapper.search(searchMap));
  }

  public void resetClientContacts(List<Client> clients) {
    if (clients == null || (clients != null && clients.size() == 0)) {
      return;
    }
    List<Long> clientIds = new ArrayList<Long>();
    for (Client client : clients) {
      clientIds.add(client.getId());
      ClientContact first = new ClientContact();
      first.setContact_person(client.getContact_person());
      first.setPhone(client.getPhone());
      first.setEmail(client.getEmail());
      first.setPosition(client.getPosition());
      client.getContacts().add(first);
    }
    List<ClientContact> clientContacts = clientContactMapper.getClientContactsByClientIdList(clientIds);
    if (clientContacts != null && clientContacts.size() > 0) {
      for (Client client : clients) {
        for (ClientContact contact : clientContacts) {
          if (client.getId().equals(contact.getClient_id())) {
            client.getContacts().add(contact);
          }
        }
      }
    }
  }

  /***
   * 合并广告主
   */
  public void merge(Map<String, Object> parameters) {
    // 页面上的结果数据
    Client keepClient = (Client) parameters.get("keepClient");
    String copy_contact_client_id = (String) parameters.get("copy_contact_client_id");
    String keepId = (String) parameters.get("keepId");
    String mergeIds = (String) parameters.get("mergeIds");
    // 数据库数据
    Client beforeClient = get(Long.parseLong(keepId));
    String refreshDataRange = (String) parameters.get("refreshDataRange");

    Date now = new Date();

    // 废弃合并后的广告主
    for (String clientId : mergeIds.split(",")) {
      if (!keepId.equals(clientId)) {
        Client abandonClient = clientMapper.get(Long.parseLong(clientId));
        if (abandonClient != null) {
          abandonClient.setStatus("Stop");
          abandonClient.setUpdated_at(now);
          clientMapper.update(abandonClient);
        }
      }
    }

    // 如果联系人需要合并
    if (!keepId.equals(copy_contact_client_id)) {
      // 第一组联系人从Client中取
      Client copyClient = clientMapper.get(Long.valueOf(copy_contact_client_id));
      keepClient.setContact_person(copyClient.getContact_person());
      keepClient.setPhone(copyClient.getPhone());
      keepClient.setEmail(copyClient.getEmail());
      keepClient.setPosition(copyClient.getPosition());
      clientMapper.update(keepClient);

      // 如果联系人改变
      if (!keepId.equalsIgnoreCase(copy_contact_client_id)) {
        // 删除原有的
        List<Long> deleteSearch = new ArrayList<Long>();
        deleteSearch.add(Long.parseLong(keepId));
        List<ClientContact> deleteContactList = clientContactMapper.getClientContactsByClientIdList(deleteSearch);
        for (ClientContact tmp : deleteContactList) {
          clientContactMapper.delete(tmp.getId());
        }
        // 添加新的,
        List<Long> addSearch = new ArrayList<Long>();
        addSearch.add(Long.parseLong(copy_contact_client_id));
        List<ClientContact> addContactList = clientContactMapper.getClientContactsByClientIdList(addSearch);
        for (ClientContact tmp : addContactList) {
          tmp.setClient_id(Long.parseLong(keepId));
          tmp.setIs_delete(null);
          tmp.setCreated_at(now);
          clientContactMapper.save(tmp);
        }
      }

    }

    Map<String, Object> searchMap = new HashMap<String, Object>();
    searchMap.put("advertiserIds", parameters.get("mergeIds"));

    if (!"3".equals(refreshDataRange)) {
      // 按日期刷新
      if ("2".equals(refreshDataRange)) {
        searchMap.put("mergedData", parameters.get("mergedData"));
      }

      // 修改受影响的商机
      List<BusinessOpportunity> listBO = businessOpportunityMapper.getListByCondition(searchMap);
      for (BusinessOpportunity bo : listBO) {
        if (!keepId.equals(bo.getAdvertiser_id().toString())) {
          bo.setAdvertiser_id(Long.parseLong(keepId));
          bo.setUpdated_at(now);
          businessOpportunityMapper.update(bo);
        }
      }

      // 获取所有的ID
      List<Long> allMergedIds = new ArrayList<Long>();
      for (String mergeId : mergeIds.split(",")) {
        allMergedIds.add(Long.parseLong(mergeId));
      }
      // 修改orders
      List<MergeTable> mergeTable1 = updateTableClient(allMergedIds, keepId, "orders");
      // share_clients
      List<MergeTable> mergeTable2 = updateTableClient(allMergedIds, keepId, "share_clients");
      // client_groups, 没有主键的表
      updateTableClient(allMergedIds, keepId, "client_groups");
      // xmo.client_contacts
      // List<MergeTable> mergeTable4 = updateTableClient(allMergedIds, keepId,
      // "xmo.client_contacts");

      insertUpdateTableLog(mergeTable1, mergeTable2, allMergedIds, keepId);
    }

    insertMergeLog(parameters, beforeClient);
  }



  private void insertUpdateTableLog(List<MergeTable> mergeTable1, List<MergeTable> mergeTable2, List<Long> allMergedIds, String keepId) {
    StringBuffer sb = new StringBuffer();
    sb.append("update orders client_id:" + formatMergeTable(mergeTable1, keepId));
    sb.append("update share_clients client_id:" + formatMergeTable(mergeTable2, keepId));
    sb.append("update client_groups client_id:" + allMergedIds.toString()).append("-->").append("[" + keepId + "];");
    insertLog(null, Operate.MODIFY.getIndex(), keepId, null, sb.toString(), sb.toString());
  }

  private String formatMergeTable(List<MergeTable> mergeTables, String keepId) {
    StringBuffer sb = new StringBuffer();
    sb.append("{");
    if (mergeTables != null && mergeTables.size() > 0) {
      for (int i = 0; i < mergeTables.size(); i++) {
        if (i != 0) {
          sb.append(",");
        }
        sb.append("[" + mergeTables.get(i).getId() + ":" + mergeTables.get(i).getClient_id() + "-->");
        sb.append(mergeTables.get(i).getId() + ":" + keepId + "]");
      }
    }
    sb.append("};");
    return sb.toString();
  }

  private List<MergeTable> updateTableClient(List<Long> allMergedIds, String keepId, String tableName) {

    if ("client_groups".equalsIgnoreCase(tableName)) {
      Map<String, Object> updateTableMap = new HashMap<String, Object>();
      updateTableMap.put("keepId", keepId);
      updateTableMap.put("list", allMergedIds);
      clientMapper.updateClientGroup(updateTableMap);
      return null;
    } else {
      Map<String, Object> searchTableIdMap = new HashMap<String, Object>();
      searchTableIdMap.put("list", allMergedIds);
      searchTableIdMap.put("table", tableName);
      // 所有需要修改的表ID
      List<MergeTable> mergeTables = clientMapper.getTableIdsByClientIds(searchTableIdMap);

      if (mergeTables != null && mergeTables.size() > 0) {
        Map<String, Object> updateTableMap = new HashMap<String, Object>();
        List<Long> mergetTableIds = new ArrayList<Long>();
        for (MergeTable mergeTable : mergeTables) {
          mergetTableIds.add(mergeTable.getId());
        }

        updateTableMap.put("table", tableName);
        updateTableMap.put("client_id", keepId);
        updateTableMap.put("list", mergetTableIds);
        clientMapper.updateTablesClientId(updateTableMap);

      }
      return mergeTables;
    }
  }


  /*
   * 合并广告主log
   */

  private void insertMergeLog(Map<String, Object> mergeMap, Client beforeClient) {
    String keepId = mergeMap.get("keepId").toString();
    String mergeIds = mergeMap.get("mergeIds").toString();
    Client afterClient = get(Long.parseLong(keepId));
    StringBuilder sb_zh = new StringBuilder();
    sb_zh.append("合并广告主ID: " + mergeIds + "; ");
    sb_zh.append("保留广告主ID: " + keepId + "; ");
    sb_zh.append("使用联系人广告主ID: " + mergeMap.get("copy_contact_client_id") + "; ");
    sb_zh.append("刷新数据范围: " + getDecodeRefreshDataRange_zh(mergeMap.get("refreshDataRange").toString()) + "; ");
    if (mergeMap.get("mergedData") != null) {
      sb_zh.append("指定日期: " + mergeMap.get("mergedData").toString() + "; ");
    }

    StringBuilder sb_en = new StringBuilder();
    sb_en.append("Review advertiser IDs: " + mergeIds + "; ");
    sb_en.append("Retain advertiser ID: " + keepId + "; ");
    sb_en.append("Use contacts client ID: " + mergeMap.get("copy_contact_client_id") + "; ");
    sb_en.append("Refresh data range: " + getDecodeRefreshDataRange_en(mergeMap.get("refreshDataRange").toString()) + "; ");
    if (mergeMap.get("mergedData") != null) {
      sb_en.append("Specified date: " + mergeMap.get("mergedData").toString() + "; ");
    }

    insertLog(afterClient, Operate.REVIEW.getIndex(), keepId, beforeClient, sb_zh.toString(), sb_en.toString());

  }

  // 提交广告主
  public String submit(Map<String, Object> parameters) {

    StringBuffer params = new StringBuffer();
    for (String key : parameters.keySet()) {
      if (params.toString().equals("")) {
        params.append(key).append("=").append(parameters.get(key) == null ? "" : parameters.get(key).toString());
      } else {
        params.append("&").append(key).append("=").append(parameters.get(key) == null ? "" : parameters.get(key).toString());
      }
    }
    OutputStreamWriter out = null;
    BufferedReader in = null;
    String result = "";
    try {
      String url = "";
      if (CommonUtil.isProduct()) {
        url = "http://sales.optimix.asia/zh-cn/clients/client_approval_from_pms";
      } else {
        url = "http://salesstg.optimix.asia/zh-cn/clients/client_approval_from_pms";
      }
      URL realUrl = new URL(url);
      HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
      conn.setDoOutput(true);
      conn.setDoInput(true);
      conn.setRequestMethod("POST");
      conn.setRequestProperty("accept", "*/*");
      conn.setRequestProperty("connection", "Keep-Alive");
      conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
      conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
      conn.setRequestProperty("Authorization", "Token token=c576f0136149a2e2d9127b3901019855");

      conn.connect();
      out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
      // 发送请求参数
      out.write(params.toString());
      // flush输出流的缓冲
      out.flush();
      // 定义BufferedReader输入流来读取URL的响应
      in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
      String line;
      while ((line = in.readLine()) != null) {
        result += line;
      }
    } catch (Exception ex) {
      ex.printStackTrace();
      return null;
    } finally {
      try {
        if (out != null) {
          out.close();
        }
        if (in != null) {
          in.close();
        }
      } catch (IOException ex) {
        ex.printStackTrace();
      }
    }

    return result;
  }

  public boolean canApproval(Map<String, Object> pMap) {
    return clientMapper.canApproval(pMap);
  }

  @SuppressWarnings("rawtypes")
  private void insertLog(Object object, int operateType, Object pKey, Object beforeObject, String appendStr_zh, String appendStr_en) {
    Log log = new Log();
    log.setOperateType(operateType);
    log.setOperateBy(getCurrUser() == null ? "" : getCurrUser().getName());
    log.setOperateUserId(getCurrUser() != null ? String.valueOf(getCurrUser().id) : null);
    log.setOperateTime(new Date());
    log.setpKey(String.valueOf(pKey));
    log.setContent(object == null ? null : new JsonMapper().toJson(object));
    log.setModule("menu.advertiser.review");

    // 如果是新增或者删除
    if (operateType == Operate.ADD.getIndex() || operateType == Operate.DELETE.getIndex()) {
      log.setRemark1(null);
      log.setRemark2(null);
    } else {
      Map map1 = jsonMapper.fromJson(jsonMapper.toJson(beforeObject), Map.class);
      Map map2 = jsonMapper.fromJson(jsonMapper.toJson(object), Map.class);
      String differentContent_zh = MapCompareUtil.compareToDifferent(map1, map2, COMPARE_KEY_ZH, "zh");
      String differentContent_en = MapCompareUtil.compareToDifferent(map1, map2, COMPARE_KEY_EN, "en");
      log.setRemark1(appendStr_zh + differentContent_zh);
      log.setRemark2(appendStr_en + differentContent_en);
    }

    logService.addLog(log);
  }

  private ShiroUser getCurrUser() {
    Subject currentUser = SecurityUtils.getSubject();
    if (null == currentUser)
      return null;
    return (ShiroUser) currentUser.getPrincipal();
  }


  public String getDecodeRefreshDataRange_zh(String refreshDataRange) {
    if ("1".equals(refreshDataRange))
      return CommonUtil.getProperty("zh", "CN", "advertiser.refresh.data.range.option1");
    else if ("2".equals(refreshDataRange))
      return CommonUtil.getProperty("zh", "CN", "advertiser.refresh.data.range.option2");
    else if ("3".equals(refreshDataRange))
      return CommonUtil.getProperty("zh", "CN", "advertiser.refresh.data.range.option3");
    return "";
  }

  public String getDecodeRefreshDataRange_en(String refreshDataRange) {
    if ("1".equals(refreshDataRange))
      return CommonUtil.getProperty("en", "US", "advertiser.refresh.data.range.option1");
    else if ("2".equals(refreshDataRange))
      return CommonUtil.getProperty("en", "US", "advertiser.refresh.data.range.option2");
    else if ("3".equals(refreshDataRange))
      return CommonUtil.getProperty("en", "US", "advertiser.refresh.data.range.option3");
    return "";
  }
}
