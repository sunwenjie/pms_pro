<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	
	<meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>
        <spring:message code="client.title.view" />
    </title>


</head>

<body>

<style>
	.box{}
	.box-noborder{border-top: 0}
	.btn-100{width: 100px;line-height: 1.1}
	.products{padding: 0 15px 0 15px;}
	.content{padding-bottom: 0;min-height:0}
</style>

	<!-- Content Header -->
      	 <section class="content-header">
          <h1>
            <spring:message code="client.title.view" />
          </h1>
          <ol class="breadcrumb">
            <li><a href="${ctx}/businessOpportunity"><i class="fa fa-dashboard"></i> <spring:message code="opportunity.home" /></a></li>
            <li><a href="${ctx}/client"><spring:message code="menu.advertiser" /></a></li>
            <li class="active">
            	<spring:message code="navigat.view" />
            </li>
          </ol>
        </section>

<div class="form-horizontal">
	
	<!-- Main content -->
		<section class="content">
	     		 <div class="box box-solid">
	      			 <div class="box-body">
	      			 	
		      			 <dl class="dl-horizontal">
		                    <dt><spring:message code="client.number" /></dt>
		                    <dd>${client.number}</dd>
		                 </dl>
		                
		                 <dl class="dl-horizontal">
		                    <dt><spring:message code="client.name" /></dt>
		                    <dd>${client.clientname}</dd>
		                 </dl>
		                 
		                 <dl class="dl-horizontal">
		                    <dt><spring:message code="client.brand.name" /></dt>
		                    <dd>${client.brand}</dd>
		                 </dl>
		                 
		                 <dl class="dl-horizontal">
		                    <dt><spring:message code="client.channel" /></dt>
		                    <dd>${client.channel_name}</dd>
		                 </dl>
		                 
		                 <dl class="dl-horizontal">
		                    <dt><spring:message code="client.industry" /></dt>
		                    <dd><tags:decodeList list="${industryTypes}" value="${client.industry_id}"></tags:decodeList></dd>
		                 </dl>
		                 
		                 <dl class="dl-horizontal">
		                    <dt><spring:message code="client.currency" /></dt>
		                    <dd><tags:decodeList list="${currencyTypes}" value="${client.currency_id}"></tags:decodeList></dd>
		                 </dl>
	      			 
	      			</div><!-- /.box-body -->
	     		 </div><!-- /.box-info -->
		 </section><!--  /Main content -->  
		 
		 
		  <!-- part2 -->
		 <section class="content">
			 		<div class="box box-solid">
		 				<div class="box-header with-border">
							<h3 class="box-title"><spring:message code="client.contact.person" /></h3>
							<i class="fa fa-w fa-caret-down"></i>
						</div>
			 			<div class="box-body">
							<c:forEach var="contact" items="${client.contacts}" varStatus="status">
							
								<dl class="dl-horizontal">
				                    <dt><spring:message code="client.contact.person" /> ${status.index+1}</dt>
				                    <dd>${contact.contact_person}</dd>
				                </dl>
				                
				                <dl class="dl-horizontal">
				                    <dt><spring:message code="client.contact.position" /> ${status.index+1}</dt>
				                    <dd>${contact.position}</dd>
				                </dl>
				                
				                <dl class="dl-horizontal">
				                    <dt><spring:message code="client.contact.phone" /> ${status.index+1}</dt>
				                    <dd>${contact.phone}</dd>
				                </dl>
				                
				                <dl class="dl-horizontal">
				                    <dt><spring:message code="client.contact.email" /> ${status.index+1}</dt>
				                    <dd>${contact.email}</dd>
				                </dl>
				                
							</c:forEach>
						</div><!-- /.box-body -->
			 		</div><!-- /.box-noborder -->
		 </section><!-- /part2 -->
		 
		 
		 <!-- part3 -->
		 <section class="content">
			 		<div class="box box-noborder">
		 				<div class="box-body">
		 				
	 						<dl class="dl-horizontal">
			                    <dt><spring:message code="client.contact.address" /></dt>
			                    <dd>${client.address}</dd>
				            </dl>
				            
				            <dl class="dl-horizontal">
			                    <dt><spring:message code="client.contact.salesperson" /></dt>
			                    <dd>
			                    	<c:if test="${client.saleNames != null}">
		                      				<c:forEach var="saleName" items="${client.saleNames}" varStatus="status">
		                      					${saleName}<c:if test="${status.last!= true}">,</c:if>
		                      				</c:forEach>
		                      		</c:if>
			                    </dd>
				            </dl>
				            
				            <dl class="dl-horizontal">
			                    <dt><spring:message code="client.cross.regional" /></dt>
			                    <dd>
			                    	<c:if test="${client.whether_cross_district == 1}" >
			                    		<spring:message code="client.channel.yes" /><spring:message code="client.cross.regional.remark" />
			                    	</c:if>
			                    	<c:if test="${client.whether_cross_district != 1}" >
			                    		<spring:message code="client.channel.no" />
			                    	</c:if>	
			                    </dd>
				            </dl>
					            
		 				</div><!-- /.box-body -->
			 				
		      		 	<div class="box-footer">
		                    <button type="button" class="btn btn-primary btn-sm btn-70" onclick="window.location.href='${ctx}/client/update/${client.id}'"><spring:message code="btn.edit"/></button>
		                    <button class="btn btn-primary btn-sm disabled btn-70" onclick="window.location.href='${ctx}/client'"><spring:message code="btn.back"/></button>
		                </div>
			 		</div><!-- /.box -->
		 </section><!-- /part3 -->
	</div>

	<script type="text/javascript">
		$(function() {
			$("#menu_client").addClass("active");
		});
	</script>
</body>
</html>



