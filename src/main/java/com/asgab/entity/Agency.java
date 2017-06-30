package com.asgab.entity;

public class Agency {

  /****
   * 代理商
   * 
   * @author Siuvan
   */

  private Long id;
  private String channel_name;
  private int is_cancel_examine;

  public Agency() {
    super();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getChannel_name() {
    return channel_name;
  }

  public void setChannel_name(String channel_name) {
    this.channel_name = channel_name;
  }

  public int getIs_cancel_examine() {
    return is_cancel_examine;
  }

  public void setIs_cancel_examine(int is_cancel_examine) {
    this.is_cancel_examine = is_cancel_examine;
  }

}
