<%@tag import="org.apache.commons.lang3.ArrayUtils"%>
<%@tag import="org.springframework.web.servlet.support.RequestContextUtils"%>
<%@tag import="org.springframework.web.servlet.LocaleResolver"%>
<%@tag import="com.asgab.util.SelectMapper"%>
<%@tag import="org.apache.commons.lang3.StringUtils"%>
<%@tag import="java.util.Map"%>
<%@tag import="java.util.Iterator"%>
<%@tag pageEncoding="UTF-8"%>
<%@ attribute name="name" type="java.lang.String" required="true"%>
<%@ attribute name="id" type="java.lang.String" %>
<%@ attribute name="value" type="java.lang.String" %>
<%@ attribute name="list" type="java.util.List" required="true"%>
<%@ attribute name="add0" type="java.lang.Boolean" %>
<%@ attribute name="addNull" type="java.lang.Boolean" %>
<%@ attribute name="style" type="java.lang.String" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%
	if(StringUtils.isBlank(id)){
	  id = name;
	}
%>

<select name="<%=name%>" class="form-control select2 <%=name%>" id="<%=id%>" style="<%=style%>" multiple="multiple">
	<%
	  	LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver (request);
		String lang =localeResolver.resolveLocale(request).getLanguage();
		boolean isZH = "zh".equalsIgnoreCase(lang);
		if(add0!=null && add0){
			%>
			<option value="0" ><%= isZH?"请选择":"Please Select" %></option>
			<%
		}else if(addNull!=null && addNull){
		   %>
			<option value="" ><%= isZH?"请选择":"Please Select" %></option>
			<%
		}

	
		for(int i = 0 ;i<list.size();i++){
		  String selected = "";
		  SelectMapper mapper = (SelectMapper)list.get(i);
		  if(StringUtils.isNoneBlank(value) && ArrayUtils.contains(value.split(","), String.valueOf(mapper.getId()))){
			selected="selected=\"selected\"";
			}
		  %>
			<option value="<%=mapper.getId() %>" <%=selected %> ><%=mapper.getValue() %></option>
			<%
		}
		
	%>
</select>