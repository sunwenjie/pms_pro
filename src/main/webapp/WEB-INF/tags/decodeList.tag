<%@tag import="java.util.List"%>
<%@tag pageEncoding="UTF-8"%>
<%@ attribute name="value" type="java.lang.String" required="true"%>
<%@ attribute name="list" type="java.util.List" required="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:forEach var="item" items="${list}"><c:if test="${item.id eq value}">${item.value}</c:if></c:forEach>