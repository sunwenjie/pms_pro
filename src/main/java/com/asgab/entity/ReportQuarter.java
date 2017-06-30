package com.asgab.entity;

import java.math.BigDecimal;

public class ReportQuarter {
	
  private Long id;
  private String ordername;
  private String deliver_start_date;
  private String deliver_end_date;
  private Integer quarter_have_day;
  private Integer total_day;
  private String name;
  private String remark;
  private String sale_mode;
  private String currency;
  private float rate;
  private BigDecimal total_budget;
  private BigDecimal product_budget;
  private BigDecimal budget_in_USD;
  private BigDecimal product_GP;
  private Integer progress;
  private Long owner_sale;
  private String username;
  private String cooperate_sales;
  private String user_group;
  private String BU;
  private Long final_gp;
  
  public Long getId() {
	return id;
  }
  public void setId(Long id) {
	this.id = id;
  }
  public String getOrdername() {
	return ordername;
  }
  public void setOrdername(String ordername) {
	this.ordername = ordername;
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
  public Integer getQuarter_have_day() {
	return quarter_have_day;
  }
  public void setQuarter_have_date(Integer quarter_have_day) {
	this.quarter_have_day = quarter_have_day;
  }
  public Integer getTotal_day() {
	return total_day;
  }
  public void setTotal_day(Integer total_day) {
	this.total_day = total_day;
  }
  public String getName() {
	return name;
  }
  public void setName(String name) {
	this.name = name;
  }
  public String getRemark() {
	return remark;
  }
  public void setRemark(String remark) {
	this.remark = remark;
  }
  public String getSale_mode() {
	return sale_mode;
  }
  public void setSale_mode(String sale_mode) {
	this.sale_mode = sale_mode;
  }
  public String getCurrency() {
	return currency;
  }
  public void setCurrency(String currency) {
	this.currency = currency;
  }
  public float getRate() {
	return rate;
  }
  public void setRate(float rate) {
	this.rate = rate;
  }
  public BigDecimal getTotal_budget() {
	return total_budget;
  }
  public void setTotal_budget(BigDecimal total_budget) {
	this.total_budget = total_budget;
  }
  public BigDecimal getProduct_budget() {
	return product_budget;
  }
  public void setProduct_budget(BigDecimal product_budget) {
	this.product_budget = product_budget;
  }
  public BigDecimal getBudget_in_USD() {
	return budget_in_USD;
  }
  public void setBudget_in_USD(BigDecimal budget_in_USD) {
	this.budget_in_USD = budget_in_USD;
  }
  public BigDecimal getProduct_GP() {
	return product_GP;
  }
  public void setProduct_GP(BigDecimal product_GP) {
	this.product_GP = product_GP;
  }
  public Integer getProgress() {
	return progress;
  }
  public void setProgress(Integer progress) {
	this.progress = progress;
  }
  public Long getOwner_sale() {
	return owner_sale;
  }
  public void setOwner_sale(Long owner_sale) {
	  this.owner_sale = owner_sale;
  }
  public String getUsername() {
	return username;
  }
  public void setUsername(String username) {
	this.username = username;
  }
  public String getCooperate_sales() {
    return cooperate_sales;
  }
  public void setCooperate_sales(String cooperate_sales) {
	this.cooperate_sales = cooperate_sales;
  }
  public String getUser_group() {
	return user_group;
  }
  public void setUser_group(String user_group) {
	this.user_group = user_group;
  }
  public String getBU() {
	return BU;
  }
  public void setBU(String bU) {
	BU = bU;
  }
  public Long getFinal_gp() {
	return final_gp;
  }
  public void setFinal_gp(Long final_gp) {
	this.final_gp = final_gp;
  }
  
}
