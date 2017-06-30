package com.asgab.util;


import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Date;
import java.util.Properties;

public class Mail {
  private static String MAIL_SMTP_HOST = "smtp.office365.com";
  private static String MAIL_USERNAME = "hris@i-click.com";
  private static String MAIL_PWD = "E@sY6@y2015";

  public static String MAIL_TO = "iori.luo@i-click.com";

  // 发件人地址
  private String from;

  // 收件人地址
  private String to;

  // 邮件标题
  private String subject;

  // 邮件内容
  private String content;

  public Mail(String to, String subject, String content) {
    this.from = MAIL_USERNAME;
    this.to = to;
    this.subject = subject;
    this.content = content;
  }

  public Mail() {}

  /**
   * 发送邮件 - 基本方法
   * 
   * @throws Exception
   */
  public void send() {

    final Properties props = new Properties();
    // 设置发送邮件的邮件服务器的属性
    props.put("mail.smtp.host", MAIL_SMTP_HOST);
    // 需要经过授权，也就是有户名和密码的校验，这样才能通过验证（一定要有这一条）
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.starttls.enable", "true");
    // 如果需要身份认证，则创建一个密码验证器
    MailAuthenticator authenticator = new MailAuthenticator(MAIL_USERNAME, MAIL_PWD);
    // 根据邮件会话属性和密码验证器构造一个发送邮件的session
    final Session session = Session.getDefaultInstance(props, authenticator);
    session.setDebug(false);

    try {
      // 根据Session创建一个邮件消息
      final Message message = new MimeMessage(session);
      // 创建邮件发送者地址
      final Address fromAddress = new InternetAddress(from);
      // 设置邮件消息的发送者
      message.setFrom(fromAddress);
      // 创建邮件的接收者地址，并设置到邮件消息中
      final Address toAddress = new InternetAddress(to);
      message.setRecipient(Message.RecipientType.TO, toAddress);
      // 设置邮件消息的主题
      message.setSubject(subject);
      // 设置邮件消息发送的时间
      message.setSentDate(new Date());

      // MiniMultipart类是一个容器类，包含MimeBodyPart类型的对象
      final Multipart multipart = new MimeMultipart("mixed");
      // 创建一个包含HTML内容的MimeBodyPart
      final BodyPart bodyPart = new MimeBodyPart();
      // 设置HTML内容
      bodyPart.setContent(getContent(), "text/html; charset=utf-8");
      multipart.addBodyPart(bodyPart);

      // 将MiniMultipart对象设置为邮件内容
      message.setContent(multipart);

      // 发送邮件
      Transport.send(message);
    } catch (final Exception e) {
      e.printStackTrace();
      System.err.println("发送邮件出错");
    }
  }

  private String getContent() {
    return "<html class='has-js' lang='en'><head><meta http-equiv='content-type' content='text/html; charset=utf-8'></head><body>" + this.content
        + "</body></html>";
  }
}
