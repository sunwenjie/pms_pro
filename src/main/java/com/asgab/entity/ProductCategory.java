package com.asgab.entity;

public class ProductCategory {

  private Long id;
  private String name;
  private String name_en;
  private String value;

  public ProductCategory() {}

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

  public String getName_en() {
    return name_en;
  }

  public void setName_en(String name_en) {
    this.name_en = name_en;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }


}
