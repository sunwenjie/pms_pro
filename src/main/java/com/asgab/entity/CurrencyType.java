package com.asgab.entity;

public class CurrencyType {

  /****
   * 货币类型
   * 
   * @author Siuvan
   */

  private Long id;
  private String name;

  public CurrencyType() {
    super();
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


}
