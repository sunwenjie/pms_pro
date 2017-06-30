<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<c:if test="${client.contacts != null}">
	<c:forEach var="contact" items="${client.contacts}" varStatus="status">
			<div class="row contacts" id="client_contact_${status.index}">
			<input type="hidden" name="contacts[${status.index}].id" class="contact_id" value="${contact.id}" />
			
		    	<div class="col-md-6">
		   			<div class="form-group">
		       			<label for="" class="col-md-3 control-label"><spring:message code="client.contact.person" /> ${status.index+1}<em> *</em></label>
		       			<div class="col-md-9">
		       				<input id="contact_person" class="form-control" type="text" name="contacts[${status.index}].contact_person" value="${contact.contact_person}" />
		       			</div>
		       		</div>
		         		
		       		<div class="form-group">
		       			<label for="" class="col-md-3 control-label"><spring:message code="client.contact.position" /> ${status.index+1}<em> *</em></label>
		       			<div class="col-md-9">
		       				<input id="position" class="form-control" type="text" name="contacts[${status.index}].position" value="${contact.position}" />
		       			</div>
		       		</div>
		         		
		       		<div class="form-group">
		       			<label for="" class="col-md-3 control-label"><spring:message code="client.contact.phone" /> ${status.index+1}<em> *</em></label>
		       			<div class="col-md-9">
		       				<input id="phone" class="form-control" type="text" name="contacts[${status.index}].phone" value="${contact.phone}" />
		       			</div>
		       		</div>
		         		
		       		<div class="form-group">
		       			<label for="" class="col-md-3 control-label"><spring:message code="client.contact.email" /> ${status.index+1}<em> *</em></label>
		       			<div class="col-md-9">
		       				<input id="email" class="form-control" type="text" name="contacts[${status.index}].email" value="${contact.email}" />
		       			</div>
		       		</div>
		         		
			</div>
			
			<div class="col-md-6">
				<div class="form-group">
					<label class="col-md-3" id="rm-contact-${status.index}"><a href="javascript:void(0);" style="color: red;">
						<i class="fa fa-w fa-minus-square"></i>&nbsp;<spring:message code="client.contact.delete" /> ${status.index+1}</a>
					</label>
				</div>
			</div>
			
		</div><!-- /.row -->
			
	</c:forEach>
</c:if>


