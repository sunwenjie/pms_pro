package com.asgab.service.mail;

import com.asgab.core.mail.MailUtil;
import com.asgab.entity.ReportSending;
import com.asgab.repository.ReportSendingMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

@Component
@Transactional
public class MailService {
  
  @Resource
  private ReportSendingMapper reportSendingMapper;
  
  public void sendFailReportEmail(){
	List<ReportSending> reportSendings = reportSendingMapper.getSendingsByStatus(2);
	if (reportSendings == null || reportSendings.size() < 1)
	  return;
	for(int i = 0; i < reportSendings.size(); i++){
	  ReportSending rs = reportSendings.get(i);
	  List<String> receivers = Arrays.asList(rs.getReceives().split(","));
	  String subject = rs.getName();
	  String maiBody = "";
	  String filePath = rs.getFile_path();
	  try {
		MailUtil.sendMailAndFile2Receivers(receivers, subject, maiBody, filePath);
		rs.setStatus(1);
		reportSendingMapper.update(rs);
	  } catch (Exception e) {
		rs.setStatus(2);
		reportSendingMapper.update(rs);
		e.printStackTrace();
	  }
	}
  }
  
}
