package com.asgab.core.mail;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendEmailUsingGMailSMTP {
  public static void main(String[] args) {
    // Recipient's email ID needs to be mentioned.
    String to = "jack.sun@i-click.com";// change accordingly

    // Sender's email ID needs to be mentioned
    String from = "automation@baiduhk.com.hk";// change accordingly
    final String username = "automation@baiduhk.com.hk";// change accordingly
    final String password = "*DrqF993";// change accordingly
    // final String username = "asgab1013@gmail.com";//change accordingly
    // final String password = "asgabgmail";//change accordingly

    Properties props = new Properties();
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.starttls.enable", "true");
    props.put("mail.smtp.host", "smtp.gmail.com");
    // props.put("mail.smtp.port", "587");

    Session session = Session.getInstance(props, new javax.mail.Authenticator() {
      protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(username, password);
      }
    });

    try {
      // Create a default MimeMessage object.
      Message message = new MimeMessage(session);

      // Set From: header field of the header.
      message.setFrom(new InternetAddress(from));

      // Set To: header field of the header.
      message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));

      // Set Subject: header field
      message.setSubject("Testing Subject");

      // Now set the actual message
      message.setText("Hello, this is sample for to check send " + "email using JavaMailAPI ");

      Transport transport = Session.getDefaultInstance(props, null).getTransport("smtps");

      transport.connect("smtp.gmail.com", 465, username, password);

      // Send message
      transport.sendMessage(message, message.getAllRecipients());
      transport.close();

      System.out.println("Sent message successfully....");

    } catch (MessagingException e) {
      throw new RuntimeException(e);
    }
  }
}
