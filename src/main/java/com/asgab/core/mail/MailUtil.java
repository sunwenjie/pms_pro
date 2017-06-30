package com.asgab.core.mail;

import com.asgab.util.CommonUtil;
import com.asgab.util.MailAuthenticator;
import freemarker.template.TemplateException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * 邮件发送工具类
 */
public class MailUtil {

  protected Log logger = LogFactory.getLog(getClass());

  public static void main(String[] args) throws UnsupportedEncodingException {
    notifyAdmin("test");
  }

  public static void sendMailByTemplate(String receiver, String subject, Map<String, Object> map, String templateName)
          throws IOException, TemplateException, MessagingException, InterruptedException, ExecutionException, TimeoutException {
    sendMailByTemplate(receiver, null, subject, map, templateName);
  }

  /**
   * 根据模板名称查找模板，加载模板内容后发送邮件
   *
   * @param receiver 收件人地址
   * @param subject 邮件主题
   * @param map 邮件内容与模板内容转换对象
   * @param templateName 模板文件名称
   */
  public static void sendMailByTemplate(String receiver, String sendCopyTo, String subject, Map<String, Object> map, String templateName)
          throws IOException, TemplateException, MessagingException, InterruptedException, ExecutionException, TimeoutException {
    String maiBody = "";
    String server = ConfigLoader.getServer();
    String sender = ConfigLoader.getSender();
    String username = ConfigLoader.getUsername();
    String password = ConfigLoader.getPassword();
    String nickname = ConfigLoader.getNickname();
    int timeout = ConfigLoader.getTimeout();
    MailSender mail = new MailSender(server, username, password, nickname, timeout);
    maiBody = TemplateFactory.generateHtmlFromFtl(templateName, map);
    mail.setSubject(subject);
    mail.setBody(maiBody);
    mail.setReceiver(receiver);
    if (StringUtils.isNoneBlank(sendCopyTo)) {
      mail.setCopyTo(sendCopyTo);
    }
    mail.setSender(sender);
    mail.sendout();

  }

  /**
   * 根据模板名称查找模板，加载模板内容后发送邮件
   *
   * @param receiver 收件人地址
   * @param subject 邮件主题
   * @param map 邮件内容与模板内容转换对象
   * @param templateName 模板文件名称
   */
  public static void sendMailAndFileByTemplate(String receiver, String subject, List<String> filePaths, Map<String, Object> map, String templateName)
          throws IOException, TemplateException, MessagingException, InterruptedException, ExecutionException, TimeoutException {
    String maiBody = "";
    String server = ConfigLoader.getServer();
    String sender = ConfigLoader.getSender();
    String username = ConfigLoader.getUsername();
    String password = ConfigLoader.getPassword();
    String nickname = ConfigLoader.getNickname();
    int timeout = ConfigLoader.getTimeout();
    MailSender mail = new MailSender(server, username, password, nickname, timeout);
    maiBody = TemplateFactory.generateHtmlFromFtl(templateName, map);
    mail.setSubject(subject);
    if (filePaths != null && filePaths.size() > 0) {
      for (String filePath : filePaths) {
        mail.addFileAffix(filePath);
      }
    }
    mail.setBody(maiBody);
    mail.setReceiver(receiver);
    mail.setSender(sender);
    mail.sendout();
  }

  /**
   * 多个收件人
   *
   * @param receivers
   * @param subject
   * @param filePaths
   * @param map
   * @param templateName
   * @throws IOException
   * @throws TemplateException
   * @throws MessagingException
   */
  public static void sendMailAndFileByTemplate(List<String> receivers, String subject, List<String> filePaths, Map<String, Object> map,
      String templateName) throws IOException, TemplateException, MessagingException, InterruptedException, ExecutionException, TimeoutException {
    String maiBody = "";
    String server = ConfigLoader.getServer();
    String sender = ConfigLoader.getSender();
    String username = ConfigLoader.getUsername();
    String password = ConfigLoader.getPassword();
    String nickname = ConfigLoader.getNickname();
    int timeout = ConfigLoader.getTimeout();
    MailSender mail = new MailSender(server, username, password, nickname, timeout);
    maiBody = TemplateFactory.generateHtmlFromFtl(templateName, map);
    mail.setSubject(subject);
    if (filePaths != null && filePaths.size() > 0) {
      for (String filePath : filePaths) {
        mail.addFileAffix(filePath);
      }
    }
    mail.setBody(maiBody);
    mail.setReceivers(receivers);
    mail.setSender(sender);
    mail.sendout();
  }

  /**
   * 普通方式发送邮件内容
   *
   * @param receiver 收件人地址
   * @param subject 邮件主题
   * @param maiBody 邮件正文
   */
  public static void sendMail(String receiver, String subject, String maiBody) throws IOException, MessagingException, InterruptedException, ExecutionException, TimeoutException {
	MailSender mail = generateNormalMail(subject, maiBody);
    mail.setReceiver(receiver);
    mail.sendout();
  }

  private static MailSender generateNormalMail(String subject, String maiBody) throws IOException, MessagingException {
	String server = ConfigLoader.getServer();
    String sender = ConfigLoader.getSender();
    String username = ConfigLoader.getUsername();
    String password = ConfigLoader.getPassword();
    String nickname = ConfigLoader.getNickname();
    int timeout = ConfigLoader.getTimeout();
    MailSender mail = new MailSender(server, username, password, nickname, timeout);
    mail.setSubject(subject);
    mail.setBody(maiBody);
    mail.setSender(sender);
    return mail;
  }

  public static void sendMailAndFile2Receivers(List<String> receivers, String subject, String maiBody, String filePath) throws IOException, MessagingException, InterruptedException, ExecutionException, TimeoutException {
    MailSender mail = generateNormalMail(subject, maiBody);
    mail.setReceivers(receivers);
    mail.addFileAffix(filePath);
    mail.sendout();
  }

  /**
   * 普通方式发送邮件内容，并且附带文件附件
   *
   * @param receiver 收件人地址
   * @param subject 邮件主题
   * @param filePath 文件的绝对路径
   * @param maiBody 邮件正文
   */
  public static void sendMailAndFile(String receiver, String subject, String filePath, String maiBody) throws IOException, MessagingException, InterruptedException, ExecutionException, TimeoutException {
    MailSender mail = generateNormalMail(subject, maiBody);
    mail.addFileAffix(filePath);
    mail.setReceiver(receiver);
    mail.sendout();
  }

  /**
   * 邮件失败通知管理员
   */
  public static void notifyAdmin(String mainBody) {

    final Properties props = new Properties();
    props.put("mail.smtp.host", "smtp.office365.com");
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.starttls.enable", "true");
    MailAuthenticator authenticator = new MailAuthenticator("hris@i-click.com", "E@sY6@y2015");
    final Session session = Session.getDefaultInstance(props, authenticator);
    session.setDebug(false);
    try {
      // 根据Session创建一个邮件消息
      final Message message = new MimeMessage(session);
      // 创建邮件发送者地址
      final Address fromAddress = new InternetAddress("Autoadmin@i-click.com");
      // 设置邮件消息的发送者
      message.setFrom(fromAddress);
      // 创建邮件的接收者地址，并设置到邮件消息中
      List<String> receivers = CommonUtil.getErrorAdminMails();

      InternetAddress[] sendTo = new InternetAddress[receivers.size()];
      for (int i = 0; i < receivers.size(); i++) {
        sendTo[i] = new InternetAddress(receivers.get(i));
      }
      message.setRecipients(Message.RecipientType.TO, sendTo);

      // 设置邮件消息的主题
      message.setSubject("CSA 邮件发送异常");
      // 设置邮件消息发送的时间
      message.setSentDate(new Date());

      // MiniMultipart类是一个容器类，包含MimeBodyPart类型的对象
      final Multipart multipart = new MimeMultipart("mixed");
      // 创建一个包含HTML内容的MimeBodyPart
      final BodyPart bodyPart = new MimeBodyPart();
      // 设置HTML内容
      bodyPart.setContent(mainBody, "text/html;charset=utf-8");
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

}
