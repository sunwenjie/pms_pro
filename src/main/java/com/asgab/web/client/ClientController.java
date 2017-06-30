package com.asgab.web.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.asgab.core.pagination.Page;
import com.asgab.entity.Channel;
import com.asgab.entity.Client;
import com.asgab.entity.ClientContact;
import com.asgab.entity.Log;
import com.asgab.service.account.ShiroDbRealm.ShiroUser;
import com.asgab.service.channel.ChannelService;
import com.asgab.service.client.ClientContactService;
import com.asgab.service.client.ClientService;
import com.asgab.service.log.LogService;
import com.asgab.util.CommonUtil;
import com.asgab.util.JsonMapper;
import com.asgab.util.MapCompareUtil;
import com.asgab.util.Operate;
import com.asgab.util.Servlets;

@Controller
@RequestMapping(value = "/client")
public class ClientController {

  private static final String PAGE_SIZE = "10";

  // Json工具类
  private final static JsonMapper jsonMapper = new JsonMapper();

  // JSON比较key
  private final static String COMPARE_KEY_ZH = "log.client.keys_zh";

  private final static String COMPARE_KEY_EN = "log.client.keys_en";

  @Autowired
  private ClientService clientService;
  @Autowired
  private ClientContactService clientContactService;
  @Autowired
  private LogService logService;
  @Autowired
  private ChannelService channelService;

  @RequestMapping(method = RequestMethod.GET)
  public String list(@RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber, @RequestParam(value = "pageSize", defaultValue = PAGE_SIZE) int pageSize, @RequestParam(value = "sort", defaultValue = "id desc") String sort,
      HttpServletRequest request, Model model) {
    Map<String, Object> params = new HashMap<String, Object>();
    if (StringUtils.isNoneBlank(request.getParameterValues("name_brand_channel"))) {
      params.put("name_brand_channel", request.getParameter("name_brand_channel"));
    }
    String clientNumber = request.getParameter("clientNumber");
    if (StringUtils.isNotBlank(clientNumber)) {
      params.put("clientNumber", clientNumber);
      params.put("fmt_clientNumber", clientNumber.replace("AD", "").replaceFirst("^0*", ""));
    }
    if (StringUtils.isNoneBlank(request.getParameterValues("industry_id"))) {
      params.put("industry_id", request.getParameter("industry_id"));
    }
    if (StringUtils.isNoneBlank(request.getParameterValues("state"))) {
      params.put("state", request.getParameter("state"));
    }
    if (StringUtils.isNotBlank(request.getParameter("created_by"))) {
      params.put("created_by", request.getParameter("created_by"));
    }
    String created_period = request.getParameter("created_period");
    if (StringUtils.isNotBlank(created_period) && created_period.contains(" - ") && created_period.split(" - ").length == 2) {
      params.put("created_period", created_period);
      params.put("created_period_start", created_period.split(" - ")[0] + " 00:00");
      params.put("created_period_end", created_period.split(" - ")[1] + " 23:59");
    }

    model.addAttribute("search", Servlets.encodeParameterString(params));
    params.put("sort", sort);

    Subject currentUser = SecurityUtils.getSubject();
    ShiroUser user = (ShiroUser) currentUser.getPrincipal();
    if (user != null && !currentUser.hasRole("admin") && !currentUser.hasRole("superAdmin")) {
      params.put("currUserId", user.getId());
      Page<Client> page = new Page<Client>(pageNumber, pageSize, sort, params);
      Page<Client> pages = clientService.searchClients(page);
      model.addAttribute("pages", pages);
    } else {
      Page<Client> page = new Page<Client>(pageNumber, pageSize, sort, params);
      Page<Client> pages = clientService.search(page);
      model.addAttribute("pages", pages);
    }

    clientService.setSelect(request);
    return "client/clientList";
  }

  @RequestMapping(value = "create", method = RequestMethod.GET)
  public String toCreate(Model model, HttpServletRequest request) {
    Client client = new Client();
    Long currency_id = ((ShiroUser) SecurityUtils.getSubject().getPrincipal()).getCurrency_id();
    // 默认偏好设置. 如果没有就为RMB
    client.setCurrency_id(currency_id != null ? currency_id.intValue() : 2);
    ShiroUser user = getCurrUser();
    if (user != null) {
      client.setSaleIds(String.valueOf(user.id));
    }
    model.addAttribute("client", client);
    model.addAttribute("action", "create");
    clientService.setSelect(request);
    return "client/clientForm";
  }

  @RequestMapping(value = "create", method = RequestMethod.POST)
  public String create(Client client, HttpServletRequest request, RedirectAttributes redirectAttributes) {

    ShiroUser user = getCurrUser();
    if (user != null) {
      client.setCreated_user(user.loginName);
      client.setUser_id(user.id);
    }
    client.setCreated_at(new Date());
    client.setStatus("Active");
    clientService.save(client);
    redirectAttributes.addFlashAttribute("message", CommonUtil.getProperty(request, "message.create.success"));
    redirectAttributes.addFlashAttribute("successId", client.getNumber());
    return "redirect:/client";
  }

  @RequestMapping(value = "view/{id}", method = RequestMethod.GET)
  public String view(@PathVariable("id") Long id, Model model, HttpServletRequest request) {
    Map<String, Object> searchMap = new HashMap<String, Object>();
    searchMap.put("client_id", id);
    Client client = clientService.get(id);
    List<ClientContact> list = new ArrayList<ClientContact>();
    ClientContact first = new ClientContact();
    first.setContact_person(client.getContact_person());
    first.setPhone(client.getPhone());
    first.setEmail(client.getEmail());
    first.setPosition(client.getPosition());
    list.add(first);
    list.addAll(clientContactService.getList(searchMap));
    client.setContacts(list);
    model.addAttribute("client", client);
    clientService.setSelect(request);
    return "client/clientView";
  }

  @RequestMapping(value = "update/{id}", method = RequestMethod.GET)
  public String toUpdate(@PathVariable("id") Long id, Model model, HttpServletRequest request) {
    Map<String, Object> searchMap = new HashMap<String, Object>();
    searchMap.put("client_id", id);
    Client client = clientService.get(id);
    List<ClientContact> list = new ArrayList<ClientContact>();
    ClientContact first = new ClientContact();
    first.setContact_person(client.getContact_person());
    first.setPhone(client.getPhone());
    first.setEmail(client.getEmail());
    first.setPosition(client.getPosition());
    list.add(first);
    list.addAll(clientContactService.getList(searchMap));
    client.setContacts(list);
    model.addAttribute("client", client);
    model.addAttribute("action", "update");
    clientService.setSelect(request);
    return "client/clientForm";
  }

  @RequestMapping(value = "update", method = RequestMethod.POST)
  public String update(@ModelAttribute("client") Client client, HttpServletRequest request, RedirectAttributes redirectAttributes) {
    Client beforeClient = clientService.get(client.getId());
    client.setUpdated_at(new Date());
    clientService.update(client);
    Client afterClient = clientService.get(client.getId());
    insertLog(afterClient, Operate.MODIFY.getIndex(), client.getId(), beforeClient);
    redirectAttributes.addFlashAttribute("message", CommonUtil.getProperty(request, "message.update.success"));
    redirectAttributes.addFlashAttribute("successId", client.getNumber());
    return "redirect:/client";
  }

  @RequestMapping(value = "delete/{id}", method = RequestMethod.GET)
  public String delete(@PathVariable("id") Long id, Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
    Client client = clientService.get(id);

    boolean success = false;
    String message_del = "";
    if (client != null) {
      if (client.getState().equals("cli_saved")) {
        clientService.delete(id);
        insertLog(null, Operate.DELETE.getIndex(), id, null, client.getClientname());
        success = true;
        message_del = CommonUtil.getProperty(request, "message.delete.client.success").replace("{0}", client.getClientname()).replace("{1}", client.getNumber());
      } else {
        success = false;
        message_del = CommonUtil.getProperty(request, "message.client.delete.fail");
      }
    }

    redirectAttributes.addFlashAttribute("success", success);
    redirectAttributes.addFlashAttribute("message_del", message_del);
    return "redirect:/client";
  }

  /***
   * 广告主：提交、审批
   */
  @RequestMapping(value = "approval", method = RequestMethod.POST)
  public String submitClient(HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {

    String client_id = request.getParameter("id");
    String status = request.getParameter("modal_status");// 1:提交，2：审批通过，3：审批不通过，4：释放客户
    boolean canApproval = "1".equals(status);
    Subject currentUser = SecurityUtils.getSubject();
    ShiroUser user = getCurrUser();
    Map<String, Object> parameters = new HashMap<String, Object>();
    Client client = clientService.get(Long.valueOf(client_id));
    // 如果具有admin权限
    if (currentUser.hasRole("admin") || currentUser.hasRole("superAdmin")) {
      canApproval = true;
    }
    if (!canApproval) {
      parameters.put("client_id", client_id);
      parameters.put("approval_user", user == null ? -1 : user.getId());
      parameters.put("node_id", !"cross_unapproved".equalsIgnoreCase(request.getParameter("modal_state")) ? 6 : 9);// 用户审批权限id（6：普通客户审批权限，9：跨区客户审批权限）
      canApproval = clientService.canApproval(parameters);
    }

    Map<String, Object> msg = new HashMap<String, Object>();
    if (canApproval) {
      parameters.clear();
      parameters.put("id", client_id);// 客户ID
      parameters.put("status", status);
      parameters.put("node_id", !"cross_unapproved".equalsIgnoreCase(request.getParameter("modal_state")) ? 6 : 9);
      parameters.put("comment", request.getParameter("comment"));// 备注
      parameters.put("common_aproved", null);
      if (user != null) {
        parameters.put("approver", user.id);
      }
      LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
      parameters.put("local_language", localeResolver.resolveLocale(request).getLanguage().contains("zh") ? "zh-cn" : "en");

      String result = clientService.submit(parameters);
      JSONObject jsonObject = JSON.parseObject(result);

      boolean success = (jsonObject != null && jsonObject.get("success") != null && (Boolean) jsonObject.get("success") == true) ? true : false;
      msg.put("success", success);
      if (success) {
        msg.put("message_modal", "<" + client.getNumber() + ">" + CommonUtil.getProperty(request, "message.submit.success"));
      } else {
        msg.put("message_modal", jsonObject.get("reason"));
        msg.put("back_edit_client_id", request.getParameter("id"));
      }
    } else {
      msg.put("success", false);
      msg.put("message_modal", CommonUtil.getProperty(request, "message.can.not.approval.advertiser"));
    }

    redirectAttributes.addFlashAttribute("msg_modal", msg);
    return "redirect:/client";
  }

  @ResponseBody
  @RequestMapping(value = "channel/{id}", method = RequestMethod.POST)
  public String getChannel(@PathVariable("id") Long id, HttpServletResponse response) {
    Channel channel = channelService.get(id);
    return channel != null ? String.valueOf(new JsonMapper().toJson(channel)) : "";
  }

  private void insertLog(Object object, int operateType, Object pKey, Object beforeObject) {
    insertLog(object, operateType, pKey, beforeObject, null);
  }

  @SuppressWarnings("rawtypes")
  private void insertLog(Object object, int operateType, Object pKey, Object beforeObject, String remark) {
    Log log = new Log();
    log.setOperateType(operateType);
    log.setOperateBy(getCurrUser() == null ? "" : getCurrUser().getName());
    log.setOperateUserId(getCurrUser() != null ? String.valueOf(getCurrUser().id) : null);
    log.setOperateTime(new Date());
    log.setpKey(String.valueOf(pKey));
    log.setContent(object == null ? null : new JsonMapper().toJson(object));
    log.setModule("menu.advertiser");

    // 如果是新增或者删除
    if (operateType == Operate.ADD.getIndex() || operateType == Operate.DELETE.getIndex()) {
      log.setRemark1(null);
      log.setRemark2(null);
      log.setRemark(remark);
    } else {
      Map map1 = jsonMapper.fromJson(jsonMapper.toJson(beforeObject), Map.class);
      Map map2 = jsonMapper.fromJson(jsonMapper.toJson(object), Map.class);
      String differentContent_zh = MapCompareUtil.compareToDifferent(map1, map2, COMPARE_KEY_ZH, "zh");
      String differentContent_en = MapCompareUtil.compareToDifferent(map1, map2, COMPARE_KEY_EN, "en");
      log.setRemark1(differentContent_zh);
      log.setRemark2(differentContent_en);
    }

    logService.addLog(log);
  }


  @ModelAttribute
  public void getClient(@RequestParam(value = "id", defaultValue = "-1") Long id, Model model) {
    if (id != -1) {
      model.addAttribute("client", clientService.get(id));
    }
  }

  @RequestMapping(value = "addContact/{index}", method = RequestMethod.POST)
  @ResponseBody
  public String addContact(@PathVariable("index") int index, RedirectAttributes redirectAttributes) {
    redirectAttributes.addFlashAttribute("index", index);
    return "client/include/clientContact";
  }

  private ShiroUser getCurrUser() {
    Subject currentUser = SecurityUtils.getSubject();
    if (null == currentUser)
      return null;
    return (ShiroUser) currentUser.getPrincipal();
  }

}
