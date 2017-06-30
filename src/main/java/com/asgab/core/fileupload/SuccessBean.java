package com.asgab.core.fileupload;

public class SuccessBean {
  private boolean success;
  private String msg;

  public SuccessBean(boolean b, String s) {
    this.success = b;
    this.msg = s;
  }

  public boolean isSuccess() {
    return success;
  }

  public void setSuccess(boolean success) {
    this.success = success;
  }

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }
}
