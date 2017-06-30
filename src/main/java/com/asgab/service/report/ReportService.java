package com.asgab.service.report;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

import com.alibaba.druid.sql.visitor.functions.Char;
import com.asgab.entity.*;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.*;
import org.apache.ibatis.annotations.Case;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.alibaba.fastjson.JSONArray;
import com.asgab.core.mail.MailUtil;
import com.asgab.repository.ReportMapper;
import com.asgab.repository.xmo.UserXMOMapper;
import com.asgab.service.account.AccountService;
import com.asgab.service.account.ShiroDbRealm.ShiroUser;
import com.asgab.service.exchange.ExchangeRateService;
import com.asgab.service.product.ProductCategoryService;
import com.asgab.service.setting.DataSharingService;
import com.asgab.util.CommonUtil;
import com.google.common.collect.Lists;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.xssf.usermodel.*;
import java.util.*;

@Component
@Transactional
public class ReportService {

  @Autowired
  ReportMapper reportMapper;

  @Resource
  UserXMOMapper userXMOMapper;

  @Autowired
  ExchangeRateService exchangeRateService;

  @Autowired
  DataSharingService dataSharingService;

  @Autowired
  AccountService accountService;

  @Autowired
  ProductCategoryService productCategoryService;

  @Resource
  private ReportSendingService reportSendingService;

  public final static Long ceoUserId = 46L;

  private static final String[] foreignLocals = {"Intl"};

  public final static String REPORT_UPLOAD_FOLDER = "/usr/upload/report/";
  //public final static String REPORT_UPLOAD_FOLDER = "/Users/jerry.li/upload/report/";
  protected org.apache.commons.logging.Log logger = LogFactory.getLog(getClass());


  public Map<String, Object> getReport(Report report, boolean isZH) {
    // 获取所有转成人民币的汇率
    List<ExchangeRate> exchangeRates = exchangeRateService.getCurrency2RMB();
    // 页面进度
    String val1 = report.getProgress().split(";")[0];
    String val2 = report.getProgress().split(";")[1];
    boolean searchOpportunity = false;
    boolean searchOrder = false;
    // 如果进度有100% 就查询订单
    if (val2.equalsIgnoreCase("100")) {
      searchOrder = true;
    }
    // 如果不是只有100%进度, 查询商机
    if (!val1.equalsIgnoreCase("100")) {
      searchOpportunity = true;
    }
    // 查询团队或者是销售代表都是userid判断
    if ("2".equalsIgnoreCase(report.getDataRight())) {
      report.setCondSaleRepresentative(report.getSaleTeam());
    } else {
      report.setCondSaleRepresentative(report.getSaleRepresentative());
    }
    // 数据权限
    if ("1".equalsIgnoreCase(report.getDataRight())) {
      report.setSaleRepresentative(getCurrentUser(false));
    } else if ("4".equalsIgnoreCase(report.getDataRight())) {
      report.setSaleRepresentative(getCurrentUser(false));
    } else if ("3".equalsIgnoreCase(report.getDataRight()) && report.getSaleRepresentative().size() == 0) {
      // 如果是查询销售代表. 为空则是当前登录用户, 否则就是页面条件. 页面条件在页面上已赋值.
      report.setSaleRepresentative(getCurrentUser(false));
    } else if ("2".equalsIgnoreCase(report.getDataRight())) {
      if (report.getSaleTeam().size() != 0) {
        // 如果查询团队, 查询的里面有管理员,就按照管理员处理, 在查询团队. 管理员按照ceo处理
        if (SecurityUtils.getSubject().hasRole("admin") || SecurityUtils.getSubject().hasRole("superAdmin")) {
          report.setSaleRepresentative(getCeoUser());
        } else {
          report.setSaleRepresentative(report.getSaleTeam());
        }
      } else {
        report.setSaleRepresentative(getCurrentUser(true));
      }
    } else {
      // 5 按照广告主来
      report.setSaleRepresentative(getCurrentUser(false));
    }

    Map<String, Object> map = null;
    if ("1".equalsIgnoreCase(report.getDataRight())) {
      map = getReportByProduct(report, exchangeRates, searchOpportunity, searchOrder, isZH);
    } else if ("4".equalsIgnoreCase(report.getDataRight())) {
      map = getReportByChannel(report, exchangeRates, searchOpportunity, searchOrder);
    } else if ("3".equalsIgnoreCase(report.getDataRight())) {
      map = getReportBySaleRepresentative(report, exchangeRates, searchOpportunity, searchOrder);
    } else if ("2".equalsIgnoreCase(report.getDataRight())) {
      map = getReportBySaleTeam(report, exchangeRates, searchOpportunity, searchOrder);
    } else if ("5".equalsIgnoreCase(report.getDataRight())) {
      map = getReportByAdvertiser(report, exchangeRates, searchOpportunity, searchOrder, isZH);
    }
    return map;
  }

  /**
   *
   * @param report 页面条件
   * @param exchangeRates 汇率
   * @param searchOpportunity 是否需要查询商机
   * @param searchOrder 是否需要查询订单
   * @return
   */
  public Map<String, Object> getReportByProduct(Report report, List<ExchangeRate> exchangeRates, boolean searchOpportunity, boolean searchOrder, boolean isZH) {
    List<Report> opportunityReports = new ArrayList<Report>();
    List<Report> orderReports = new ArrayList<Report>();
    if (searchOpportunity) {
      opportunityReports = getOpportunitysByProduct(report, exchangeRates);
    }
    if (searchOrder) {
      orderReports = getOrdersByProduct(report, exchangeRates);
    }
    for (Report tmp : orderReports) {
      System.out.println(tmp.getOrder_id() + "," + tmp.getBudget() + "," + tmp.getProduct_name(true) + "," + tmp.getClientname());
    }
    // 是否是查询GP
    boolean calGP = "2".equalsIgnoreCase(report.getMetric());
    // 按照产品分类统计
    sumReport(opportunityReports, "product", calGP);
    sumReport(orderReports, "product", calGP);
    // 格式化最后数据, 方便页面显示
    return formatProductReport(opportunityReports, orderReports, isZH);
  }

  /**
   * 根据广告主 和根据product公用一个查询
   */
  public Map<String, Object> getReportByAdvertiser(Report report, List<ExchangeRate> exchangeRates, boolean searchOpportunity, boolean searchOrder, boolean isZH) {
    List<Report> opportunityReports = new ArrayList<Report>();
    List<Report> orderReports = new ArrayList<Report>();
    if (searchOpportunity) {
      opportunityReports = getOpportunitysByProduct(report, exchangeRates);
    }
    if (searchOrder) {
      orderReports = getOrdersByProduct(report, exchangeRates);
    }

    // 是否是查询GP
    boolean calGP = "2".equalsIgnoreCase(report.getMetric());
    // 按照产品分类统计
    sumReport(opportunityReports, "advertiser", calGP);
    sumReport(orderReports, "advertiser", calGP);
    // 格式化最后数据, 方便页面显示
    return formatAdvertiser(opportunityReports, orderReports);
  }

  // 和销售代表一样的. 只不过显示的时候用销售团队的名字
  public Map<String, Object> getReportBySaleTeam(Report report, List<ExchangeRate> exchangeRates, boolean searchOpportunity, boolean searchOrder) {
    List<Report> opportunityReports = new ArrayList<Report>();
    List<Report> orderReports = new ArrayList<Report>();
    if (searchOpportunity) {
      opportunityReports = getOpportunityBySaleTeam(report, exchangeRates);
    }
    if (searchOrder) {
      orderReports = getOrderBySaleTeam(report, exchangeRates);
    }
    boolean calGP = "2".equalsIgnoreCase(report.getMetric());
    if (calGP) {
      opportunityReports = mergeReport(opportunityReports);
      orderReports = mergeReport(orderReports);
    }
    // 有些是合作销售的. 需要先算先算小计.
    // 签约订单小计
    BigDecimal orderSum = getSaleSum(orderReports);
    // 商机小计
    BigDecimal opportunitySum = getSaleSum(opportunityReports);
    sumReport(opportunityReports, "saleTeam", false);
    sumReport(orderReports, "saleTeam", false);
    Map<String, Object> map = formatSaleTeam(opportunityReports, orderReports);
    map.put("orderSum", orderSum);
    map.put("opportunitySum", opportunitySum);
    return map;
  }

  /**
   * 根据销售代表
   */
  public Map<String, Object> getReportBySaleRepresentative(Report report, List<ExchangeRate> exchangeRates, boolean searchOpportunity, boolean searchOrder) {
    List<Report> opportunityReports = new ArrayList<Report>();
    List<Report> orderReports = new ArrayList<Report>();
    if (searchOpportunity) {
      opportunityReports = getOpportunityBySaleRepresentative(report, exchangeRates);
    }
    if (searchOrder) {
      orderReports = getOrderBySaleRepresentative(report, exchangeRates);
    }
    boolean calGP = "2".equalsIgnoreCase(report.getMetric());
    if (calGP) {
      opportunityReports = mergeReport(opportunityReports);
      orderReports = mergeReport(orderReports);
    }
    // 有些是合作销售的. 这么先算小计.
    // 签约订单小计
    BigDecimal orderSum = getSaleSum(orderReports);
    // 商机小计
    BigDecimal opportunitySum = getSaleSum(opportunityReports);
    sumReport(opportunityReports, "saleRepresentative", false);
    sumReport(orderReports, "saleRepresentative", false);
    for (Report r : orderReports) {
      if ("roger.fan".equalsIgnoreCase(r.getUser_name())) {
        System.out.println(r.getUser_id() + "------" + r.getUser_name());
      }
    }
    Map<String, Object> map = formatSaleRepresentative(opportunityReports, orderReports);
    map.put("orderSum", orderSum);
    map.put("opportunitySum", opportunitySum);
    return map;
  }

  /**
   * 根据渠道
   */
  public Map<String, Object> getReportByChannel(Report report, List<ExchangeRate> exchangeRates, boolean searchOpportunity, boolean searchOrder) {
    List<Report> opportunityReports = new ArrayList<Report>();
    List<Report> orderReports = new ArrayList<Report>();
    if (searchOpportunity) {
      opportunityReports = getOpportunitysByChannel(report, exchangeRates);
    }
    if (searchOrder) {
      orderReports = getOrdersByChannel(report, exchangeRates);
    }
    boolean calGP = "2".equalsIgnoreCase(report.getMetric());
    if (calGP) {
      opportunityReports = mergeReport(opportunityReports);
      orderReports = mergeReport(orderReports);
    }
    sumReport(opportunityReports, "channel", false);
    sumReport(orderReports, "channel", false);
    return formatChannelReport(opportunityReports, orderReports);
  }

  private void exchangeRates(List<Report> reports, List<ExchangeRate> exchangeRates) {
    if (reports == null || (reports != null && reports.size() == 0)) {
      return;
    }
    for (Report o : reports) {
      boolean findBudgetCurrency = false;
      boolean findPublicPriceCurrency = false;
      for (ExchangeRate e : exchangeRates) {
        // 如果找到汇率就转换
        if (!findBudgetCurrency && "HKD".equalsIgnoreCase(o.getCurrency()) && "RMB".equalsIgnoreCase(e.getBase_currency()) && o.getCurrency().equalsIgnoreCase(e.getCurrency())) {
          // budget
          o.setBudget(o.getBudget().divide(new BigDecimal(e.getRate()), 2, BigDecimal.ROUND_HALF_EVEN));
          // totalBudget
          o.setTotalBudget(o.getTotalBudget().divide(new BigDecimal(e.getRate()), 2, BigDecimal.ROUND_HALF_EVEN));
          // cost 售价
          if (o.getCost() != null) {
            o.setCost(o.getCost().divide(new BigDecimal(e.getRate()), 2, BigDecimal.ROUND_HALF_EVEN));
          }
          findBudgetCurrency = true;
        }
        // 如果不是港币
        else if (!findBudgetCurrency && !"HKD".equalsIgnoreCase(o.getCurrency()) && o.getCurrency().equalsIgnoreCase(e.getBase_currency()) && "RMB".equalsIgnoreCase(e.getCurrency())) {
          o.setBudget(new BigDecimal(e.getRate()).multiply(o.getBudget()));
          o.setTotalBudget(new BigDecimal(e.getRate()).multiply(o.getTotalBudget()));
          if (o.getCost() != null) {
            o.setCost(new BigDecimal(e.getRate()).multiply(o.getCost()));
          }
          findBudgetCurrency = true;
        }

        // public_price汇率转换
        if (o.getProduct_currency() == null) {
          continue;
        }
        if (!findPublicPriceCurrency && "HKD".equalsIgnoreCase(o.getProduct_currency()) && "RMB".equalsIgnoreCase(e.getBase_currency()) && o.getProduct_currency().equalsIgnoreCase(e.getCurrency())) {
          o.setPublic_price(o.getPublic_price().divide(new BigDecimal(e.getRate()), 2, BigDecimal.ROUND_HALF_EVEN));
          findPublicPriceCurrency = true;
        } else if (!findPublicPriceCurrency && !"HKD".equalsIgnoreCase(o.getProduct_currency()) && o.getProduct_currency().equalsIgnoreCase(e.getBase_currency()) && "RMB".equalsIgnoreCase(e.getCurrency())) {
          o.setPublic_price(new BigDecimal(e.getRate()).multiply(o.getPublic_price()));
          findPublicPriceCurrency = true;
        }
      }

    }
  }

  /**
   * 计算GP才会调用 订单/商机的合并. 同一个订单, 同一个商机需要合并成一条计算GP. 计算GP的时候, 因为需要每一个订单的广告分配计算. 所以计算GP需要在合并的时候计算
   *
   * @param reports
   * @return
   */
  private List<Report> mergeReport(List<Report> reports) {
    if (reports == null || reports != null && reports.size() == 0) {
      return new ArrayList<Report>();
    }
    List<Report> targetReport = new ArrayList<Report>();
    // 首先按照order_id , opportunity_id分组. 同类合并.
    // 所有的订单
    List<String> orderIds = new ArrayList<String>();
    // 所有的商机
    List<String> opportunityIds = new ArrayList<String>();
    for (Report report : reports) {
      if (report.getOrder_id() != null) {
        orderIds.add(String.valueOf(report.getOrder_id()));
      } else if (report.getOpportunity_id() != null) {
        opportunityIds.add(String.valueOf(report.getOpportunity_id()));
      }
    }
    CommonUtil.distinctList(orderIds);
    CommonUtil.distinctList(opportunityIds);

    // 订单是国内还是国外, true为国内. false国外
    Map<Long, Boolean> localMap = getLocalMap(reports);

    // 同一个订单合并计算GP
    for (String orderId : orderIds) {
      Report report = null;
      for (int i = 0; i < reports.size(); i++) {
        if (orderId.equals(String.valueOf(reports.get(i).getOrder_id()))) {
          Report tmpReport = new Report();
          // 复制一份report. 防止源数据被修改
          cpReport(reports.get(i), tmpReport);
          // 计算GP
          calGP(tmpReport, localMap);
          // 订单budget合并计算
          if (report == null) {
            report = tmpReport;
          } else {
            report.setBudget(report.getBudget().add(tmpReport.getBudget()));
          }
        }
      }

      if (report != null) {
        // 计算GP 后面一部分
        calGP2(report);
        targetReport.add(report);
      }
    }

    for (String opportunityId : opportunityIds) {
      Report report = null;
      for (int i = 0; i < reports.size(); i++) {
        if (opportunityId.equals(String.valueOf(reports.get(i).getOpportunity_id()))) {
          Report tmpReport = new Report();
          // 复制一份
          cpReport(reports.get(i), tmpReport);
          // 计算GP
          calGP(tmpReport, localMap);
          // 商机合并
          if (report == null) {
            report = tmpReport;
          } else {
            report.setBudget(report.getBudget().add(tmpReport.getBudget()));
          }
        }
      }
      // 计算GP 后面一部分 减去
      if (report != null) {
        calGP2(report);
        targetReport.add(report);
      }
    }

    return targetReport;
  }

  private Map<Long, Boolean> getLocalMap(List<Report> reports) {
    Map<Long, Boolean> localMap = new HashMap<Long, Boolean>();
    for (Report report : reports) {
      Long id = null;
      if (report.getOrder_id() != null) {
        id = report.getOrder_id();
      } else {
        id = report.getOpportunity_id();
      }

      Boolean orderLocal = localMap.get(id);
      if (orderLocal == null) {
        localMap.put(id, isCN(report));
      } else {
        // 如果map里面是国内. 当前是国外. 修改map为国外
        if (orderLocal && !isCN(report)) {
          localMap.put(id, false);
        }
      }
    }
    return localMap;
  }

  private void cpReportList(List<Report> sourceReports, List<Report> targetReports) {
    if (targetReports == null) {
      targetReports = new ArrayList<Report>();
    }
    if (sourceReports != null && sourceReports.size() > 0) {
      for (Report sourceReport : sourceReports) {
        Report targetReport = new Report();
        cpReport(sourceReport, targetReport);
        targetReports.add(targetReport);
      }
    }
  }

  /**
   * 复制一份report
   *
   * @param sourceReport 源数据
   * @param targetReport 目标数据
   */
  private void cpReport(Report sourceReport, Report targetReport) {
    if (targetReport == null) {
      targetReport = new Report();
    }
    targetReport.setAd_platform(sourceReport.getAd_platform());
    targetReport.setBudget(sourceReport.getBudget());
    targetReport.setCalGP(sourceReport.getCalGP());
    targetReport.setClient_id(sourceReport.getClient_id());
    targetReport.setClientname(sourceReport.getClientname());
    targetReport.setChannel(sourceReport.getChannel());
    targetReport.setChannel_name(sourceReport.getChannel_name());
    targetReport.setCost(sourceReport.getCost());
    targetReport.setCurrency(sourceReport.getCurrency());
    targetReport.setProduct_currency(sourceReport.getProduct_currency());
    targetReport.setFloor_discount(sourceReport.getFloor_discount());
    targetReport.setGp(sourceReport.getGp());
    targetReport.setIs_distribute_gp(sourceReport.getIs_distribute_gp());
    targetReport.setGp_evaluate(sourceReport.getGp_evaluate());
    targetReport.setIncomeType(sourceReport.getIncomeType());
    targetReport.setLocal(sourceReport.getLocal());
    targetReport.setMetric(sourceReport.getMetric());
    targetReport.setOpportunity_id(sourceReport.getOpportunity_id());
    targetReport.setOrder_id(sourceReport.getOrder_id());
    targetReport.setOrderType(sourceReport.getOrderType());
    targetReport.setProduct_id(sourceReport.getProduct_id());
    targetReport.setProduct_nameZh(sourceReport.getProduct_nameZh());
    targetReport.setProduct_nameEn(sourceReport.getProduct_nameEn());
    targetReport.setPublic_price(sourceReport.getPublic_price());
    targetReport.setRebate(sourceReport.getRebate());
    targetReport.setRegional(sourceReport.getRegional());
    targetReport.setSaleRepresentative(sourceReport.getSaleRepresentative());
    targetReport.setSaleTeam(sourceReport.getSaleTeam());
    targetReport.setService_charges_scale(sourceReport.getService_charges_scale());
    targetReport.setTeam_id(sourceReport.getTeam_id());
    targetReport.setTeam_name(sourceReport.getTeam_name());
    targetReport.setTotalBudget(sourceReport.getTotalBudget());
    targetReport.setUser_id(sourceReport.getUser_id());
    targetReport.setUser_ids(sourceReport.getUser_ids());
    targetReport.setUser_name(sourceReport.getUser_name());
    targetReport.setShare_user_ids(sourceReport.getShare_user_ids());
    targetReport.setShare_user_names(sourceReport.getShare_user_names());
    targetReport.setWhether_service(sourceReport.getWhether_service());
    targetReport.setOrder_code(sourceReport.getOrder_code());
    targetReport.setExpenses(sourceReport.getExpenses());
  }

  /**
   * 按照类型分类统计. 类型有产品, 销售团队, 销售代表, 渠道(channel), 广告类型(用于饼图), 广告主
   *
   * @param reports
   * @param type
   * @param calGp 是否计算GP. 只有product计算GP的时候. 才为true
   */
  private void sumReport(List<Report> reports, String type, boolean calGp) {
    if (reports == null || (reports != null && reports.size() == 0)) {
      return;
    }
    // 如果是按照销售. 有多个销售.则每个都需要算, userIds逗号分隔的数据,都变变成一个个report
    if ("saleRepresentative".equalsIgnoreCase(type) || "saleTeam".equalsIgnoreCase(type)) {
      dispersedEachUsers(reports, type);
    }

    Map<Long, Boolean> localMap = getLocalMap(reports);

    // 如果是产品||adtype&算GP, 先全部算GP. 然后下面就直接同产品相加
    if (("adType".equalsIgnoreCase(type) || "product".equalsIgnoreCase(type) || "advertiser".equalsIgnoreCase(type)) && calGp) {
      for (Report report : reports) {
        report.setBudget(calGp4Product(report, localMap));
      }
    }

    // 按照类型进行合并
    for (int i = reports.size() - 1; i >= 0; i--) {
      Report ri = reports.get(i);
      for (int j = i - 1; j >= 0; j--) {
        Report rj = reports.get(j);
        if ("product".equalsIgnoreCase(type)) {
          if ((ri.getProduct_id() != null && rj.getProduct_id() != null && ri.getProduct_id().longValue() == rj.getProduct_id().longValue()) || (ri.getProduct_id() == null && rj.getProduct_id() == null)) {
            rj.setBudget(rj.getBudget().add(ri.getBudget()));
            // 删掉ri
            reports.remove(ri);
            break;
          }
        } else if ("channel".equalsIgnoreCase(type)) {
          if (ri.getChannel().equals(rj.getChannel())) {
            rj.setBudget(rj.getBudget().add(ri.getBudget()));
            // 删掉ri
            reports.remove(ri);
            break;
          }
        } else if ("saleRepresentative".equalsIgnoreCase(type)) {
          if (ri.getUser_id().equals(rj.getUser_id())) {
            rj.setBudget(rj.getBudget().add(ri.getBudget()));
            // 删掉
            reports.remove(ri);
            break;
          }
        } else if ("saleTeam".equalsIgnoreCase(type)) {
          if ((ri.getTeam_name() != null && rj.getTeam_name() != null && ri.getTeam_name().equalsIgnoreCase(rj.getTeam_name())) || (ri.getTeam_name() == null && rj.getTeam_name() == null)) {
            rj.setBudget(rj.getBudget().add(ri.getBudget()));
            // 删掉
            reports.remove(ri);
            break;
          }
        } else if ("adType".equalsIgnoreCase(type)) {
          if (ri.getAd_platform().equalsIgnoreCase(rj.getAd_platform())) {
            rj.setBudget(rj.getBudget().add(ri.getBudget()));
            // 删掉
            reports.remove(ri);
            break;
          }
        } else if ("advertiser".equalsIgnoreCase(type)) {
          if ((ri.getClient_id() != null && rj.getClient_id() != null && ri.getClient_id().longValue() == rj.getClient_id().longValue()) || (ri.getClient_id() == null && rj.getClient_id() == null)) {
            rj.setBudget(rj.getBudget().add(ri.getBudget()));
            // 删掉
            reports.remove(ri);
            break;
          }
        }

      }
    }

  }

  /**
   * product计算GP
   *
   * @param
   * @return 国内单 产品1 Total GP= 产品1GP%
   *         *[（订单1的产品1budget*（1-订单1的返点%）+订单2的产品1budget*（1-订单2的返点%）+…+订单n的产品1budget*（1-订单n的返点%）] 海外单
   *         产品1 Total GP=[（订单1的产品1售价-订单1的产品1底价）x
   *         （订单1的产品1预算÷订单1的产品1售价）-订单1的产品1budget*当前日期下返点%]+[（订单2的产品1售价-订单2的产品1底价）x
   *         （订单2的产品1预算÷订单2的产品1售价）]-订单2的产品1budget*当前日期下返点%]+…+[（订单n的产品1售价-订单1的产品1底价）x
   *         （订单n的产品1预算÷订单1的产品1售价）-订单n的产品1budget*当前日期下返点%]
   */
  private BigDecimal calGp4Product(Report report, Map<Long, Boolean> localMap) {
    Long id = report.getOrder_id() != null ? report.getOrder_id() : report.getOpportunity_id();
    BigDecimal result = BigDecimal.ZERO;
    // 服务类
    if (report.getWhether_service().equals(1)) {
      if (report.getOpportunity_id() != null) {
        result = report.getTotalBudget().multiply(new BigDecimal(0.5));
      } else if (report.getService_charges_scale() == null) {
        result = report.getTotalBudget();
      } else {
        result = report.getTotalBudget().multiply(new BigDecimal(report.getService_charges_scale() / 100));
      }
    } else {
      // 非服务类
      if (localMap.get(id)) {
        // 国内 产品1GP%*产品1budget*(1-订单1的返点%）
        BigDecimal tmpVal = new BigDecimal(1).subtract(new BigDecimal(report.getRebate() / 100));
        result = new BigDecimal(getReportGp(report) / 100).multiply(report.getBudget()).multiply(tmpVal);
      } else {
        // 海外 （订单1的产品1售价-订单1的产品1底价）x（订单1的产品1预算÷订单1的产品1售价）-订单1的产品1budget*当前日期下返点%
        if (report.getPublic_price() != null && report.getFloor_discount() != null && report.getCost().compareTo(BigDecimal.ZERO) != 0) {
          BigDecimal val1 = report.getCost().subtract(report.getPublic_price().multiply(report.getFloor_discount()));
          BigDecimal val2 = report.getBudget().divide(report.getCost(), 2, BigDecimal.ROUND_HALF_EVEN);
          BigDecimal val3 = report.getBudget().multiply(new BigDecimal(report.getRebate() / 100));
          result = val1.multiply(val2).subtract(val3);
        } else {
          result = report.getBudget().multiply(new BigDecimal(0.5));
        }
      }
    }
    return result;
  }

  /**
   * 如果是销售团队, 销售代表. 则每个参与的都有自己的一份 eg. 订单10001 有销售1055,1056参与. 那么对于1055来说拥有10001这个订单, 对于1056同样也拥有这个订单
   * 在按照销售代表或者是销售团队分组. 那么10001就变成了2条数据. 这里处理订单/商机多销售, 按照销售代表/销售团队分组
   *
   * @param reports
   * @param type
   */
  private void dispersedEachUsers(List<Report> reports, String type) {
    List<Report> additionReports = new ArrayList<Report>();
    for (int i = reports.size() - 1; i >= 0; i--) {
      if (StringUtils.isNoneBlank(reports.get(i).getUser_ids())) {
        String user_ids = reports.get(i).getUser_ids();
        String[] userIds = user_ids.split(",");
        // 获取订单的所有销售
        List<Long> userIdList = new ArrayList<Long>();
        if (userIds != null && userIds.length > 0) {
          for (String userId : userIds) {
            userIdList.add(Long.parseLong(userId));
          }
        }
        // 这里防止销售和合作销售有重复数据
        CommonUtil.distinctLongList(userIdList);
        // 如果是团队, 是在datasharing表查询
        List<DataSharing> dataSharings = null;
        // 如果是销售代表, 就在xmo.users表
        List<User> users = null;
        if ("saleTeam".equalsIgnoreCase(type)) {
          dataSharings = dataSharingService.getTeamByUserIds(userIdList);
        } else if ("saleRepresentative".equalsIgnoreCase(type)) {
          users = accountService.findUsersByUserIds(userIdList);
        }

        for (int j = 0; j < userIdList.size(); j++) {
          Report tmpReport = new Report();
          cpReport(reports.get(i), tmpReport);
          tmpReport.setUser_id(userIdList.get(j));

          // 要重新查询赋值对应的名字.数据库查出来的名字的对的. 但是顺序无法确认. (数据库查询名字那个字段可以去掉).
          if ("saleTeam".equalsIgnoreCase(type)) {
            tmpReport.setTeam_id(tmpReport.getUser_id());
            tmpReport.setTeam_name(getTeamNameByDataSharings(dataSharings, tmpReport.getUser_id()));
          }
          // 如果是销售代表. 需要赋值user_name
          else if ("saleRepresentative".equalsIgnoreCase(type)) {
            tmpReport.setUser_name(getUserNameByUsers(users, tmpReport.getUser_id()));
          }
          additionReports.add(tmpReport);
        }
        reports.remove(i);
      } else {
        reports.get(i).setUser_id(Long.parseLong(reports.get(i).getUser_ids()));
      }
    }
    reports.addAll(additionReports);
  }

  /**
   * 根据用户ID, 查询用户
   *
   * @param users
   * @param user_id
   * @return
   */
  private String getUserNameByUsers(List<User> users, Long user_id) {
    if (user_id != null && users != null && users.size() > 0) {
      for (User user : users) {
        if (user.getId().equals(user_id)) {
          return user.getName();
        }
      }
    }
    return "";
  }

  /**
   * 根据用户ID 查询团队
   *
   * @param dataSharings
   * @param user_id
   * @return
   */
  private String getTeamNameByDataSharings(List<DataSharing> dataSharings, Long user_id) {
    if (user_id != null && dataSharings != null && dataSharings.size() > 0) {
      for (DataSharing dataSharing : dataSharings) {
        if (dataSharing.getUser_id().equals(user_id)) {
          return dataSharing.getTeam_name();
        }
      }
    }
    return "";
  }

  // 1:服务类
  // 2:非服务类 国内
  // 3:非服务类 国外
  private void calGP(Report report, Map<Long, Boolean> localMap) {
    Long id = report.getOrder_id() != null ? report.getOrder_id() : report.getOpportunity_id();
    if (report.getWhether_service().equals(1)) {
      // 服务类 服务类在calGP2 计算
      report.setCalGP(1);
    } else {
      // 非服务类
      // 如果是国内
      if (localMap.get(id)) {
        // 1. 国内单 Total GP＝（产品1 budget×产品1 GP%+产品2 budget×产品2 GP%+…+产品n
        // budget×产品n
        // GP%）－总budget×当前日期下返点%）
        report.setBudget(report.getBudget().multiply(new BigDecimal(getReportGp(report) / 100)));
        // 非服务类 国内
        report.setCalGP(2);
      } else {
        // 2. 海外单 Total GP＝（产品1售价-产品1底价）x （产品1预算÷产品1售价）+（产品2售价-产品2底价）x
        // （产品2预算÷产品2售价）+…+
        // （产品n售价-产品n底价）x （产品n预算÷产品n售价）－总budget×当前日期下返点%
        // 非服务类 国外 售价=cost 底价public_price*floor_discount 预算=budget
        // 如果是其他产品类型. 可能这个都为空
        if (report.getPublic_price() != null && report.getFloor_discount() != null) {
          // 如果售价为空, 不按照公式,直接算50%,
          if (report.getCost().compareTo(BigDecimal.ZERO) == 0) {
            report.setBudget(report.getBudget().multiply(new BigDecimal(0.5)));
          } else {
            BigDecimal val1 = report.getCost().subtract(report.getPublic_price().multiply(report.getFloor_discount()));
            BigDecimal val2 = report.getBudget().divide(report.getCost(), 2, BigDecimal.ROUND_HALF_EVEN);
            report.setBudget(val1.multiply(val2));
          }
        } else {
          // 按照50%算
          report.setBudget(report.getBudget().multiply(new BigDecimal(0.5)));
        }
        report.setCalGP(3);
      }
    }
  }

  private boolean isCN(Report report) {
    boolean isCN = true;
    if (StringUtils.isBlank(report.getLocal())) {
      isCN = true;
    } else {
      for (String local : foreignLocals) {
        if (report.getLocal().toLowerCase().contains(local.toLowerCase())) {
          isCN = false;
          break;
        }
      }
    }
    return isCN;
  }

  /**
   * a. 当国内单下的产品分量中，某些产品选择了“支持GP媒体分量”选项的，该产品的GP%以人工输入的GP%计，没有选择“支持GP媒体分量”的，
   * 该产品的GP%默认以后台提供的“分产品GP%映射表”计 服务类商机. GP都是50%. 在获取数据的时候处理. 这里不需要考虑
   *
   * @param report
   * @return
   */
  private Double getReportGp(Report report) {
    if (report.getIs_distribute_gp() != null && report.getIs_distribute_gp().equals(1) && report.getGp_evaluate() != null) {
      return report.getGp_evaluate();
    }
    return report.getGp();
  }

  // 1:服务类
  // 2:非服务类 国内
  // 3:非服务类 国外
  private void calGP2(Report report) {
    if (report.getCalGP() == 1) {
      // a. 对于服务类商机尚未走到订单步骤（即没有服务类订单关联到商机时），商机的收入以总预算计，GP以预算*50%计
      // b. 当订单以一个总体预算表示营收和GtP时（没有提供服务费率百分比），在报表和分析上收入/GP都以总体预算表示；
      // c. 当订单有总体预算，又提供服务费率百分比时，收入以总体预算表示，GP以（总体预算*服务费率）表示
      if (report.getOpportunity_id() != null) {
        report.setBudget(report.getTotalBudget().multiply(new BigDecimal(0.5)));
      } else if (report.getService_charges_scale() == null) {
        report.setBudget(report.getTotalBudget());
      } else {
        report.setBudget(report.getTotalBudget().multiply(new BigDecimal(report.getService_charges_scale() / 100)));
      }
    } else if (report.getCalGP() == 2) {
      // -总budget×当前日期下返点%
      report.setBudget(report.getBudget().subtract(report.getTotalBudget().multiply(new BigDecimal(report.getRebate() / 100))));
    } else if (report.getCalGP() == 3) {
      // －总budget×当前日期下返点% , 如果为0就算50%.这部分也不用减了
      if (report.getCost().compareTo(BigDecimal.ZERO) != 0) {
        report.setBudget(report.getBudget().subtract(report.getTotalBudget().multiply(new BigDecimal(report.getRebate() / 100))));
      }
    }
  }

  /**
   * 格式化数据, 方便页面显示
   */
  private Map<String, Object> formatProductReport(List<Report> listA, List<Report> listB, boolean isZH) {
    Map<String, Object> map = new TreeMap<String, Object>();
    // 获取
    List<Long> productList = new ArrayList<Long>();
    Map<Long, String> productMap = new HashMap<Long, String>();
    // 获取所有的产品
    for (Report tmp : listA) {
      if (StringUtils.isNoneBlank(tmp.getProduct_name(isZH))) {
        productList.add(tmp.getProduct_id());
        productMap.put(tmp.getProduct_id(), tmp.getProduct_name(isZH));
      }
    }
    for (Report tmp : listB) {
      if (StringUtils.isNoneBlank(tmp.getProduct_name(isZH))) {
        productList.add(tmp.getProduct_id());
        productMap.put(tmp.getProduct_id(), tmp.getProduct_name(isZH));
      }
    }
    // 去重复
    CommonUtil.distinctLongList(productList);
    List<Report> targetReportOpportunity = new ArrayList<Report>();
    List<Report> targetReportOrder = new ArrayList<Report>();

    // 按照每一个产品进行填充, 空缺的补上null. 保证订单和商机的size和产品顺序一样
    for (Long product_id : productList) {
      boolean findA = false;
      boolean findB = false;
      for (Report tmp : listA) {
        if (product_id.equals(tmp.getProduct_id())) {
          targetReportOpportunity.add(tmp);
          findA = true;
          break;
        }
      }
      if (!findA) {
        targetReportOpportunity.add(null);
      }

      for (Report tmp : listB) {
        if (product_id.equals(tmp.getProduct_id())) {
          targetReportOrder.add(tmp);
          findB = true;
          break;
        }
      }
      if (!findB) {
        targetReportOrder.add(null);
      }
    }
    // 把ID 转成名字
    List<String> productNames = new ArrayList<String>();
    for (Long s : productList) {
      productNames.add(productMap.get(s));
    }
    map.put("names", productNames);
    map.put("opportunityReports", targetReportOpportunity);
    map.put("orderReports", targetReportOrder);

    return map;
  }

  /**
   * 格式化数据, 方便页面显示
   */
  private Map<String, Object> formatAdvertiser(List<Report> listA, List<Report> listB) {
    Map<String, Object> map = new TreeMap<String, Object>();
    // 获取
    List<Long> clientList = new ArrayList<Long>();
    Map<Long, String> clientMap = new HashMap<Long, String>();
    // 获取所有的广告主
    for (Report tmp : listA) {
      clientList.add(tmp.getClient_id());
      clientMap.put(tmp.getClient_id(), tmp.getClientname());
    }
    for (Report tmp : listB) {
      clientList.add(tmp.getClient_id());
      clientMap.put(tmp.getClient_id(), tmp.getClientname());
    }

    // 去重复
    CommonUtil.distinctLongList(clientList);
    List<Report> targetReportOpportunity = new ArrayList<Report>();
    List<Report> targetReportOrder = new ArrayList<Report>();

    // 按照每一个产品进行填充, 空缺的补上null. 保证订单和商机的size和产品顺序一样
    for (Long client_id : clientList) {
      boolean findA = false;
      boolean findB = false;
      for (Report tmp : listA) {
        if (client_id != null && tmp.getClient_id() != null && (client_id.equals(tmp.getClient_id()))) {
          targetReportOpportunity.add(tmp);
          findA = true;
          break;
        }
      }
      if (!findA) {
        targetReportOpportunity.add(null);
      }

      for (Report tmp : listB) {
        if (client_id != null && tmp.getClient_id() != null && (client_id.equals(tmp.getClient_id()))) {
          targetReportOrder.add(tmp);
          findB = true;
          break;
        }
      }
      if (!findB) {
        targetReportOrder.add(null);
      }
    }
    // 把ID 转成名字
    List<String> clientNames = new ArrayList<String>();
    for (Long s : clientList) {
      clientNames.add(clientMap.get(s));
    }
    // 如果名字有重复. 名字前面加*号
    HashMap<String, String> tmpMap = new HashMap<String, String>();
    List<String> multiClientNames = new ArrayList<String>();
    for (String clientName : clientNames) {
      if (tmpMap.containsKey(clientName)) {
        multiClientNames.add(clientName);
      } else {
        tmpMap.put(clientName, clientName);
      }
    }
    for (int i = 0; i < clientNames.size(); i++) {
      if (CommonUtil.containValue(multiClientNames, clientNames.get(i))) {
        clientNames.set(i, "*" + clientNames.get(i));
      }
    }
    map.put("names", clientNames);
    map.put("opportunityReports", targetReportOpportunity);
    map.put("orderReports", targetReportOrder);

    return map;
  }

  /**
   * 格式化销售团队的数据
   */
  private Map<String, Object> formatSaleTeam(List<Report> listA, List<Report> listB) {
    Map<String, Object> map = new TreeMap<String, Object>();
    // 获取
    List<String> teamList = new ArrayList<String>();
    // 如果是管理员或者是ceo, 没有other, 如果团队名字为空. 就用人名
    // boolean isCeo = isAdmin();
    boolean notEmptyTeamName = false;
    for (Report tmp : listA) {
      if (tmp.getTeam_id() != null) {
        // notEmptyTeamName==true, 团队名字为空. 用人名
        if (notEmptyTeamName && StringUtils.isBlank(tmp.getTeam_name())) {
          User teamUser = accountService.get(tmp.getTeam_id());
          tmp.setTeam_name(teamUser.getName());
        }

        if (StringUtils.isNotBlank(tmp.getTeam_name())) {
          teamList.add(tmp.getTeam_name());
        } else {
          teamList.add("");
        }
      } else {
        // 为空
        teamList.add("");
      }

    }
    for (Report tmp : listB) {
      if (tmp.getTeam_id() != null) {
        // notEmptyTeamName==true, 团队名字为空. 用人名
        if (notEmptyTeamName && StringUtils.isBlank(tmp.getTeam_name())) {
          User teamUser = accountService.get(tmp.getTeam_id());
          tmp.setTeam_name(teamUser.getName());
        }

        if (StringUtils.isNotBlank(tmp.getTeam_name())) {
          teamList.add(0, tmp.getTeam_name());
        } else {
          teamList.add("");
        }
      } else {
        teamList.add("");
      }
    }

    CommonUtil.distinctList(teamList);;

    List<Report> targetReportOpportunity = new ArrayList<Report>();
    List<Report> targetReportOrder = new ArrayList<Report>();
    for (String team_name : teamList) {
      boolean findA = false;
      boolean findB = false;
      for (Report tmp : listA) {
        if ((team_name.equals(tmp.getTeam_name())) || (StringUtils.isBlank(team_name) && StringUtils.isBlank(tmp.getTeam_name()))) {
          targetReportOpportunity.add(tmp);
          findA = true;
          break;
        }
      }
      if (!findA) {
        targetReportOpportunity.add(null);
      }

      for (Report tmp : listB) {
        if ((team_name.equals(tmp.getTeam_name())) || (StringUtils.isBlank(team_name) && StringUtils.isBlank(tmp.getTeam_name()))) {
          targetReportOrder.add(tmp);
          findB = true;
          break;
        }
      }
      if (!findB) {
        targetReportOrder.add(null);
      }
    }

    // 名字为空的转成其他
    for (int i = 0; i < teamList.size(); i++) {
      if (StringUtils.isBlank(teamList.get(i))) {
        teamList.set(i, "Other");
      }
    }
    map.put("names", teamList);
    map.put("opportunityReports", targetReportOpportunity);
    map.put("orderReports", targetReportOrder);

    return map;
  }

  private Map<String, Object> formatSaleRepresentative(List<Report> listA, List<Report> listB) {
    Map<String, Object> map = new TreeMap<String, Object>();
    // 获取
    List<Long> userList = new ArrayList<Long>();
    Map<Long, String> userMap = new HashMap<Long, String>();
    for (Report tmp : listA) {
      if (tmp.getUser_id() != null) {
        userList.add(tmp.getUser_id());
        userMap.put(tmp.getUser_id(), tmp.getUser_name());
      }
    }
    for (Report tmp : listB) {
      if (tmp.getUser_id() != null) {
        userList.add(tmp.getUser_id());
        userMap.put(tmp.getUser_id(), tmp.getUser_name());
      }
    }

    CommonUtil.distinctLongList(userList);;

    List<Report> targetReportOpportunity = new ArrayList<Report>();
    List<Report> targetReportOrder = new ArrayList<Report>();
    for (Long user_id : userList) {
      boolean findA = false;
      boolean findB = false;
      for (Report tmp : listA) {
        if (user_id.equals(tmp.getUser_id())) {
          targetReportOpportunity.add(tmp);
          findA = true;
          break;
        }
      }
      if (!findA) {
        targetReportOpportunity.add(null);
      }

      for (Report tmp : listB) {
        if (user_id.equals(tmp.getUser_id())) {
          targetReportOrder.add(tmp);
          findB = true;
          break;
        }
      }
      if (!findB) {
        targetReportOrder.add(null);
      }
    }

    // 把ID 转成名字
    List<String> userNames = new ArrayList<String>();
    for (Long s : userList) {
      userNames.add(userMap.get(s));
    }
    map.put("names", userNames);
    map.put("opportunityReports", targetReportOpportunity);
    map.put("orderReports", targetReportOrder);

    return map;
  }

  private Map<String, Object> formatChannelReport(List<Report> listA, List<Report> listB) {
    Map<String, Object> map = new TreeMap<String, Object>();
    // 获取
    List<Long> channelList = new ArrayList<Long>();
    Map<Long, String> channelMap = new HashMap<Long, String>();
    for (Report tmp : listA) {
      if (tmp.getChannel() != null) {
        channelList.add(tmp.getChannel());
        channelMap.put(tmp.getChannel(), tmp.getChannel_name());
      }
    }
    for (Report tmp : listB) {
      if (StringUtils.isNoneBlank(tmp.getChannel_name())) {
        channelList.add(tmp.getChannel());
        channelMap.put(tmp.getChannel(), tmp.getChannel_name());
      }
    }
    // 去重复
    CommonUtil.distinctLongList(channelList);
    List<Report> targetReportOpportunity = new ArrayList<Report>();
    List<Report> targetReportOrder = new ArrayList<Report>();
    for (Long channel : channelList) {
      boolean findA = false;
      boolean findB = false;
      for (Report tmp : listA) {
        if (channel.equals(tmp.getChannel())) {
          targetReportOpportunity.add(tmp);
          findA = true;
          break;
        }
      }
      if (!findA) {
        targetReportOpportunity.add(null);
      }

      for (Report tmp : listB) {
        if (channel.equals(tmp.getChannel())) {
          targetReportOrder.add(tmp);
          findB = true;
          break;
        }
      }
      if (!findB) {
        targetReportOrder.add(null);
      }
    }
    // 把ID 转成channel_name
    List<String> channelNames = new ArrayList<String>();
    for (Long s : channelList) {
      channelNames.add(channelMap.get(s));
    }
    map.put("names", channelNames);
    map.put("opportunityReports", targetReportOpportunity);
    map.put("orderReports", targetReportOrder);

    return map;
  }

  private Map<String, Object> formatAdType(List<Report> orderReports) {
    Map<String, Object> map = new TreeMap<String, Object>();
    // 获取
    List<String> adPlatformList = new ArrayList<String>();
    for (Report tmp : orderReports) {
      if (tmp.getAd_platform() != null) {
        adPlatformList.add(tmp.getAd_platform());
      }
    }
    // 去重复
    CommonUtil.distinctList(adPlatformList);
    map.put("names", adPlatformList);
    map.put("orderReports", orderReports);
    return map;
  }

  /**
   * @param isSaleTeam 是否是查询saleTeam 如果是saleTeam 则admin的权限按照ceo来
   * @return
   */
  private List<Long> getCurrentUser(boolean isSaleTeam) {
    if (isAdmin()) {
      if (isSaleTeam) {
        // 如果是管理员, 查询团队.就按照ceo权限
        return getCeoUser();
      } else {
        return null;
      }
    }
    ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
    Long userId = user.master_id != null ? user.master_id : user.id;
    List<Long> currentUser = Lists.newArrayList(userId);
    return currentUser;
  }

  private List<Long> getCeoUser() {
    List<Long> currentUser = Lists.newArrayList(ceoUserId);
    return currentUser;
  }

  private boolean isAdmin() {
    return SecurityUtils.getSubject().hasRole("admin") || SecurityUtils.getSubject().hasRole("superAdmin") || SecurityUtils.getSubject().hasRole("product") || SecurityUtils.getSubject().hasRole("corporate");
  }

  /**
   * 分析报表
   *
   * @param report 页面条件
   * @param reportModel 返回页面的数据模型
   * @param exchangeRates 汇率
   * @param type 报表类型 按照团队, 产品, 广告类型
   * @return
   */
  public ReportModel getAnalyseChart(Report report, ReportModel reportModel, List<ExchangeRate> exchangeRates, String type, boolean isZH) {
    // 如果是查询销售团队, admin的权限按照ceo的来
    if ("saleTeam".equalsIgnoreCase(type)) {
      report.setSaleRepresentative(getCurrentUser(true));
    } else {
      report.setSaleRepresentative(getCurrentUser(false));
    }
    // 总收入 非GP
    List<Report> opportunityReports = null;
    List<Report> orderReports = null;
    if ("saleTeam".equalsIgnoreCase(type)) {
      // 2是查询团队
      report.setDataRight("2");
      opportunityReports = getOpportunityBySaleTeam(report, exchangeRates);
      orderReports = getOrderBySaleTeam(report, exchangeRates);
    } else if ("product".equalsIgnoreCase(type)) {
      // by product
      opportunityReports = getOpportunitysByProduct(report, exchangeRates);
      orderReports = getOrdersByProduct(report, exchangeRates);
    } else if ("adType".equalsIgnoreCase(type)) {
      orderReports = getOrdersByAdPlatform(report, exchangeRates);
    }

    // GP
    List<Report> opportunityGPReports = new ArrayList<Report>();
    List<Report> orderGPReports = new ArrayList<Report>();
    // 复制一份, 是否算GP区别在于计算上, 这里复制一份就可以
    cpReportList(opportunityReports, opportunityGPReports);
    cpReportList(orderReports, orderGPReports);
    // 不是计算GP . 不需要mergeReport
    sumReport(opportunityReports, type, false);
    sumReport(orderReports, type, false);
    Map<String, Object> totalMap = null;
    if ("saleTeam".equalsIgnoreCase(type)) {
      totalMap = formatSaleTeam(opportunityReports, orderReports);
    } else if ("product".equalsIgnoreCase(type)) {
      totalMap = formatProductReport(opportunityReports, orderReports, isZH);
    } else if ("adType".equalsIgnoreCase(type)) {
      totalMap = formatAdType(orderReports);
    }

    // GP
    // 产品,adtype 计算GP在sumreport
    if (!"product".equalsIgnoreCase(type) && !"adType".equalsIgnoreCase(type)) {
      opportunityGPReports = mergeReport(opportunityGPReports);
      orderGPReports = mergeReport(orderGPReports);
    }
    // 如果是product. 计算gp是在sumreport计算. 其他是通过mergeReport计算GP
    boolean calGp = "product".equalsIgnoreCase(type) || "adType".equalsIgnoreCase(type);
    sumReport(opportunityGPReports, type, calGp);
    sumReport(orderGPReports, type, calGp);
    Map<String, Object> gpMap = null;
    if ("saleTeam".equalsIgnoreCase(type)) {
      gpMap = formatSaleTeam(opportunityGPReports, orderGPReports);
    } else if ("product".equalsIgnoreCase(type)) {
      gpMap = formatProductReport(opportunityGPReports, orderGPReports, isZH);
    } else if ("adType".equalsIgnoreCase(type)) {
      // 如果没有GP权限
      if (!SecurityUtils.getSubject().isPermitted("gp:query")) {
        orderGPReports = new ArrayList<Report>();
      }
      gpMap = formatAdType(orderGPReports);
    }

    // ReportModel
    // reportModel.setDataIncome1(new ArrayList<BigDecimal>());
    // reportModel.setDateGp1(new ArrayList<BigDecimal>());
    List<BigDecimal> dataIncome = new ArrayList<BigDecimal>();
    List<BigDecimal> dataGP = new ArrayList<BigDecimal>();
    List<String> labels = new ArrayList<String>();
    if ("saleTeam".equalsIgnoreCase(type)) {
      reportModel.setDataIncome1(dataIncome);
      reportModel.setDataGp1(dataGP);
      reportModel.setLabels1(labels);
    } else if ("product".equalsIgnoreCase(type)) {
      reportModel.setDataIncome3(dataIncome);
      reportModel.setDataGp3(dataGP);
      reportModel.setLabels3(labels);
    } else if ("adType".equalsIgnoreCase(type)) {
      reportModel.setDataIncome4(dataIncome);
      reportModel.setDataGp4(dataGP);
      reportModel.setLabels4(labels);
    }

    // income
    List<String> names = (List<String>) totalMap.get("names");
    List<Report> totalMapOpportunitys = (List<Report>) totalMap.get("opportunityReports");
    List<Report> totalMapOrders = (List<Report>) totalMap.get("orderReports");
    if ("adType".equalsIgnoreCase(type)) {
      for (int i = 0; i < totalMapOrders.size(); i++) {
        // 广告类型没有商机.
        dataIncome.add(totalMapOrders.get(i).getBudget());
      }
    } else {
      for (int i = 0; i < totalMapOpportunitys.size(); i++) {
        BigDecimal val1 = totalMapOpportunitys.get(i) == null ? BigDecimal.ZERO : totalMapOpportunitys.get(i).getBudget();
        BigDecimal val2 = totalMapOrders.get(i) == null ? BigDecimal.ZERO : totalMapOrders.get(i).getBudget();
        dataIncome.add(val1.add(val2));
      }
    }
    labels.addAll(names);
    // GP
    List<Report> gpMapOpportunitys = (List<Report>) gpMap.get("opportunityReports");
    List<Report> gpMapOrders = (List<Report>) gpMap.get("orderReports");
    if ("adType".equalsIgnoreCase(type)) {
      for (int i = 0; i < gpMapOrders.size(); i++) {
        dataGP.add(gpMapOrders.get(i).getBudget());
      }
    } else {
      for (int i = 0; i < names.size(); i++) {
        BigDecimal val1 = gpMapOpportunitys.get(i) == null ? BigDecimal.ZERO : gpMapOpportunitys.get(i).getBudget();
        BigDecimal val2 = gpMapOrders.get(i) == null ? BigDecimal.ZERO : gpMapOrders.get(i).getBudget();
        dataGP.add(val1.add(val2));
      }
    }

    // 排序
    reportModel.sort();
    return reportModel;

  }

  /**
   * 上面的分析数据
   *
   * @param reportModel 返回页面的数据模型
   * @param report 页面条件
   * @param exchangeRates 汇率
   */
  public void getAnalyseData(ReportModel reportModel, Report report, List<ExchangeRate> exchangeRates) {
    report.setSaleRepresentative(getCurrentUser(false));
    // 非GP
    List<Report> opportunityReports = getOpportunitysByProduct(report, exchangeRates);
    List<Report> orderReports = getOrdersByProduct(report, exchangeRates);

    // 统计 广告主 签约订单 推广计划
    Map<String, Integer> map = getCountReport(opportunityReports, orderReports);

    // GP
    List<Report> opportunityGPReports = new ArrayList<Report>();
    List<Report> orderGPReports = new ArrayList<Report>();
    // 复制一份
    cpReportList(opportunityReports, opportunityGPReports);
    cpReportList(orderReports, orderGPReports);

    // 总收入
    sumReport(opportunityReports, "product", false);
    sumReport(orderReports, "product", false);

    // GP
    sumReport(opportunityGPReports, "product", true);
    sumReport(orderGPReports, "product", true);

    // 收入总商机
    BigDecimal totalOpportunityBudget = getSum(opportunityReports);
    // 收入总签约订单
    BigDecimal totalOrderBudget = getSum(orderReports);
    // 预估毛利总商机
    BigDecimal totalGPOpportunity = getSum(opportunityGPReports);
    // 预估毛利总订单
    BigDecimal totalGPOrder = getSum(orderGPReports);

    //
    reportModel.setTotalOpportunity(totalOpportunityBudget);
    reportModel.setTotalOrder(totalOrderBudget);
    reportModel.setTotalGPOpportunity(totalGPOpportunity);
    reportModel.setTotalGPOrder(totalGPOrder);
    reportModel.setAdvertiserCount(map.get("advertiserCount"));
    reportModel.setOrderCount(map.get("orderCount"));
    // campaign 和柱状图的campaign一起算的.这里避免查询2次
    // reportModel.setCampaignCount(map.get("campaignCount"));
  }

  /**
   * 统计广告主 签约订单 推广计划
   *
   * @param opportunityReports
   * @param orderReports
   * @return
   */
  private Map<String, Integer> getCountReport(List<Report> opportunityReports, List<Report> orderReports) {
    Map<String, Integer> map = new HashMap<String, Integer>();
    List<Long> clientList = new ArrayList<>();
    List<Long> orderList = new ArrayList<>();
    for (Report tmp : opportunityReports) {
      clientList.add(tmp.getClient_id());
    }
    for (Report tmp : orderReports) {
      clientList.add(tmp.getClient_id());
      orderList.add(tmp.getOrder_id());
    }
    CommonUtil.distinctLongList(clientList);
    CommonUtil.distinctLongList(orderList);
    map.put("advertiserCount", clientList.size());
    map.put("orderCount", orderList.size());
    // campaignCount和查询一起
    // map.put("campaignCount", 0);
    return map;
  }

  private BigDecimal getSum(List<Report> reports) {
    BigDecimal sum = BigDecimal.ZERO;
    for (Report report : reports) {
      sum = sum.add(report.getBudget());
    }
    return sum;
  }

  /**
   * 小计, 搜索table 如果是按照销售来的. 需要计算多人共一个订单
   *
   * @param orderReports
   */
  private BigDecimal getSaleSum(List<Report> reports) {
    BigDecimal sum = BigDecimal.ZERO;
    if (reports != null) {
      for (Report report : reports) {
        sum = sum.add(report.getBudget());
      }
    }
    return sum;
  }

  List<Report> getOpportunitysByProduct(Report report, List<ExchangeRate> exchangeRates) {
    List<Report> reports = reportMapper.getOpportunitysByProduct(report);
    // 过滤页面GP条件
    filterGPReport(reports, report);
    // 如果是Mobile-Banner -> Mobile DSP 34->1
    dealMobileBanner(reports);
    // 服务类订单的,纠正一些数据
    dealServiceData(reports);
    // 过滤页面时间条件, 按照时间百分比来分预算
    filterPageDateCondition(reports, report);
    // 汇率转换
    exchangeRates(reports, exchangeRates);
    // 过滤页面budget条件, sql有汇率问题无法过滤
    filterPageBudgetCondition(reports, report);
    return reports;
  }

  List<Report> getOrdersByProduct(Report report, List<ExchangeRate> exchangeRates) {
    List<Report> reports = reportMapper.getOrdersByProduct(report);
    filterGPReport(reports, report);
    dealMobileBanner(reports);
    dealServiceData(reports);
    filterPageDateCondition(reports, report);
    exchangeRates(reports, exchangeRates);
    filterPageBudgetCondition(reports, report);
    return reports;
  }

  // 如果是Mobile-Banner -> Mobile DSP 34->1
  private void dealMobileBanner(List<Report> reports) {
    if (reports != null && reports.size() > 0) {
      List<ProductCategory> mobileDspCategory = productCategoryService.getByName("Mobile DSP");
      if (mobileDspCategory != null) {
        for (Report report : reports) {
          if ("Mobile-Banner".equalsIgnoreCase(report.getProduct_nameZh()) || "Mobile-Banner".equalsIgnoreCase(report.getProduct_nameEn())) {
            report.setProduct_id(1L);
            report.setProduct_nameZh(mobileDspCategory.get(0).getName());
            report.setProduct_nameEn(mobileDspCategory.get(0).getName_en());
          }
        }
      }
    }
  }

  // SaleTeam
  List<Report> getOpportunityBySaleTeam(Report report, List<ExchangeRate> exchangeRates) {
    List<Report> reports = reportMapper.getOpportunityBySaleTeam(report);
    filterGPReport(reports, report);
    // 处理服务类订单。 没有产品
    dealServiceData(reports);
    filterPageDateCondition(reports, report);
    // 數據權限分開合併，opportunity 销售和合作销售在一张表， order销售和合作在2张表，无法SQL合并， 只能java合并
    dealReportUsers(reports, report, true);
    exchangeRates(reports, exchangeRates);
    filterPageBudgetCondition(reports, report);
    return reports;

  }

  List<Report> getOrderBySaleTeam(Report report, List<ExchangeRate> exchangeRates) {
    List<Report> reports = reportMapper.getOrderBySaleTeam(report);
    filterGPReport(reports, report);
    dealServiceData(reports);
    filterPageDateCondition(reports, report);
    dealReportUsers(reports, report, true);
    exchangeRates(reports, exchangeRates);
    filterPageBudgetCondition(reports, report);
    return reports;
  }

  // Representative
  List<Report> getOpportunityBySaleRepresentative(Report report, List<ExchangeRate> exchangeRates) {
    List<Report> reports = reportMapper.getOpportunityBySaleRepresentative(report);
    filterGPReport(reports, report);
    // 处理服务类订单。 没有产品
    dealServiceData(reports);
    filterPageDateCondition(reports, report);
    // 數據權限分開合併，opportunity 销售和合作销售在一张表， order销售和合作在2张表，无法SQL合并， 只能java合并
    dealReportUsers(reports, report, false);
    exchangeRates(reports, exchangeRates);
    filterPageBudgetCondition(reports, report);
    return reports;

  }

  List<Report> getOrderBySaleRepresentative(Report report, List<ExchangeRate> exchangeRates) {
    List<Report> reports = reportMapper.getOrderBySaleRepresentative(report);
    filterGPReport(reports, report);
    dealServiceData(reports);
    filterPageDateCondition(reports, report);
    dealReportUsers(reports, report, false);
    exchangeRates(reports, exchangeRates);
    filterPageBudgetCondition(reports, report);
    return reports;
  }

  /**
   * 如果是团队， 或者是销售代表。 userId和share_user_id 需要合并 对于订单。 user_ids是全部的销售。 共享的团队这里不计算
   * user_id是创建者，不一定是销售。在SQL里已处理好，這裡不計算 对于商机 user_ids + share_user_ids 是全部的销售
   *
   */
  private void dealReportUsers(List<Report> reports, Report condReport, boolean isSaleTeam) {
    // 获取自己所有能看的
    List<DataSharing> allDataSharings = null;

    // 不为空, 不是管理员, 或者是管理员查询销售团队
    if (getCurrentUser(isSaleTeam) != null) {
      allDataSharings = dataSharingService.getDataSharingByUserId(getCurrentUser(isSaleTeam).get(0));
    } else {
      // 如果为空. 那就是管理员查询非销售团队
      allDataSharings = dataSharingService.getDataSharingByUserId(null);
    }

    // 过滤权限销售代表和销售团队, 如果页面带了权限条件的. 无关的销售都不要; 只用于销售和销售团队
    int condMode = 0;
    if ("2".equalsIgnoreCase(condReport.getDataRight()) && condReport.getCondSaleRepresentative().size() == 0) {
      // 如果是销售团队, 条件为空
      condMode = 1;
    }
    if ("2".equalsIgnoreCase(condReport.getDataRight()) && condReport.getCondSaleRepresentative().size() > 0) {
      // 如果是销售团队, 条件不为空
      condMode = 2;
    }
    if ("3".equalsIgnoreCase(condReport.getDataRight()) && condReport.getCondSaleRepresentative().size() == 0) {
      // 如果是销售代表, 条件为空
      condMode = 3;
    }
    if ("3".equalsIgnoreCase(condReport.getDataRight()) && condReport.getCondSaleRepresentative().size() > 0) {
      // 如果是销售代表, 条件不为空
      condMode = 4;
    }

    if (reports != null) {
      for (int i = reports.size() - 1; i >= 0; i--) {
        Report report = reports.get(i);
        // 分开的原因是。 对于商机来说销售和合作销售是同一张表列数据，所以join的时候就一次查询出来了。
        // 对于取order的商机，一个是在order表，一个是在share_order表。无法放同一个字段。 所以这里放到2个字段。
        if (StringUtils.isNotBlank(report.getShare_user_ids())) {
          if (StringUtils.isBlank(report.getUser_ids())) {
            report.setUser_ids(report.getShare_user_ids());
            report.setUser_name(report.getShare_user_names());
          } else {
            report.setUser_ids(report.getUser_ids() + "," + report.getShare_user_ids());
            report.setUser_name(report.getUser_name() + "," + report.getShare_user_names());
          }
        }

        // 如果是销售团队, 条件为空 就查询所有的团队. 不是自己团队不要
        // 如果是销售代表, 条件为空, 查询所有的人, 不是自己的销售代表不要
        List<Long> condUserIds = new ArrayList<Long>();
        if (condMode == 1 || condMode == 3) {
          if (allDataSharings != null) {
            // 所有自己能看的
            for (DataSharing d : allDataSharings) {
              condUserIds.add(d.getUser_id());
            }
          }
        } else if (condMode == 2) {
          for (Long condSaleRepresentative : condReport.getCondSaleRepresentative()) {
            // 添加自己
            condUserIds.add(condSaleRepresentative);
            // 添加自己后面所有的级
            fetchSaleTeamUserIds(condUserIds, condSaleRepresentative, allDataSharings);
          }
        } else if (condMode == 4) {
          // 如果是销售代表, 条件不为空, 只显示条件里面名字的数据
          // 条件里面的销售
          condUserIds = condReport.getCondSaleRepresentative();
        }

        // condUserIds 是根据datasharing表获取所有自己能看的. userIds是订单里面的所有销售. 需要去掉不能看的销售.
        // datasharing和userids的交集就是能看的
        if (condUserIds != null && condUserIds.size() > 0) {
          // 订单可能有不是自己团队,或不是自己销售的. 这里只要自己的. 取所有能看的和查询出来的交集.没有交集就是不能看的
          String[] userIds = report.getUser_ids().split(",");
          // 交集的用户
          List<Long> mixedUserIds = new ArrayList<Long>();

          for (String userId : userIds) {
            // 如果是管理员 没有条件. 则不需要取交集
            if (getCurrentUser(isSaleTeam) == null && condMode == 3) {
              // 查询代表管理员能看所有人. 都是自己的. 如果是查询团队. 管理员算ceo权限. 走else代码
              mixedUserIds.add(Long.parseLong(userId));
            } else {
              // 如果有条件, 都需要取交集
              for (Long condUserId : condUserIds) {
                // 找到交集
                if (userId.equalsIgnoreCase(String.valueOf(condUserId))) {
                  if (condMode == 1 || condMode == 2) {
                    // 如果是团队的. 则都算到一级团队下面, admin 算ceo
                    Long tmpUserId = null;
                    if (getCurrentUser(isSaleTeam) == null) {
                      tmpUserId = ceoUserId;
                    } else {
                      tmpUserId = getCurrentUser(isSaleTeam).get(0);
                    }
                    mixedUserIds.add(getSonDataSharingUserId(allDataSharings, condUserId, tmpUserId));
                  } else {
                    mixedUserIds.add(condUserId);
                  }
                }
              }
            }
          }

          if (mixedUserIds.size() > 0) {
            // 这里只取条件选择的
            CommonUtil.distinctLongList(mixedUserIds);
            report.setUser_ids(list2String(mixedUserIds));
          } else {
            // 如果没有交集. 说明是共享来的数据. 不显示的.
            reports.remove(i);
          }

        }

      }
    }
  }


  /**
   * 获取团队下面的userids
   *
   * @param condSaleRepresentative
   * @param list
   * @param allDataSharings
   * @return
   */
  private void fetchSaleTeamUserIds(List<Long> condUserIds, Long condSaleRepresentative, List<DataSharing> allDataSharings) {

    for (DataSharing dataSharing : allDataSharings) {
      if (condSaleRepresentative.equals(dataSharing.getParent_id())) {
        condUserIds.add(dataSharing.getUser_id());
        fetchSaleTeamUserIds(condUserIds, dataSharing.getUser_id(), allDataSharings);
      }
    }

  }


  /**
   * 获取一级团队的ID
   *
   * @param dataSharingDTOs
   * @param condUserId
   * @return
   */
  private Long getSonDataSharingUserId(List<DataSharing> dataSharings, Long condUserId, Long targetId) {
    if (dataSharings == null) {
      return condUserId;
    } else {
      boolean find = false;
      for (DataSharing dataSharing : dataSharings) {
        if (dataSharing.getUser_id().equals(condUserId)) {
          if (dataSharing.getParent_id().equals(targetId)) {
            return dataSharing.getUser_id();
          } else {
            return getSonDataSharingUserId(dataSharings, dataSharing.getParent_id(), targetId);
          }
        }
      }
      if (!find) {
        return condUserId;
      }
    }
    return condUserId;
  }

  // 吧list 转成逗号分隔的字符串
  private String list2String(List<Long> userIds) {
    String result = "";
    for (int i = 0; i < userIds.size(); i++) {
      if (i > 0) {
        result += ",";
      }
      result += String.valueOf(userIds.get(i));
    }
    return result;
  }

  // channel
  List<Report> getOpportunitysByChannel(Report report, List<ExchangeRate> exchangeRates) {
    List<Report> reports = reportMapper.getOpportunitysByChannel(report);
    filterGPReport(reports, report);
    dealServiceData(reports);
    filterPageDateCondition(reports, report);
    exchangeRates(reports, exchangeRates);
    filterPageBudgetCondition(reports, report);
    filterPageBudgetCondition(reports, report);
    return reports;

  }

  List<Report> getOrdersByChannel(Report report, List<ExchangeRate> exchangeRates) {
    List<Report> reports = reportMapper.getOrdersByChannel(report);
    filterGPReport(reports, report);
    dealServiceData(reports);
    filterPageDateCondition(reports, report);
    exchangeRates(reports, exchangeRates);
    filterPageBudgetCondition(reports, report);
    return reports;
  }

  // 這裡是報表的，沒有這個條件。 不需要filterGPReport(reports, report);
  List<Report> getOrdersByAdPlatform(Report report, List<ExchangeRate> exchangeRates) {
    List<Report> reports = reportMapper.getOrdersByAdPlatform(report);
    filterGPReport(reports, report);
    dealServiceData(reports);
    filterPageDateCondition(reports, report);
    exchangeRates(reports, exchangeRates);
    return reports;
  }

  /**
   * 页面的时间条件, 按照当前时间占据总时间的百分比
   *
   * @param reports
   * @param report
   */
  private void filterPageDateCondition(List<Report> reports, Report report) {
    if (StringUtils.isNotBlank(report.getReportDate_start()) && StringUtils.isNotBlank(report.getReportDate_end())) {
      String s2 = report.getReportDate_start();
      String e2 = report.getReportDate_end();
      for (Report reportDB : reports) {
        String s1 = reportDB.getOrderStartDate();
        String e1 = reportDB.getOrderEndDate();
        int day = CommonUtil.crossDate(s1, e1, s2, e2);
        int totalDay = CommonUtil.gapDate(s1, e1);
        reportDB.setBudget(reportDB.getBudget().multiply(new BigDecimal(day)).divide(new BigDecimal(totalDay), 2, BigDecimal.ROUND_HALF_EVEN));
        reportDB.setTotalBudget(reportDB.getTotalBudget().multiply(new BigDecimal(day)).divide(new BigDecimal(totalDay), 2, BigDecimal.ROUND_HALF_EVEN));
      }
    }
  }

  /**
   * 页面预算条件
   *
   * @param reports
   * @param report
   */
  private void filterPageBudgetCondition(List<Report> reports, Report report) {
    if (StringUtils.isNotBlank(report.getBudget_start()) && StringUtils.isNotBlank(report.getBudget_end())) {
      String pageBudgetStart = report.getBudget_start();
      String pageBudgetEnd = report.getBudget_end();
      // 如果都是0, 就是不限制
      if ("0".equalsIgnoreCase(pageBudgetStart) && "0".equalsIgnoreCase(pageBudgetEnd)) {
        return;
      }
      for (int i = reports.size() - 1; i >= 0; i--) {
        Report reportDB = reports.get(i);
        // 如果小于开始, 就删除
        if (!"0".equalsIgnoreCase(pageBudgetStart) && reportDB.getTotalBudget().compareTo(new BigDecimal(pageBudgetStart)) < 0) {
          reports.remove(i);
          continue;
        }
        // 如果大于等于结束, 删除
        if (!"0".equalsIgnoreCase(pageBudgetEnd) && reportDB.getTotalBudget().compareTo(new BigDecimal(pageBudgetEnd)) >= 0) {
          reports.remove(i);
          continue;
        }
      }
    }
  }

  // SQL中无法过滤GP。 需要在代码中过滤,因为GP有2个字段决定
  // 0 全部
  // 1 0%
  // 2 < 30
  // 3 30-39
  // 4 40-49
  // 5 50-59
  // 6 60-69
  // 7 >= 70
  private void filterGPReport(List<Report> reports, Report condReport) {
    // 如果值类型为空， 或者是值类型为收入
    if (StringUtils.isBlank(condReport.getMetric()) || "1".equalsIgnoreCase(condReport.getMetric())) {
      return;
    }
    int gpCond = condReport.getGp() == null ? 0 : condReport.getGp().intValue();
    if (gpCond == 0) {
      return;
    }
    if (reports != null) {
      for (int i = reports.size() - 1; i >= 0; i--) {
        Double dbGPValue = getReportGp(reports.get(i));
        boolean isDeleted = true;
        if (gpCond == 1 && dbGPValue == 0) {
          isDeleted = false;
        } else if (gpCond == 2 && dbGPValue < 30) {
          isDeleted = false;
        } else if (gpCond == 3 && dbGPValue >= 30 && dbGPValue < 40) {
          isDeleted = false;
        } else if (gpCond == 4 && dbGPValue >= 40 && dbGPValue < 50) {
          isDeleted = false;
        } else if (gpCond == 5 && dbGPValue >= 50 && dbGPValue < 60) {
          isDeleted = false;
        } else if (gpCond == 6 && dbGPValue >= 60 && dbGPValue < 70) {
          isDeleted = false;
        } else if (gpCond == 7 && dbGPValue >= 70) {
          isDeleted = false;
        }
        if (isDeleted) {
          reports.remove(i);
        }
      }
    }
  }

  /**
   * 获取Campaign 数据
   *
   * @param report
   * @param reportModel
   */

  public void getCampaignData(Report report, ReportModel reportModel) {
    List<Long> ids = reportMapper.getCampaignOrderIds(report);
    List<Report> targetReports = null;
    if (ids.size() > 0) {
      Map<String, Object> map = new HashMap<String, Object>();
      map.put("reportDate_start", Integer.parseInt(report.getReportDate_start().replaceAll("-", "")));
      map.put("reportDate_end", Integer.parseInt(report.getReportDate_end().replaceAll("-", "")));
      CommonUtil.distinctLongList(ids);
      map.put("orderList", ids);
      targetReports = reportMapper.getCampaign(map);
    }
    int count = 0;
    BigDecimal sum = BigDecimal.ZERO;
    if (targetReports != null) {
      List<String> order_codes = new ArrayList<String>();
      for (Report r : targetReports) {
        order_codes.add(r.getOrder_code());
        sum = sum.add(r.getExpenses());
      }
      CommonUtil.distinctList(order_codes);
      count = order_codes.size();
    }
    reportModel.setCampaignCount(count);
    reportModel.setCampaignSum(sum);
  }

  /**
   * 处理服务类订单。 如果是服务类订单。 没有产品。 totalbudget就是budget 如果是服务类订单, 依然有产品. 就是数据错误. 这里需要矫正 如果不是服务类订单,
   * 理论上必须有产品, 如果依然没有产品, 就是数据错误. 这里需要矫正
   *
   * 执行类, 必须有产品. 有些数据还是没产品. 矫正
   *
   * 居然还有销售为空的.
   *
   * @param reports
   */
  private void dealServiceData(List<Report> reports) {
    // 服务类 有产品的数据矫正
    if (reports != null && reports.size() > 0) {
      for (int i = reports.size() - 1; i >= 0; i--) {
        if (reports.get(i).getWhether_service().equals(1)) {
          // 对于服务类,totalbudget 就是budget
          reports.get(i).setBudget(reports.get(i).getTotalBudget());
          // 对于服务类商机尚未走到订单步骤（即没有服务类订单关联到商机时），商机的收入以总预算计，GP以预算*50%计
          if (reports.get(i).getOpportunity_id() != null) {
            // 因为后面有个支持GP分量判断的. 所以这里就把GP和Gp_evaluate都设置50.
            reports.get(i).setGp(50d);
            reports.get(i).setGp_evaluate(50d);
          }

          // 服务类多个产品只留一条数据 (矫正数据库数据错误) 理论上是没有产品. 这里只留下一个
          for (int j = i - 1; j >= 0; j--) {
            // 如果是同订单/同商机 说明是服务类有订单. 需要删除
            if ((reports.get(i).getOpportunity_id() != null && reports.get(j).getOpportunity_id() != null && reports.get(i).getOpportunity_id().equals(reports.get(j).getOpportunity_id()))
                || (reports.get(i).getOrder_id() != null && reports.get(j).getOrder_id() != null && reports.get(i).getOrder_id().equals(reports.get(j).getOrder_id()))) {
              reports.remove(i);
              break;
            }
          }
        }

      }
    }

    // budget为空数据矫正
    if (reports != null && reports.size() > 0) {
      for (Report report : reports) {
        if (report.getBudget() == null) {
          if (report.getTotalBudget() == null) {
            // 连totalBudget都为空
            report.setTotalBudget(BigDecimal.ZERO);
          }
          report.setBudget(report.getTotalBudget());
        }
        // 处理产品为空的或者是服务类 都归到未分类, 服务类理论是无产品的
        if (report.getProduct_id() == null || report.getWhether_service().equals(1)) {
          report.setProduct_id(new Long(0));
          report.setProduct_nameZh("未分类");
          report.setProduct_nameEn("Unspecified");
        }

        // 销售为空的, 归为Unspecified
        if (StringUtils.isBlank(report.getUser_ids()) && StringUtils.isBlank(report.getShare_user_ids())) {
          report.setUser_ids("0");
          report.setUser_name("Unspecified");
        }

        // 广告主为空
        if (report.getClient_id() == null) {
          report.setClient_id(new Long(0));
          report.setClientname("Unspecified");
        }
      }
      System.out.println("end");
    }
  }

  public static final String[] dataRightColumns = {"report.product", "report.sale.team", "report.sale.representative", "report.channel.company", "report.advertiser"};

  public static final Map<String, String> currencyKV = new HashMap<String, String>();
  public static final Map<String, String> currencyStr = new HashMap<String, String>();

  static {
    currencyKV.put("RMB", "￥");
    currencyKV.put("AUD", "AUD");
    currencyKV.put("EUR", "€");
    currencyKV.put("GBP", "￡");
    currencyKV.put("HKD", "HKD");
    currencyKV.put("IDR", "IDR");
    currencyKV.put("JPY", "JPY");
    currencyKV.put("KRW", "₩");
    currencyKV.put("MYR", "RM");
    currencyKV.put("RUB", "RUB");
    currencyKV.put("SGD", "SGD");
    currencyKV.put("THB", "฿");
    currencyKV.put("TWD", "TWD");
    currencyKV.put("USD", "$");

    currencyStr.put("RMB", "人民币");
    currencyStr.put("AUD", "澳大利亚元");
    currencyStr.put("EUR", "欧元");
    currencyStr.put("GBP", "英镑");
    currencyStr.put("HKD", "港元");
    currencyStr.put("IDR", "印尼卢比");
    currencyStr.put("JPY", "日元");
    currencyStr.put("KRW", "韩元");
    currencyStr.put("MYR", "马来西亚林吉特");
    currencyStr.put("RUB", "俄罗斯卢布");
    currencyStr.put("SGD", "新加坡元");
    currencyStr.put("THB", "泰铢");
    currencyStr.put("TWD", "新台币");
    currencyStr.put("USD", "美元");
  }

  // 导出报表 excel
  @SuppressWarnings("unchecked")
  public XSSFWorkbook downloadReport(Report report, HttpServletRequest request) throws IOException {
    LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
    String lang = localeResolver.resolveLocale(request).getLanguage();
    String country = localeResolver.resolveLocale(request).getCountry();
    boolean isZH = "zh".equalsIgnoreCase(lang) && "CN".equalsIgnoreCase(country);
    Map<String, Object> map = this.getReport(report, isZH);

    String currencyCode = currencyKV.get(report.getCurrencyCode());

    InputStream is = ReportService.class.getClassLoader().getResourceAsStream("excel/reportTable.xlsx");
    XSSFWorkbook workbook = new XSSFWorkbook(is);
    XSSFSheet reportSheet = workbook.getSheetAt(0);
    reportSheet.setColumnWidth(0, 25 * 256);
    reportSheet.setColumnWidth(1, 25 * 256);
    reportSheet.setColumnWidth(2, 25 * 256);
    reportSheet.setColumnWidth(3, 25 * 256);

    // 宋体
    XSSFFont songFont = workbook.createFont();
    songFont.setFontName("宋体");

    // 宋体加粗
    XSSFFont songBoldFont = workbook.createFont();
    songBoldFont.setFontName("宋体");

    // Arial字体
    XSSFFont arialFont = workbook.createFont();
    arialFont.setFontName("Calibri（主题正文）");

    // Arial字体加粗
    XSSFFont arialBoldFont = workbook.createFont();
    arialBoldFont.setFontName("Calibri（主题正文）");
    arialBoldFont.setBold(true);

    // 会计样式
    XSSFCellStyle currencyCellStyle = workbook.createCellStyle();
    currencyCellStyle.setFont(arialFont);
    XSSFDataFormat currencyFomat = workbook.createDataFormat();
    currencyCellStyle.setDataFormat(currencyFomat.getFormat("[$" + currencyCode + "] #,##0.00_);([$" + currencyCode + "] #,##0.00)"));

    // 头部样式
    XSSFCellStyle topCellStyle = workbook.createCellStyle();
    topCellStyle.setFont(songBoldFont);
    topCellStyle.getFont().setBold(true);

    // 正文样式
    XSSFCellStyle centerCellStyle = workbook.createCellStyle();
    centerCellStyle.setFont(songFont);

    // 底部样式
    XSSFCellStyle bottomCellStyle = workbook.createCellStyle();
    bottomCellStyle.setFont(arialBoldFont);
    bottomCellStyle.setDataFormat(currencyFomat.getFormat("[$" + currencyCode + "] #,##0.00_);([$" + currencyCode + "] #,##0.00)"));

    // 总计样式
    XSSFCellStyle totalCellStyle = workbook.createCellStyle();
    totalCellStyle.setAlignment(HorizontalAlignment.RIGHT);
    totalCellStyle.setFont(songBoldFont);

    // 表头
    int rowHeadIndex = 11;
    XSSFRow firstRow = reportSheet.createRow(rowHeadIndex);
    XSSFCell title1 = firstRow.createCell(0);
    title1.setCellValue(CommonUtil.getProperty(request, dataRightColumns[Integer.parseInt(report.getDataRight()) - 1]));
    title1.setCellStyle(topCellStyle);
    XSSFCell title2 = firstRow.createCell(1);
    title2.setCellValue(CommonUtil.getProperty(request, "report.clo1"));
    title2.setCellStyle(topCellStyle);
    XSSFCell title3 = firstRow.createCell(2);
    title3.setCellValue(CommonUtil.getProperty(request, "report.clo2"));
    title3.setCellStyle(topCellStyle);
    XSSFCell title4 = firstRow.createCell(3);
    title4.setCellValue(CommonUtil.getProperty(request, "report.clo3"));
    title4.setCellStyle(topCellStyle);

    List<String> rows = (List<String>) map.get("names");
    List<Report> opportunityReports = (List<Report>) map.get("opportunityReports");
    List<Report> orderReports = (List<Report>) map.get("orderReports");

    // 合计是否累加
    boolean addUp = false;
    // 订单合计
    double orderSum = 0;
    // 商机合计
    double opportunitySum = 0;
    if (map.get("orderSum") != null) {
      opportunitySum = CommonUtil.format2Number(((BigDecimal) map.get("opportunitySum")).doubleValue());
      orderSum = CommonUtil.format2Number(((BigDecimal) map.get("orderSum")).doubleValue());
    } else {
      addUp = true;
    }

    if (rows != null && rows.size() > 0) {
      for (int i = 0; i < rows.size(); i++) {
        XSSFRow thisRow = reportSheet.createRow(i + rowHeadIndex + 1);
        Report thisOpportunity = opportunityReports.get(i);
        Report thisOrder = orderReports.get(i);

        XSSFCell nameCell = thisRow.createCell(0);
        XSSFCell orderCell = thisRow.createCell(1);
        XSSFCell opportunityCell = thisRow.createCell(2);
        XSSFCell subtotalCell = thisRow.createCell(3);
        nameCell.setCellStyle(centerCellStyle);
        orderCell.setCellStyle(currencyCellStyle);
        opportunityCell.setCellStyle(currencyCellStyle);
        subtotalCell.setCellStyle(currencyCellStyle);

        double opportunity_budget = 0;
        if (thisOpportunity != null) {
          opportunity_budget = CommonUtil.format2Number(thisOpportunity.getBudget().doubleValue());
          if (addUp)
            opportunitySum += opportunity_budget;
        }

        double order_budget = 0;
        if (thisOrder != null) {
          order_budget = CommonUtil.format2Number(thisOrder.getBudget().doubleValue());
          if (addUp)
            orderSum += order_budget;
        }

        nameCell.setCellValue(rows.get(i));
        double d1 = CommonUtil.format2Number(opportunity_budget * report.getExchangeRate().doubleValue());
        opportunityCell.setCellValue(d1);
        double d2 = CommonUtil.format2Number(order_budget * report.getExchangeRate().doubleValue());
        orderCell.setCellValue(d2);
        subtotalCell.setCellValue(d1 + d2);
      }
    }

    // 表尾
    XSSFRow lastRow = reportSheet.createRow(rows.size() + rowHeadIndex + 1);
    XSSFCell buttom1 = lastRow.createCell(0);
    buttom1.setCellValue(CommonUtil.getProperty(request, "report.total"));
    buttom1.setCellStyle(totalCellStyle);

    XSSFCell buttom2 = lastRow.createCell(1);
    buttom2.setCellStyle(bottomCellStyle);
    double d1 = CommonUtil.format2Number(orderSum * report.getExchangeRate().doubleValue());
    buttom2.setCellValue(d1);

    XSSFCell buttom3 = lastRow.createCell(2);
    buttom3.setCellStyle(bottomCellStyle);
    double d2 = CommonUtil.format2Number(opportunitySum * report.getExchangeRate().doubleValue());
    buttom3.setCellValue(d2);

    XSSFCell buttom4 = lastRow.createCell(3);
    buttom4.setCellStyle(bottomCellStyle);
    buttom4.setCellValue(d1 + d2);

    // 替换值
    // 数据类型
    XSSFRow rowTitle = reportSheet.getRow(6);
    XSSFCell cellTitle = rowTitle.getCell(0);
    String mertric = "2".equals(report.getMetric()) ? CommonUtil.getProperty(request, "report.estimate.gp") : CommonUtil.getProperty(request, "report.income");
    cellTitle.setCellValue(cellTitle.getStringCellValue() + mertric);

    // 时间段
    XSSFRow rowDate = reportSheet.getRow(7);
    XSSFCell cellDate = rowDate.getCell(0);
    cellDate.setCellValue(cellDate.getStringCellValue() + report.getReportDate());

    // 货币
    XSSFRow rowCurrency = reportSheet.getRow(8);
    XSSFCell cellCurrency = rowCurrency.getCell(0);
    cellCurrency.setCellValue(cellCurrency.getStringCellValue() + currencyStr.get(report.getCurrencyCode()));

    // 创建时间
    XSSFRow rowNow = reportSheet.getRow(9);
    XSSFCell cellNow = rowNow.getCell(0);
    cellNow.setCellValue(cellNow.getStringCellValue() + CommonUtil.formatDate(new Date(), "yyyy/MM/dd HH:mm:ss"));

    return workbook;
  }

  @SuppressWarnings("unchecked")
  public XSSFWorkbook downloadReportDashboard(Report report, HttpServletRequest request) throws IOException {
    String date = request.getParameter("date");
    String currency = request.getParameter("currency");
    String totalOpportunity = request.getParameter("totalOpportunity");
    String totalOrder = request.getParameter("totalOrder");
    String totalGPOpportunity = request.getParameter("totalGPOpportunity");
    String totalGPOrder = request.getParameter("totalGPOrder");
    String advertiserCount = request.getParameter("advertiserCount");
    String orderCount = request.getParameter("orderCount");
    String campaignCount = request.getParameter("campaignCount");

    JSONArray saleManagerData = JSONArray.parseArray(request.getParameter("saleManagerData"));
    JSONArray saleManagerGP = JSONArray.parseArray(request.getParameter("saleManagerGP"));
    JSONArray adTypeIncomeData = JSONArray.parseArray(request.getParameter("adTypeIncomeData"));
    JSONArray adTypeGpData = JSONArray.parseArray(request.getParameter("adTypeGpData"));
    JSONArray saleGroupLabels = JSONArray.parseArray(request.getParameter("saleGroupLabels"));
    JSONArray saleGroupIncomeData = JSONArray.parseArray(request.getParameter("saleGroupIncomeData"));
    JSONArray saleGroupGpData = JSONArray.parseArray(request.getParameter("saleGroupGpData"));
    JSONArray productLabels = JSONArray.parseArray(request.getParameter("productLabels"));
    JSONArray productIncomeData = JSONArray.parseArray(request.getParameter("productIncomeData"));
    JSONArray productGpData = JSONArray.parseArray(request.getParameter("productGpData"));

    InputStream is = ReportService.class.getClassLoader().getResourceAsStream("excel/reportDashboard.xlsx");
    XSSFWorkbook workbook = new XSSFWorkbook(is);
    XSSFSheet reportSheet = workbook.getSheetAt(0);

    // Arial字体
    XSSFFont arialFont = workbook.createFont();
    arialFont.setFontName("Calibri（主题正文）");

    String currencyCode = currencyKV.get(currency);

    // 会计样式
    XSSFCellStyle currencyCellStyle = workbook.createCellStyle();
    currencyCellStyle.setFont(arialFont);
    XSSFDataFormat currencyFomat = workbook.createDataFormat();
    currencyCellStyle.setDataFormat(currencyFomat.getFormat("[$" + currencyCode + "] #,##0_);([$" + currencyCode + "] #,##0)"));


    // 7个值
    XSSFRow rowTotalValues = reportSheet.getRow(11);
    XSSFCell rowTotalCell0 = rowTotalValues.createCell(0);
    rowTotalCell0.setCellStyle(currencyCellStyle);
    rowTotalCell0.setCellValue(Double.parseDouble(totalOpportunity));

    XSSFCell rowTotalCell1 = rowTotalValues.createCell(1);
    rowTotalCell1.setCellStyle(currencyCellStyle);
    rowTotalCell1.setCellValue(Double.parseDouble(totalOrder));

    XSSFCell rowTotalCell2 = rowTotalValues.createCell(2);
    rowTotalCell2.setCellStyle(currencyCellStyle);
    rowTotalCell2.setCellValue(Double.parseDouble(totalGPOpportunity));

    XSSFCell rowTotalCell3 = rowTotalValues.createCell(3);
    rowTotalCell3.setCellStyle(currencyCellStyle);
    rowTotalCell3.setCellValue(Double.parseDouble(totalGPOrder));

    XSSFCell rowTotalCell4 = rowTotalValues.createCell(4);
    rowTotalCell4.setCellValue(Double.parseDouble(advertiserCount));

    XSSFCell rowTotalCell5 = rowTotalValues.createCell(5);
    rowTotalCell5.setCellValue(Double.parseDouble(orderCount));

    XSSFCell rowTotalCell6 = rowTotalValues.createCell(6);
    rowTotalCell6.setCellValue(Double.parseDouble(campaignCount));

    // 收入管理 设备
    for (int i = 0; i < 3; i++) {
      XSSFRow tmpRow = reportSheet.getRow(14 + i);
      XSSFCell tmpSaleManageCell = tmpRow.createCell(1);
      tmpSaleManageCell.setCellStyle(currencyCellStyle);
      tmpSaleManageCell.setCellValue(saleManagerData.getDoubleValue(i));

      // campaign 没有 GP
      if (saleManagerGP.size() > i) {
        XSSFCell tmpSaleMangeCellGp = tmpRow.createCell(2);
        tmpSaleMangeCellGp.setCellStyle(currencyCellStyle);
        tmpSaleMangeCellGp.setCellValue(saleManagerGP.getDoubleValue(i));
      }

      XSSFCell tmpAdTypeMangeCell = tmpRow.createCell(5);
      tmpAdTypeMangeCell.setCellStyle(currencyCellStyle);
      tmpAdTypeMangeCell.setCellValue(adTypeIncomeData.getDoubleValue(i));

      if (adTypeGpData.size() > i) {
        XSSFCell tmpAdTypeMangeCellGp = tmpRow.createCell(6);
        tmpAdTypeMangeCellGp.setCellStyle(currencyCellStyle);
        tmpAdTypeMangeCellGp.setCellValue(adTypeGpData.getDoubleValue(i));
      }
    }

    // 销售团队
    int saleGroupRowIndex = 19;
    for (int i = 0; i < saleGroupIncomeData.size(); i++) {
      XSSFRow saleGroupRow = reportSheet.createRow(saleGroupRowIndex + i);
      XSSFCell cellLabel = saleGroupRow.createCell(4);
      cellLabel.setCellStyle(currencyCellStyle);
      cellLabel.setCellValue(saleGroupLabels.getString(i));

      XSSFCell cellIncome = saleGroupRow.createCell(5);
      cellIncome.setCellStyle(currencyCellStyle);
      cellIncome.setCellValue(saleGroupIncomeData.getDoubleValue(i));

      XSSFCell cellGp = saleGroupRow.createCell(6);
      cellGp.setCellStyle(currencyCellStyle);
      cellGp.setCellValue(saleGroupGpData.getDoubleValue(i));
    }

    // 广告形式
    int productRowIndex = 19;
    for (int i = 0; i < productIncomeData.size(); i++) {
      XSSFRow productRow = reportSheet.getRow(productRowIndex + i);
      if (productRow == null) {
        productRow = reportSheet.createRow(productRowIndex + i);
      }
      XSSFCell cellLabel = productRow.createCell(0);
      cellLabel.setCellStyle(currencyCellStyle);
      cellLabel.setCellValue(productLabels.getString(i));

      XSSFCell cellIncome = productRow.createCell(1);
      cellIncome.setCellStyle(currencyCellStyle);
      cellIncome.setCellValue(productIncomeData.getDoubleValue(i));

      XSSFCell cellGp = productRow.createCell(2);
      cellGp.setCellStyle(currencyCellStyle);
      cellGp.setCellValue(productGpData.getDoubleValue(i));
    }

    // 时间段
    XSSFRow rowDate = reportSheet.getRow(5);
    XSSFCell cellDate = rowDate.getCell(0);
    cellDate.setCellValue(cellDate.getStringCellValue() + date);

    // 货币
    XSSFRow rowCurrency = reportSheet.getRow(6);
    XSSFCell cellCurrency = rowCurrency.getCell(0);
    cellCurrency.setCellValue(cellCurrency.getStringCellValue() + currencyStr.get(currency));

    // 创建时间
    XSSFRow rowNow = reportSheet.getRow(7);
    XSSFCell cellNow = rowNow.getCell(0);
    cellNow.setCellValue(cellNow.getStringCellValue() + CommonUtil.formatDate(new Date(), "yyyy/MM/dd HH:mm:ss"));

    return workbook;
  }

  private String getQuarterBeginDate(){
	  Calendar cal = Calendar.getInstance();
	  cal.add(Calendar.MONTH, cal.get(Calendar.MONTH) % 3 * (-1));
	  cal.set(Calendar.DAY_OF_MONTH,1);
	  return CommonUtil.formatDate(cal.getTime(), "yyyy-MM-dd");
  }

  private String getQuarterEndDate(){
	  Calendar cal = Calendar.getInstance();
	  cal.add(Calendar.MONTH, 2 - cal.get(Calendar.MONTH) % 3);
	  cal.set(Calendar.DAY_OF_MONTH,cal.getActualMaximum(Calendar.DAY_OF_MONTH));
	  return CommonUtil.formatDate(cal.getTime(), "yyyy-MM-dd");
  }

  
  public void generateQuarterExcel() throws IOException{
	String beginDate = getQuarterBeginDate();
	String endDate = getQuarterEndDate();
	List<ReportQuarter> reports = new ArrayList<>();
	List<ReportQuarter> originReports = reportMapper.getQuarterReport(beginDate, endDate);
	for(int i = 0; i < originReports.size(); i++){
	  boolean bFind = false;
	  ReportQuarter rqi = originReports.get(i);
	  for(int j = 0; j < originReports.size(); j++){
		ReportQuarter rqj = originReports.get(j);
		if (i != j && rqi.getId().equals(rqj.getId()) && rqi.getName().equals(rqj.getName()) && rqi.getProduct_budget().compareTo(rqj.getProduct_budget()) == 0){
		  bFind = true;
		  break;
		}
	  }
	  if (!bFind){
		reports.add(rqi);
	  }
	}
	System.out.println(reports.size());

	InputStream is = ReportService.class.getClassLoader().getResourceAsStream("excel/reportQuarter.xlsx");
	String file_name = "iCRM Report " + CommonUtil.formatDate(new Date(), "yyyyMMdd") + ".xlsx";
	String path = REPORT_UPLOAD_FOLDER + file_name;
	FileOutputStream outputStream = new FileOutputStream(path);
    XSSFWorkbook workbook = new XSSFWorkbook(is);
    XSSFSheet reportSheet = workbook.getSheetAt(0);
    if (reportSheet == null){
    	reportSheet = workbook.createSheet("sheet1");
    }

    // Arial字体
    XSSFFont arialFont = workbook.createFont();
    arialFont.setFontName("Calibri（主题正文）");
    XSSFDataFormat format = workbook.createDataFormat();
    // 数字靠右,千位分割
    XSSFCellStyle moneyStyle = workbook.createCellStyle();
    moneyStyle.setDataFormat(format.getFormat("#,##0.00"));
    moneyStyle.setAlignment(XSSFCellStyle.ALIGN_RIGHT);
    
    // 表头
    int rowHeadIndex = 5;
    for(int i = 0; i < reports.size(); i++){
      XSSFRow row = reportSheet.getRow(rowHeadIndex + i);
      ReportQuarter report = reports.get(i);
      if (row == null){
    	row = reportSheet.createRow(rowHeadIndex + i);
      }
      int index = 0;
      row.createCell(index++).setCellValue(report.getId());
      row.createCell(index++).setCellValue(report.getOrdername());
      row.createCell(index++).setCellValue(report.getDeliver_start_date());
      row.createCell(index++).setCellValue(report.getDeliver_end_date());
      row.createCell(index++).setCellValue(report.getQuarter_have_day());
      row.createCell(index++).setCellValue(report.getTotal_day());
      row.createCell(index++).setCellValue(report.getName());
      row.createCell(index++).setCellValue(report.getRemark());
      row.createCell(index++).setCellValue(report.getSale_mode());
      row.createCell(index++).setCellValue(report.getCurrency());
      row.createCell(index++).setCellValue(report.getRate());
      int a = index;
      row.createCell(index++).setCellValue(report.getTotal_budget().doubleValue());
      row.createCell(index++).setCellValue(report.getProduct_budget().doubleValue());
      row.createCell(index++).setCellValue(report.getBudget_in_USD().doubleValue());
      int b = index;
      for( ; a < b; a++){
    	  row.getCell(a).setCellStyle(moneyStyle);
      }
      row.createCell(index++).setCellValue(report.getProduct_GP().doubleValue());
      row.createCell(index++).setCellValue(report.getProgress());
      row.createCell(index++).setCellValue(report.getOwner_sale());
      row.createCell(index++).setCellValue(report.getUsername());
      if (StringUtils.isEmpty(report.getCooperate_sales())){
    	row.createCell(index++).setCellValue(report.getCooperate_sales());
      }
      else{
    	String[] idStr = report.getCooperate_sales().split(",");
    	List<Long> ids = new ArrayList<>();
    	for(int idIndex = 0; idIndex < idStr.length; idIndex++){
    	  if (!StringUtils.isEmpty(idStr[idIndex]))
    		ids.add(Long.parseLong(idStr[idIndex]));
    	}
    	if (ids.size() > 0){
    	  List<User> users = userXMOMapper.findUsersByUserIds(ids);
    	  List<String> names = new ArrayList<>();
    	  for(int idIndex = 0; idIndex < users.size(); idIndex++){
    		names.add(users.get(idIndex).getName());
    	  }
    	  row.createCell(index++).setCellValue(StringUtils.join(names, ","));
    	}
    	else{
      	  row.createCell(index++).setCellValue("");
    	}
      }
      //row.createCell(index++).setCellValue(report.getUser_group());
      row.createCell(index++).setCellValue(report.getBU());
      row.createCell(index++).setCellValue(report.getFinal_gp());
      row.getCell(index - 1).setCellStyle(moneyStyle);
    }
    workbook.write(outputStream);
    outputStream.flush();
    outputStream.close();
 // 发送邮件
    List<String> receivers = new ArrayList<>();
	receivers.add("eva.xiao@i-click.com");
	receivers.add("grace.liang@i-click.com");
    receivers.add("jerry.li@i-click.com");
    sendIcrmReportMail(receivers,file_name,path);
  }
  


  public void generatePipelineExcel() throws Exception{
//   清空本周数据，插入本周最新数据
     int count_current_week = clearByTableName("current_week_data");
    int count_current_week_all = clearByTableName("current_week_all_data");
     if (count_current_week == 0 && reportMapper.insertCurrentWeekData() > 0 && count_current_week_all == 0 && reportMapper.insertCurrentWeekAllData() > 0){
//    生成Excel
     InputStream is = ReportService.class.getClassLoader().getResourceAsStream("excel/reportPipeline.xlsx");
     String file_name = "Pipeline in Booking " + CommonUtil.formatDate(new Date(), "yyyyMMdd") + ".xlsx";
     String path = REPORT_UPLOAD_FOLDER + file_name;
     FileOutputStream outputStream = new FileOutputStream(path);
     XSSFWorkbook workbook = new XSSFWorkbook(is);

     XSSFCellStyle dateStyle = workbook.createCellStyle();
     XSSFDataFormat format = workbook.createDataFormat();
     dateStyle.setDataFormat(format.getFormat("yyyy-MM-dd"));

//      数字靠右,千位分割
     XSSFCellStyle numberStyle = workbook.createCellStyle();
     numberStyle.setDataFormat(format.getFormat("#,##0.00"));
//     数字靠右，百分比显示
     XSSFCellStyle numberPercentStyle = workbook.createCellStyle();
       numberPercentStyle.setDataFormat(format.getFormat("0.00%"));


     XSSFSheet reportSheet = workbook.getSheetAt(0);
     if (reportSheet == null) {
       reportSheet = workbook.createSheet("sheet1");
     }

    // Arial字体
    XSSFFont arialFont = workbook.createFont();
    arialFont.setFontName("Calibri（主题正文）");

    List<ReportPipeline> lists = reportMapper.getPipelineReport();
    // 表头
    int rowHeadIndex = 5;
    for(int i = 0; i < lists.size(); i++){
      XSSFRow row = reportSheet.getRow(rowHeadIndex + i);
      ReportPipeline reportPipeline = lists.get(i);
      if (row == null){
        row = reportSheet.createRow(rowHeadIndex + i);
      }
      int index = 0;
      row.createCell(index++).setCellValue(reportPipeline.getId());
      row.createCell(index++).setCellValue(reportPipeline.getClient_id());
      row.createCell(index++).setCellValue(reportPipeline.getName());
      row.createCell(index++).setCellValue(reportPipeline.getCode());
      row.createCell(index++).setCellValue(reportPipeline.getClientname());
      row.createCell(index++).setCellValue(reportPipeline.getTitle());

      XSSFCell cell6 = row.createCell(index++);
      cell6.setCellValue(reportPipeline.getStart_date());
      cell6.setCellStyle(dateStyle);

      XSSFCell cell7 = row.createCell(index++);
      cell7.setCellValue(reportPipeline.getEnding_date());
      cell7.setCellStyle(dateStyle);

      row.createCell(index++).setCellValue(reportPipeline.getTotal_day());
      row.createCell(index++).setCellValue(reportPipeline.getCurrent_quarter_have_day());
      row.createCell(index++).setCellValue(reportPipeline.getShang_quarter_have_day());

      XSSFCell cell11 = row.createCell(index++);
      cell11.setCellValue(reportPipeline.getBudget());
      cell11.setCellStyle(numberStyle);

      row.createCell(index++).setCellValue(reportPipeline.getBudget_currency());

      XSSFCell cell13 = row.createCell(index++);
      cell13.setCellValue(reportPipeline.getBudget_ratio());
      cell13.setCellStyle(numberStyle);
      row.createCell(index++).setCellValue(reportPipeline.getAd_platform());
      row.createCell(index++).setCellValue(reportPipeline.getAd_type());
      row.createCell(index++).setCellValue(reportPipeline.getSale_model());

      XSSFCell cell17 = row.createCell(index++);
      cell17.setCellValue(reportPipeline.getCost());
      cell17.setCellStyle(numberStyle);

      String[] bu = StringUtils.isNotBlank(reportPipeline.getBu())? reportPipeline.getBu().trim().replace("-","").split("\n") : null;
      row.createCell(index++).setCellValue(StringUtils.join(bu,","));
      row.createCell(index++).setCellValue(reportPipeline.getGroup_names());
      row.createCell(index++).setCellValue(reportPipeline.getArea());
      row.createCell(index++).setCellValue(reportPipeline.getCreated_user());
      row.createCell(index++).setCellValue(reportPipeline.getIs_standard()? "是" : "否");
      String free_tag = reportPipeline.getFree_tag();
      if (StringUtils.isNotBlank(free_tag)){
        free_tag = free_tag.equals("1") ? "是" : "否";
      }
      row.createCell(index++).setCellValue(free_tag);

      XSSFCell cell24 = row.createCell(index++);
      cell24.setCellValue(reportPipeline.getGp_evaluate()/100);
      cell24.setCellStyle(numberPercentStyle);

      XSSFCell cell25 = row.createCell(index++);
      cell25.setCellValue(reportPipeline.getRebate()/100);
      cell25.setCellStyle(numberPercentStyle);

      XSSFCell cell26 = row.createCell(index++);
      cell26.setCellValue(reportPipeline.getMarket_cost()/100);
      cell26.setCellStyle(numberPercentStyle);

      row.createCell(index++).setCellValue(reportPipeline.getIs_add() == 0 ? "" : Integer.toString(reportPipeline.getIs_add()));
      row.createCell(index++).setCellValue(reportPipeline.getIs_delete() == 0 ? "" : Integer.toString(reportPipeline.getIs_delete()));
      row.createCell(index++).setCellValue(reportPipeline.getIs_test() == 0 ? "" : Integer.toString(reportPipeline.getIs_test()));
      row.createCell(index++).setCellValue(reportPipeline.getIs_update()== 0 ? "" : Integer.toString(reportPipeline.getIs_update()));
    }
    workbook.write(outputStream);
    outputStream.flush();
    outputStream.close();
// 发送邮件
    List<String> receivers = new ArrayList<>();
	receivers.add("will.wang@i-click.com");
	receivers.add("grace.liang@i-click.com");
	receivers.add("wenjie.sun@i-click.com");
     sendIcrmReportMail(receivers,file_name,path);
//    生成报表之后清空上周数据，将本周数据保存作为下次比对依据
     int count_last_week = clearByTableName("last_week_data");
     if (count_last_week == 0) {reportMapper.insertLastWeekData();}

     }
  }

  public  int clearByTableName(String tableName) throws Exception{
     reportMapper.clearTable(tableName);
     return reportMapper.getCountByTable(tableName);
  }

  public void sendIcrmReportMail(List<String> receivers,String file_name,String path){
    try {
      MailUtil.sendMailAndFile2Receivers(receivers, file_name, "", path);
      reportSendingService.insert(new ReportSending(file_name, path, StringUtils.join(receivers, ","), new Date(), 1));
    } catch (Exception e) {
      reportSendingService.insert(new ReportSending(file_name, path, StringUtils.join(receivers, ","), new Date(), 2));
      // 异常
      logger.error("", e);
      MailUtil.notifyAdmin("CSA 邮件发送异常");
    }
  }
  
  public void generateFinanceExcel() throws Exception{
	Calendar cal = Calendar.getInstance();
	cal.add(Calendar.MONTH, -1);
	cal.set(Calendar.DAY_OF_MONTH,1);
	int beginDate = Integer.parseInt(CommonUtil.formatDate(cal.getTime(), "yyyyMMdd"));
	cal.set(Calendar.DAY_OF_MONTH,cal.getActualMaximum(Calendar.DAY_OF_MONTH));
	int endDate = Integer.parseInt(CommonUtil.formatDate(cal.getTime(), "yyyyMMdd"));
	int dtdate = Integer.parseInt(CommonUtil.formatDate(cal.getTime(), "yyyyMM"));
	List<ReportFinance> reports = reportMapper.generateFinance(beginDate, endDate,dtdate);

	InputStream is = ReportService.class.getClassLoader().getResourceAsStream("excel/reportFinanceAdx.xlsx");
	String file_name = "Finance PC Adx Report " + CommonUtil.formatDate(new Date(), "yyyyMMdd") + ".xlsx";
	String path = REPORT_UPLOAD_FOLDER + file_name;
	FileOutputStream outputStream = new FileOutputStream(path);
	XSSFWorkbook workbook = new XSSFWorkbook(is);
	XSSFSheet reportSheet = workbook.getSheetAt(0);
	if (reportSheet == null) {
	  reportSheet = workbook.createSheet("sheet1");
	}

	// Arial字体
	XSSFFont arialFont = workbook.createFont();
	arialFont.setFontName("Calibri（主题正文）");
	XSSFDataFormat format = workbook.createDataFormat();
	// 数字靠右,千位分割
	XSSFCellStyle moneyStyle = workbook.createCellStyle();
	moneyStyle.setDataFormat(format.getFormat("#,##0.00"));
	moneyStyle.setAlignment(XSSFCellStyle.ALIGN_RIGHT);

	// 表头
	int rowHeadIndex = 1;
	for(int i = 0; i < reports.size(); i++) {
	  ReportFinance finance = reports.get(i);
	  XSSFRow row = reportSheet.getRow(rowHeadIndex + i);
      if (row == null){
    	row = reportSheet.createRow(rowHeadIndex + i);
      }
      int index = 0;
      row.createCell(index++).setCellValue(finance.getOrderID());
      row.createCell(index++).setCellValue(finance.getBu());
      row.createCell(index++).setCellValue(finance.getPlatform());
      row.createCell(index++).setCellValue(finance.getClient());
      row.createCell(index++).setCellValue(finance.getCampagin());
      row.createCell(index++).setCellValue(finance.getAdGroup());
      row.createCell(index++).setCellValue(finance.getCurrency());
//      int beginIndex = index;
//      row.createCell(index++).setCellValue();
//      row.createCell(index++).setCellValue();
//      row.createCell(index++).setCellValue();
//      row.createCell(index++).setCellValue();
//      row.createCell(index++).setCellValue();
//      row.createCell(index++).setCellValue();
//      row.createCell(index++).setCellValue();
//      int endIndex = index;
//      for (int j = beginIndex; j < endIndex; j++){
//    	row.getCell(j).setCellStyle(moneyStyle);
//      }
      String[] array = {finance.getOriginalCost(), finance.getAccuen(), finance.getTanx(), finance.getVam(), finance.getMediamax(), finance.getGdn(), finance.getMarkUpCost()};
      for(String cellString : array){
    	row = setCell(row, index, cellString);
    	row.getCell(index++).setCellStyle(moneyStyle);
      }
      row.createCell(index++).setCellValue(finance.getSellModel());
      row.createCell(index++).setCellValue(finance.getAm());
      row.createCell(index++).setCellValue(finance.getEmail());
	}
	workbook.write(outputStream);
	outputStream.flush();
	outputStream.close();
	// 发送邮件
	List<String> receivers = new ArrayList<>();
	receivers.add("ihk_finance@i-click.com");
	receivers.add("grace.liang@i-click.com");
	receivers.add("jerry.li@i-click.com");
	sendIcrmReportMail(receivers,file_name,path);
  }
  
  private XSSFRow setCell(XSSFRow row, int index, String cellString){
	try {
	  double num = Double.parseDouble(cellString);
	  row.createCell(index).setCellValue(num);
	}
	catch (Exception e){
	  row.createCell(index).setCellValue(cellString);
	}
	return row;
  }

}
