package com.asgab.entity;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.asgab.entity.xmo.Currency;
import com.asgab.service.business.opportunity.BusinessOpportunityService;
import com.asgab.util.CommonUtil;

public class BusinessOpportunity {
  private Long id;
  private String name;
  private Long advertiser_id;
  private BigDecimal budget;
  private String budget_format;
  private Long currency_id;
  private String deliver_start_date;
  private String deliver_end_date;
  private Long owner_sale;
  private String cooperate_sales;
  private Integer exist_msa;
  private Integer exist_service;
  private Integer status;
  private Integer progress;
  private String remark;
  private Date deleted_at;
  private Date created_at;
  private Date updated_at;
  private Long created_by;
  private String username;
  private Date lostDate;
  private Date completedDate;

  private List<BusinessOpportunityProduct> businessOpportunityProducts = new ArrayList<BusinessOpportunityProduct>();
  private List<BusinessOpportunityRemark> businessOpportunityRemarks = new ArrayList<BusinessOpportunityRemark>();
  private String[] deleteProductIds;

  private String deliver_date;

  private ProgressBar progressBar;

  // for app
  private String advertiser;
  private String owner_sale_name;
  private String cooperate_sale_names;
  private String agency;
  private String currencyName;
  private List<Currency> currencys = new ArrayList<Currency>();
  // 页面progressRemark文本框
  private String progressRemarkPage;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getAdvertiser_id() {
    return advertiser_id;
  }

  public void setAdvertiser_id(Long advertiser_id) {
    this.advertiser_id = advertiser_id;
  }

  public BigDecimal getBudget() {
    return budget;
  }

  public String getFormatBudget() {
    if (budget != null) {
      return CommonUtil.digSeg(budget.doubleValue());
    }
    return "";
  }

  public void setBudget(BigDecimal budget) {
    this.budget = budget;
  }

  public Long getCurrency_id() {
    return currency_id;
  }

  public void setCurrency_id(Long currency_id) {
    this.currency_id = currency_id;
  }

  public String getDeliver_start_date() {
    return deliver_start_date;
  }

  public void setDeliver_start_date(String deliver_start_date) {
    this.deliver_start_date = deliver_start_date;
  }

  public String getDeliver_end_date() {
    return deliver_end_date;
  }

  public void setDeliver_end_date(String deliver_end_date) {
    this.deliver_end_date = deliver_end_date;
  }

  /**
   * 用于页面显示 yyyy/MM/dd -yyyy/MM/dd
   * 
   * @return
   */
  public String getDecodeDeliver_date() {
    if (StringUtils.isNotBlank(getDeliver_start_date()) && StringUtils.isNotBlank(getDeliver_end_date())) {
      return CommonUtil.changeDateFormat(getDeliver_start_date(), "-", "/") + " - " + CommonUtil.changeDateFormat(getDeliver_end_date(), "-", "/");
    }
    return "";
  }

  public Long getOwner_sale() {
    return owner_sale;
  }

  public void setOwner_sale(Long owner_sale) {
    this.owner_sale = owner_sale;
  }

  public String getCooperate_sales() {
    return cooperate_sales;
  }

  public void setCooperate_sales(String cooperate_sales) {
    this.cooperate_sales = cooperate_sales;
  }

  public Integer getExist_msa() {
    return exist_msa;
  }

  public void setExist_msa(Integer exist_msa) {
    this.exist_msa = exist_msa;
  }

  public Integer getExist_service() {
    return exist_service;
  }

  public void setExist_service(Integer exist_service) {
    this.exist_service = exist_service;
  }

  public Integer getStatus() {
    return BusinessOpportunityService.statusMap.get(progress);
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public Integer getProgress() {
    return progress;
  }

  public void setProgress(Integer progress) {
    this.progress = progress;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  public List<BusinessOpportunityProduct> getBusinessOpportunityProducts() {
    return businessOpportunityProducts;
  }

  public void setBusinessOpportunityProducts(List<BusinessOpportunityProduct> businessOpportunityProducts) {
    this.businessOpportunityProducts = businessOpportunityProducts;
  }

  public String getDeliver_date() {
    return deliver_date;
  }

  public void setDeliver_date(String deliver_date) {
    this.deliver_date = deliver_date;
  }

  public ProgressBar getProgressBar() {
    return progressBar;
  }

  public void setProgressBar(ProgressBar progressBar) {
    this.progressBar = progressBar;
  }

  public String getAdvertiser() {
    return advertiser;
  }

  public void setAdvertiser(String advertiser) {
    this.advertiser = advertiser;
  }

  public Date getDeleted_at() {
    return deleted_at;
  }

  public void setDeleted_at(Date deleted_at) {
    this.deleted_at = deleted_at;
  }

  public List<Currency> getCurrencys() {
    return currencys;
  }

  public void setCurrencys(List<Currency> currencys) {
    this.currencys = currencys;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getDecodeCurrency() {
    for (Currency c : currencys) {
      if (c.getId().longValue() == this.getCurrency_id().longValue()) {
        return c.getName();
      }
    }
    return "";
  }

  public String getDecodeStatus(String lang) {
    Map<Integer, String> statuses;
    if ("zh".equalsIgnoreCase(lang)) {
      statuses = BusinessOpportunityService.statusZH;
    } else {
      statuses = BusinessOpportunityService.statusEN;
    }
    return statuses.get(this.status);
  }

  public String getOwner_sale_name() {
    return owner_sale_name;
  }

  public void setOwner_sale_name(String owner_sale_name) {
    this.owner_sale_name = owner_sale_name;
  }

  public Date getCreated_at() {
    return created_at;
  }

  public void setCreated_at(Date created_at) {
    this.created_at = created_at;
  }

  public Date getUpdated_at() {
    return updated_at;
  }

  public void setUpdated_at(Date updated_at) {
    this.updated_at = updated_at;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getCooperate_sale_names() {
    return cooperate_sale_names;
  }

  public void setCooperate_sale_names(String cooperate_sale_names) {
    this.cooperate_sale_names = cooperate_sale_names;
  }

  public Long getCreated_by() {
    return created_by;
  }

  public void setCreated_by(Long created_by) {
    this.created_by = created_by;
  }

  public String getBudget_format() {
    return budget_format;
  }

  public void setBudget_format(String budget_format) {
    this.budget_format = budget_format;
  }

  public String[] getDeleteProductIds() {
    return deleteProductIds;
  }

  public void setDeleteProductIds(String[] deleteProductIds) {
    this.deleteProductIds = deleteProductIds;
  }

  public String getDeocdeExist_msa_zh() {
    if (0 == exist_msa)
      return CommonUtil.getProperty("zh", "CN", "business.opportunity.no");
    else if (1 == exist_msa)
      return CommonUtil.getProperty("zh", "CN", "business.opportunity.yes");
    return "";
  }

  public String getDeocdeExist_msa_en() {
    if (0 == exist_msa)
      return CommonUtil.getProperty("en", "US", "business.opportunity.no");
    else if (1 == exist_msa)
      return CommonUtil.getProperty("en", "US", "business.opportunity.yes");
    return "";
  }

  public String getDecodeExist_service_zh() {
    if (0 == exist_service)
      return CommonUtil.getProperty("zh", "CN", "business.opportunity.exec");
    else if (1 == exist_service)
      return CommonUtil.getProperty("zh", "CN", "business.opportunity.service");
    return "";
  }

  public String getDecodeExist_service_en() {
    if (0 == exist_service)
      return CommonUtil.getProperty("en", "US", "business.opportunity.exec");
    else if (1 == exist_service)
      return CommonUtil.getProperty("en", "US", "business.opportunity.service");
    return "";
  }

  public String getNumber() {
    String ret = new String("SO");
    for (int fill = 0; fill < 9 - String.valueOf(id).length(); fill++) {
      ret += "0";
    }
    ret += id;
    return ret;
  }

  public String getAgency() {
    return agency;
  }

  public void setAgency(String agency) {
    this.agency = agency;
  }

  public String getCurrencyName() {
    return currencyName;
  }

  public void setCurrencyName(String currencyName) {
    this.currencyName = currencyName;
  }

  // 创建时间到目前的时间距离. 如果是0/100%, 则取lostDate 和 completedDate
  public int getDays() {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    Date targetDate = null;
    if (getProgress() == 0 && lostDate != null) {
      targetDate = lostDate;
    } else if (getProgress() == 100 && completedDate != null) {
      targetDate = completedDate;
    } else {
      targetDate = new Date();
    }
    return CommonUtil.gapDate(sdf.format(getCreated_at()), sdf.format(targetDate));
  }

  public String getProgressRemarkPage() {
    return progressRemarkPage;
  }

  public void setProgressRemarkPage(String progressRemarkPage) {
    this.progressRemarkPage = progressRemarkPage;
  }

  // 获取当前数据库的进度备注,页面显示用
  public String getCurrentProgressRemark() {
    String remark = "";
    if (businessOpportunityRemarks != null && businessOpportunityRemarks.size() > 0) {
      for (BusinessOpportunityRemark businessOpportunityRemark : businessOpportunityRemarks) {
        if (businessOpportunityRemark.getProgress() == this.progress) {
          remark = businessOpportunityRemark.getContent();
          break;
        }
      }
    }
    return remark;
  }

  // 获取进度备注列表. view页面备注用
  public String getProgressRemarkList() {
    StringBuffer content = new StringBuffer();
    if (businessOpportunityRemarks != null && getBusinessOpportunityRemarks().size() > 0) {
      content.append("<ul class='ul-first'>");
      for (BusinessOpportunityRemark businessOpportunityRemark : businessOpportunityRemarks) {
        content.append("<li>" + businessOpportunityRemark.getProgress() + "%: " + CommonUtil.formatDate(businessOpportunityRemark.getCreated_at()) + "</li>");
        content.append("<ul><li>" + businessOpportunityRemark.getContent() + "</li></ul>");
      }
      content.append("</ul>");
    }

    return content.toString();
  }

  public Date getLostDate() {
    return lostDate;
  }

  public void setLostDate(Date lostDate) {
    this.lostDate = lostDate;
  }

  public Date getCompletedDate() {
    return completedDate;
  }

  public void setCompletedDate(Date completedDate) {
    this.completedDate = completedDate;
  }

  public List<BusinessOpportunityRemark> getBusinessOpportunityRemarks() {
    return businessOpportunityRemarks;
  }

  public void setBusinessOpportunityRemarks(List<BusinessOpportunityRemark> businessOpportunityRemarks) {
    this.businessOpportunityRemarks = businessOpportunityRemarks;
  }

}
