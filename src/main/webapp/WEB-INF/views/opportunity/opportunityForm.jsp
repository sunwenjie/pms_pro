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
    	<c:if test="${action eq 'create' }"><spring:message code="opportunity.title.create"/></c:if>
        <c:if test="${action eq 'update' }"><spring:message code="opportunity.title.update"/></c:if>
    </title>


</head>

<body>
	
 <!-- Content Header -->
       <section class="content-header">
          <h1>
           	<c:if test="${action eq 'create' }"><spring:message code="opportunity.title.create"/></c:if>
       		<c:if test="${action eq 'update' }"><spring:message code="opportunity.title.update"/></c:if>
          </h1>
          <ol class="breadcrumb">
            <li><a href="${ctx}/opportunity"><i class="fa fa-dashboard"></i> <spring:message code="opportunity.home" /></a></li>
            <li class="active">
            	<c:if test="${action eq 'create' }"><spring:message code="opportunity.title.create"/></c:if>
            	<c:if test="${action eq 'update' }"><spring:message code="opportunity.title.update"/></c:if>
            </li>
          </ol>
        </section>
        
           <!-- Main content -->
          <section class="content">
         
              <div class="box box-info">
              
                <div class="box-body">
                <!-- form start -->
                <form role="form" action="${ctx}/opportunity/${action}" method="post" id="primaryForm" class="form-horizontal">
                <input type="hidden" name="id" id="id" value="${opportunity.id}" />
                   
           			<div class="col-md-6">
	                    <div class="form-group">
	                      <label for="task" class="col-md-3"><spring:message code="opportunity.task.label" /></label>
	                      <div class="col-md-9">
	                      	<textarea rows="3" class="form-control"  id="task" name="task" placeholder="<spring:message code='opportunity.task.placeholder'/>">${opportunity.task}</textarea>
	                      </div>
	                    </div>
	                    
	                    <div class="form-group">
	                      <label for="progress" class="col-md-3"><spring:message code="opportunity.progress.label" /></label>
	                      <div class="col-md-9">
				          	<input id="progress" class="form-control" type="text" name="progress" value="${opportunity.progress}">
				          </div>
	                    </div>
                    </div>
                  
                </form>
                </div><!-- /.box-body -->
                
                <div class="box-footer">
                    <button type="submit" class="btn btn-primary btn-sm" onclick="$('#primaryForm').submit();"><spring:message code="btn.save"/></button>
                    <button  class="btn btn-primary btn-sm disabled" onclick="cancel();"><spring:message code="btn.cancel"/></button>
                </div>
                
              </div>
           
          </section>
          
		<script>    
			$(document).ready(function() {
				$("#menu_business_opportunity").addClass("active");
				
				$("#primaryForm").validate({
					rules:{
						task:"required"
					}
				});

				$("#progress").ionRangeSlider({
					min:0,
					max:100,
					keyboard:true,
					keyboard_step:10
				});
			});
			
			function cancel(){
				window.location.href='${ctx}/opportunity';
			};
		</script>
</body>
</html>
 



