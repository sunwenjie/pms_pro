package com.asgab.entity;

import java.util.Date;

public class Log {

  private Long id;
  private int operateType;
  private String module;
  private String operateBy;
  private String operateUserId;
  private Date operateTime;
  private String content;
  private String filePath;
  private String pKey;
  private String remark;
  private String remark1;
  private String remark2;
  private String deleted;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public int getOperateType() {
    return operateType;
  }

  public void setOperateType(int operateType) {
    this.operateType = operateType;
  }

  public String getModule() {
    return module;
  }

  public void setModule(String module) {
    this.module = module;
  }

  public String getOperateBy() {
    return operateBy;
  }

  public void setOperateBy(String operateBy) {
    this.operateBy = operateBy;
  }

  public String getOperateUserId() {
    return operateUserId;
  }

  public void setOperateUserId(String operateUserId) {
    this.operateUserId = operateUserId;
  }

  public Date getOperateTime() {
    return operateTime;
  }

  public void setOperateTime(Date operateTime) {
    this.operateTime = operateTime;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getFilePath() {
    return filePath;
  }

  public void setFilePath(String filePath) {
    this.filePath = filePath;
  }

  public String getpKey() {
    return pKey;
  }

  public void setpKey(String pKey) {
    this.pKey = pKey;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  public String getRemark1() {
    return remark1;
  }

  public void setRemark1(String remark1) {
    this.remark1 = remark1;
  }

  public String getRemark2() {
    return remark2;
  }

  public void setRemark2(String remark2) {
    this.remark2 = remark2;
  }

  public String getDeleted() {
    return deleted;
  }

  public void setDeleted(String deleted) {
    this.deleted = deleted;
  }

}
