<%@tag import="java.util.Map"%>
<%@tag pageEncoding="UTF-8"%>
<%@ attribute name="value" type="java.lang.String" required="true"%>
<%@ attribute name="items" type="java.util.Map" required="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:forEach var="item" items="${items}">
	<c:if test="${item.key==value}">${item.value}</c:if>
</c:forEach>
