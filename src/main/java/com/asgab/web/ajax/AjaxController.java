package com.asgab.web.ajax;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.asgab.core.pagination.Page;
import com.asgab.entity.Agency;
import com.asgab.entity.BusinessOpportunity;
import com.asgab.entity.Client;
import com.asgab.entity.ClientExamination;
import com.asgab.entity.CurrencyType;
import com.asgab.entity.DataSharing;
import com.asgab.entity.DataSharingDTO;
import com.asgab.entity.ExchangeRate;
import com.asgab.entity.Product;
import com.asgab.entity.Report;
import com.asgab.entity.ReportModel;
import com.asgab.entity.User;
import com.asgab.repository.ProductCategoryMapper;
import com.asgab.service.account.AccountService;
import com.asgab.service.account.ShiroDbRealm.ShiroUser;
import com.asgab.service.agency.AgencyService;
import com.asgab.service.business.opportunity.BusinessOpportunityService;
import com.asgab.service.client.ClientExaminationService;
import com.asgab.service.client.ClientService;
import com.asgab.service.exchange.ExchangeRateService;
import com.asgab.service.management.CurrencyTypeService;
import com.asgab.service.product.ProductService;
import com.asgab.service.report.ReportService;
import com.asgab.service.setting.DataSharingService;
import com.asgab.util.CommonUtil;
import com.asgab.util.SelectMapper;

@Controller
@RequestMapping(value = "/ajax")
public class AjaxController {

  @Autowired
  private ClientService clientService;

  @Autowired
  private AccountService accountService;

  @Autowired
  private ProductService productService;

  @Autowired
  private AgencyService agencyService;

  @Autowired
  private ReportService reportService;

  @Autowired
  private ExchangeRateService exchangeRateService;

  @Autowired
  private ProductCategoryMapper productCategoryMapper;

  @Autowired
  private DataSharingService dataSharingService;

  @Autowired
  private ClientExaminationService clientExaminationService;

  @Autowired
  private CurrencyTypeService currencyTypeService;
  
  @Autowired
  private BusinessOpportunityService businessOpportunityService;

  @RequestMapping(value = "addProduct", method = RequestMethod.POST)
  public String addProduct(HttpServletRequest request, Model model) {
    List<SelectMapper> mappers = new ArrayList<SelectMapper>();
    mappers.add(new SelectMapper("CPC", "CPC"));
    mappers.add(new SelectMapper("CPM", "CPM"));
    model.addAttribute("index", request.getParameter("index"));
    model.addAttribute("saleModes", mappers);
    List<Product> products_data = new ArrayList<Product>();
    products_data.addAll(productService.getAllProduct());
    model.addAttribute("products_data", products_data);
    model.addAttribute("productCategories", productCategoryMapper.getList());
    return "businessOpportunity/product";
  }

  @ResponseBody
  @RequestMapping(value = "getSales", method = RequestMethod.GET)
  public String getSales(@RequestParam("q") String name) {

    Map<String, Object> params = new HashMap<String, Object>();
    if (StringUtils.isNotBlank(name)) {
      params.put("name", name);
    }
    Page<User> page = new Page<User>(1, 10, null, params);
    Page<User> pages = accountService.getAllUser(page);

    JSONArray array = new JSONArray();
    for (int i = 0; i < pages.getContent().size(); i++) {
      JSONObject tmp = new JSONObject();
      tmp.put("id", pages.getContent().get(i).getId());
      tmp.put("text", pages.getContent().get(i).getName());
      array.add(tmp);
    }
    return array.toJSONString();

  }

  @ResponseBody
  @RequestMapping(value = "getProducts", method = RequestMethod.GET)
  public String getProducts(@RequestParam("pt") String productType, @RequestParam("sv") String selectedVal, HttpServletRequest request) {

    Map<String, Object> params = new HashMap<String, Object>();
    LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
    params.put("lang", localeResolver.resolveLocale(request).getLanguage());
    if (StringUtils.isNotBlank(productType)) {
      params.put("productType", productType);
    }
    List<Product> allProduct = productService.getAllProduct(params);
    StringBuilder sb = new StringBuilder("<option value></option>");
    if (allProduct != null && allProduct.size() > 0) {

      for (Product thisProduct : allProduct) {
        if (StringUtils.isNotBlank(selectedVal) && selectedVal.equals(String.valueOf(thisProduct.getId()))) {
          sb.append("<option value='" + thisProduct.getId() + "' selected>" + thisProduct.getName() + "</option>");
        } else {
          sb.append("<option value='" + thisProduct.getId() + "'>" + thisProduct.getName() + "</option>");
        }
      }
    }
    return sb.toString();
  }

  @RequestMapping(value = "addContact", method = RequestMethod.POST)
  public String addContact(HttpServletRequest request, Model model) {
    model.addAttribute("index", request.getParameter("index"));
    return "client/include/clientContact";
  }

  @ResponseBody
  @RequestMapping(value = "getAgencys", method = RequestMethod.GET)
  public String getAgencys(@RequestParam("q") String name) {

    Map<String, Object> params = new HashMap<String, Object>();
    if (StringUtils.isNotBlank(name)) {
      params.put("name", name);
    }
    Page<Agency> page = new Page<Agency>(1, 10, null, params);
    Page<Agency> pages = agencyService.search(page);

    JSONArray array = new JSONArray();
    for (int i = 0; i < pages.getContent().size(); i++) {
      JSONObject tmp = new JSONObject();
      tmp.put("id", pages.getContent().get(i).getId());
      tmp.put("text", pages.getContent().get(i).getChannel_name());
      array.add(tmp);
    }

    return array.toJSONString();
  }

  @RequestMapping(value = "report/list", method = RequestMethod.POST)
  public String list(Report report, Model model, HttpServletRequest request) {
    LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
    String lang = localeResolver.resolveLocale(request).getLanguage();
    String country = localeResolver.resolveLocale(request).getCountry();
    boolean isZH = "zh".equalsIgnoreCase(lang) && "CN".equalsIgnoreCase(country);

    // 返回页面列表. 显示的第一列列名
    String[] dataRightColumns = {"report.product", "report.sale.team", "report.sale.representative", "report.channel.company", "report.advertiser"};
    // 获取拼装好的数据
    Map<String, Object> map = reportService.getReport(report, isZH);
    List<Report> opportunityReports = (List<Report>) map.get("opportunityReports");
    if (opportunityReports == null || opportunityReports.size() > 1) {
      dataRightColumns[3] = "report.channel.company.s";
    }
    model.addAttribute("names", map.get("names"));
    model.addAttribute("opportunityReports", opportunityReports);
    model.addAttribute("orderReports", map.get("orderReports"));
    // 如果是按照团队, 或者是个人, 统计需要在后台统计. product和channel在前台js统计
    if (map.get("orderSum") != null) {
      model.addAttribute("orderSum", map.get("orderSum"));
      model.addAttribute("opportunitySum", map.get("opportunitySum"));
    }
    model.addAttribute("boxtitle", ((report.getMetric() != null && report.getMetric().equals("2")) ? "report.total.GP" : "report.total.sale"));
    model.addAttribute("exchangeRates", exchangeRateService.getCurrencyRMB2Others());
    Long currency_id = ((ShiroUser) SecurityUtils.getSubject().getPrincipal()).currency_id;
    CurrencyType currencyType = null;
    if (currency_id != null) {
      currencyType = currencyTypeService.getCurrencyTypeById(currency_id);
    }
    // 优先页面currency . 没有就datasharing的. 再没有就RMB
    model.addAttribute("currency", StringUtils.isNotBlank(report.getCurrency()) ? report.getCurrency() : (currencyType != null ? currencyType.getName() : "RMB"));

    model.addAttribute("dataRightColumn", CommonUtil.getProperty(request, dataRightColumns[Integer.parseInt(report.getDataRight()) - 1]));
    return "report/reportResultList";
  }

  @ResponseBody
  @RequestMapping(value = "tree")
  public JSONArray getTree(@RequestParam("id") String id) {
    JSONArray jsonArray = new JSONArray();

    if (StringUtils.isNotBlank(id)) {
      List<DataSharingDTO> sharingDTOs = dataSharingService.getDataSharingDTOs(Long.parseLong(id));

      for (DataSharingDTO dto : sharingDTOs) {
        pushData(jsonArray, dto, true);
      }
    }

    return jsonArray;
  }

  private void pushData(JSONArray jsonArray, DataSharingDTO dto, boolean open) {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("id", dto.getParentNode().getUser_id());
    map.put("pId", dto.getParentNode().getParent_id());
    map.put("name", dto.getParentNode().getShowName());
    map.put("open", open);
    jsonArray.add(map);
    if (dto.getChidrenNodes() != null && dto.getChidrenNodes().size() > 0) {
      for (DataSharingDTO chilrenDTO : dto.getChidrenNodes()) {
        pushData(jsonArray, chilrenDTO, true);
      }
    }
  }

  @ResponseBody
  @RequestMapping(value = "client/approval/modal/{id}")
  public ModelAndView showModal(@PathVariable("id") Long id, ModelAndView m) {
    Client client = clientService.get(id);
    m.addObject("client", client);
    m.setViewName("client/include/clientModal");

    // 提交人
    ClientExamination submitedBy = null;
    // 批准人
    ClientExamination approvedBy = null;
    // 特批人
    ClientExamination cross_approvedBy = null;
    // 草稿
    if (!"cli_saved".equalsIgnoreCase(client.getState())) {
      Map<String, Object> parameters = new HashMap<String, Object>();
      parameters.put("client_id", id);
      parameters.put("from_state", "cli_saved");
      parameters.put("to_state", "unapproved");
      submitedBy = clientExaminationService.getNewestProcess(parameters);

      // 审批通过
      if ("approved".equalsIgnoreCase(client.getState())) {
        parameters.put("from_state", "unapproved");
        parameters.put("to_state", "approved");
        approvedBy = clientExaminationService.getNewestProcess(parameters);
        if (approvedBy == null) {
          parameters.put("from_state", "unapproved");
          parameters.put("to_state", "cross_unapproved");
          approvedBy = clientExaminationService.getNewestProcess(parameters);
          parameters.put("from_state", "cross_unapproved");
          parameters.put("to_state", "approved");
          cross_approvedBy = clientExaminationService.getNewestProcess(parameters);
        }
      }
      // 审批未通过
      else if ("cli_rejected".equalsIgnoreCase(client.getState())) {
        parameters.put("from_state", "unapproved");
        parameters.put("to_state", "cli_rejected");
        approvedBy = clientExaminationService.getNewestProcess(parameters);
        parameters.put("from_state", "cross_unapproved");
        parameters.put("to_state", "cli_rejected");
        cross_approvedBy = clientExaminationService.getNewestProcess(parameters);
      }
      // 跨区特批中
      else if ("cross_unapproved".equalsIgnoreCase(client.getState())) {
        parameters.put("from_state", "unapproved");
        parameters.put("to_state", "cross_unapproved");
        approvedBy = clientExaminationService.getNewestProcess(parameters);
        parameters.put("from_state", "unapproved");
        parameters.put("to_state", "approved");
        m.addObject("approvedBy", clientExaminationService.getNewestProcess(parameters));
      }
      // 已释放
      else if ("released".equalsIgnoreCase(client.getState())) {
        parameters.put("from_state", "approved");
        parameters.put("to_state", "released");
        approvedBy = clientExaminationService.getNewestProcess(parameters);

        parameters.put("from_state", "cross_unapproved");
        parameters.put("to_state", "approved");
        cross_approvedBy = clientExaminationService.getNewestProcess(parameters);
      }
    }

    m.addObject("submitedBy", submitedBy);
    m.addObject("approvedBy", approvedBy);
    m.addObject("cross_approvedBy", cross_approvedBy);
    return m;
  }

  @ResponseBody
  @RequestMapping(value = "analyse", method = RequestMethod.POST)
  public ReportModel analyse(Model model, HttpServletRequest request) {
    // 语言
    LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
    String lang = localeResolver.resolveLocale(request).getLanguage();
    String country = localeResolver.resolveLocale(request).getCountry();
    boolean isZH = "zh".equalsIgnoreCase(lang) && "CN".equalsIgnoreCase(country);
    // 所有转成人民币的汇率
    List<ExchangeRate> exchangeRates = exchangeRateService.getCurrency2RMB();
    Report report = new Report();
    String[] dates = request.getParameter("reportDate").split(" - ");
    report.setReportDate_start(dates[0].replace("/", "-"));
    report.setReportDate_end(dates[1].replace("/", "-"));
    // 4张报表
    ReportModel dataModel = new ReportModel();
    // 1:saleteam 2 3:product 4
    reportService.getAnalyseChart(report, dataModel, exchangeRates, "saleTeam", isZH);
    reportService.getAnalyseChart(report, dataModel, exchangeRates, "product", isZH);
    reportService.getAnalyseChart(report, dataModel, exchangeRates, "adType", isZH);
    // 补充缺失的adType
    fillAdType(dataModel);
    // 上面固定值
    reportService.getAnalyseData(dataModel, report, exchangeRates);
    // 推广计划
    reportService.getCampaignData(report, dataModel);
    // GP权限
    if (SecurityUtils.getSubject().isPermitted("gp:query")) {
      dataModel.setGpQuery(true);
    }

    return dataModel;
  }

  /**
   * 补充丢失的Adtype
   * 
   * @param dataModel
   */
  private void fillAdType(ReportModel dataModel) {
    List<BigDecimal> gps = dataModel.getDataGp4();
    List<BigDecimal> incomes = dataModel.getDataIncome4();
    List<String> labels = dataModel.getLabels4();
    // 如果没有GP权限
    boolean queryGP = SecurityUtils.getSubject().isPermitted("gp:query");
    if (!CommonUtil.containValue(labels, "PC")) {
      labels.add("PC");
      incomes.add(BigDecimal.ZERO);
      if (queryGP) {
        gps.add(BigDecimal.ZERO);
      }
    }
    if (!CommonUtil.containValue(labels, "Mobile")) {
      labels.add("Mobile");
      incomes.add(BigDecimal.ZERO);
      if (queryGP) {
        gps.add(BigDecimal.ZERO);
      }
    }
    if (!CommonUtil.containValue(labels, "Other")) {
      labels.add("Other");
      incomes.add(BigDecimal.ZERO);
      if (queryGP) {
        gps.add(BigDecimal.ZERO);
      }
    }

  }

  /**
   * datasharing 搜索框用
   * 
   * @param name
   * @return
   */
  @ResponseBody
  @RequestMapping(value = "getUsers", method = RequestMethod.GET)
  public String getUsers(@RequestParam("q") String name) {

    Map<String, Object> params = new HashMap<String, Object>();
    if (StringUtils.isNotBlank(name)) {
      params.put("name", name);
    }
    Page<User> page = new Page<User>();
    page.setSearchMap(params);
    Page<User> pages = accountService.getUsersForSetting(page);

    JSONArray array = new JSONArray();
    // 禁选用户
    String disabledUserIds = dataSharingService.getViewDataSharing(((ShiroUser) SecurityUtils.getSubject().getPrincipal()).id);
    for (int i = 0; i < pages.getContent().size(); i++) {
      JSONObject tmp = new JSONObject();
      User tmpUser = pages.getContent().get(i);
      tmp.put("id", tmpUser.getId());
      tmp.put("text", tmpUser.getName() + (StringUtils.isNotBlank(tmpUser.getEmail()) ? ("(" + tmpUser.getEmail() + ")") : ""));
      if (containUsers(disabledUserIds, String.valueOf(tmpUser.getId())) || !("Active".equalsIgnoreCase(tmpUser.getStatus()) || "Expired".equalsIgnoreCase(tmpUser.getStatus()))) {
        tmp.put("disabled", "true");
      }
      if (!"true".equalsIgnoreCase(tmp.getString("disabled"))) {
        // 如果不是禁用. 就放前面
        array.add(0, tmp);
      } else {
        array.add(tmp);
      }
    }
    return array.toJSONString();

  }

  @ResponseBody
  @RequestMapping(value = "getAssistants", method = RequestMethod.GET)
  public String getAssistants(@RequestParam("q") String name) {

    Map<String, Object> params = new HashMap<String, Object>();
    if (StringUtils.isNotBlank(name)) {
      params.put("name", name);
    }
    Page<User> page = new Page<User>();
    page.setSearchMap(params);
    Page<User> pages = accountService.getUsersForSetting(page);

    JSONArray array = new JSONArray();
    // 禁选用户
    List<DataSharing> disabledUserIds = dataSharingService.getAllAssistants();
    for (int i = 0; i < pages.getContent().size(); i++) {
      JSONObject tmp = new JSONObject();
      User tmpUser = pages.getContent().get(i);
      tmp.put("id", tmpUser.getId());
      tmp.put("text", tmpUser.getName() + (StringUtils.isNotBlank(tmpUser.getEmail()) ? ("(" + tmpUser.getEmail() + ")") : ""));
      if (containDatasharings(disabledUserIds, String.valueOf(tmpUser.getId())) || !("Active".equalsIgnoreCase(tmpUser.getStatus()) || "Expired".equalsIgnoreCase(tmpUser.getStatus()))) {
        tmp.put("disabled", "true");
      }
      if (!"true".equalsIgnoreCase(tmp.getString("disabled"))) {
        // 如果不是禁用. 就放前面
        array.add(0, tmp);
      } else {
        array.add(tmp);
      }
    }
    return array.toJSONString();

  }

  /**
   * 是否在禁选用户内
   * 
   * @param disabledUserIds
   * @param id
   * @return
   */
  private boolean containUsers(String disabledUserIds, String id) {
    boolean contain = false;
    String[] userIds = disabledUserIds.split(",");
    for (String userId : userIds) {
      if (userId.equalsIgnoreCase(id)) {
        contain = true;
        break;
      }
    }
    return contain;
  }

  private boolean containDatasharings(List<DataSharing> dataSharings, String id) {
    boolean contain = false;
    if (dataSharings != null && dataSharings.size() > 0) {
      for (DataSharing dataSharing : dataSharings) {
        if (dataSharing.getAssistant_id() != null && dataSharing.getAssistant_id().equals(new Long(id))) {
          contain = true;
        }
      }
    }
    return contain;
  }

  /**
   * opportunity用. 查询所有的用户, 如果是不是管理员. 需要排除带admin的用户
   * 
   * @param name
   * @return
   */
  @ResponseBody
  @RequestMapping(value = "opportunity/users", method = RequestMethod.GET)
  public String getOpportunityUsers(@RequestParam("q") String name) {

    boolean isAdmin = SecurityUtils.getSubject().hasRole("admin") || SecurityUtils.getSubject().hasRole("superAdmin");
    Map<String, Object> params = new HashMap<String, Object>();
    if (StringUtils.isNotBlank(name)) {
      params.put("name", name);
    }
    if (!isAdmin) {
      // 如果不是admin , 就不排除用户为admin的数据
      params.put("excludeAdmin", "true");
    }
    params.put("sort", "name asc");
    Page<User> page = new Page<User>(1, 10, null, params);
    Page<User> pages = accountService.getAllUser(page);

    JSONArray array = new JSONArray();
    // 禁选用户
    for (int i = 0; i < pages.getContent().size(); i++) {
      JSONObject tmp = new JSONObject();
      User tmpUser = pages.getContent().get(i);
      tmp.put("id", tmpUser.getId());
      tmp.put("text", tmpUser.getName() + (StringUtils.isNotBlank(tmpUser.getAgency()) ? ("(" + tmpUser.getAgency() + ")") : ""));
      array.add(tmp);
    }
    return array.toJSONString();

  }

  @ResponseBody
  @RequestMapping(value = "remark/{id}")
  public String remark(@PathVariable("id") Long id) {
    BusinessOpportunity tmp = new BusinessOpportunity();
    tmp.setBusinessOpportunityRemarks(businessOpportunityService.getRemarksByOpportunityId(id));
    return tmp.getProgressRemarkList();
  }
  
}
