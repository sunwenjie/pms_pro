package com.asgab.util;


import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class MailAuthenticator extends Authenticator {
  String userName = null;

  String password = null;

  public MailAuthenticator() {
    super();
  }

  public MailAuthenticator(final String username, final String password) {
    this.userName = username;
    this.password = password;
  }

  protected PasswordAuthentication getPasswordAuthentication() {
    return new PasswordAuthentication(userName, password);
  }
}
