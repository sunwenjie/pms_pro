package com.asgab.entity;

import java.math.BigDecimal;
import java.util.List;

public class ReportModel {

  private BigDecimal totalOpportunity;
  private BigDecimal totalOrder;
  private BigDecimal totalGPOpportunity;
  private BigDecimal totalGPOrder;
  private Integer advertiserCount;
  private Integer orderCount;
  private Integer campaignCount;


  // report1 saleTeam
  private List<String> labels1;
  private List<BigDecimal> dataIncome1;
  private List<BigDecimal> dataGp1;



  // report2 这个label其实没用到. 值来自上面7个值的第1,2,3,4值
  private List<String> labels2;
  private List<BigDecimal> dataIncome2;
  private List<BigDecimal> dataGp2;
  private BigDecimal campaignSum;



  // report3 product
  private List<String> labels3;
  private List<BigDecimal> dataIncome3;
  private List<BigDecimal> dataGp3;



  // report4 adType
  private List<String> labels4;
  private List<BigDecimal> dataIncome4;
  private List<BigDecimal> dataGp4;

  private boolean gpQuery = false;

  public BigDecimal getTotalOpportunity() {
    return totalOpportunity;
  }

  public void setTotalOpportunity(BigDecimal totalOpportunity) {
    this.totalOpportunity = totalOpportunity;
  }

  public BigDecimal getTotalOrder() {
    return totalOrder;
  }

  public void setTotalOrder(BigDecimal totalOrder) {
    this.totalOrder = totalOrder;
  }

  public BigDecimal getTotalGPOpportunity() {
    return totalGPOpportunity;
  }

  public void setTotalGPOpportunity(BigDecimal totalGPOpportunity) {
    this.totalGPOpportunity = totalGPOpportunity;
  }

  public BigDecimal getTotalGPOrder() {
    return totalGPOrder;
  }

  public void setTotalGPOrder(BigDecimal totalGPOrder) {
    this.totalGPOrder = totalGPOrder;
  }

  public Integer getAdvertiserCount() {
    return advertiserCount;
  }

  public void setAdvertiserCount(Integer advertiserCount) {
    this.advertiserCount = advertiserCount;
  }

  public Integer getOrderCount() {
    return orderCount;
  }

  public void setOrderCount(Integer orderCount) {
    this.orderCount = orderCount;
  }

  public Integer getCampaignCount() {
    return campaignCount;
  }

  public void setCampaignCount(Integer campaignCount) {
    this.campaignCount = campaignCount;
  }

  public List<String> getLabels1() {
    return labels1;
  }

  public void setLabels1(List<String> labels1) {
    this.labels1 = labels1;
  }

  public List<BigDecimal> getDataIncome1() {
    return dataIncome1;
  }

  public void setDataIncome1(List<BigDecimal> dataIncome1) {
    this.dataIncome1 = dataIncome1;
  }

  public List<String> getLabels2() {
    return labels2;
  }

  public void setLabels2(List<String> labels2) {
    this.labels2 = labels2;
  }

  public List<BigDecimal> getDataIncome2() {
    return dataIncome2;
  }

  public void setDataIncome2(List<BigDecimal> dataIncome2) {
    this.dataIncome2 = dataIncome2;
  }

  public List<String> getLabels3() {
    return labels3;
  }

  public void setLabels3(List<String> labels3) {
    this.labels3 = labels3;
  }

  public List<BigDecimal> getDataIncome3() {
    return dataIncome3;
  }

  public void setDataIncome3(List<BigDecimal> dataIncome3) {
    this.dataIncome3 = dataIncome3;
  }

  public List<String> getLabels4() {
    return labels4;
  }

  public void setLabels4(List<String> labels4) {
    this.labels4 = labels4;
  }

  public List<BigDecimal> getDataIncome4() {
    return dataIncome4;
  }

  public void setDataIncome4(List<BigDecimal> dataIncome4) {
    this.dataIncome4 = dataIncome4;
  }

  public List<BigDecimal> getDataGp1() {
    return dataGp1;
  }

  public void setDataGp1(List<BigDecimal> dataGp1) {
    this.dataGp1 = dataGp1;
  }

  public List<BigDecimal> getDataGp2() {
    return dataGp2;
  }

  public void setDataGp2(List<BigDecimal> dataGp2) {
    this.dataGp2 = dataGp2;
  }

  public List<BigDecimal> getDataGp3() {
    return dataGp3;
  }

  public void setDataGp3(List<BigDecimal> dataGp3) {
    this.dataGp3 = dataGp3;
  }

  public List<BigDecimal> getDataGp4() {
    return dataGp4;
  }

  public void setDataGp4(List<BigDecimal> dataGp4) {
    this.dataGp4 = dataGp4;
  }

  public boolean isGpQuery() {
    return gpQuery;
  }

  public void setGpQuery(boolean gpQuery) {
    this.gpQuery = gpQuery;
  }

  public BigDecimal getCampaignSum() {
    return campaignSum;
  }

  public void setCampaignSum(BigDecimal campaignSum) {
    this.campaignSum = campaignSum;
  }

  // 排序 倒序 销售团队1, 广告形式3
  public void sort() {
    if (labels1 != null && labels1.size() > 0) {
      sort(labels1, dataIncome1, dataGp1);
    }

    if (labels3 != null && labels3.size() > 0) {
      sort(labels3, dataIncome3, dataGp3);
    }
  }

  // 冒泡排序
  private void sort(List<String> labels, List<BigDecimal> dataIncome, List<BigDecimal> dataGp) {
    for (int i = 0; i < dataIncome.size(); i++) {
      for (int j = i + 1; j < dataIncome.size(); j++) {
        // 如果比较大. 换位置
        if (dataIncome.get(j).compareTo(dataIncome.get(i)) == 1) {
          // 值
          BigDecimal tmp = dataIncome.get(i);
          dataIncome.set(i, dataIncome.get(j));
          dataIncome.set(j, tmp);
          // labels
          String tmpLabel = labels.get(i);
          labels.set(i, labels.get(j));
          labels.set(j, tmpLabel);
          // gp
          BigDecimal tmpGp = dataGp.get(i);
          dataGp.set(i, dataGp.get(j));
          dataGp.set(j, tmpGp);
        }
      }
    }
  }

}


