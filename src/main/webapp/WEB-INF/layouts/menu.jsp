<%@page import="org.apache.shiro.SecurityUtils"%>
<%@page import="com.asgab.service.account.ShiroDbRealm.ShiroUser"%>
<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<%
   ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
%>
  <header class="main-header">
  
        <nav class="navbar navbar-static-top ">
          <div class="container">
            <div class="navbar-header">
              <a href="${ctx}/businessOpportunity" class="navbar-brand"><img style="width: 110px;height: 40px;margin-top: -6px;" src="${ctx}/static/images/iclick-logo.png"></a>
              <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar-collapse">
                <i class="fa fa-bars"></i>
              </button>
            </div>
            <!-- Collect the nav links, forms, and other content for toggling -->
            <div class="collapse navbar-collapse pull-left" id="navbar-collapse">
              <ul class="nav navbar-nav">
                <li id="menu_business_opportunity"><a href="${ctx}/businessOpportunity"><spring:message code="menu.business.opportunity"/></a></li>
             
	            <li id="menu_client"><a href="${ctx}/client"><spring:message code="menu.advertiser"/></a></li>
                <shiro:hasPermission name="agency">
                    <li id="menu_agency"><a href="${ctx}/channel"><spring:message code="menu.agency"/></a></li>
                </shiro:hasPermission>
                  <shiro:hasAnyRoles name="admin,superAdmin">
                    <li id="menu_advertiser_review"><a href="${ctx}/advertiser"><spring:message code="menu.advertiser.review"/></a></li>
                </shiro:hasAnyRoles>
                
                
                <li id="menu_report">
                  <a href="${ctx}/report" ><spring:message code="menu.report"/></a>
                </li>
                <shiro:user>
	                <shiro:hasAnyRoles name="admin,superAdmin">
		                <li id="menu_log" class="hidden-sm hidden-xs">
		                	<a href="${ctx}/log"><spring:message code="menu.log.manage"/></a>
		                </li>
                	</shiro:hasAnyRoles>
                </shiro:user>
             
              </ul>
            </div><!-- /.navbar-collapse -->
            <!-- Navbar Right Menu -->
              <div class="navbar-custom-menu">
                <ul class="nav navbar-nav navbar-right">
                  <li class="dropdown" >
                    <a href="javascript:void(0)" class="dropdown-toggle" style="cursor: default;height: 50px;" >
                    	<span class="x-lang-ch <c:if test="${pageContext.response.locale.language=='zh' }">selected</c:if>" onclick="changeLang('zh_CN')" ></span>
                    	<span class="x-lang-en <c:if test="${pageContext.response.locale.language=='en' }">selected</c:if>" onclick="changeLang('en_US')" ></span>
                    </a>
                  </li>
               
	               <li class="dropdown">
	                  <a href="#" class="dropdown-toggle" data-toggle="dropdown" aria-expanded="false">
	                  	<span class="glyphicon glyphicon-user"></span>
	                  	<span class="hidden-sm hidden-xs"><shiro:principal/></span>
	                  	<span class="caret"></span>
	                  </a>
	                  <ul class="dropdown-menu" role="menu">
	                    <li><a href="${ctx}/setting"><span class="glyphicon glyphicon-cog"></span><spring:message code="setting.personal"/></a></li>
                            <shiro:hasAnyRoles name="superAdmin">
                            <li><a href="${ctx}/setting/global"><span class="glyphicon glyphicon-certificate"></span><spring:message code='setting.global' /></a></li>
	                       </shiro:hasAnyRoles>
	                    <shiro:user>
	                    	<shiro:hasAnyRoles name="admin,superAdmin">
	                    	<li><a href="${ctx}/setting/tree"><span class="glyphicon glyphicon-search"></span><spring:message code='setting.data.sharing.tree' /></a></li>
	                    	</shiro:hasAnyRoles>
	                    </shiro:user>
	                    
	                    <li><a href="${ctx}/logout"><span class="glyphicon glyphicon-log-out"></span><spring:message code="menu.logout"/></a></li>
	                  </ul>
	                </li>
               
                </ul>
              </div><!-- /.navbar-custom-menu -->
          </div><!-- /.container-fluid -->
        </nav>
      </header>