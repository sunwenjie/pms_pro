package com.asgab.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.asgab.util.CommonUtil;

public class Report {
  // 数据维度
  private String dataRight;
  private String reportDate;
  private String progress;
  private List<Long> saleTeam = new ArrayList<Long>();
  private List<Long> saleRepresentative = new ArrayList<Long>();
  // 预算 用于子表的预算
  private BigDecimal budget;
  private String orderType;
  private Double gp;
  private Double gp_evaluate;
  private Integer is_distribute_gp;
  private String incomeType;
  private String currency;
  private String product_currency;
  // 是否服务类
  private Integer whether_service;
  // 地区
  private String regional;
  // 总预算 用于主表的预算
  private BigDecimal totalBudget;
  private Long product_id;
  private String product_name;
  private String product_nameZh;
  private String product_nameEn;

  private Long user_id;
  private String user_name;
  private String user_ids;
  private String share_user_ids;
  private String share_user_names;

  private Long order_id;
  private Long opportunity_id;
  private String local;
  private Double rebate;
  // 售价
  private BigDecimal cost;
  // 底价 = public_price*floor_discount 底价
  private BigDecimal public_price;
  private BigDecimal floor_discount;
  private Long team_id;
  private String team_name;
  private String ad_platform;

  private Long client_id;
  private String clientname;
  private String order_code;
  private BigDecimal expenses;

  // 服务费率
  private Double service_charges_scale;
  private String metric;
  private Long channel;
  private List<Long> channels = new ArrayList<Long>();
  private List<Long> productCategories = new ArrayList<Long>();
  private String channel_name;

  // 用户页面条件
  private String reportDate_start;
  private String reportDate_end;
  private String progress_start;
  private String progress_end;
  // 页面搜索条件预算
  private String budget_page;
  private String budget_start;
  private String budget_end;

  private String orderStartDate;
  private String orderEndDate;

  // 用来保存页面条件的
  private List<Long> condSaleRepresentative = new ArrayList<Long>();

  private String currencyCode;
  private BigDecimal exchangeRate;


  // 默认不算GP,
  // 1:服务类
  // 2:非服务类 国内
  // 3:非服务类 国外
  private int calGP = 0;

  public String getDataRight() {
    return dataRight;
  }

  public void setDataRight(String dataRight) {
    this.dataRight = dataRight;
  }

  public String getReportDate() {
    return reportDate;
  }

  public void setReportDate(String reportDate) {
    this.reportDate = reportDate;
  }

  public String getProgress() {
    return progress;
  }

  public void setProgress(String progress) {
    this.progress = progress;
  }

  public List<Long> getSaleTeam() {
    return saleTeam;
  }

  public void setSaleTeam(List<Long> saleTeam) {
    this.saleTeam = saleTeam;
  }

  public List<Long> getSaleRepresentative() {
    return saleRepresentative;
  }

  public void setSaleRepresentative(List<Long> saleRepresentative) {
    this.saleRepresentative = saleRepresentative;
  }

  public List<Long> getChannels() {
    return channels;
  }

  public void setChannels(List<Long> channels) {
    this.channels = channels;
  }

  public String getOrderType() {
    return orderType;
  }

  public void setOrderType(String orderType) {
    this.orderType = orderType;
  }

  public String getIncomeType() {
    return incomeType;
  }

  public void setIncomeType(String incomeType) {
    this.incomeType = incomeType;
  }

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  public String getFmtBudget() {
    return CommonUtil.digSeg(getBudget().doubleValue());
  }

  public Long getProduct_id() {
    return product_id;
  }

  public void setProduct_id(Long product_id) {
    this.product_id = product_id;
  }

  public String getProduct_name(boolean isZH) {
    return isZH ? product_nameZh : product_nameEn;
  }

  public void setProduct_name(String product_name) {
    this.product_name = product_name;
  }

  public Long getChannel() {
    return channel;
  }

  public void setChannel(Long channel) {
    this.channel = channel;
  }

  public String getChannel_name() {
    return channel_name;
  }

  public void setChannel_name(String channel_name) {
    this.channel_name = channel_name;
  }

  public String getReportDate_start() {
    if (StringUtils.isNotBlank(reportDate) && reportDate.length() >= 10) {
      this.reportDate_start = reportDate.substring(0, 10).replace("/", "-");
    }
    return reportDate_start;
  }

  public void setReportDate_start(String reportDate_start) {
    this.reportDate_start = reportDate_start;
  }

  public String getReportDate_end() {
    if (StringUtils.isNotBlank(reportDate) && reportDate.length() >= 23) {
      this.reportDate_end = reportDate.substring(13, 23).replace("/", "-");
    }
    return reportDate_end;
  }

  public void setReportDate_end(String reportDate_end) {
    this.reportDate_end = reportDate_end;
  }

  public String getProgress_start() {
    if (StringUtils.isNotBlank(progress)) {
      this.progress_start = progress.split(";")[0];
    }
    return this.progress_start;
  }

  public void setProgress_start(String progress_start) {
    this.progress_start = progress_start;
  }

  public String getProgress_end() {
    if (StringUtils.isNotBlank(progress)) {
      this.progress_end = progress.split(";")[1];
    }
    return this.progress_end;
  }

  public void setProgress_end(String progress_end) {
    this.progress_end = progress_end;
  }

  public Integer getWhether_service() {
    return whether_service;
  }

  public void setWhether_service(Integer whether_service) {
    this.whether_service = whether_service;
  }

  public String getRegional() {
    return regional;
  }

  public void setRegional(String regional) {
    this.regional = regional;
  }

  public BigDecimal getBudget() {
    return budget;
  }

  public void setBudget(BigDecimal budget) {
    this.budget = budget;
  }

  public Double getGp() {
    return gp;
  }

  public void setGp(Double gp) {
    this.gp = gp;
  }

  public BigDecimal getTotalBudget() {
    return totalBudget;
  }

  public void setTotalBudget(BigDecimal totalBudget) {
    this.totalBudget = totalBudget;
  }

  public Long getOrder_id() {
    return order_id;
  }

  public void setOrder_id(Long order_id) {
    this.order_id = order_id;
  }

  public String getLocal() {
    return local;
  }

  public void setLocal(String local) {
    this.local = local;
  }

  public Double getRebate() {
    return rebate;
  }

  public void setRebate(Double rebate) {
    this.rebate = rebate;
  }

  public Double getService_charges_scale() {
    return service_charges_scale;
  }

  public void setService_charges_scale(Double service_charges_scale) {
    this.service_charges_scale = service_charges_scale;
  }

  public Long getOpportunity_id() {
    return opportunity_id;
  }

  public void setOpportunity_id(Long opportunity_id) {
    this.opportunity_id = opportunity_id;
  }

  public int getCalGP() {
    return calGP;
  }

  public void setCalGP(int calGP) {
    this.calGP = calGP;
  }

  public String getMetric() {
    return metric;
  }

  public void setMetric(String metric) {
    this.metric = metric;
  }

  public BigDecimal getCost() {
    return cost;
  }

  public void setCost(BigDecimal cost) {
    this.cost = cost;
  }

  public BigDecimal getPublic_price() {
    return public_price;
  }

  public void setPublic_price(BigDecimal public_price) {
    this.public_price = public_price;
  }

  public BigDecimal getFloor_discount() {
    return floor_discount;
  }

  public void setFloor_discount(BigDecimal floor_discount) {
    this.floor_discount = floor_discount;
  }

  public String getBudget_page() {
    return budget_page;
  }

  public void setBudget_page(String budget_page) {
    this.budget_page = budget_page;
  }

  public String getBudget_start() {
    if (StringUtils.isNotBlank(budget_page)) {
      this.budget_start = budget_page.split(",")[0];
    }
    return this.budget_start;
  }

  public void setBudget_start(String budget_start) {
    this.budget_start = budget_start;
  }

  public String getBudget_end() {
    if (StringUtils.isNotBlank(budget_page)) {
      this.budget_end = budget_page.split(",")[1];
    }
    return this.budget_end;
  }

  public void setBudget_end(String budget_end) {
    this.budget_end = budget_end;
  }

  public Long getUser_id() {
    return user_id;
  }

  public void setUser_id(Long user_id) {
    this.user_id = user_id;
  }

  public String getUser_name() {
    return user_name;
  }

  public void setUser_name(String user_name) {
    this.user_name = user_name;
  }

  public Long getTeam_id() {
    return team_id;
  }

  public void setTeam_id(Long team_id) {
    this.team_id = team_id;
  }

  public String getTeam_name() {
    return team_name;
  }

  public void setTeam_name(String team_name) {
    this.team_name = team_name;
  }

  public Long getClient_id() {
    return client_id;
  }

  public void setClient_id(Long client_id) {
    this.client_id = client_id;
  }

  public String getAd_platform() {
    return ad_platform;
  }

  public void setAd_platform(String ad_platform) {
    this.ad_platform = ad_platform;
  }

  public String getUser_ids() {
    return user_ids;
  }

  public void setUser_ids(String user_ids) {
    this.user_ids = user_ids;
  }

  public String getOrder_code() {
    return order_code;
  }

  public void setOrder_code(String order_code) {
    this.order_code = order_code;
  }

  public BigDecimal getExpenses() {
    return expenses;
  }

  public void setExpenses(BigDecimal expenses) {
    this.expenses = expenses;
  }

  public Double getGp_evaluate() {
    return gp_evaluate;
  }

  public void setGp_evaluate(Double gp_evaluate) {
    this.gp_evaluate = gp_evaluate;
  }

  public Integer getIs_distribute_gp() {
    return is_distribute_gp;
  }

  public void setIs_distribute_gp(Integer is_distribute_gp) {
    this.is_distribute_gp = is_distribute_gp;
  }

  public String getShare_user_ids() {
    return share_user_ids;
  }

  public void setShare_user_ids(String share_user_ids) {
    this.share_user_ids = share_user_ids;
  }

  public String getShare_user_names() {
    return share_user_names;
  }

  public void setShare_user_names(String share_user_names) {
    this.share_user_names = share_user_names;
  }

  public String getOrderStartDate() {
    return orderStartDate;
  }

  public void setOrderStartDate(String orderStartDate) {
    this.orderStartDate = orderStartDate;
  }

  public String getOrderEndDate() {
    return orderEndDate;
  }

  public void setOrderEndDate(String orderEndDate) {
    this.orderEndDate = orderEndDate;
  }

  public List<Long> getCondSaleRepresentative() {
    return condSaleRepresentative;
  }

  public void setCondSaleRepresentative(List<Long> condSaleRepresentative) {
    this.condSaleRepresentative = condSaleRepresentative;
  }

  public List<Long> getProductCategories() {
    return productCategories;
  }

  public void setProductCategories(List<Long> productCategories) {
    this.productCategories = productCategories;
  }

  public String getCurrencyCode() {
    return currencyCode;
  }

  public void setCurrencyCode(String currencyCode) {
    this.currencyCode = currencyCode;
  }

  public BigDecimal getExchangeRate() {
    return exchangeRate;
  }

  public void setExchangeRate(BigDecimal exchangeRate) {
    this.exchangeRate = exchangeRate;
  }

  public String getProduct_nameEn() {
    return product_nameEn;
  }

  public void setProduct_nameEn(String product_nameEn) {
    this.product_nameEn = product_nameEn;
  }

  public String getProduct_nameZh() {
    return product_nameZh;
  }

  public void setProduct_nameZh(String product_nameZh) {
    this.product_nameZh = product_nameZh;
  }

  public String getProduct_currency() {
    return product_currency;
  }

  public void setProduct_currency(String product_currency) {
    this.product_currency = product_currency;
  }

  public String getClientname() {
    return clientname;
  }

  public void setClientname(String clientname) {
    this.clientname = clientname;
  }

  public String getProduct_name() {
    return product_name;
  }
  
}
