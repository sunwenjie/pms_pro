<%@tag pageEncoding="UTF-8"%>
<%@ attribute name="page" type="com.asgab.core.pagination.Page" required="true"%>
<%@ attribute name="column" type="java.lang.String" required="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String sortStr ="";
	if(page.getSort()!=null && page.getSort()!="" && page.getSort().startsWith(column)){
		if(page.getSort().indexOf("desc")>0){
			sortStr = column;
		}else{
			sortStr = column +" desc";
		}
	}else{
		sortStr = column;
	}
%>
onclick = 'javascript:window.location.href="?sort=<%=sortStr%>&${search}"' class=""
