package com.asgab.task;

import com.asgab.service.report.ReportService;

import javax.annotation.Resource;
import java.io.IOException;

public class IcrmReportTask {
	
	@Resource
	private ReportService reportService;
	
	public void run() throws Exception {
	  System.out.println("=======IcrmReport=======");
	  try {
		reportService.generatePipelineExcel();
		reportService.generateQuarterExcel();
	  } catch (IOException e) {
		e.printStackTrace();
	  }
	}

}
