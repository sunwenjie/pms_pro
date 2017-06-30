package com.asgab.core.fileupload;

import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.ProgressListener;

public class MyProgressListener implements ProgressListener {

  private HttpSession session;

  public MyProgressListener() {}

  // 构造函数
  public MyProgressListener(HttpSession session) {
    this.session = session;
    ProgressEntity ps = new ProgressEntity();
    session.setAttribute("upload_ps", ps);
  }

  public void setSession(HttpSession session) {
    this.session = session;
  }

  @Override
  public void update(long bytesRead, long contentLength, int items) {
    ProgressEntity ps = (ProgressEntity) session.getAttribute("upload_ps");
    ps.setpBytesRead(bytesRead);
    ps.setpContentLength(contentLength);
    ps.setpItems(items);
    // 更新
    session.setAttribute("upload_ps", ps);
    // System.out.println("当前进度:"+ps.getpBytesRead());
  }
}
