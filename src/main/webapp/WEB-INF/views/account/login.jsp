<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="org.apache.shiro.web.filter.authc.FormAuthenticationFilter"%>
<%@ page import="org.apache.shiro.authc.ExcessiveAttemptsException"%>
<%@ page import="org.apache.shiro.authc.IncorrectCredentialsException"%>
<%@ page import="java.util.Locale"%>
<%@ page import="org.springframework.context.i18n.LocaleContextHolder"%>
<%@ page import="org.apache.commons.lang3.StringUtils"%>
<%@ page import="org.apache.shiro.web.util.SavedRequest"%>
<%@ page import="org.apache.shiro.web.util.WebUtils"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <link rel="shortcut icon" href="${ctx}/static/images/favicon.ico">
    <title>iCRM | Log in</title>
    <!-- Tell the browser to be responsive to screen width -->
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
    <!-- Bootstrap 3.3.5 -->
    <link rel="stylesheet" href="${ctx}/static/AdminLTE-2.3.3/bootstrap/css/bootstrap.min.css">
    <!-- Font Awesome -->
    <link rel="stylesheet" href="${ctx}/static/styles//font-awesome.min.css">
    <!-- Ionicons -->
    <link rel="stylesheet" href="${ctx}/static/styles//ionicons.min.css">
    
    <!-- Theme style -->
    <link rel="stylesheet" href="${ctx}/static/AdminLTE-2.3.3/dist/css/AdminLTE.min.css">
    
    <!-- custom style -->
    <link rel="stylesheet" href="${ctx}/static/styles/custom.css">
    
    <!-- Override default font -->
     <style type="text/css">
    body,button, input, select, textarea,h1 ,h2, h3, h4, h5, h6 { font-family: "Helvetica Neue",Helvetica,Arial,sans-serif;}
   
    </style>
    
    <!-- iCheck -->
    <link rel="stylesheet" href="${ctx}/static/AdminLTE-2.3.3/plugins/iCheck/square/blue.css">
    
    <!-- validate -->
    <link href="${ctx}/static/jquery-validation/1.11.1/validate.css" type="text/css" rel="stylesheet" />

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
        <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
    
    <%
	String lang  = LocaleContextHolder.getLocale().getLanguage();
    request.setAttribute("lang", lang);
    %>
    
    <style type="text/css">
    	.loginbg{ background-image: url( ${ctx}/static/images/bg.jpg);background-size: cover;}
    </style>
  </head>
  <body class="hold-transition login-page loginbg" >
  
    <div class="login-box">
      
      <div class="login-box-body" >
        <p style="text-align: center;">
             <a href="javascript:void(0)" class="dropdown-toggle text-right" style="cursor: default;height: 50px;" >
                    <span class="x-lang-ch pull-right <c:if test="${pageContext.response.locale.language=='zh' }">selected</c:if>" onclick="changeLang('zh_CN')" ></span>
                    <span class="x-lang-en pull-right <c:if test="${pageContext.response.locale.language=='en' }">selected</c:if>" onclick="changeLang('en_US')" ></span>
            </a>
            <br/>
            <br/>
            <a href="javascript:void(0);"><img  src="${ctx}/static/images/iclick-logo.png"></a>
            <br/>
           
        </p>
        <p class="login-box-msg">
        	<spring:message code="login.welcome"/><b style="font-size: 16px;"> iCRM</b>
        	
        </p>
        <%
                            	String error = (String) request.getAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);
                            	SavedRequest savedRequest = WebUtils.getSavedRequest(request);
                        		String url = savedRequest!=null?savedRequest.getRequestUrl():"";
                        		if(StringUtils.isNotBlank(url)){
                        			url = url.replaceFirst(request.getServletContext().getContextPath(), "");
                        		}
                        		if(StringUtils.isBlank(error) && StringUtils.isNotBlank(url) && !url.startsWith("/login")&& !url.startsWith("/favicon.ico") && !url.equals("/")){
                        			error = "zh".equalsIgnoreCase(lang)? "请登录后进行操作。":"Please login before performing any operations.";
                        		}
								
								if(error != null){
									if(error.endsWith("UnknownAccountException") || error.endsWith("IncorrectCredentialsException")){
										error = "zh".equalsIgnoreCase(lang)? "邮箱地址或密码错误。":"Email or password error.";
									}
									if(error.endsWith("LockedAccountException")){
										error = "zh".equalsIgnoreCase(lang)? "帐号被锁定，请联系XMO系统管理员。":"Account has locked. Please contact XMO system administrator。 ";
									}
								%>
									<div class="alert alert-danger alert-dismissable">
	                                <button type="button" class="close" data-dismiss="alert" aria-hidden="true">×</button>
	                                <%=error%>
	                            </div>
								<%
								}
								%>
        
        <form id="loginForm" action="${ctx}/login" method="post">
          <div class="form-group has-feedback">
            <input type="text" class="form-control" name="username" placeholder="<spring:message code="login.username" />">
            <span class="glyphicon glyphicon-envelope form-control-feedback"></span>
          </div>
          <div class="form-group has-feedback">
            <input type="password" class="form-control" name="password" placeholder="<spring:message code="login.password" />">
            <span class="glyphicon glyphicon-lock form-control-feedback"></span>
          </div>
          
            <c:if test="${jcaptchaEbabled}">
            <div class="form-group input-group">
              	<label for="jcaptchaCode"><spring:message code="login.verificationcode" />:</label><br/>
			<input type="text" id="jcaptchaCode" name="jcaptchaCode" class="form-control col-sm-6" style="width: 50%">
			<img class="col-sm-6 jcaptcha-btn jcaptcha-img" style="height: 34px" id="jcaptchaCodeImg" src="${pageContext.request.contextPath}/jcaptcha.jpg" title="<spring:message code='login.verificationcode' />">
           </div>
           </c:if>
           
          <div class="row">
            <div class="col-xs-8">
             <div class="checkbox icheck">
                <label>
                  <input  name="rememberMe" type="checkbox" value="true"> <spring:message code="login.rememberme" />
                </label>
       		 </div> 
            </div><!-- /.col -->
            <div class="col-xs-4" style="margin-top: 10px;">
              <button type="submit" class="btn btn-primary btn-block btn-flat"><spring:message code="login.signin" /></button>
            </div><!-- /.col -->
          </div>
        </form>

      <%
		if(lang.equals("zh")){
			%>
			 <a href="https://xmo.optimix.asia/zh-cn/admin/forget_password"><spring:message code="login.forgotpassword"/></a>
			<%
		}else{
		  %>
			 <a href="https://xmo.optimix.asia/en/admin/forget_password"><spring:message code="login.forgotpassword"/></a>
			<%
		}
		%>

       

      </div><!-- /.login-box-body -->
    </div><!-- /.login-box -->

    <!-- jQuery 2.1.4 -->
    <script src="${ctx}/static/AdminLTE-2.3.3/plugins/jQuery/jQuery-2.2.0.min.js"></script>
    <!-- Bootstrap 3.3.5 -->
    <script src="${ctx}/static/AdminLTE-2.3.3/bootstrap/js/bootstrap.min.js"></script>
    <!-- iCheck -->
    <script src="${ctx}/static/AdminLTE-2.3.3/plugins/iCheck/icheck.min.js"></script>
    
    <!-- validate -->
    <script src="${ctx}/static/jquery-validation/1.14.0/dist/jquery.validate.js" type="text/javascript"></script>
	<script src="${ctx}/static/jquery-validation/1.14.0/dist/jquey.validate.override.js" type="text/javascript"></script>
	<%
	if(lang.equals("zh")){
		%>
		<script src="${ctx}/static/jquery-validation/1.14.0/dist/localization/messages_zh.js" type="text/javascript"></script>
		<%
	}
	%>
    <script>
      $(function () {
        $('input').iCheck({
          checkboxClass: 'icheckbox_square-blue',
          radioClass: 'iradio_square-blue',
          increaseArea: '20%' // optional
        });
        
      
        $("#loginForm").validate({
			errorPlacement: function(error, element) {
				if (element.attr("name") == "jcaptchaCode") {
					 error.insertAfter("#jcaptchaCodeImg");
				} 
				else if (element.parent('.input-group').length || element.prop('type') === 'checkbox' || element.prop('type') === 'radio') {
		            error.insertAfter(element.parent());
		        } else {
		            error.insertAfter(element);
		        }
		    },
		    rules: {
		    	username:"required",
		    	password:"required",
		    	jcaptchaCode:"required",
		    }
		   
		});
		
		 $(".jcaptcha-btn").click(function() {
	            $(".jcaptcha-img").attr("src", '${pageContext.request.contextPath}/jcaptcha.jpg?'+new Date().getTime());
	     }); 
      });
      
      function changeLang(lang){
      	url = window.location.href;
      	if(url.indexOf("#")>-1){
      		window.location.href = url.replace("#","");
      	}
      	if(url.indexOf("lang=zh_CN")>-1){
      		window.location.href = url.replace("lang=zh_CN","lang="+lang);
      	}
      	else if(url.indexOf("lang=en_US")>-1){
      		window.location.href = url.replace("lang=en_US","lang="+lang);
      	}else if( url.indexOf("?")==-1){
  			window.location.href = url+"?lang="+lang;
  		}else {
  			window.location.href = url+"&lang="+lang;
  		}
      }
      
     
    </script>
  </body>
</html>



