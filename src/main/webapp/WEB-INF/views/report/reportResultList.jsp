<%@page import="com.asgab.util.JsonMapper"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.asgab.util.CommonUtil"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.asgab.entity.Report"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="org.springframework.web.servlet.support.RequestContextUtils"%>
<%@page import="org.springframework.web.servlet.LocaleResolver"%>
<%@page import="org.apache.shiro.SecurityUtils"%>
<%@page import="com.asgab.service.account.ShiroDbRealm.ShiroUser"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<%
    ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
	LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
	String lang = localeResolver.resolveLocale(request).getLanguage();
%>

<div class="box-header with-border" style="padding-left: 0px">
	<h3 class="box-title">
	   <c:if test="${boxtitle != null}">
		<spring:message code="${boxtitle}" />
	   </c:if> <b class="text-green"><span id="totalSum"></span></b>
	</h3>
	<div style="float: right">
		<a class="pull-right" onclick="download_report();" style="cursor: pointer;margin: 10px 0 0 10px;"><i class="fa fa-fw fa-download"></i><spring:message code="report.download" /></a>
		<select id="changeRate" class="pull-right select3" style="width: 100px;"></select>
	</div>
		
</div>
<br/>
	<table class="table table-bordered table-responsive" id="dataTableReport">
		<thead>
		</thead>
		<tbody>
			<%
				List<List<String>> datas = new ArrayList<List<String>>();
			
				List<String> names = (List<String>)request.getAttribute("names");
				List<Report> opportunityReports = (List<Report>)request.getAttribute("opportunityReports");
				List<Report> orderReports = (List<Report>)request.getAttribute("orderReports");
				BigDecimal totalSum = BigDecimal.ZERO;
				BigDecimal orderSum = BigDecimal.ZERO;
				BigDecimal opportunitySum = BigDecimal.ZERO;
				for(int i = 0 ;i <names.size();i++){
				  List<String> data = new ArrayList<String>();
				  BigDecimal sum = BigDecimal.ZERO;
				  Report orderReport = orderReports.get(i);
				  if(orderReport!=null){
				    sum = sum.add(orderReport.getBudget());
				    orderSum = orderSum.add(orderReport.getBudget());
				  }
				  Report opportunityReport = opportunityReports.get(i);
				  if(opportunityReport!=null){
				    sum = sum.add(opportunityReport.getBudget());
				    opportunitySum = opportunitySum.add(opportunityReport.getBudget());
				  }
				  totalSum = totalSum.add(sum);
				  
				  data.add(names.get(i));
				  data.add(orderReport!=null?orderReport.getFmtBudget():"0.00");
				  data.add(opportunityReport!=null?opportunityReport.getFmtBudget():"0.00");
				  data.add(CommonUtil.digSeg(sum.doubleValue()));
				  datas.add(data);
				}
				
				if(request.getAttribute("orderSum")!=null){
				  orderSum  = (BigDecimal)request.getAttribute("orderSum");
				  opportunitySum  = (BigDecimal)request.getAttribute("opportunitySum");
				  totalSum = orderSum.add(opportunitySum);
				}
				request.getAttribute("opportunitySum");
				
				request.setAttribute("totalSum", CommonUtil.digSeg(totalSum.doubleValue()));
				request.setAttribute("dataSet", JsonMapper.nonEmptyMapper().toJson(datas));
				
				request.setAttribute("orderSumData", CommonUtil.digSeg(orderSum.doubleValue()));
				request.setAttribute("opportunitySumData", CommonUtil.digSeg(opportunitySum.doubleValue()));
				request.setAttribute("totalSumData", CommonUtil.digSeg(totalSum.doubleValue()));
				
				
			%>
		</tbody>
		<tfoot>
			<tr>
				<th class="text-right"><spring:message code="report.total" /></th>
				<th style="text-align: right;"><%=CommonUtil.digSeg(orderSum.doubleValue()) %></th>
				<th style="text-align: right;"><%=CommonUtil.digSeg(opportunitySum.doubleValue()) %></th>
				<th style="text-align: right;"><%=CommonUtil.digSeg(totalSum.doubleValue()) %></th>
			</tr>
		</tfoot>
	</table>


<script type="text/javascript">
	// 货币图标
	var currencySymbol={symbols:[{name:"RMB",symbol:"fa-cny"},
	                               {name:"AUD",symbol:"fa-dollar",addition:"A"},
	                               {name:"EUR",symbol:"fa-eur"},
	                               {name:"GBP",symbol:"fa-gbp"},
	                               {name:"HKD",symbol:"fa-dollar",addition:"HK"},
	                               {name:"IDR",symbol:"fa-inr"},
	                               {name:"JPY",symbol:"fa-jpy"},
	                               {name:"KRW",symbol:"fa-krw"},
	                               {name:"MYR",symbol:"",addition:"RM"},
	                               {name:"RUB",symbol:"fa-rub"},
	                               {name:"SGD",symbol:"fa-dollar",addition:"S"},
	                               {name:"THB",symbol:"",addition:"฿"},
	                               {name:"TWD",symbol:"fa-dollar",addition:"NT"},
	                               {name:"USD",symbol:"fa-dollar"}
	                               ]};
		$(function() {
			
			$("#changeRate").select2({
	            data:currencyData,
	            templateResult:templateResult,
	            templateSelection:templateResult,
	            language: lang_select2()
	        });
	        function templateResult(repo){
	            if (repo.loading) return repo.text;
	            return  $("<span><img src='${ctx}/static/images/currency/"+repo.text+".gif' class='img-flag' />&nbsp;"+repo.text+"</span>");
	        };
			
			$("#totalSum").html('<i class="fa fa-w fa-cny"></i>${totalSum}');
			// 数据
			var dataSet = ${dataSet};
			// 统计
			var orderSumData = "${orderSumData}";
			var opportunitySumData = "${opportunitySumData}";
			var totalSumData = "${totalSumData}";
			// 国际化
			var dataRightColumn = "${dataRightColumn}";
			var col1 = "<spring:message code='report.clo1'/>";
			var col2 = "<spring:message code='report.clo2'/>";
			var col3 = "<spring:message code='report.clo3'/>";
			var sInfo = "<spring:message code='report.sinfo'/>";
			var sFirst= "<spring:message code='report.sfirst'/>";
			var sPrevious= "<spring:message code='report.sprevious'/>";
			var sNext= "<spring:message code='report.snext'/>";
			var sLast= "<spring:message code='report.slast'/>";
			var sLengthMenu= "<spring:message code='report.slengthmenu'/>";
			var table = $("#dataTableReport").DataTable({
				
				data: dataSet,
				
				columns: [
				            { title: dataRightColumn },
				            { title: col1 },
				            { title: col2 },
				            { title: col3 }
				        ],
				language: {
					"sInfo":sInfo,
					"oPaginate": {
			            "sFirst": sFirst,
			            "sPrevious": sPrevious,
			            "sNext": sNext,
			            "sLast": sLast,
			        },
			        "sSearch" :"<spring:message code='btn.search'/>",
			        "sLengthMenu": sLengthMenu
				},
				"createdRow": function( row, data, dataIndex ) {
                    $(row).children('td').eq(1).attr('style', 'text-align: right;');
                    $(row).children('td').eq(2).attr('style', 'text-align: right;');
                    $(row).children('td').eq(3).attr('style', 'text-align: right;');
                },
                "oLanguage": {
                	"sSearch": '<span class="glyphicon glyphicon-search"></span>'
                },
                "aaSorting": [[ 1, "desc" ]]
			});
			
			// 排序的时候用
			table.on( 'draw', function () {
				addSymbol($("#changeRate").find("option:selected").text());
			} );
			
			$("#changeRate").change(function(){
				var rate = +$(this).val();
				var symbol = $(this).find("option:selected").text();
		        table.clear();
		        var tmpDatas = [];
		        for(var i = 0 ; i<dataSet.length;i++){
		        	var tmpData = [];
		        	tmpData[0] = dataSet[i][0];
		        	for(var j = 1 ; j<dataSet[i].length;j++){
		        		tmpData[j] = formatMoney(rate * rmoney(dataSet[i][j]),2);
		        	}
		        	tmpDatas[i] = tmpData;
		        }
		        table.rows.add(tmpDatas);
		        table.draw();
		        
		        $("tfoot th:nth-child(2)").html(formatMoney(rate * rmoney(orderSumData,2)));
		        $("tfoot th:nth-child(3)").html(formatMoney(rate * rmoney(opportunitySumData,2)));
		        $("tfoot th:nth-child(4)").html(formatMoney(rate * rmoney(totalSumData,2)));
		        $("#totalSum").text(formatMoney(rate * rmoney(totalSumData,2)));
		        addSymbol(symbol);
			});
			
			$("#changeRate option:contains('<c:out value="${currency}"/>')").attr("selected", true);
			// 加载触发
			$("#changeRate").change();
			
		});
		
		// 源数据是格式化好的. 这里需要反格式化
		function rmoney(s)   
		{   
		   return parseFloat(s.replace(/[^\d\.-]/g, ""));   
		};
		
		// 格式化金钱
		function formatMoney(s, n)   
		{   
		   n = n > 0 && n <= 20 ? n : 2;   
		   s = parseFloat((s + "").replace(/[^\d\.-]/g, "")).toFixed(n) + "";   
		   var l = s.split(".")[0].split("").reverse(),   
		   r = s.split(".")[1];   
		   t = "";   
		   for(i = 0; i < l.length; i ++ )   
		   {   
		      t += l[i] + ((i + 1) % 3 == 0 && (i + 1) != l.length ? "," : "");   
		   }   
		   return t.split("").reverse().join("") + "." + r;   
		};
		
		// 添加货币符号
		function addSymbol(currency){
			var symbolData = getSymbol(currency);
			console.log(symbolData);
			var symbol = symbolData.symbol;
			var addition = (symbolData.addition===undefined?"":symbolData.addition);
			$("#dataTableReport tr").each(function(index){
				if(index>=1){
					var tds = $(this).children();
					for(var i=1;i<tds.length;i++){
					   tds.eq(i).find(".tdSymbol").remove();
				       tds.eq(i).html("<span class='tdSymbol'>"+addition + "<i class='fa fa-w "+symbol+"'></i></span>" +tds.eq(i).html());
				    }
				}
			});
			$("#totalSum").find(".tdSymbol").remove();
			$("#totalSum").html("<span class='tdSymbol'>"+addition + "<i class='fa fa-w "+symbol+"'></i></span>" + $("#totalSum").html());
		};
		
		function getSymbol(currency){
			for(var i = 0 ; i < currencySymbol.symbols.length;i++){
				if(currency == currencySymbol.symbols[i].name){
					return currencySymbol.symbols[i];
				}
			}
		};
		
		// 导出报表
        function download_report(){
            var origin_action = $("#primaryForm").attr("action");
            $("#primaryForm #currencyCode").val($("#changeRate").find("option:selected").text());
            $("#primaryForm #exchangeRate").val($("#changeRate").val());
            $("#primaryForm").attr("action","${ctx}/report/download");
            $("#primaryForm").submit();
            $("#primaryForm").attr("action",origin_action);
        };
</script>