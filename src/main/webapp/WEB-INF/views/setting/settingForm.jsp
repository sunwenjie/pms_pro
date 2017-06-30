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
	    <title><spring:message code="setting.personal" /></title>
	</head>
	
	<body>
		
		<section class="content-header">
			<h1><spring:message code="setting.personal" /></h1>
			<ol class="breadcrumb">
				<li><a href="${ctx}/businessOpportunity"><i class="fa fa-dashboard"></i> <spring:message code="opportunity.home" /></a></li>
				<li class="active"><spring:message code="setting.personal" /></li>
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
	                  <h3 class="box-title"><spring:message code="setting.data.sharing.title" /></h3>
	                </div><!-- /.box-header -->
	                <!-- form -->
					<form role="form" action="${ctx}/setting/${action}" method="post" id="primaryForm" class="form-horizontal">
					<input type="hidden" name="id" id="id" value="${dataSharing.id}" />	
	                <div class="box-body">
	                  	<div class="form-group">
							 <label for="parent_id" class="col-md-3 control-label"><spring:message code="setting.data.sharing" /><!-- <em> *</em> --></label>
		                     <div class="col-md-9">
		                    	<select class="form-control select2 parent_id" name="parent_id" id="parent_id">
		                    		<c:if test="${action eq 'update'}">
		                    			<option value></option>
		                    			<c:if test="${parentUser != null && dataSharing.parent_id eq parentUser.id }">
		                    				<option value="${parentUser.id}" selected="selected">${parentUser.name}(${parentUser.email})</option>
		                    			</c:if>
		                    		</c:if>
		                    	</select>
		                    </div>
						</div>
						
						<div class="form-group">
							 <label for="team_name" class="col-md-3 control-label"><spring:message code="setting.team.name" /></label>
		                     <div class="col-md-9">
		                     	<input name="team_name" class="form-control team_name"  id="team_name" value="${dataSharing.team_name}" />
		                     </div>
						</div>
						<div class="form-group">
							 <label for="team_name" class="col-md-3 control-label"></label>
		                     <div class="col-md-9">
		                     	<p class="text-yellow"><i class="fa fa-fw fa-warning"></i><spring:message code="setting.team.name.warning" /></p>
		                     </div>
						</div>
						<div class="form-group">
                             <label for="assistant_id" class="col-md-3 control-label"><spring:message code="setting.assistant" /></label>
                             <div class="col-md-9">
                                <select class="form-control select2 assistant_id" name="assistant_id" id="assistant_id">
                                    <c:if test="${action eq 'update'}">
                                        <option value></option>
                                        <c:if test="${assistantUser != null }">
                                            <option value="${assistantUser.id}" selected="selected">${assistantUser.name}(${assistantUser.email})</option>
                                        </c:if>
                                    </c:if>
                                </select>
                             </div>
                        </div>
                        
                        <div class="form-group">
                             <label for="currency_id" class="col-md-3 control-label"><spring:message code="business.opportunity.currency" /></label>
                             <div class="col-md-9">
                                <tags:selectbox name="currency_id" style="select2" list="${currencys}" addNull="true" value="${dataSharing.currency_id}"></tags:selectbox>
                             </div>
                        </div>
	                </div><!-- /.box-body -->
	                </form><!-- /.form -->
	                <div class="box-footer">
			          	 <button type="submit" class="btn btn-primary btn-sm" onclick="$('#primaryForm').submit();"><spring:message code="btn.save"/></button>
					</div>
	              </div><!-- /.box -->
	            </div><!-- ./col -->
	            
	            <div class="col-md-6">
	              <div class="box box-solid">
	                <div class="box-header with-border">
	                 <!--  <i class="fa fa-text-width"></i> -->
	                  <h3 class="box-title"><spring:message code="setting.my.view.data.sharing.title" /></h3>
	                </div><!-- /.box-header -->
	                <div class="box-body">
	                  <ul class="">
	                    <c:forEach var="subUser" items="${subUsers}">
	                    	<LI>${subUser.name}(${subUser.email})</LI>
	                    </c:forEach>
	                  </dl>
	                </div><!-- /.box-body -->
	              </div><!-- /.box -->
	            </div><!-- ./col -->
	          </div><!-- /.row -->
          <!-- END TYPOGRAPHY -->
		</section>
</form><!-- /form -->	

	<script type="text/javascript"> 
	
		$(document).ready(function() {
			$("#parent_id").select2({
			    ajax: {
				    url: '${ctx}/ajax/getUsers',
				    delay: 250,
				    dataType: 'json',
				    data: function (params) {
				    	return {
			                q: params.term
			            };
			        },
			        processResults: function (data) {
			            return {results: data};
			        }
				},
				placeholder: "<spring:message code='setting.data.sharing.remark' />",
				allowClear: true,
				minimumInputLength: 1,
				language: lang_select2()
			});
			
			$("#assistant_id").select2({
                ajax: {
                    url: '${ctx}/ajax/getAssistants',
                    delay: 250,
                    dataType: 'json',
                    data: function (params) {
                        return {
                            q: params.term
                        };
                    },
                    processResults: function (data) {
                        return {results: data};
                    }
                },
                placeholder: "<spring:message code='setting.data.sharing.remark.assistant' />",
                allowClear: true,
                minimumInputLength: 1,
                language: lang_select2()
            });
		});
		
	</script>
	
	</body>

</html>