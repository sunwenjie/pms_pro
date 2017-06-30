package com.asgab.task;

import com.asgab.service.mail.MailService;

import javax.annotation.Resource;

public class EmailRetryTask {
	
  @Resource
  private MailService mailService;
	
  public void run(){
	mailService.sendFailReportEmail();
  }

}
