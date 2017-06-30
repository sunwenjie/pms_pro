package com.asgab.service.report;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.asgab.entity.ReportSending;
import com.asgab.repository.ReportSendingMapper;

@Component
@Transactional
public class ReportSendingService {
  
  @Resource
  private ReportSendingMapper reportSendingMapper;
  
  public void insert (ReportSending reportSending){
	reportSendingMapper.insert(reportSending);
  }
	
}
