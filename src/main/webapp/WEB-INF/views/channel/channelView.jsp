<%@page import="com.asgab.entity.ProgressBar"%>
<%@page import="org.springframework.web.servlet.support.RequestContextUtils"%>
<%@page import="org.springframework.web.servlet.LocaleResolver"%>
<%@page import="com.asgab.entity.BusinessOpportunity"%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="pms" uri="http://i-click/authTag"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	
	<meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <title><spring:message code="menu.agency"/></title>


</head>

<body>
 <style>
 	.box{}
 	.box-noborder{border-top: 0}
 	.btn-100{width: 100px;line-height: 1.1}
 	.btn-70{width: 70px;line-height: 1.1}
 	.products{padding: 0 15px 0 15px;}
 	.content{padding-bottom: 0;min-height:0}
 	ul li{list-style-type: none;}
 	ul.ul-first{padding-left: 0px;}
 </style>
 <!-- Content Header -->
       <section class="content-header">
          <h1>
       		<spring:message code="opportunity.title.view"/>
          </h1>
          <ol class="breadcrumb">
            <li><a href="${ctx}/businessOpportunity"><i class="fa fa-dashboard"></i> <spring:message code="opportunity.home" /></a></li>
            <li class="active">
            	<spring:message code="menu.agency"/>
            </li>
          </ol>
          
          <c:if test="${orderMessage != null}">
            <c:set var="alertclass" value="alert-success" scope="session"></c:set>
            <c:if test="${success != true}">
            <c:set var="alertclass" value="alert-danger" scope="session"></c:set>
            </c:if>
            <div class="alert ${alertclass} alert-dismissable" style="margin: 10px 0 0 0">
                    <button type="button" class="close" data-dismiss="alert" aria-hidden="true">×</button>
                    <c:if test="${success == true}">
                         <h4><i class="icon fa fa-check"></i> <spring:message code="message.alert.success"/>!</h4>
                    </c:if>
                    <c:if test="${success != true}">
                         <h4><i class="icon fa fa-close"></i> <spring:message code="message.alert.fail"/>!</h4>
                    </c:if>
                    ${orderMessage}
            </div>
        </c:if>
        </section>
          <!-- form start -->
          <form action="" method="post" id="primaryForm" class="form-horizontal">
          <input type="hidden" name="id" id="id" value="${channel.id}" />
           <!-- Main content -->
          <section class="content">
              <div class="box">
                <div class="box-body">
                	<div class="col-md-6">
                	 <dl class="dl-horizontal">
	                    <dt><spring:message code="channel.id" /></dt>
	                    <dd>AG<fmt:formatNumber value="${channel.id}" pattern="000000000"/></dd>
	                 </dl>
	                 
	                 <dl class="dl-horizontal">
	                    <dt><spring:message code="channel.name" /></dt>
	                    <dd>${channel.channel_name}</dd>
	                 </dl>
	                 
	                 <dl class="dl-horizontal">
	                    <dt><spring:message code="channel.qualification" /></dt>
	                    <dd>${channel.qualification_name}</dd>
	                 </dl>
	                 
	                 <dl class="dl-horizontal">
	                    <dt><spring:message code="channel.currency" /></dt>
	                    <dd>${currency}</dd>
	                 </dl>
	                 
	                 <dl class="dl-horizontal">
	                    <dt><spring:message code="channel.contact.name" /></dt>
	                    <dd>${channel.contact_person}</dd>
	                 </dl>
	                 
	                 <dl class="dl-horizontal">
	                    <dt><spring:message code="channel.contact.position" /></dt>
	                    <dd>${channel.position}</dd>
	                 </dl>
	                 
	                 <dl class="dl-horizontal">
                        <dt><spring:message code="channel.contact.number" /></dt>
                        <dd>${channel.phone}</dd>
                     </dl>
	                 
	                 <dl class="dl-horizontal">
                        <dt><spring:message code="channel.email" /></dt>
                        <dd>${channel.email}</dd>
                     </dl>
	                 
	                 <dl class="dl-horizontal">
                        <dt><spring:message code="channel.company" /></dt>
                        <dd>${channel.company_adress}</dd>
                     </dl>
	                 
	                 <dl class="dl-horizontal">
                        <dt><spring:message code="channel.rebate" /></dt>
                        <c:forEach items="${channelRebates}" varStatus="stat" var="rebate">
                        <dd><fmt:formatDate value="${rebate.start_date}" pattern="yyyy-MM-dd"/> - <fmt:formatDate value="${rebate.end_date}" pattern="yyyy-MM-dd"/> ${rebate.rebate}%</dd>
                        </c:forEach>
                     </dl>
                     
                     <dl class="dl-horizontal">
                        <dt><spring:message code="channel.salesperson" /></dt>
                        <c:forEach items="${sale_users}" var="user">
                           <dd>${user.name}(${user.agency })</dd>
                   		</c:forEach>
                     </dl>
	               </div>
                </div><!-- /.box-body -->
              </div>
              <div class="box-footer">
                <!-- 没有到100%才可以编辑 -->
                    <shiro:hasAnyRoles name="superAdmin">
                       <button type="button" class="btn btn-primary btn-sm btn-70" onclick="edit();"><spring:message code="btn.edit"/></button>
                    </shiro:hasAnyRoles>
                    <button type="button" class="btn btn-primary btn-sm disabled btn-sm btn-70" onclick="cancel();"><spring:message code="btn.cancel"/></button>
                </div>
          </section>
          
         </form>
          
		<script type="text/javascript">
          
          	$(function(){
                $("#menu_agency").addClass("active");
          	});
          	
			function cancel(){
				window.location.href='${ctx}/channel';
			};
			
			function edit(){
				window.location.href='${ctx}/channel/update/${channel.id}';
			};
		</script>
</body>
</html>
 



