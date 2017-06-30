<%@page import="org.apache.shiro.SecurityUtils"%>
<%@page import="com.asgab.service.account.ShiroDbRealm.ShiroUser"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<%
	ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
%>

<html>
<head>
<title><spring:message code="menu.business.opportunity"/></title>
</head>
<body>
	<style type="text/css">
		.btn-sm{line-height: 1.1}
	</style>
	 <!-- Content Header -->
       <section class="content-header">
          <h1>
            <spring:message code="menu.business.opportunity"/>
          </h1>
          
          <ol class="breadcrumb">
            <li><a href="${ctx}/opportunity"><i class="fa fa-dashboard"></i> <spring:message code="opportunity.home" /></a></li>
            <li class="active"><spring:message code="opportunity.header"/></li>
          </ol>
		
		<c:if test="${message != null}">
			<div class="alert alert-success alert-dismissable" style="margin: 10px 0 0 0">
                    <button type="button" class="close" data-dismiss="alert" aria-hidden="true">×</button>
                    <h4>	<i class="icon fa fa-check"></i> <spring:message code="message.alert.success"/>!</h4>
                    ${message}
            </div>
        </c:if>
		          
        </section>

          <!-- Main content -->
          <section class="content">
         
                  <div class="box box-info ">
		            <div class="box-header with-border">
		              <h3 class="box-title">
		              <!-- 没有title -->
		             	<button class="btn btn-primary btn-sm" onclick="window.location.href='${ctx}/opportunity/create';"><i class="fa fa-w fa-pencil-square-o"></i>&nbsp;<spring:message code="btn.create"/></button>
                		<button class="btn btn-sm" onclick="$('#searchForm').submit();"><i class="fa fa-w fa-search"></i>&nbsp;<spring:message code="btn.search"/></button>
		              	<button class="btn btn-warning btn-sm" onclick="resetForm();"><i class="fa fa-w fa-undo"></i>&nbsp;<spring:message code="btn.reset"/></button>
		              </h3>
		              <div class="box-tools pull-right">
		                <button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>
		              </div>
		            </div><!-- /.box-header -->
		            <div class="box-body" style="display: block;">
		            	<form action="${ctx}/opportunity" method="get" id="searchForm">
						<div class="row">
							<div class="col-md-4">
								<div class="form-group">
									<label><spring:message code="opportunity.task"/></label>
									<input type="text" class="form-control" name="task" id="task" value="<c:out value="${pages.searchMap['task']}"/>" placeholder="<spring:message code='opportunity.task.placeholder'/>">
								</div>
							</div>
							<div class="col-md-4">
								<div class="form-group">
									<label><spring:message code="opportunity.adv.name"/></label>
									<input type="text" class="form-control" name="advName" id="advName" value="<c:out value="${pages.searchMap['advName']}"/>" placeholder="<spring:message code='opportunity.adv.name.placeholder'/>">
								</div>
							</div>
							<div class="col-md-4">
								<div class="form-group">
									<label><spring:message code="opportunity.brand.name"/></label>
									<input type="text" class="form-control" name="brandName" id="brandName" value="<c:out value="${pages.searchMap['brandName']}"/>" placeholder="<spring:message code='opportunity.brand.name.placeholder'/>">
								</div>
							</div>
						</div><!-- /.row -->
						</form>
					</div><!-- /.box-body -->
					</div>
                	
                	<div class="box-body  table-responsive no-padding">
          
	                  <table class="table table-striped table-condensed table-hover">
	                    <tbody><tr>
	                      <th style="width: 100px"><spring:message code="opportunity.id" /></th>
	                      <th <tags:sort column="task" page="${pages}"/>><spring:message code="opportunity.task"/><i class="fa fa-w fa-sort"></i></th>
	                      <th><spring:message code="opportunity.progress"/></th>
	                      <th style="width: 40px"><spring:message code="opportunity.label"/></th>
	                      <th style="width: 152px"><spring:message code="opportunity.operate"/></th>
	                    </tr>
                    
	                    <c:forEach items="${pages.content}" var="opportunity" varStatus="status">
	                    	<tr>
		                      <td>${opportunity.id}</td>
		                      <td>${opportunity.task}</td>
		                      <td>
		                        <div class="progress progress-xs progress-striped active">
		                          <div class="progress-bar ${opportunity.progressBar.barClass}" style="width: ${opportunity.progressBar.value}%"></div>
		                        </div>
		                      </td>
		                      <td><span class="badge ${opportunity.progressBar.bgClass}">${opportunity.progressBar.value}%</span></td>
		                      <td>
		                      		<a href="#" onclick="view(${opportunity.id});" class="btn btn-sm btn-warning"><i class="fa fa-w fa-file-text-o"></i>&nbsp;<spring:message code="btn.view"/></a>
		                      		<a href="#" onclick="del(${opportunity.id});" class="btn btn-sm btn-danger"><i class="fa fa-w fa-trash-o"></i>&nbsp;<spring:message code="btn.delete"/></a>
		                      </td>
		                    </tr>
	                    </c:forEach>
                    
                  </tbody></table>
                </div><!-- /.box-body -->
                <div class="box-footer clearfix">
                  <tags:pagination page="${pages}" paginationSize="3" />
                </div>

              
          </section>
          
          
          <script type="text/javascript">
          	$(document).ready(function() {
        		$("#menu_business_opportunity").addClass("active");  
        	});
          
          	function view(id){
          		window.location.href="${ctx}/opportunity/view/"+id;
          	};
          	
          	function del(id){
          		bootbox.dialog({
          			message: "<spring:message code="message.confirm.del"/>?",
          		  	title: "",
          		  	buttons: {
          		  		cancel: {
	          		      label: "<spring:message code='btn.cancel' />",
	          		      className: "",
	          		      callback: function() {
	          		    	
	          		      }
	          		    },
	          			success: {
	          		      label: "<spring:message code='btn.delete' />",
	          		      className: "btn-danger",
	          		      callback: function() {
	          		    	window.location.href="${ctx}/opportunity/delete/"+id;
	          		      }
	          		    }
          		  	}
          		});
          	};
          	
          	function resetForm(){
          		$("#task").val('');
          	};
          </script>
</body>
</html>
