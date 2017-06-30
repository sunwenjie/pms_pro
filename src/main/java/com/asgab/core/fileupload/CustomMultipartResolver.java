package com.asgab.core.fileupload;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

public class CustomMultipartResolver extends CommonsMultipartResolver {


  private HttpServletRequest request;

  protected FileUpload newFileUpload(FileItemFactory fileItemFactory) {
    ServletFileUpload upload = new ServletFileUpload(fileItemFactory);
    if (request != null) {
      HttpSession session = request.getSession();
      MyProgressListener uploadProgressListener = new MyProgressListener(session);
      upload.setProgressListener(uploadProgressListener);
    }
    return upload;
  }

  public MultipartHttpServletRequest resolveMultipart(HttpServletRequest request) throws MultipartException {
    this.request = request;// 获取到request,要用到session
    return super.resolveMultipart(request);
  }

  @Override
  public MultipartParsingResult parseRequest(HttpServletRequest request) throws MultipartException {
    String encoding = determineEncoding(request);
    FileUpload fileUpload = prepareFileUpload(encoding);
    // 设置监听器
    MyProgressListener progressListener = new MyProgressListener(request.getSession());
    fileUpload.setProgressListener(progressListener);
    try {
      List<FileItem> fileItems = ((ServletFileUpload) fileUpload).parseRequest(request);
      return parseFileItems(fileItems, encoding);
    } catch (FileUploadBase.SizeLimitExceededException ex) {
      throw new MaxUploadSizeExceededException(fileUpload.getSizeMax(), ex);
    } catch (FileUploadException ex) {
      throw new MultipartException("Could not parse multipart servlet request", ex);
    }
  }

}
