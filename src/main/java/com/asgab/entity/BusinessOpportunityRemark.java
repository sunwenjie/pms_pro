package com.asgab.entity;

import java.util.Date;

public class BusinessOpportunityRemark {
  private Long id;
  private Long business_opportunity_id;
  private Integer progress;
  private String content;
  private Date created_at;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getBusiness_opportunity_id() {
    return business_opportunity_id;
  }

  public void setBusiness_opportunity_id(Long business_opportunity_id) {
    this.business_opportunity_id = business_opportunity_id;
  }

  public Integer getProgress() {
    return progress;
  }

  public void setProgress(Integer progress) {
    this.progress = progress;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public Date getCreated_at() {
    return created_at;
  }

  public void setCreated_at(Date created_at) {
    this.created_at = created_at;
  }

  public void reset(BusinessOpportunity businessOpportunity) {
    if (businessOpportunity != null) {
      this.business_opportunity_id = businessOpportunity.getId();
      this.progress = businessOpportunity.getProgress();
      this.content = businessOpportunity.getProgressRemarkPage();
      this.created_at = new Date();
    }
  }

}
