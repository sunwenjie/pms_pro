package com.asgab.entity;

import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class Channel {
  private Long id;
  private String channel_name;

  private String contact_person;
  private String position;
  private String phone;
  private String email;
  private String is_delete;
  private String qualification_name;
  private int currency_id;
  private String company_adress;
  private String sales;
  private Date created_at;
  private Date updated_at;
  private String rebate_date;
  private String ch_rebate;
  private String rebate_date_totip;
  private String current_rebate_date;
  private String current_rebate;

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


  public String getIs_delete() {
	return is_delete;
  }

  public void setIs_delete(String is_delete) {
	  this.is_delete = is_delete;
  }

  public String getQualification_name() {
	  return qualification_name;
  }

  public void setQualification_name(String qualification_name) {
	  this.qualification_name = qualification_name;
  }

  public int getCurrency_id() {
	  return currency_id;
  }

  public void setCurrency_id(int currency_id) {
	  this.currency_id = currency_id;
  }

  public String getCompany_adress() {
	  return company_adress;
  }

  public void setCompany_adress(String company_adress) {
	  this.company_adress = company_adress;
  }

  public String getSales() {
      String[] salesArray = sales.split(",");
      List<String> salesList = new ArrayList<>(new HashSet<>(Arrays.asList(salesArray)));
	  return StringUtils.join(salesList,",");
  }

  public void setSales(String sales) {
	  this.sales = sales;
  }

public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getChannel_name() {
    return channel_name;
  }

  public void setChannel_name(String channel_name) {
    this.channel_name = channel_name;
  }

  public String getContact_person() {
    return contact_person;
  }

  public void setContact_person(String contact_person) {
    this.contact_person = contact_person;
  }

  public String getPosition() {
    return position;
  }

  public void setPosition(String position) {
    this.position = position;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getRebate_date() {
    return rebate_date;
  }

  public void setRebate_date(String rebate_date) {
    this.rebate_date = rebate_date;
  }

  public String getCh_rebate() {
    return ch_rebate;
  }

  public void setCh_rebate(String ch_rebate) {
    this.ch_rebate = ch_rebate;
  }

  public String getRebate_date_totip() {
    String rebate_date_totip_str = "";
    if (StringUtils.isNotBlank(rebate_date_totip)) {
      String[] array = rebate_date_totip.split(",");
      List<String> list = Arrays.asList(array);
      List<String> listUniq = new ArrayList<>(new HashSet<>(list));
      listUniq.remove(0);
      rebate_date_totip_str = StringUtils.join(listUniq, "\n");
    }
    return rebate_date_totip_str;
  }

  public void setRebate_date_totip(String rebate_date_totip) {
    this.rebate_date_totip = rebate_date_totip;
  }

  public String getCurrent_rebate_date() {

    return StringUtils.isBlank(rebate_date)? "" : rebate_date.split(",")[0];
  }

  public void setCurrent_rebate_date(String current_rebate_date) {
    this.current_rebate_date = current_rebate_date;
  }

  public String getCurrent_rebate() {
    return StringUtils.isBlank(ch_rebate)? "" : ch_rebate.split(",")[0];
  }

  public void setCurrent_rebate(String current_rebate) {
    this.current_rebate = current_rebate;
  }

}
