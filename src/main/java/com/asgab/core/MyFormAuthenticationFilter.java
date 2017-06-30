package com.asgab.core;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.SavedRequest;
import org.apache.shiro.web.util.WebUtils;

/**
 * 
 * @author Jack
 *
 */
public class MyFormAuthenticationFilter extends FormAuthenticationFilter {

  /**
   * 验证码错误，后面的filter无须执行
   */
  protected boolean onAccessDenied(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
    if (request.getAttribute(getFailureKeyAttribute()) != null) {
      return true;
    }
    return super.onAccessDenied(request, response, mappedValue);
  }

  /**
   * 解决shiro登录成功后不跳转
   */
  protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) throws Exception {
    SavedRequest savedRequest = WebUtils.getSavedRequest(request);
    String url = savedRequest != null ? savedRequest.getRequestUrl() : "";
    if (StringUtils.isNotBlank(url)) {
      url = url.replaceFirst(request.getServletContext().getContextPath(), "");
    }
    if (StringUtils.isBlank(url) || "/".equals(url) || "//".equals(url) || "/favicon.ico".equals(url)) {
      url = getSuccessUrl();
    }
    WebUtils.issueRedirect(request, response, url);
    return false;
  }
}
