package com.asgab.tag;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

public class AuthTag extends TagSupport {

  private static final long serialVersionUID = 1L;

  /** 三个参数逻辑或 **/
  private String equal1;// 标签传入参数1，用以比较当前UserID
  private String equal2;// 标签传入参数2，用以比较当前UserID
  private String array;// 标签传入参数2，逗号分隔，是否包含当前UserID

  public String getEqual1() {
    return equal1;
  }

  public void setEqual1(String equal1) {
    this.equal1 = equal1;
  }

  public String getEqual2() {
    return equal2;
  }

  public void setEqual2(String equal2) {
    this.equal2 = equal2;
  }

  public String getArray() {
    return array;
  }

  public void setArray(String array) {
    this.array = array;
  }

  @Override
  public int doStartTag() throws JspException {

    int ret = AuthTag.SKIP_BODY;

    final HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();

    if (AuthUtils.hasAuthority(equal1, equal2, array, request)) {

      ret = AuthTag.EVAL_BODY_INCLUDE;

    }

    return ret;
  }



}
