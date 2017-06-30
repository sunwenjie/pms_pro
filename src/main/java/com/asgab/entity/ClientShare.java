package com.asgab.entity;

public class ClientShare {

  /***
   * 广告主分配人员
   * 
   * @author Siuvan
   */

  private Long id;
  private Long client_id;
  private Long share_id;

  public ClientShare() {
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

  public Long getShare_id() {
    return share_id;
  }

  public void setShare_id(Long share_id) {
    this.share_id = share_id;
  }

}
