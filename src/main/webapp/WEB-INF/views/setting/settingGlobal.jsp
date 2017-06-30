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
	    <title><spring:message code="setting.global" /></title>
	</head>
	
	<body>
		
		<section class="content-header">
			<h1><spring:message code="setting.global" /></h1>
			<ol class="breadcrumb">
				<li><a href="${ctx}/businessOpportunity"><i class="fa fa-dashboard"></i> <spring:message code="opportunity.home" /></a></li>
				<li class="active"><spring:message code="setting.global" /></li>
			</ol>
			
			<c:if test="${message != null}">
				<div class="alert alert-success alert-dismissable" style="margin: 10px 0 0 0">
	                    <button type="button" class="close" data-dismiss="alert" aria-hidden="true">×</button>
	                    <h4>	<i class="icon fa fa-check"></i> <spring:message code="message.alert.success"/>!</h4>
	                    ${message}
	            </div>
      		</c:if>
		</section>


		<section class="content">
		
			<div class="row">
	            <div class="col-md-6">
	              <div class="box box-solid">
	                <div class="box-header with-border">
	                  <!-- <i class="fa fa-text-width"></i> -->
	                  <h3 class="box-title"><spring:message code="setting.global.title"/></h3>
	                </div><!-- /.box-header -->
	                <!-- form -->
					<form role="form" action="${ctx}/setting/save" method="post" id="primaryForm" class="form-horizontal">
	                <div class="box-body">
	                  	<div class="form-group">
							 <label for="parent_id" class="col-md-3 control-label"><spring:message code="setting.data.sharing.search.user.info"/></label>
		                     <div class="col-md-9">
		                    	<select class="form-control select2" name="user_id" id="user_id">
		                    	         <option value="0"></option>
		                    	         <c:forEach var="user" items="${users}">
		                    	             <option value="${user.id}">${user.name}(${user.email})</option>
		                    	         </c:forEach>
		                    	</select>
		                    </div>
						</div>
						
						<div class="form-group">
                             <label for="team_name" class="col-md-3 control-label"><spring:message code="setting.global.shareto"/></label>
                             <div class="col-md-9">
                                <select class="form-control select2" name="parent_id" id="parent_id">
                                        <option value="0"></option>
                                         <c:forEach var="user" items="${users}">
                                             <option value="${user.id}">${user.name}(${user.email})</option>
                                         </c:forEach>
                                </select>
                             </div>
                        </div>
						<div class="form-group">
							 <label for="team_name" class="col-md-3 control-label"><spring:message code="setting.global.teamname"/></label>
		                     <div class="col-md-9">
		                     	<input name="team_name" class="form-control team_name"  id="team_name" value="${dataSharing.team_name}" />
		                     </div>
						</div>
						<div class="form-group">
                             <label for="assistant_id" class="col-md-3 control-label"><spring:message code="setting.assistant"/></label>
                             <div class="col-md-9">
                                <select class="form-control select2" name="assistant_id" id="assistant_id">
                                        <option value="0"></option>
                                         <c:forEach var="user" items="${users}">
                                             <option value="${user.id}">${user.name}(${user.email})</option>
                                         </c:forEach>
                                </select>
                             </div>
                        </div>
						
	                </div><!-- /.box-body -->
	                </form><!-- /.form -->
	                <div class="box-footer">
			          	 <button type="submit" class="btn btn-primary btn-sm" onclick="$('#primaryForm').submit();"><spring:message code="btn.save"/></button>
					</div>
	              </div><!-- /.box -->
	            </div><!-- ./col -->
	            
	           
	          </div><!-- /.row -->
          <!-- END TYPOGRAPHY -->
		</section>
</form><!-- /form -->	

	<script type="text/javascript"> 
	
		$(document).ready(function() {
			var userSelect2 =    $("#user_id").select2({
                placeholder: "<spring:message code='public.please.select' />",
                allowClear: true,
                language: lang_select2()
            });
			
			 var parentSelect2 =    $("#parent_id").select2({
	                placeholder: "<spring:message code='public.please.select' />",
	                allowClear: true,
	                language: lang_select2()
	            });
			 
			 var assistantSelect2 =    $("#assistant_id").select2({
                 placeholder: "<spring:message code='public.please.select' />",
                 allowClear: true,
                 language: lang_select2()
             });
			
			userSelect2.on("change", function (e) {
				var userID = $(this).val();
				$.ajax({
	                  url: "${ctx}/setting/getDatashare/"+userID,
	                  dataType:"json",
	                  method: "GET"
	                }).done(function ( data ) {
	                  parentSelect2.val(data.parent_id).trigger("change");
	                  $("#team_name").val(data.team_name);
	                  assistantSelect2.val(data.assistant_id).trigger("change");
	               });
			});
			
			
			
		});
		
	</script>
	
	</body>

</html>