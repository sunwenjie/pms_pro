package com.asgab.web.business.opportunity;

import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.alibaba.fastjson.JSONObject;
import com.asgab.core.pagination.Page;
import com.asgab.entity.BusinessOpportunity;
import com.asgab.entity.Client;
import com.asgab.entity.Log;
import com.asgab.entity.Product;
import com.asgab.entity.ProductCategory;
import com.asgab.entity.User;
import com.asgab.repository.ProductCategoryMapper;
import com.asgab.service.account.AccountService;
import com.asgab.service.account.ShiroDbRealm.ShiroUser;
import com.asgab.service.business.opportunity.BusinessOpportunityService;
import com.asgab.service.client.ClientService;
import com.asgab.service.log.LogService;
import com.asgab.service.product.ProductService;
import com.asgab.tag.AuthUtils;
import com.asgab.util.CommonUtil;
import com.asgab.util.JsonMapper;
import com.asgab.util.MapCompareUtil;
import com.asgab.util.Operate;
import com.asgab.util.SelectMapper;
import com.asgab.util.Servlets;

@Controller
@RequestMapping(value = "/businessOpportunity")
public class BusinessOpportunityController {
  private static final String PAGE_SIZE = "10";

  // Json工具类
  private final static JsonMapper jsonMapper = new JsonMapper();

  // JSON比较key
  private final static String COMPARE_KEY_ZH = "log.business.opportunity.keys_zh";
  private final static String COMPARE_KEY_EN = "log.business.opportunity.keys_en";

  @Autowired
  private BusinessOpportunityService businessOpportunityService;

  @Autowired
  private ClientService clientService;

  @Autowired
  private AccountService accountService;

  @Autowired
  private ProductService productService;

  @Autowired
  private ProductCategoryMapper productCategoryMapper;
  @Autowired
  private LogService logService;

  @RequestMapping(method = RequestMethod.GET)
  public String list(@RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber, @RequestParam(value = "pageSize", defaultValue = PAGE_SIZE) int pageSize, @RequestParam(value = "sort", defaultValue = "orderByDate desc") String sort,
      HttpServletRequest request, HttpServletResponse response, Model model) {
    Map<String, Object> params = new HashMap<String, Object>();
    if (StringUtils.isNotBlank(request.getParameter("advertiser"))) {
      params.put("advertiser", request.getParameter("advertiser"));
    }
    if (StringUtils.isNotBlank(request.getParameter("name"))) {
      String fmt_name = request.getParameter("name");
      if (fmt_name.startsWith("SO")) {
        fmt_name = fmt_name.replace("SO", "").replaceFirst("^0*", "");
      }
      params.put("name", request.getParameter("name"));
      params.put("fmt_name", fmt_name);
    }
    if (request.getParameterValues("statuses") != null && request.getParameterValues("statuses").length > 0) {
      params.put("statuses", request.getParameterValues("statuses"));
      model.addAttribute("statusesPage", request.getParameterValues("statuses"));
    }
    if (StringUtils.isNotBlank(request.getParameter("created_by"))) {
      params.put("created_by", request.getParameter("created_by"));
      model.addAttribute("created_by_user", accountService.get(Long.parseLong(request.getParameter("created_by"))));
    }
    String created_period = request.getParameter("created_period");
    if (StringUtils.isNotBlank(created_period) && created_period.contains(" - ") && created_period.split(" - ").length == 2) {
      params.put("created_period", created_period);
      params.put("created_period_start", (created_period.split(" - ")[0] + " 00:00").replaceAll("/", "-"));
      params.put("created_period_end", (created_period.split(" - ")[1] + " 23:59").replaceAll("/", "-"));
    }
    model.addAttribute("search", Servlets.encodeParameterString(params));

    params.put("sort", sort);
    Page<BusinessOpportunity> page = new Page<BusinessOpportunity>(pageNumber, pageSize, sort, params);
    // 如果是下载
    if ("download".equalsIgnoreCase(request.getParameter("download"))) {
      List<BusinessOpportunity> businessOpportunitys = businessOpportunityService.searchAll(page);
      response.setCharacterEncoding("utf-8");
      response.setContentType("application/vnd.ms-excel");
      try {
        response.setHeader("Content-Disposition", "attachment;filename=\"" + new String(URLDecoder.decode("Business Opportunities.xlsx", "UTF-8").getBytes(), "iso-8859-1") + "\"");
        XSSFWorkbook workbook = businessOpportunityService.downloadReport(businessOpportunitys, request);
        workbook.write(response.getOutputStream());
        response.getOutputStream().flush();
        response.getOutputStream().close();
      } catch (Exception ex) {
        ex.printStackTrace();
      }
      return null;
    } else {

      Page<BusinessOpportunity> pages = businessOpportunityService.search(page);
      model.addAttribute("pages", pages);
      model.addAttribute("statusesMap", BusinessOpportunityService.statusMap);
      model.addAttribute("statusesZH", BusinessOpportunityService.statusZH);
      model.addAttribute("statusesEN", BusinessOpportunityService.statusEN);
      return "businessOpportunity/businessOpportunityList";
    }
  }

  @RequestMapping(value = "create", method = RequestMethod.GET)
  public String toCreate(Model model, HttpServletRequest request) {
    BusinessOpportunity businessOpportunity = new BusinessOpportunity();
    businessOpportunity.setProgress(10);
    businessOpportunity.setExist_msa(0);
    businessOpportunity.setExist_service(0);
    businessOpportunity.setCurrency_id(((ShiroUser) SecurityUtils.getSubject().getPrincipal()).currency_id);
    ShiroUser user = getCurrUser();
    if (user != null) {
      businessOpportunity.setOwner_sale(user.id);
    }
    model.addAttribute("businessOpportunity", businessOpportunity);
    model.addAttribute("currencys", businessOpportunityService.getCurrencyMappers());
    model.addAttribute("advertisers", clientService.getAdvertiserMappers());
    model.addAttribute("action", "create");
    setSelect(request, businessOpportunity);
    return "businessOpportunity/businessOpportunityForm";
  }

  @RequestMapping(value = "create", method = RequestMethod.POST)
  public String create(BusinessOpportunity businessOpportunity, HttpServletRequest request, RedirectAttributes redirectAttributes) {
    // 数据库时间格式用-分割 yyyy-MM-dd
    businessOpportunity.setDeliver_start_date(CommonUtil.changeDateFormat(businessOpportunity.getDeliver_date().substring(0, 10), "/", "-"));
    businessOpportunity.setDeliver_end_date(CommonUtil.changeDateFormat(businessOpportunity.getDeliver_date().substring(13, 23), "/", "-"));
    ShiroUser currUser = getCurrUser();
    businessOpportunity.setCreated_by(currUser == null ? null : currUser.getId());
    businessOpportunity.setBudget(new BigDecimal(businessOpportunity.getBudget_format().replaceAll(",", "")));
    businessOpportunityService.save(businessOpportunity);
    redirectAttributes.addFlashAttribute("message", CommonUtil.getProperty(request, "message.create.success"));
    redirectAttributes.addFlashAttribute("successId", businessOpportunity.getNumber());
    return "redirect:/businessOpportunity";
  }

  @RequestMapping(value = "view/{id}", method = RequestMethod.GET)
  public String view(@PathVariable("id") Long id, Model model) {
    BusinessOpportunity businessOpportunity = businessOpportunityService.get(id);
    String code = businessOpportunityService.getOrderCodeByOpportunityId(id);
    model.addAttribute("businessOpportunity", businessOpportunity);
    if (StringUtils.isNotBlank(code)) {
      model.addAttribute("orderCode", code);
    }
    return "businessOpportunity/businessOpportunityView";
  }

  @RequestMapping(value = "copy/{id}", method = RequestMethod.GET)
  public String copy(@PathVariable("id") Long id, HttpServletRequest request) {
    LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
    String lang = localeResolver.resolveLocale(request).getLanguage();
    BusinessOpportunity businessOpportunity = businessOpportunityService.get(id);
    businessOpportunity.setName(("zh".equalsIgnoreCase(lang) ? "[复制]" : "[Copy]") + businessOpportunity.getName());
    businessOpportunityService.copy(businessOpportunity);
    return "redirect:/businessOpportunity/update/" + businessOpportunity.getId();
  }

  @RequestMapping(value = "update/{id}", method = RequestMethod.GET)
  public String toUpdate(@PathVariable("id") Long id, Model model, HttpServletRequest request) {
    BusinessOpportunity businessOpportunity = businessOpportunityService.get(id);
    model.addAttribute("businessOpportunity", businessOpportunity);
    model.addAttribute("action", "update");
    model.addAttribute("currencys", businessOpportunityService.getCurrencyMappers());
    List<SelectMapper> mappers = new ArrayList<SelectMapper>();
    mappers.add(new SelectMapper("CPC", "CPC"));
    mappers.add(new SelectMapper("CPM", "CPM"));
    model.addAttribute("saleModes", mappers);
    model.addAttribute("advertisers", clientService.getAdvertiserMappers(businessOpportunity.getAdvertiser_id()));
    setSelect(request, businessOpportunity);
    return "businessOpportunity/businessOpportunityForm";
  }

  @RequestMapping(value = "update", method = RequestMethod.POST)
  public String update(@ModelAttribute("businessOpportunity") BusinessOpportunity businessOpportunity, HttpServletRequest request, RedirectAttributes redirectAttributes) {
    BusinessOpportunity beforeBusinessOpportunity = businessOpportunityService.get(businessOpportunity.getId());
    // 数据库时间格式用-分割 yyyy-MM-dd
    businessOpportunity.setDeliver_start_date(CommonUtil.changeDateFormat(businessOpportunity.getDeliver_date().substring(0, 10), "/", "-"));
    businessOpportunity.setDeliver_end_date(CommonUtil.changeDateFormat(businessOpportunity.getDeliver_date().substring(13, 23), "/", "-"));
    businessOpportunity.setBudget(new BigDecimal(businessOpportunity.getBudget_format().replaceAll(",", "")));
    businessOpportunityService.update(businessOpportunity);
    redirectAttributes.addFlashAttribute("message", CommonUtil.getProperty(request, "message.update.success"));
    redirectAttributes.addFlashAttribute("successId", businessOpportunity.getNumber());
    BusinessOpportunity afterBusinessOpportunity = businessOpportunityService.get(businessOpportunity.getId());
    insertLog(afterBusinessOpportunity, Operate.MODIFY.getIndex(), businessOpportunity.getId(), beforeBusinessOpportunity);
    return "redirect:/businessOpportunity";
  }

  @RequestMapping(value = "gen/{id}", method = RequestMethod.GET)
  public String genOrder(@ModelAttribute("businessOpportunity") BusinessOpportunity businessOpportunity, HttpServletRequest request, RedirectAttributes redirectAttributes) {
    BusinessOpportunity businessOpportunityDB = businessOpportunityService.get(businessOpportunity.getId());
    // 判断广告主是否是审批状态
    Client client = clientService.get(businessOpportunityDB.getAdvertiser_id());
    String orderMessage = "";
    boolean success = false;
    if (client != null && "approved".equalsIgnoreCase(client.getState())) {
      String result = businessOpportunityService.createOrder(businessOpportunity.getId());
      JSONObject jsonObject = JSONObject.parseObject(result);
      if (jsonObject.containsKey("success")) {
        if (jsonObject.getBoolean("success")) {
          orderMessage = CommonUtil.getProperty(request, "message.create.order.success") + ", ";
          orderMessage += CommonUtil.getProperty(request, "message.create.order.id") + ": " + jsonObject.getInteger("order_id");
          success = true;
        } else {
          LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
          String lang = localeResolver.resolveLocale(request).getLanguage();
          orderMessage = "zh".equalsIgnoreCase(lang) ? jsonObject.getString("reason") : jsonObject.getString("reason_en");
        }
      } else { // token access denied orderMessage =
        CommonUtil.getProperty(request, "message.token.denied");
      }
    } else {
      orderMessage = CommonUtil.getProperty(request, "message.please.approve.advertiser").replaceAll("\\$ADVERTISER", client.getClientname());
    }

    redirectAttributes.addFlashAttribute("orderMessage", orderMessage);
    redirectAttributes.addFlashAttribute("success", success);
    return "redirect:/businessOpportunity/view/" + businessOpportunity.getId();
  }

  @RequestMapping(value = "delete/{id}", method = RequestMethod.GET)
  public String delete(@PathVariable("id") Long id, Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
    BusinessOpportunity businessOpportunity = businessOpportunityService.get(id);
    boolean success = false;
    String message_del = "";
    String alert_type = "alert-danger";
    if (businessOpportunity != null && businessOpportunity.getStatus() != null) {
      if (AuthUtils.hasAuthority(String.valueOf(businessOpportunity.getCreated_by()), String.valueOf(businessOpportunity.getOwner_sale()), businessOpportunity.getCooperate_sales(), request)) {
        if (businessOpportunityService.countOrdersByOpportunityId(id)>0) {
          message_del = CommonUtil.getProperty(request, "message.opportunity.delete.fail");
          success = false;
          alert_type = "alert-danger";
        } else {
          businessOpportunityService.delete(id);
          insertLog(null, Operate.DELETE.getIndex(), id, null, businessOpportunity.getName());
          message_del = CommonUtil.getProperty(request, "message.delete.opportunity.success").replace("{0}", businessOpportunity.getName()).replace("{1}", businessOpportunity.getNumber());
          success = true;
          alert_type = "alert-success";
        }
      } else {
        message_del = CommonUtil.getProperty(request, "message.opportunity.delete.rights");
        success = false;
        alert_type = "alert-danger";
      }
    }
    redirectAttributes.addFlashAttribute("alert_type", alert_type);
    redirectAttributes.addFlashAttribute("success", success);
    redirectAttributes.addFlashAttribute("message_del", message_del);
    return "redirect:/businessOpportunity";
  }

  @RequestMapping(value = "addProduct", method = RequestMethod.POST)
  public String addProduct(HttpServletRequest request, RedirectAttributes redirectAttributes) {
    redirectAttributes.addFlashAttribute("index", request.getParameter("index"));
    return "businessOpportunity/product";
  }

  @ModelAttribute
  public void getCustMaster(@RequestParam(value = "id", defaultValue = "-1") Long id, Model model) {
    if (id != -1) {
      model.addAttribute("businessOpportunity", businessOpportunityService.getModelAttribute(id));
    }
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
    log.setModule("menu.business.opportunity");

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

  private void setSelect(HttpServletRequest request, BusinessOpportunity businessOpportunity) {
    Long owner_sale_id = ((ShiroUser) SecurityUtils.getSubject().getPrincipal()).id;
    if (businessOpportunity.getOwner_sale() != null) {
      owner_sale_id = businessOpportunity.getOwner_sale();
    }
    User owner_sale_user = accountService.get(owner_sale_id);
    request.setAttribute("owner_sale_user", owner_sale_user);

    List<Long> cooperateSaleUserIds = new ArrayList<Long>();
    if (StringUtils.isNotBlank(businessOpportunity.getCooperate_sales())) {
      for (String userId : businessOpportunity.getCooperate_sales().split(",")) {
        cooperateSaleUserIds.add(Long.parseLong(userId));
      }
      List<User> cooperateSales = accountService.findUsersByUserIds(cooperateSaleUserIds);
      request.setAttribute("cooperate_sale_users", cooperateSales);
    }

    List<Product> products = productService.getAllProduct();
    request.setAttribute("products_data", products);
    List<ProductCategory> productCategories = productCategoryMapper.getList();
    request.setAttribute("productCategories", productCategories);
  }

  private ShiroUser getCurrUser() {
    Subject currentUser = SecurityUtils.getSubject();
    if (null == currentUser)
      return null;
    return (ShiroUser) currentUser.getPrincipal();
  }

}
