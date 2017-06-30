package com.asgab.web.account;

import java.util.Locale;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.SavedRequest;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.asgab.service.account.ShiroDbRealm.ShiroUser;

/**
 * LoginController负责打开登录页面(GET请求)和登录出错页面(POST请求)，
 * 
 * 真正登录的POST请求由Filter完成,
 * 
 * @author calvin
 */
@Controller
@RequestMapping(value = "/login")
public class LoginController {

  @RequestMapping(method = RequestMethod.GET)
  public String login(HttpServletRequest request,RedirectAttributes redirectAttributes) {
    Subject currentUser = SecurityUtils.getSubject();
    if (null != currentUser) {
      // 如果已经登录，则退出or跳转到主页
      if (null != (ShiroUser) currentUser.getPrincipal()) {
        //currentUser.logout(); //安全起见，用这种，退出再登录
        return "redirect:/businessOpportunity";
      }
    }
    return "account/login";
  }

  @RequestMapping(method = RequestMethod.POST)
  public String fail(@RequestParam(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM) String userName, Model model, ServletRequest request) {
 
    model.addAttribute(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM, userName);
    return "account/login";
  }



}
