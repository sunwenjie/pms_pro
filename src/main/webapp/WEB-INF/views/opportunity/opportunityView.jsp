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
        <spring:message code="opportunity.title.view"/>
    </title>


</head>

<body>
	
 <!-- Content Header -->
       <section class="content-header">
          <h1>
            <spring:message code="opportunity.title.view"/>
          </h1>
          <ol class="breadcrumb">
            <li><a href="${ctx}/opportunity"><i class="fa fa-dashboard"></i> <spring:message code="opportunity.home" /></a></li>
            <li class="active">
            	<spring:message code="opportunity.title.view"/>
            </li>
          </ol>
        </section>
        
           <!-- Main content -->
          <section class="content">
         
              <div class="box box-info">
              
                <!-- form start -->
                  <div class="box-body">
	                <form role="form" class="form-horizontal">
	                <input type="hidden" name="id" id="id" value="${opportunity.id}" />
                   
           			<div class="col-md-6">
	                    <div class="form-group">
	                      <label for="task" class="col-sm-3"><spring:message code="opportunity.task.label" /></label>
	                      <label for="task" class="col-sm-9">${opportunity.task}</label>
	                    </div>
	                    
	                    <div class="form-group">
	                      <label for="progress" class="col-sm-3"><spring:message code="opportunity.progress.label" /></label>
	                      <div class="col-sm-9">
				          	<input id="progress" type="text" name="progress" value="${opportunity.progress}">
				          </div>
	                    </div>
                    </div>
                    
	                </form>
                  </div><!-- /.box-body -->

                  <div class="box-footer">
                    <button  class="btn btn-primary btn-sm" onclick="update(${opportunity.id});"><spring:message code="btn.edit"/></button>
                    <button  class="btn btn-primary btn-sm disabled" onclick="list();"><spring:message code="btn.back"/></button>
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
			
			function list(){
				window.location.href="${ctx}/opportunity";
			};
			function update(id){
				window.location.href="${ctx}/opportunity/update/"+id;
			};
		</script>
</body>
</html>
 



