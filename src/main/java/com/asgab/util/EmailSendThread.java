package com.asgab.util;

import com.asgab.core.mail.MailUtil;
import freemarker.template.TemplateException;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;


public class EmailSendThread implements Runnable {

  List<String> receivers = new ArrayList<String>();
  String subject;
  List<String> filePaths = new ArrayList<String>();
  Map<String, Object> map = new HashMap<String, Object>();
  String templateName;

  public EmailSendThread(List<String> receivers, String subject, List<String> filePaths, Map<String, Object> map, String templateName) {
    this.receivers = receivers;
    this.subject = subject;
    this.filePaths = filePaths;
    this.map = map;
    this.templateName = templateName;
  }

  @Override
  public void run() {
    try {
      MailUtil.sendMailAndFileByTemplate(receivers, subject, filePaths, map, templateName);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (TemplateException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (MessagingException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (ExecutionException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (TimeoutException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public List<String> getReceivers() {
    return receivers;
  }

  public void setReceivers(List<String> receivers) {
    this.receivers = receivers;
  }

  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public List<String> getFilePaths() {
    return filePaths;
  }

  public void setFilePaths(List<String> filePaths) {
    this.filePaths = filePaths;
  }

  public Map<String, Object> getMap() {
    return map;
  }

  public void setMap(Map<String, Object> map) {
    this.map = map;
  }

  public String getTemplateName() {
    return templateName;
  }

  public void setTemplateName(String templateName) {
    this.templateName = templateName;
  }

}
