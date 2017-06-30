<%@tag import="org.springframework.web.servlet.support.RequestContextUtils"%>
<%@tag import="org.springframework.web.servlet.LocaleResolver"%>
<%@tag pageEncoding="UTF-8"%>
<%@ attribute name="page" type="com.asgab.core.pagination.Page"
	required="true"%>
<%@ attribute name="paginationSize" type="java.lang.Integer"
	required="true"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%
	int current = page.getPageNumber();
	int begin = Math.max(1, current - paginationSize / 2);
	int end = Math.min(begin + (paginationSize - 1), page.getPageCount());
	request.setAttribute("current", current);
	request.setAttribute("begin", begin);
	request.setAttribute("end", end);
%>

		<ul class="pagination pull-left no-margin hidden-sm hidden-xs">
			<%
			LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver (request);
			String lang =localeResolver.resolveLocale(request).getLanguage();
			if("zh".equals(lang)){
				%>
				<li>共<%=page.getTotal()%>条记录。</li> 
				每页显示
				<select class="pageSizeSelect" name="pageSize" onchange="pageSizeChange();">
					<option value="10" <c:if test="${page.pageSize == '10'}">selected</c:if>>10</option>
					<option value="20" <c:if test="${page.pageSize == '20'}">selected</c:if>>20</option>
					<option value="50" <c:if test="${page.pageSize == '50'}">selected</c:if>>50</option>
					<option value="100" <c:if test="${page.pageSize == '100'}">selected</c:if>>100</option>
				</select> 条
				<%
			}else{
				%>
				<li>Total <%=page.getTotal()%> entries.</li>
				 Show  
				<select class="pageSizeSelect" name="pageSize" onchange="pageSizeChange();">
					<option value="10" <c:if test="${page.pageSize == '10'}">selected</c:if>>10</option>
					<option value="20" <c:if test="${page.pageSize == '20'}">selected</c:if>>20</option>
					<option value="50" <c:if test="${page.pageSize == '50'}">selected</c:if>>50</option>
					<option value="100" <c:if test="${page.pageSize == '100'}">selected</c:if>>100</option>
				</select> entries
				<%
			}
			%>
		</ul>
		
		<script type="text/javascript">
		
			function pageSizeChange(){
				var pageSize = $(".pageSizeSelect").val();
				window.location.href = '?pageNumber=1&sort=<%=page.getSort()%>&pageSize='+pageSize+'&'+'${search}';
			}
			
		</script>

		<ul class="pagination pagination-sm no-margin pull-right">
			<%
				if (page.hasPrevious()) {
			%>
			<li><a href="?pageNumber=1&sort=<%=page.getSort()%>&pageSize=<%=page.getPageSize()%>&${search}">&lt;&lt;</a></li>
			<li><a href="?pageNumber=${current-1}&sort=<%=page.getSort()%>&pageSize=<%=page.getPageSize()%>&${search}">&lt;</a></li>
			<%
				} else {
			%>
			<li class="disabled"><a href="#">&lt;&lt;</a></li>
			<li class="disabled"><a href="#">&lt;</a></li>
			<%
				}
			%>

			<c:forEach var="i" begin="${begin}" end="${end}">
				<c:choose>
					<c:when test="${i == current}">
						<li class="active"><a
							href="?pageNumber=${i}&sort=<%=page.getSort()%>&pageSize=<%=page.getPageSize()%>&${search}">${i}</a></li>
					</c:when>
					<c:otherwise>
						<li><a href="?pageNumber=${i}&sort=<%=page.getSort()%>&pageSize=<%=page.getPageSize()%>&${search}">${i}</a></li>
					</c:otherwise>
				</c:choose>
			</c:forEach>
			<%
				if (page.hasNext()) {
			%>
			<li><a href="?pageNumber=${current+1}&sort=<%=page.getSort()%>&pageSize=<%=page.getPageSize()%>&${search}">&gt;</a></li>
			<li><a
				href="?pageNumber=${page.pageCount}&sort=<%=page.getSort()%>&pageSize=<%=page.getPageSize()%>&${search}">&gt;&gt;</a></li>
			<%
				} else {
			%>
			<li class="disabled"><a href="#">&gt;</a></li>
			<li class="disabled"><a href="#">&gt;&gt;</a></li>
			<%
				}
			%>

		</ul>


