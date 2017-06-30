<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<div class="row contacts" id="client_contact_${index}">
    <div class="col-md-6">
   			<div class="form-group">
       			<label for="" class="col-md-3 control-label"><spring:message code="client.contact.person" /> ${index+1}<em> *</em></label>
       			<div class="col-md-9">
       				<input id="contact_person" class="form-control" type="text" name="contacts[${index}].contact_person" value="" />
       			</div>
       		</div>
         		
       		<div class="form-group">
       			<label for="" class="col-md-3 control-label"><spring:message code="client.contact.position" /> ${index+1}<em> *</em></label>
       			<div class="col-md-9">
       				<input id="position" class="form-control" type="text" name="contacts[${index}].position" value="" />
       			</div>
       		</div>
         		
       		<div class="form-group">
       			<label for="" class="col-md-3 control-label"><spring:message code="client.contact.phone" /> ${index+1}<em> *</em></label>
       			<div class="col-md-9">
       				<input id="phone" class="form-control" type="text" name="contacts[${index}].phone" value="" />
       			</div>
       		</div>
         		
       		<div class="form-group">
       			<label for="" class="col-md-3 control-label"><spring:message code="client.contact.email" /> ${index+1}<em> *</em></label>
       			<div class="col-md-9">
       				<input id="email" class="form-control" type="text" name="contacts[${index}].email" value="" />
       			</div>
       		</div>
         		
	</div>
	
	<div class="col-md-6">
		<div class="form-group">
			<c:if test="${index != 0}">
				<label class="col-md-3" id="rm-contact-${index}"><a href="javascript:void(0);" style="color: red;">
					<i class="fa fa-w fa-minus-square"></i>&nbsp;<spring:message code="client.contact.delete" /> ${index+1}</a>
				</label>
			</c:if>	
		</div>
	</div>
	
</div><!-- /.row -->

<script>    
	$(document).ready(function() {
		$("#client_contact_${index}").find("input[name='contacts[${index}].contact_person']").rules('add',{required:true});
		$("#client_contact_${index}").find("input[name='contacts[${index}].position']").rules('add',{required:true});
		$("#client_contact_${index}").find("input[name='contacts[${index}].phone']").rules('add',{required:true,maxlength:20});
		$("#client_contact_${index}").find("input[name='contacts[${index}].email']").rules('add',{required:true,email:true});
	});
</script>