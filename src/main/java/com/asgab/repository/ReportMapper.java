package com.asgab.repository;

import com.asgab.entity.Report;
import com.asgab.entity.ReportFinance;
import com.asgab.entity.ReportPipeline;
import com.asgab.entity.ReportQuarter;
import com.asgab.repository.mybatis.MyBatisRepository;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface ReportMapper {
  // product
  List<Report> getOpportunitysByProduct(Report report);

  List<Report> getOrdersByProduct(Report report);

  // SaleTeam
  List<Report> getOpportunityBySaleTeam(Report report);

  List<Report> getOrderBySaleTeam(Report report);

  // Representative
  List<Report> getOpportunityBySaleRepresentative(Report report);

  List<Report> getOrderBySaleRepresentative(Report report);

  // channel
  List<Report> getOpportunitysByChannel(Report report);

  List<Report> getOrdersByChannel(Report report);

  //
  List<Report> getOrdersByAdPlatform(Report report);

  List<Long> getCampaignOrderIds(Report report);

  List<Report> getCampaign(Map<String, Object> params);
  
  List<ReportQuarter> getQuarterReport(String beginDate, String endDate);
  
  List<ReportFinance> generateFinance(int beginDate, int endDate, int dtdate);

  int clearTable(@Param("tableName") String tableName);
  int insertCurrentWeekData();
  int insertLastWeekData();
  int insertCurrentWeekAllData();
  List<ReportPipeline> getPipelineReport();
  int getCountByTable(@Param("tableName") String tableName);

}
