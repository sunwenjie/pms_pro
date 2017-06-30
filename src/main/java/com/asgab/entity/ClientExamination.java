package com.asgab.entity;

import java.util.Date;

public class ClientExamination {

  private Long id;
  private int approval;
  private String comment;
  private String created_user;
  private String from_state;
  private String to_state;
  private Long examinable_id;
  private String examinable_type;
  private Date created_at;
  private int node_id;
  private int status;
  private String language;

  public ClientExamination() {
    super();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public int getApproval() {
    return approval;
  }

  public void setApproval(int approval) {
    this.approval = approval;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public String getCreated_user() {
    return created_user;
  }

  public void setCreated_user(String created_user) {
    this.created_user = created_user;
  }

  public String getFrom_state() {
    return from_state;
  }

  public void setFrom_state(String from_state) {
    this.from_state = from_state;
  }

  public String getTo_state() {
    return to_state;
  }

  public void setTo_state(String to_state) {
    this.to_state = to_state;
  }

  public Long getExaminable_id() {
    return examinable_id;
  }

  public void setExaminable_id(Long examinable_id) {
    this.examinable_id = examinable_id;
  }

  public String getExaminable_type() {
    return examinable_type;
  }

  public void setExaminable_type(String examinable_type) {
    this.examinable_type = examinable_type;
  }

  public Date getCreated_at() {
    return created_at;
  }

  public void setCreated_at(Date created_at) {
    this.created_at = created_at;
  }

  public int getNode_id() {
    return node_id;
  }

  public void setNode_id(int node_id) {
    this.node_id = node_id;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public String getLanguage() {
    return language;
  }

  public void setLanguage(String language) {
    this.language = language;
  }



}
