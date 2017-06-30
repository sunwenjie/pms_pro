<%@page import="com.asgab.entity.BusinessOpportunityProduct"%>
<%@page import="java.util.List"%>
<%@page import="org.springframework.web.servlet.support.RequestContextUtils"%>
<%@page import="org.springframework.web.servlet.LocaleResolver"%>
<%@page import="com.asgab.entity.BusinessOpportunity"%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<%
    BusinessOpportunity businessOpportunity = (BusinessOpportunity)request.getAttribute("businessOpportunity");
	List<BusinessOpportunityProduct> products = businessOpportunity.getBusinessOpportunityProducts();
    LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver (request);
	String lang =localeResolver.resolveLocale(request).getLanguage();
	request.setAttribute("products", products);
	request.setAttribute("lang", lang);
%>

<c:forEach items="${products}" var="product" varStatus="status">
<div id="row_product_${status.index}">
		<dl class="dl-horizontal">
            <dt><spring:message code='business.opportunity.product.type'/> ${status.index+1}</dt>
            <dd>${product.productCategory.value}</dd>
        </dl>
        
        <dl class="dl-horizontal">
            <dt><spring:message code="business.opportunity.product"/> ${status.index+1}</dt>
            <dd>
                <c:if test="${lang eq 'zh' }"> 
                    ${product.decodeProductZH}
                </c:if>
                <c:if test="${lang ne 'zh' }"> 
                    ${product.decodeProductEN}
                </c:if>
            </dd>
        </dl>
        
        <dl class="dl-horizontal">
            <dt><spring:message code="business.opportunity.sale.mode"/> ${status.index+1}</dt>
            <dd>${product.sale_mode}</dd>
        </dl>
        
        <dl class="dl-horizontal">
            <dt><spring:message code="business.opportunity.product.budget"/> ${status.index+1}</dt>
            <dd><fmt:formatNumber value="${product.budget}" pattern="#,###.00" /></dd>
        </dl>
</div>
</c:forEach>