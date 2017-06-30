package com.asgab.entity;

public class ReportFinance {
  private String orderID;
  private String bu;
  private String platform;
  private String client;
  private String campaign;
  private String adGroup;
  private String currency;
  
  //double
  private String originalCost;
  private String accuen;
  private String tanx;
  private String vam;
  private String mediamax;
  private String gdn;
  private String markUpCost;
  //double
  
  private String sellModel;
  private String am;
  private String email;
  
  public String getOrderID() {
	return orderID;
  }
  public void setOrderID(String orderID) {
	this.orderID = orderID;
  }
  public String getBu() {
	return bu;
  }
  public void setBu(String bu) {
	this.bu = bu;
  }
  public String getPlatform() {
	return platform;
  }
  public void setPlatform(String platform) {
	this.platform = platform;
  }
  public String getClient() {
	return client;
  }
  public void setClient(String client) {
	this.client = client;
  }
  public String getCampagin() {
	return campaign;
  }
  public void setCampagin(String campaign) {
	this.campaign = campaign;
  }
  public String getAdGroup() {
	return adGroup;
  }
  public void setAdGroup(String adGroup) {
	this.adGroup = adGroup;
  }
  public String getCurrency() {
	return currency;
  }
  public void setCurrency(String currency) {
	this.currency = currency;
  }
  public String getOriginalCost() {
	return originalCost;
  }
  public void setOriginalCost(String originalCost) {
	this.originalCost = originalCost;
  }
  public String getAccuen() {
	return accuen;
  }
  public void setAccuen(String accuen) {
	this.accuen = accuen;
  }
  public String getTanx() {
	return tanx;
  }
  public void setTanx(String tanx) {
	this.tanx = tanx;
  }
  public String getVam() {
	return vam;
  }
  public void setVam(String vam) {
	this.vam = vam;
  }
  public String getMediamax() {
	return mediamax;
  }
  public void setMediamax(String mediamax) {
	this.mediamax = mediamax;
  }
  public String getGdn() {
	if ("GDN".equals(platform)){
	  return originalCost;
	}
	return gdn;
  }
  public void setGdn(String gdn) {
	this.gdn = gdn;
  }
  public String getAm() {
	return am;
  }
  public void setAm(String am) {
	this.am = am;
  }
  public String getEmail() {
	return email;
  }
  public void setEmail(String email) {
	this.email = email;
  }
  public String getMarkUpCost() {
	return markUpCost;
  }
  public void setMarkUpCost(String markUpCost) {
	this.markUpCost = markUpCost;
  }
  public String getSellModel() {
	return sellModel;
  }
  public void setSellModel(String sellModel) {
	this.sellModel = sellModel;
  }
}
