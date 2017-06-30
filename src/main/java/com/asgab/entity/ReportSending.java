package com.asgab.entity;

import java.util.Date;

public class ReportSending {
  private Long id;
  private String name;
  private String file_path;
  private String receives;
  private Date send_time;
  private int status;
  
  public ReportSending(){
	  
  }
  
  public ReportSending(String name, String file_path, String receives, Date send_time, int status){
	this.name = name;
	this.file_path = file_path;
	this.receives = receives;
	this.send_time = send_time;
	this.status = status;
  }
  
  public Long getId() {
	return id;
  }
  public void setId(Long id) {
	this.id = id;
  }
  public String getName() {
	return name;
  }
  public void setName(String name) {
	this.name = name;
  }
  public String getFile_path() {
	return file_path;
  }
  public void setFile_path(String file_path) {
	this.file_path = file_path;
  }
  public String getReceives() {
	return receives;
  }
  public void setReceives(String receives) {
	this.receives = receives;
  }
  public Date getSend_time() {
	return send_time;
  }
  public void setSend_time(Date send_time) {
	this.send_time = send_time;
  }

  public int getStatus() {
	return status;
  }

  public void setStatus(int status) {
	this.status = status;
  }
}
