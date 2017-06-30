package com.asgab.entity;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.ImmutableList;

public class User {
  private Long id;

  private String loginName;
  private String name;
  private String plainPassword;
  private String password;
  private String salt;
  private String roles;
  private String emailaudit;

  private Date registerDate;

  private String email;
  private String bu;
  private String financeEmail;
  private String company;
  private String department;
  private String function;

  private String status;

  private Boolean hasSetDataShare;
  private String agency;

  public User() {}

  public User(Long id) {
    this.id = id;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @NotBlank
  public String getLoginName() {
    return loginName;
  }

  public void setLoginName(String loginName) {
    this.loginName = loginName;
  }

  @NotBlank
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @JsonIgnore
  public String getPlainPassword() {
    return plainPassword;
  }

  public void setPlainPassword(String plainPassword) {
    this.plainPassword = plainPassword;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getSalt() {
    return salt;
  }

  public void setSalt(String salt) {
    this.salt = salt;
  }

  public String getRoles() {
    return roles;
  }

  public void setRoles(String roles) {
    this.roles = roles;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getCompany() {
    return company;
  }

  public void setCompany(String company) {
    this.company = company;
  }

  public String getDepartment() {
    return department;
  }

  public void setDepartment(String department) {
    this.department = department;
  }

  public String getFunction() {
    return function;
  }

  public void setFunction(String function) {
    this.function = function;
  }

  public String getEmailaudit() {
    return emailaudit;
  }

  public void setEmailaudit(String emailaudit) {
    this.emailaudit = emailaudit;
  }

  public String getFinanceEmail() {
    return financeEmail;
  }

  public void setFinanceEmail(String financeEmail) {
    this.financeEmail = financeEmail;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getBu() {
    return bu;
  }

  public void setBu(String bu) {
    this.bu = bu;
  }


  public Boolean getHasSetDataShare() {
    return hasSetDataShare;
  }

  public void setHasSetDataShare(Boolean hasSetDataShare) {
    this.hasSetDataShare = hasSetDataShare;
  }

  @JsonIgnore
  public List<String> getRoleList() {
    // 角色列表在数据库中实际以逗号分隔字符串存储，因此返回不能修改的List.
    return ImmutableList.copyOf(StringUtils.split(roles, ","));
  }

  // 设定JSON序列化时的日期格式
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
  public Date getRegisterDate() {
    return registerDate;
  }

  public void setRegisterDate(Date registerDate) {
    this.registerDate = registerDate;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

  public String getAgency() {
    return agency;
  }

  public void setAgency(String agency) {
    this.agency = agency;
  }
  
}
