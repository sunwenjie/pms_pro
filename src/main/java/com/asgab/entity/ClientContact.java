package com.asgab.entity;

import java.util.Date;

public class ClientContact {

  /***
   * 广告主联系人
   * 
   * @author Siuvan
   */

  private Long id;
  private Long client_id;
  private String contact_person;
  private String phone;
  private String email;
  private String position;
  private Integer is_delete;
  private Date created_at;
  private Date updated_at;

  public void update(ClientContact cc) {
    this.setContact_person(cc.getContact_person());
    this.setPhone(cc.getPhone());
    this.setEmail(cc.getEmail());
    this.setPosition(cc.getPosition());
    this.setUpdated_at(new Date());
  }

  public ClientContact() {
    super();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getClient_id() {
    return client_id;
  }

  public void setClient_id(Long client_id) {
    this.client_id = client_id;
  }

  public String getContact_person() {
    return contact_person;
  }

  public void setContact_person(String contact_person) {
    this.contact_person = contact_person;
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

  public String getPosition() {
    return position;
  }

  public void setPosition(String position) {
    this.position = position;
  }

  public Integer getIs_delete() {
    return is_delete;
  }

  public void setIs_delete(Integer is_delete) {
    this.is_delete = is_delete;
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
