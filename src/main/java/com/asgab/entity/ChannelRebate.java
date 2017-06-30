package com.asgab.entity;

import java.math.BigDecimal;
import java.util.Date;

public class ChannelRebate {
  private Long id;
  private Date start_date;
  private Date end_date;
  private BigDecimal rebate;
  private Long channel_id;
  private Date created_at;
  private Date updated_at;
  
  public Long getId() {
	  return id;
  }
  public void setId(Long id) {
	  this.id = id;
  }
  public Date getStart_date() {
	  return start_date;
  }
  public void setStart_date(Date start_date) {
	  this.start_date = start_date;
  }
  public Date getEnd_date() {
	  return end_date;
  }
  public void setEnd_date(Date end_date) {
	  this.end_date = end_date;
  }
  public BigDecimal getRebate() {
	  return rebate;
  }
  public void setRebate(BigDecimal rebate) {
	  this.rebate = rebate;
  }
  public Long getChannel_id() {
	  return channel_id;
  }
  public void setChannel_id(Long channel_id) {
	  this.channel_id = channel_id;
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
}
