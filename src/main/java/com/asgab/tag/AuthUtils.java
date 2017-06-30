package com.asgab.tag;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import com.asgab.service.account.ShiroDbRealm.ShiroUser;

public class AuthUtils {

  public static boolean hasAuthority(String equal1, String equal2, String array,
      HttpServletRequest request) {

    Subject subject = SecurityUtils.getSubject();
    if (null == subject)
      return false;

    ShiroUser currUser = (ShiroUser) subject.getPrincipal();

    if (subject.hasRole("admin") || subject.hasRole("superAdmin") || currUser.getId().toString().equals(equal1) || currUser.getId().toString().equals(equal2)
        || (array != null && ArrayUtils.contains(array.split(","), currUser.getId().toString())))
      return true;

    return false;
  }

}
