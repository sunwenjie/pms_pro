package com.asgab.task;

import javax.annotation.Resource;

import com.asgab.service.report.ReportService;

public class FinanceReportTask {

  @Resource
  private ReportService reportService;

  public void run() {
	try {
		reportService.generateFinanceExcel();
	} catch (Exception e) {
		e.printStackTrace();
	}
  }
	
}
