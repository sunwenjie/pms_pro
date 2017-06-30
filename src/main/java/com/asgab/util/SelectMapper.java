package com.asgab.util;

public class SelectMapper {
  private String id;
  private String value;
  private String tmp;

  public SelectMapper() {}

  public SelectMapper(String id, String value) {
    this.id = id;
    this.value = value;
  }

  public SelectMapper(String id, String value, String tmp) {
    this.id = id;
    this.value = value;
    this.tmp = tmp;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public String getTmp() {
    return tmp;
  }

  public void setTmp(String tmp) {
    this.tmp = tmp;
  }

}
