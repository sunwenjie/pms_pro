<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<div class="row" id="row_product_${index}">
	<div class="col-md-6">
		<div class="form-group">
			<label class="col-md-3 control-label" for="product_category_id"><spring:message code='business.opportunity.product.type'/> ${index+1} *</label>
			<div class="col-md-9">
				<select class="form-control select2" name="businessOpportunityProducts[${index}].product_category_id" 
				style="width: 100%;" onchange="loadProducts(this,'${index}', null);">
					<option value></option>
					<c:forEach var="pc" items="${productCategories}">
						<option value="${pc.id}">${pc.value}</option>
					</c:forEach>
				</select>
			</div>
		</div>
		<div class="form-group">
			<label class="col-md-3 control-label" for="product_id"><spring:message code="business.opportunity.product"/> ${index+1} *</label>
			<div class="col-md-9">
				<select class="form-control select2" name="businessOpportunityProducts[${index}].product_id" style="width: 100%;">
					<option value></option>
					<%-- <c:forEach var="product" items="${products_data}">
						<option value="${product.id}">${product.name}</option>
					</c:forEach> --%>
				</select>
			</div>
		</div>
		<div class="form-group">
			<label class="col-md-3 control-label" for="sale_mode"><spring:message code="business.opportunity.sale.mode"/> ${index+1} *</label>
			<div class="col-md-9">
				<tags:selectbox name="businessOpportunityProducts[${index}].sale_mode" list="${saleModes}" addNull="true"></tags:selectbox>
			</div>
		</div>
		<div class="form-group">
			<label class="col-md-3 control-label" for="budget"><spring:message code="business.opportunity.product.budget"/> ${index+1} *</label>
			<div class="col-md-9">
				<div class="input-group">
				<input type="text" class="form-control text-right"  data-mask data-inputmask="'alias': 'decimal', 'groupSeparator': ',', 'autoGroup': true, 'digits': 2" name="businessOpportunityProducts[${index}].budget_format" placeholder="<spring:message code='business.opportunity.input.product.budget' />">
				<div class="input-group-addon"><i class="fa fa-money"></i></div>
				</div>
			</div>
		</div>
	</div>
	<div class="col-md-6">
		<div class="form-group">
			<label class="col-md-3"><a href="javascript:void(0);" class="rm_product_link" onclick="removeProduct('${index}');" style="color: red;"><i class="fa fa-w fa-minus-square"></i>&nbsp;<spring:message code="business.opportunity.product.remove" /></a></label>
		</div>
	</div>
</div>

<script>    
	$(document).ready(function() {
		$("[data-mask]").inputmask();
		$("select[name='businessOpportunityProducts[${index}].product_category_id']").select2({
			 placeholder: "<spring:message code='business.opportunity.product.type.remark'/>",
			 allowClear: true,
			 language: lang_select2()
		});
		$("select[name='businessOpportunityProducts[${index}].product_id']").select2({
		    placeholder: "<spring:message code='business.opportunity.input.product'/>",
		    allowClear: true,
		    language: lang_select2()
		}).on('change',function(evt){
			validateSelect2(this);
		});
	});
</script>