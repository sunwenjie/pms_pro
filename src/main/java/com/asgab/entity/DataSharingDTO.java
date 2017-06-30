package com.asgab.entity;

import java.util.ArrayList;
import java.util.List;

public class DataSharingDTO {

  private DataSharing parentNode;

  private List<DataSharingDTO> chidrenNodes = new ArrayList<DataSharingDTO>();

  public DataSharing getParentNode() {
    return parentNode;
  }

  public void setParentNode(DataSharing parentNode) {
    this.parentNode = parentNode;
  }

  public List<DataSharingDTO> getChidrenNodes() {
    return chidrenNodes;
  }

  public void setChidrenNodes(List<DataSharingDTO> chidrenNodes) {
    this.chidrenNodes = chidrenNodes;
  }

}
