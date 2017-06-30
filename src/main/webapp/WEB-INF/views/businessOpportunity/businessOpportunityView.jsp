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

    <title>
        <spring:message code="opportunity.title.view"/>
    </title>


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
            	<spring:message code="opportunity.title.view"/>
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
          <input type="hidden" name="id" id="id" value="${opportunity.id}" />
        
        	<%
        		BusinessOpportunity businessOpportunity = (BusinessOpportunity)request.getAttribute("businessOpportunity");
        		LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver (request);
				String lang =localeResolver.resolveLocale(request).getLanguage();
        	%>
        
           <!-- Main content -->
          <section class="content">
              <div class="box">
                <div class="box-body">
                	<div class="col-md-6">
                	 <dl class="dl-horizontal">
	                    <dt><spring:message code="opportunity.id" /></dt>
	                    <dd>${businessOpportunity.number}</dd>
	                 </dl>
	                 
	                 <dl class="dl-horizontal">
	                    <dt><spring:message code="business.opportunity.name" /></dt>
	                    <dd>${businessOpportunity.name}</dd>
	                 </dl>
	                 
	                 <dl class="dl-horizontal">
	                    <dt><spring:message code="business.opportunity.advertiser" /></dt>
	                    <dd>${businessOpportunity.advertiser}</dd>
	                 </dl>
	                 
	                 <dl class="dl-horizontal">
	                    <dt><spring:message code="business.opportunity.deliver.date" /></dt>
	                    <dd>${businessOpportunity.decodeDeliver_date}</dd>
	                 </dl>
	                 
	                 <dl class="dl-horizontal">
	                    <dt><spring:message code="business.opportunity.currency" /></dt>
	                    <dd>${businessOpportunity.decodeCurrency}</dd>
	                 </dl>
	                 
	                 <dl class="dl-horizontal">
	                    <dt><spring:message code="business.opportunity.budget" /></dt>
	                    <dd>
	                    	<fmt:formatNumber value="${businessOpportunity.budget}" pattern="#,###.00" />
	                    </dd>
	                 </dl>
	                 
	                 <dl class="dl-horizontal">
	                    <dt><spring:message code="business.opportunity.progress" /></dt>
	                    <dd>
	                    	<h4 class="control-sidebar-subheading">
                   			<%=businessOpportunity.getDecodeStatus(lang) %>
                    		<span class="label ${businessOpportunity.progressBar.labelClass} pull-right">${businessOpportunity.progress}%</span>
                  			</h4>
                  			<div class="progress progress-xxs">
                    			<div class="progress-bar ${businessOpportunity.progressBar.barClass}" style="width: ${businessOpportunity.progress}%"></div>
                  			</div>
	                    </dd>
	                 </dl>
	                 
	                 <dl class="dl-horizontal">
                        <dt><spring:message code="business.opportunity.progress.remark" /></dt>
                        <dd>
                            ${businessOpportunity.progressRemarkList}
                        </dd>
                     </dl>
	               </div> 
                </div><!-- /.box-body -->
              </div>
          </section>
          
          <!-- part2 -->
          <section class="content">
              <div class="box box-noborder">
                <div class="box-body">
           			<div class="col-md-6">
           				<dl class="dl-horizontal">
		                    <dt><spring:message code="business.opportunity.msa" /></dt>
		                    <dd>
		                    	<c:choose>
		                    		<c:when test="${businessOpportunity.exist_msa == 1}"><spring:message code="business.opportunity.yes" /></c:when>
		                    		<c:otherwise><spring:message code="business.opportunity.no" /></c:otherwise>
		                    	</c:choose>
		                    </dd>
		                </dl>
		                
		                <dl class="dl-horizontal">
		                    <dt><spring:message code="business.opportunity.opportunity.type" /></dt>
		                    <dd>
		                    	<c:choose>
		                    		<c:when test="${businessOpportunity.exist_service == 1}"><spring:message code="business.opportunity.service" /></c:when>
		                    		<c:otherwise><spring:message code="business.opportunity.exec" /></c:otherwise>
		                    	</c:choose>
		                    </dd>
		                </dl>
		                
		                <dl class="dl-horizontal">
		                    <dt><spring:message code="business.opportunity.sale" /></dt>
		                    <dd>${businessOpportunity.owner_sale_name}</dd>
		                </dl>
		                
		                <dl class="dl-horizontal">
		                    <dt><spring:message code="business.opportunity.cooperate" /></dt>
		                    <dd>${businessOpportunity.cooperate_sale_names}</dd>
		                </dl>
                    </div>
                </div><!-- /.box-body -->
              </div>
          </section>
          
          <!-- part3 -->
          <section class="content">
              <div class="box box-solid">
              	<div class="box-header with-border">
					<h3 class="box-title"><spring:message code="business.opportunity.setting" /></h3>
					<i class="fa fa-w fa-caret-down"></i>
				</div>
                <div class="box-body">
                    <div class="col-md-6">
	                    <jsp:include page="productView.jsp" flush="true"></jsp:include>
	                    <dl class="dl-horizontal">
		                    <dt><spring:message code="business.opportunity.remark" /></dt>
		                    <dd>${businessOpportunity.remark}</dd>
		                </dl>
		                
		                <c:if test="${not empty orderCode}">
	                     <dl class="dl-horizontal">
	                        <dt><spring:message code="business.opportunity.order.code" /></dt>
	                        <dd>
	                            ${orderCode}
	                        </dd>
	                     </dl>
	                     </c:if>
	                </div>	
                </div><!-- /.box-body -->
                <div class="box-footer">
                <!-- 没有到100%才可以编辑 -->
                    <c:if test="${ businessOpportunity.progress ne 100}">
                    <shiro:hasAnyRoles name="superAdmin">
                            <button type="button" class="btn btn-primary btn-sm btn-70" onclick="edit();"><spring:message code="btn.edit"/></button>
                    </shiro:hasAnyRoles>
                    
                    <shiro:lacksRole name="superAdmin">
	                	<pms:auth equal1="${businessOpportunity.created_by}" equal2="${businessOpportunity.owner_sale}" array="${businessOpportunity.cooperate_sales}">
	                    	<button type="button" class="btn btn-primary btn-sm btn-70" onclick="edit();"><spring:message code="btn.edit"/></button> 
	                    </pms:auth>
                    </shiro:lacksRole>
                    </c:if>
                    <button type="button" class="btn btn-primary btn-sm disabled btn-sm btn-70" onclick="cancel();"><spring:message code="btn.cancel"/></button>
                    <!-- 已生成订单/或者0不能生成 -->
                    <c:if test="${empty orderCode && 0 ne businessOpportunity.progress}">
                    <button type="button" class="btn btn-warning btn-sm btn-sm" style="line-height: 1.1" onclick="genOrder();"><spring:message code="btn.generate.order"/></button>
                    </c:if>
                </div>
              </div>
          </section>
          
          
         </form>
          
		<script>    
			$(document).ready(function() {
				
				$("#menu_business_opportunity").addClass("active");

				$("#progress").ionRangeSlider({
					min:0,
					max:100,
					keyboard:true,
					values:[0,10,30,50,70,90,100],
					disable: true
				});
			});
			
			function cancel(){
				window.location.href='${ctx}/businessOpportunity';
			};
			
			function edit(){
				window.location.href='${ctx}/businessOpportunity/update/${businessOpportunity.id}';
			};
			
			function genOrder(){
				if(confirm('<spring:message code="btn.confirm.generate.order" />')){
				    window.location.href='${ctx}/businessOpportunity/gen/${businessOpportunity.id}';
				}
			}
		</script>
</body>
</html>
 



