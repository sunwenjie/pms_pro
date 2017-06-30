package com.asgab.repository;

import java.util.List;

import com.asgab.entity.ReportSending;
import com.asgab.repository.mybatis.MyBatisRepository;

@MyBatisRepository
public interface ReportSendingMapper {
	
  void insert(ReportSending reportSending);
  
  void update(ReportSending reportSending);
  
  List<ReportSending> getSendingsByStatus(int status);
  
}
