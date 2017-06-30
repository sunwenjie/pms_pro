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

<html>
<head>
<title><spring:message code="menu.report" /></title>
</head>
<body>
	<!-- Content Header -->
	<section class="content-header">
		<h1><spring:message code="report.report" /></h1>
		<ol class="breadcrumb">
			<li><a href="${ctx}/businessOpportunity"><i class="fa fa-dashboard"></i> <spring:message code="report.home" /></a></li>
			<li class="active"><spring:message code="report.report" /></li>
		</ol>
		
		<!-- only display on mobile -->
		<div class="alert alert-info alert-dismissible visible-xs-block" style="margin: 10px 0 0 0">
                <button type="button" class="close" data-dismiss="alert" aria-hidden="true">×</button>
                <h4><i class="icon fa fa-info"></i><spring:message code="reprot.reminder" /></h4>
               <spring:message code="reprot.mobile.reminder" />
        </div>
        
	</section>

	<!-- Main content -->
	<section class="content">
		<div class="nav-tabs-custom">
			<ul class="nav nav-tabs">
				<li class="active"><a href="#activity" data-toggle="tab"
					aria-expanded="true"><spring:message code="report.sale.data"/></a></li>
				<li class=""><a href="#timeline" data-toggle="tab"
					aria-expanded="false"><spring:message code="report.analysis"/></a></li>
			</ul>
			<div class="tab-content">
				<div class="tab-pane active" id="activity">

					<div class="box  box-solid">
						<div class="box-header with-border">
							<h3 class="box-title">
								<button type="button" onclick="submitForm();" class="btn btn-primary btn-sm btn-60"><spring:message code="btn.apply" /></button>
								<button type="button" onclick="resetForm();" class="btn btn-warning btn-sm btn-60"><spring:message code="btn.reset" /></button>
							</h3>
							<div class="box-tools pull-right">
								<button class="btn btn-box-tool" data-widget="collapse">
									<i class="fa fa-minus"></i>
								</button>
							</div>
						</div>
						<!-- /.box-header -->
						<form action="${ctx}/report/list" method="get" role="form" id="primaryForm">
						<input class="currencyCode" type="hidden" name="currencyCode" id="currencyCode"  />
						<input class="exchangeRate" type="hidden" name="exchangeRate" id="exchangeRate"  />
						<div class="box-body" style="display: block;">
							<div class="row">
								<div class="col-md-6">
									<div class="form-group">
										<label><spring:message code="report.data.right" /></label> 
										<select class="form-control select2 select3" name="dataRight" id="dataRight" style="width: 100%;">
											<option value="5" selected="selected"><spring:message code="report.advertiser" /></option>
											<option value="3"><spring:message code="report.sale.representative" /></option>
											<option value="2"><spring:message code="report.sale.team" /></option>
											<option value="1"><spring:message code="report.product" /></option>
											<option value="4"><spring:message code="report.channel.company" /></option>
										</select>
									</div>
									<!-- /.form-group -->

								</div>
								<!-- /.col -->

								<div class="col-md-6">
									<div class="form-group">
										<label><spring:message code="report.metric" /></label> 
										<select class="form-control select2 select3" name="metric" id="metric" style="width: 100%;">
											<option value="1" selected="selected"><spring:message code="report.income" /></option>
											<c:if test="${gpHidden eq false}">
												<option value="2"><spring:message code="report.estimate.gp" /></option>
											</c:if>
										</select>
									</div>
								</div>
								<!-- /.col -->

							</div>
							<!-- /.row -->
						</div>
						<!-- /.box-body -->

						<div class="box-footer">
							<div class="row">
								<div class="col-md-6">
									<div class="form-group">
										<label><spring:message code="report.date" /></label>
                                   		 <div id="reportDateDiv" style="background: #fff; cursor: pointer; padding: 7px 8px; border: 1px solid #ccc; width: 100%">
                                       			<i class="pull-left glyphicon glyphicon-calendar fa fa-calendar"></i>&nbsp;
                                        		<span id="reportDateSpan" ></span> <b style="margin-top: 6px;" class="caret pull-right"></b>
                                        		<input type="hidden" name="reportDate" id="reportDate">
                                   		 </div>
									</div>
									<!-- /.form-group -->

									<div class="form-group">
										<label><spring:message code="report.progress"/></label> 
										<input id="progress" class="form-control" data-slider-id="blue" type="text" name="progress">
									</div>
									<!-- /.form-group -->

								</div>
								<!-- /.col -->

								<div class="col-md-6">
								    <!-- product type-->
                                    <div class="form-group">
                                        <label><spring:message code="business.opportunity.product.type" /></label>
                                        <select class="productCategories" name="productCategories" id="productCategories" style="width: 100%" multiple="multiple">
                                              <c:forEach items="${productCategories}" var="p">
                                                  <option value="${p.id}">${p.value}</option>
                                              </c:forEach>
                                        </select> 
                                    </div>
								
									<div class="form-group" id="saleTeamDiv">
										<label><spring:message code="report.sale.team.title" /></label> 
										<tags:selectbox id="saleTeam" name="saleTeam" list="${saleTeams}" style="width: 100%;" multiple="multiple"></tags:selectbox>
									</div>
									<!-- /.form-group -->

									<div class="form-group" id="saleRepresentativeDiv">
										<label><spring:message code="report.sale.representative" /></label> 
										<tags:selectbox id="saleRepresentative" name="saleRepresentative" list="${saleRepresentatives}" style="width: 100%;" multiple="multiple"></tags:selectbox>
									</div>
									<!-- channel -->
									<div class="form-group" id="channelsDiv">
										<label><spring:message code="report.channel.company" /></label> 
										<tags:selectbox id="channels" name="channels" list="${channels}" style="width: 100%;" multiple="multiple"></tags:selectbox>
									</div>
									
									
									<c:if test="${gpHidden eq false}">
									<div class="form-group" style="display: none;">
										<label><spring:message code="report.gp" /></label>
										<select class="form-control select2 select3" name="gp" id="gp" style="width: 100%;">
											<option selected="selected" value="0"><spring:message code="public.search.select.option.null" /></option>
											<option value="1"><spring:message code="report.gp1" /></option>
											<option value="2"><spring:message code="report.gp2" /></option>
											<option value="3"><spring:message code="report.gp3" /></option>
											<option value="4"><spring:message code="report.gp4" /></option>
											<option value="5"><spring:message code="report.gp5" /></option>
											<option value="6"><spring:message code="report.gp6" /></option>
											<option value="7"><spring:message code="report.gp7" /></option>
										</select>
									</div>
									</c:if>
									
									<div class="form-group">
										<label><spring:message code="report.budget" /></label> 
										<select class="form-control select2 select3" name="budget_page" id="budget_page" style="width: 100%;">
											<option selected="selected" value="0,0"><spring:message code="public.search.select.option.null" /></option>
											<option value="0,10000"><spring:message code="report.budget1" /></option>
											<option value="10000,100000"><spring:message code="report.budget2" /></option>
											<option value="100000,500000"><spring:message code="report.budget3" /></option>
											<option value="500000,1000000"><spring:message code="report.budget4" /></option>
											<option value="1000000,0"><spring:message code="report.budget5" /></option>
										</select>
									</div>
									
									<div class="form-group" style="display: none;">
										<label><spring:message code="report.order.type" /></label> 
										<select class="form-control select2 select3" name="orderType" id="orderType" style="width: 100%;">
											<option selected="selected" value="0"><spring:message code="public.search.select.option.null" /></option>
											<option value="1"><spring:message code="report.order.type1" /></option>
											<option value="2"><spring:message code="report.order.type2" /></option>
										</select>
									</div>


								</div>
								<!-- /.col -->

							</div>
							<!-- /.row -->

						</div>
						<!-- /.box-footer -->

					</form>
					</div>
					<!--  -->
					<div class="reportResultList">
					
					</div>

				</div>
				<!-- /.tab-pane -->
				<div class="tab-pane" id="timeline">
					<jsp:include page="analyse.jsp"></jsp:include>
				</div>
				<!-- /.tab-pane -->
			</div>
			<!-- /.tab-content -->
		</div>
	</section>
	
	<script type="text/javascript">
		$(function() {
			
			$("#menu_report").addClass("active");
			
			$(".select3").select2({
				language: lang_select2()
			});
			
			$("#productCategories").select2({
				placeholder: "<spring:message code='public.search.select.option.null' />"
			});
			$("#saleTeam").select2({
				placeholder: "<spring:message code='public.search.select.option.null' />"
			});
			$("#saleRepresentative").select2({
				placeholder: "<spring:message code='public.search.select.option.null' />"
			});
			$("#channels").select2({
				placeholder: "<spring:message code='public.search.select.option.null' />"
			});
			
			// 默认隐藏
            $("#saleTeamDiv,#saleRepresentativeDiv,#channelsDiv").hide();
			
			$("#progress").ionRangeSlider({
				min:0,
				max:100,
				type: "double",
			    grid: true,
				values:[0,10,30,50,70,90,100],
				postfix:"%"
			});
			
			$("#metric").change(function(){
				if($(this).val()=="2"){
					$("#gp").parent().show();
				}else{
					$("#gp").parent().hide();
				}
			});
			
			// dataRight change
			$("#dataRight").change(function(){
				// 销售团队
				if($(this).val()=="2"){
					$("#saleTeam").parent().show();
					$("#saleRepresentative").parent().hide();
					$("#channels").parent().hide();
					$("#productCategories").parent().hide();
				}
				// 销售代表
				else if($(this).val()=="3"){
					$("#saleRepresentative").parent().show();
					$("#saleTeam").parent().hide();
					$("#channels").parent().hide();
					$("#productCategories").parent().hide();
				}
				// channel
				else if($(this).val()=="4"){
					$("#channels").parent().show();
					$("#saleTeam").parent().hide();
					$("#saleRepresentative").parent().hide();
					$("#productCategories").parent().hide();
				}
				else if($(this).val()=="1"){
					$("#productCategories").parent().show();
					$("#saleTeam").parent().hide();
					$("#saleRepresentative").parent().hide();
					$("#channels").parent().hide();
				}else{
					$("#saleTeam").parent().hide();
					$("#saleRepresentative").parent().hide();
                    $("#channels").parent().hide();
                    $("#productCategories").parent().hide();
				}
			});
			
			//default value
			$('#reportDateSpan').html(
					moment().startOf('quarter').format('YYYY/MM/DD') + ' - '+ moment().endOf('quarter').format('YYYY/MM/DD'));
			$('#reportDate').val(
					moment().startOf('quarter').format('YYYY/MM/DD') + ' - '+ moment().endOf('quarter').format('YYYY/MM/DD'));

			$('#reportDateDiv').daterangepicker(
					{
						ranges : {
							'<spring:message code="date.today"/>' : [ moment(), moment() ],
							'<spring:message code="date.yesterday"/>' : [ moment().subtract(1, 'days'),moment().subtract(1, 'days') ],
							'<spring:message code="date.last7Days"/>' : [ moment().subtract(6, 'days'),moment() ],
							'<spring:message code="date.last30Days"/>' : [ moment().subtract(29, 'days'),moment() ],
							'<spring:message code="date.thisMonth"/>' : [ moment().startOf('month'),moment().endOf('month') ],
							'<spring:message code="date.lastMonth"/>' : [
									moment().subtract(1, 'month').startOf('month'),
									moment().subtract(1, 'month').endOf('month') ],
							'<spring:message code="date.thisQuarter"/>' : [moment().subtract(0, 'quarter').startOf('quarter'),
											  moment().subtract(0, 'quarter').endOf('quarter') ],
							'<spring:message code="date.lastQuarter"/>' : [moment().subtract(1, 'quarter').startOf('quarter'),
											  moment().subtract(1, 'quarter').endOf('quarter') ],
							'<spring:message code="date.nextQuarter"/>' : [ moment().add(1, 'quarter').startOf('quarter'),moment().add(1, 'quarter').endOf('quarter') ]
									
						},
						format : 'YYYY/MM/DD',
					    locale :{
			                applyLabel: '<spring:message code="date.apply"/>',
			                cancelLabel: '<spring:message code="date.cancel"/>',
			                fromLabel: '<spring:message code="date.from"/>',
			                toLabel: '<spring:message code="date.to"/>',
			                weekLabel: 'W',
			                customRangeLabel: '<spring:message code="date.customRange"/>',
			                daysOfWeek: moment.weekdaysMin(),
			                monthNames: moment.monthsShort(),
			                firstDay: moment.localeData()._week.dow
			            },
						startDate : moment().startOf('quarter'),
						endDate : moment().endOf('quarter')
					},
					function(start, end) {
						$('#reportDateSpan').html(
								start.format('YYYY/MM/DD') + ' - '+ end.format('YYYY/MM/DD'));
						
						$('#reportDate').val(start.format('YYYY/MM/DD') + ' - '+ end.format('YYYY/MM/DD'));
					});
		})
		
		function submitForm(){
			
			var dataRight=$("#dataRight").val();
			var metric=$("#metric").val();
			var reportDate=$("#reportDate").val();
			var progress=$("#progress").val();
			var budget_page=$("#budget_page").val();
			var orderType=$("#orderType").val();
			var productCategories = $("#productCategories").val();
			var saleTeam=$("#saleTeam").val();
			var saleRepresentative=$("#saleRepresentative").val();
			var channels=$("#channels").val();
			var gp=$("#gp").val();
			var currency=$("#changeRate option:selected").text();
			console.log(saleTeam);
			console.log(saleRepresentative);
			$.ajax({
					url:"${ctx}/ajax/report/list",
					traditional: true,
					type: "POST",
					data:{
						'dataRight':dataRight,
						'metric':metric,
						'reportDate':reportDate,
						'progress':progress,
						'budget_page':budget_page,
						'orderType':orderType,
						'productCategories':productCategories,
						'saleTeam':saleTeam,
						'saleRepresentative':saleRepresentative,
						'gp':gp,
						'currency':currency,
						'channels':channels
						},
					dataTypeString:"html",
					success:function(data){
						$(".reportResultList").html(data);
					}
			});
		};
		
		function resetForm(){
			$("#dataRight").val("1").trigger('change');
			$("#metric").val("1").trigger('change');
			$('#reportDateSpan').html(moment().startOf('quarter').format('YYYY/MM/DD') + ' - '+ moment().endOf('quarter').format('YYYY/MM/DD'));
			$('#reportDate').val(moment().startOf('quarter').format('YYYY/MM/DD') + ' - '+ moment().endOf('quarter').format('YYYY/MM/DD'));
			$('#reportDateDiv').data('daterangepicker').setStartDate(moment().startOf('quarter'));
			$('#reportDateDiv').data('daterangepicker').setEndDate(moment().endOf('quarter'));
			$("#progress").data("ionRangeSlider").reset();
			$('#budget_page').val('0,0').trigger('change');
			$('#orderType').val('0').trigger('change');
			$('#saleTeam').val('').trigger('change');
			$('#saleRepresentative').val('').trigger('change');
			$('#channels').val('').trigger('change');
			$('#gp').val('0').trigger('change');
			$("#changeRate").val('1').change();
			$("#productCategories").val(null).change();
		};
	</script>
</body>
</html>
