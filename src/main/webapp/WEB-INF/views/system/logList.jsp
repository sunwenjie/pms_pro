<%@page import="org.apache.shiro.SecurityUtils"%>
<%@page import="com.asgab.service.account.ShiroDbRealm.ShiroUser"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<html>
<head>
<title><spring:message code="menu.log.manage" /></title>
</head>

<body>
	
	<section class="content-header">
		<h1><spring:message code="menu.log.manage" /></h1>
		<ol class="breadcrumb">
          <li><a href="${ctx}/businessOpportunity"><i class="fa fa-dashboard"></i> <spring:message code="opportunity.home" /></a></li>
          <li class="active"><spring:message code="menu.log.manage" /></li>
        </ol>
	</section>
	
	<section class="content">
	<div class="nav-tabs-custom">
	<div class="tab-content">
	<div class="tab-pane active" id="activity">
		<div class="box box-primary">
		
				<div class="box-header with-border">
				
					<h3 class="box-title">
                		<div class="btn-group">
                		<button class="btn btn-primary btn-sm btn-80" onclick="$('#searchForm').submit();"><i class="fa fa-w fa-search"></i>&nbsp;<spring:message code="btn.search"/></button>
		              	<button class="btn btn-warning btn-sm btn-80" onclick="resetForm();"><i class="fa fa-w fa-undo"></i>&nbsp;<spring:message code="btn.reset"/></button>
		              	</div>
		              </h3>
		              <div class="box-tools pull-right">
		                <button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>
		              </div>
				
				</div>
				
				<div class="box-body" style="display: block;">
					<form action="${ctx}/log" method="get" id="searchForm">
					<div class="row">
						<div class="col-lg-6">
							<div class="form-group">
								<label for="pKey"><spring:message code="sys.log.pkey" /></label> <input type="text"
									class="form-control" id="pKey" name="pKey"
									value="<c:out value="${pages1.searchMap['pKey']}"/>"
									placeholder="<spring:message code="sys.log.pkey" />">
							</div>
						</div>
					
						<div class="col-md-6">
							<div class="form-group">
								<label><spring:message code="sys.log.module" /></label>
								<select name="module" class="form-control module" id="module" onchange="$('#searchForm').submit();">
									<option value="menu.business.opportunity" <c:if test="${pages1.searchMap['module'] eq 'menu.business.opportunity'}">selected</c:if>><spring:message code="menu.business.opportunity" /></option>
									<option value="menu.advertiser" <c:if test="${pages1.searchMap['module'] eq 'menu.advertiser'}">selected</c:if>><spring:message code="menu.advertiser" /></option>
									<option value="menu.advertiser.review" <c:if test="${pages1.searchMap['module'] eq 'menu.advertiser.review'}">selected</c:if>><spring:message code="menu.advertiser.review" /></option>
								</select>
							</div>
						</div>
					</div>
					</form>			
				</div>
		</div>
		
			
				<div class="box-body  <!-- table-responsive --> no-padding">
					<table class="table table-striped table-bordered table-hover">
						<thead>
							<%-- <th><spring:message code="sys.log.module" /></th> --%>
							<th><spring:message code="sys.log.pkey" /></th>
							<th><spring:message code="sys.log.operate.by" /></th>
							<th><spring:message code="sys.log.operate.time" /></th>
							<th><spring:message code="sys.log.operate.type" /></th>
							<th><spring:message code="sys.log.content" /></th>
						</thead>
						
						<tbody>
							<c:forEach items="${pages1.content}" var="log" varStatus="status">
								<tr>	
									<%-- <td><spring:message code="${log.module}" /> </td> --%>
									<td>${log.pKey}</td>
									<td>${log.operateBy}</td>
									<td><fmt:formatDate value="${log.operateTime}" pattern="yyyy/MM/dd HH:mm"/></td>
									<td>
										<c:if test="${pageContext.response.locale.language=='zh' }">
											<tags:decode items="${operateTypes_zh}" value="${log.operateType}" />
										</c:if>
										<c:if test="${pageContext.response.locale.language=='en' }">
											<tags:decode items="${operateTypes_en}" value="${log.operateType}" />
										</c:if>
									</td>
									<td>
										<c:choose>
											<c:when test="${log.operateType == 3}">
												${log.remark}
											</c:when>
											<c:otherwise>
												<c:if test="${pageContext.response.locale.language=='zh' }">
													${log.remark1}
												</c:if>
												<c:if test="${pageContext.response.locale.language=='en' }">
													${log.remark2}
												</c:if>
											</c:otherwise>
										</c:choose>
									</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
				
				<div class="box-footer clearfix">
			          <tags:pagination page="${pages1}" paginationSize="5" />
			    </div>
			

		
	</div>
	</div>
	</div>
	</section>
	
<script type="text/javascript">

		$(document).ready(function() {
			$("#menu_log").addClass("active");  
		});
		
		function resetForm(){
      		$("#searchForm input").val('');
      		$("#searchForm select").val('menu.business.opportunity').trigger("change");
      		$('#searchForm').submit();
      	};

</script>	

</body>
</html>