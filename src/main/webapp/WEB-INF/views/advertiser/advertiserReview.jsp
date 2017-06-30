<%@page import="com.asgab.entity.ClientContact"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="org.springframework.web.servlet.support.RequestContextUtils"%>
<%@page import="org.springframework.web.servlet.LocaleResolver"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="org.apache.shiro.SecurityUtils"%>
<%@page import="com.asgab.service.account.ShiroDbRealm.ShiroUser"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<%
    ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
	LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver (request);
	String lang =localeResolver.resolveLocale(request).getLanguage();
	request.setAttribute("lang", lang);
%>

<html>
<head>
<title><spring:message code="menu.advertiser.review" /></title>
</head>
<body>
	<!-- Content Header -->
	<style>
		.input-text{width: 100%;text-align: left;background-color: #eee !important;border:1px solid #d2d6de !important;font-weight: bold;font-size: 12px;}
		.input-text-check{width: 100%;text-align: left;background-color: #fff !important;border:1px solid #d2d6de !important;font-weight: bold;font-size: 12px;}
		.input-div{width: 100%;background-color: #eee !important;border:1px solid #d2d6de !important;font-weight: bold;font-size: 12px;padding: 10px;}
		.input-div-check{width: 100%;background-color: #fff !important;border:1px solid #d2d6de !important;font-weight: bold;font-size: 12px;padding: 10px;}
		
	</style>
	<section class="content-header">
		<h1>
			<spring:message code="menu.advertiser.review" />
		</h1>
		<ol class="breadcrumb">
			<li><a href="${ctx}/businessOpportunity"><i class="fa fa-dashboard"></i>
					<spring:message code="opportunity.home" /></a></li>
			<li class="active"><spring:message code="menu.advertiser.review" /></li>
		</ol>
	</section>

	<!-- Content -->
	<section class="content">
		<div class="box box-default">

			<div class="box-body no-padding">
				<form action="${ctx}/advertiser/merge" method="post" id="primaryForm">
				<input type="hidden" class="input-ignore" name="checkIds" value="${checkIds }">
				<table class="table table-condensed table-striped ">
					<thead>
						<tr>
							<th style="width: 80px;"></th>
							<c:forEach items="${map['id']}" var="val" varStatus="status">
								<th class="merge-head-${status.count}" style="vertical-align: bottom;">
									<label for="${status.count}" style="vertical-align: bottom; margin-bottom: 1px;">
										<c:if test="${status.count == 1}">
											<input type="radio" name="id" class="flat-red allcheck first-checked" value="${val}"> 
										</c:if>
										<c:if test="${status.count != 1}">
											<input type="radio" name="id" class="flat-red allcheck" value="${val}">
										</c:if>
										<spring:message code="btn.retain" />
									</label>
								</th>
							</c:forEach>
						</tr>
					</thead>
					
					<tbody>
						<tr>
							<th style="vertical-align: middle;"><spring:message code="advertiser.clientname" /></th>
							<c:forEach items="${map['clientname']}" var="val" varStatus="status">
								<td class="merge-body-${status.count}">
									<div class="input-group col-xs-10">
										<div class="input-group-addon"><input type="radio" name="clientname_radio" /></div>
										<div class="clientname_disabled">
											<input type="text" name="clientname" class="form-control input-sm" value="${val}" disabled />
										</div> 
									</div>
								</td>
							</c:forEach>
						</tr>
						
						<tr>
							<th style="vertical-align: middle;"><spring:message code="advertiser.brand" /></th>
							<c:forEach items="${map['brand']}" var="val" varStatus="status">
								<td class="merge-body-${status.count}">
									<div class="input-group col-xs-10">
										<div class="input-group-addon"><input type="radio" name="brand_radio" /></div> 
										<div class="brand_disabled">
											<input type="text" name="brand" class="form-control input-sm" value="${val}" disabled />
										</div>
									</div>
								</td>
							</c:forEach>
						</tr>
						
						<tr>
							<th style="vertical-align: middle;"><spring:message code="advertiser.channle" /> </th>
							<c:forEach items="${map['channels']}" var="val" varStatus="status">
								<td class="merge-body-${status.count}">
									<div class="input-group col-xs-10">
										<div class="input-group-addon"><input type="radio" name="channel_name_radio" /></div>
										<div class="channel_name_disabled">
											<input type="text" readonly="readonly" class="form-control input-sm" disabled value="${val['channel_name']}" />
											<input type="hidden" name="whether_channel" value="${val['whether_channel']}" disabled />
											<input type="hidden" name="channel"  value="${val['channel']}" disabled />
										</div>
									</div>
								</td>
							</c:forEach>
						</tr>
						
						<tr>
							<th style="vertical-align: middle;"><spring:message code="advertiser.industry" /></th>
							<c:forEach items="${map['industry_id']}" var="val" varStatus="status">
								<td class="merge-body-${status.count}">
									<div class="input-group col-xs-10">
										<div class="input-group-addon"><input type="radio" name="industry_id_radio" /></div>
										<div class="industry_id_disabled">
											<input type="text" readonly="readonly" class="form-control input-sm" disabled value="<tags:decodeList list="${industryTypes}" value="${val}" />" />
											<input type="hidden" name="industry_id" value="${val}" disabled />
										</div>
									</div>
								</td>
							</c:forEach>
						</tr>
						
						<tr>
							<th style="vertical-align: middle;"><spring:message code="client.currency" /></th>
							<c:forEach items="${map['currency_id']}" var="val" varStatus="status">
								<td class="merge-body-${status.count}">
									<div class="input-group col-xs-10">
										<div class="input-group-addon"><input type="radio" name="currency_id_radio" /></div>
										<div class="currency_id_disabled">
											<input type="text" readonly="readonly" class="form-control input-sm" disabled value="<tags:decodeList list="${currencyTypes}" value="${val}" />" />
											<input type="hidden" name="currency_id" value="${val}" disabled />
										</div>
									</div>
								</td>
							</c:forEach>
						</tr>
						
						<tr>
							<th style="vertical-align: middle;"><spring:message code="advertiser.company.address" /></th>
							<c:forEach items="${map['address']}" var="val" varStatus="status">
								<td class="merge-body-${status.count}">
									<div class="input-group col-xs-10">
										<div class="input-group-addon"><input type="radio" name="address_radio" /></div>
										<div class="address_disabled">
											<input type="text" name="address" class="form-control input-sm" value="${val}" disabled />
										</div>
									</div>
								</td>
							</c:forEach>
						</tr>
						
						<tr>
							<th style="vertical-align: middle;"><spring:message code="advertiser.whether_cross_district" /></th>
							<c:forEach items="${map['whether_cross_districts']}" var="val" varStatus="status">
								<td class="merge-body-${status.count}">
									<div class="input-group col-xs-10">
										<div class="input-group-addon"><input type="radio" name="whether_cross_districts_radio" /></div>
										<div class="whether_cross_districts_disabled">
											<c:if test="${val == 1}">
												<input type="text" readonly="readonly" class="form-control input-sm" disabled value="<spring:message code="client.channel.yes" />" />
											</c:if>
											<c:if test="${val != 1}">
												<input type="text" readonly="readonly" class="form-control input-sm" disabled value="<spring:message code="client.channel.no" />" />
											</c:if>
											<input type="hidden" name="whether_cross_districts" class="form-control input-sm ${status.count}" value="${val}" disabled />
										</div>
									</div>
								</td>
							</c:forEach>
						</tr>
						
						<tr>
							<th style="vertical-align: middle;"><spring:message code="advertiser.qualification.name" /></th>
							<c:forEach items="${map['company_name']}" var="val" varStatus="status">
								<td class="merge-body-${status.count}">
									<div class="input-group col-xs-10">
										<div class="input-group-addon"><input type="radio" name="company_name_radio" /></div>
										<div class="company_name_disabled">
											<input type="text" name="company_name" class="form-control input-sm ${status.count}" value="${val}" disabled />
										</div>
									</div>
								</td>
							</c:forEach>
						</tr>
						
						<tr>
							<th style="vertical-align: middle;"><spring:message code="advertiser.website.name"/></th>
							<c:forEach items="${map['website_name']}" var="val" varStatus="status">
								<td class="merge-body-${status.count}">
									<div class="input-group col-xs-10">
										<div class="input-group-addon"><input type="radio" name="website_name_radio" /></div>
										<div class="website_name_disabled">
											<input type="text" name="website_name" class="form-control input-sm ${status.count}" value="${val}" disabled />
										</div>
									</div>
								</td>
							</c:forEach>
						</tr>
						
						<tr>
							<th style="vertical-align: middle;"><spring:message code="advertiser.website.address"/></th>
							<c:forEach items="${map['website_address']}" var="val" varStatus="status">
								<td class="merge-body-${status.count}">
									<div class="input-group col-xs-10">
										<div class="input-group-addon"><input type="radio" name="website_address_radio" /></div>
										<div class="website_address_disabled">
											<input type="text" name="website_address" class="form-control input-sm ${status.count}" value="${val}" disabled />
										</div>
									</div>
								</td>
							</c:forEach>
						</tr>
						
						<tr>
							<th style="vertical-align: middle;"><spring:message code="advertiser.organization.code"/></th>
							<c:forEach items="${map['organization_code']}" var="val" varStatus="status">
								<td class="merge-body-${status.count}">
									<div class="input-group col-xs-10">
										<div class="input-group-addon"><input type="radio" name="organization_code_radio" /></div>
										<div class="organization_code_disabled">
											<input type="text" name="organization_code" class="form-control input-sm ${status.count}" value="${val}" disabled />
										</div>
									</div>
								</td>
							</c:forEach>
						</tr>
						
						<tr>
							<th style="vertical-align: middle;"><spring:message code="advertiser.icp" /></th>
							<c:forEach items="${map['icp']}" var="val" varStatus="status">
								<td class="merge-body-${status.count}">
									<div class="input-group col-xs-10">
										<div class="input-group-addon"><input type="radio" name="icp_radio" /></div>
										<div class="icp_disabled">
											<input type="text" name="icp" class="form-control input-sm ${status.count}" value="${val}" disabled />
										</div>
									</div>
								</td>
							</c:forEach>
						</tr>
						
						<tr>
							<th style="vertical-align: middle;"><spring:message code="advertiser.business.licence" /></th>
							<c:forEach items="${map['business_licence']}" var="val" varStatus="status">
								<td class="merge-body-${status.count}">
									<div class="input-group col-xs-10">
										<div class="input-group-addon"><input type="radio" name="business_licence_radio" /></div>
										<div class="business_licence_disabled">
											<input type="text" name="business_licence" class="form-control input-sm ${status.count}" value="${val}" disabled />
										</div>
									</div>
								</td>
							</c:forEach>
						</tr>
						
						<tr>
							<th style="vertical-align: middle;"><spring:message code="advertiser.clientcontact" /></th>
							<c:forEach items="${map['contacts']}" var="contacts" varStatus="status">
								<td class="merge-body-${status.count}">
									<div class="input-group col-xs-10">
										<div class="input-group-addon"><input type="radio" name="contact_radio" /></div>
										<div class="contact_disabled">
											<c:if test="${fn:length(contacts) == 0 }">
												<input type="text" name="contact" class="form-control input-sm ${status.count}" disabled readonly="readonly"  />
											</c:if>
											<c:if test="${fn:length(contacts) > 0 }">
												<div class="input-div contact_div">
													<c:forEach items="${contacts}" var="val" varStatus="contactIndex">
														<spring:message code="advertiser.client.name" />${contactIndex.count}: ${val.contact_person }<br/>
														<spring:message code="advertiser.client.phone" />${contactIndex.count}: ${val.phone }<br/>
														<spring:message code="advertiser.client.position" />${contactIndex.count}: ${val.position }<br/>
														<div class="divider-horizontal"></div>
													</c:forEach>
												</div>	
											</c:if>	
											
											<input type="hidden" name="copy_contact_client_id" value="${map['id'][status.count-1]}" disabled="disabled" />
										</div>
									</div>
								</td>
							</c:forEach>
						</tr>
					</tbody>
				</table>
				
				<!-- -------modal------- -->
				<div class="modal fade" id="submitModal">
				  <div class="modal-dialog">
				    <div class="modal-content">
				      <div class="modal-header">
				        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
				        <h4 class="modal-title"><spring:message code="message.please.confirm" /></h4>
				      </div>
				      <div class="modal-body">
					      <div class="row">
					      	<div class="col-md-12 form-horizontal">
			                    <div class="form-group">
				                      <label for="remark" class="col-md-3"><spring:message code="advertiser.refresh.data.range" /><em> *</em></label>
				                      <div class="col-md-9">
				                      	 	<label>
						                    	<input type="radio" class="minimal refreshDataRange" name="refreshDataRange" checked value="1">
						                    	&nbsp;<spring:message code="advertiser.refresh.data.range.option1"/>&nbsp;&nbsp; 
						                    </label>
						                    <label>
						                      	<input type="radio" class="minimal refreshDataRange" name="refreshDataRange" value="2">
						                      	&nbsp;<spring:message code="advertiser.refresh.data.range.option2"/>&nbsp;&nbsp;
						                    </label>
						                    <label>
						                      	<input type="radio" class="minimal refreshDataRange" name="refreshDataRange" value="3">
						                      	&nbsp;<spring:message code="advertiser.refresh.data.range.option3"/>&nbsp;&nbsp;
						                    </label>
				                      </div>
			                    </div>
			                    
			                    <div class="form-group" id="mergedDataDIV"">
			                    	<label for="remark" class="col-md-3"><spring:message code="advertiser.merged.data"/><em> *</em></label>
			                      	<div class="col-md-9">
			                      		<input type="text" class="form-control" id="mergedData" name="mergedData" 
			                      		placeholder="<spring:message code='advertiser.merge.data' />" disabled="disabled" />
			                      	</div>
			                    </div>
			                </div>
					      </div>
				      </div>
				      <div class="modal-footer">
				        <button type="button" class="btn btn-sm btn-primary" onclick="submitForm();"><spring:message code="btn.confirm.submit"/></button>
				        <button type="button" class="btn btn-sm btn-primary disabled " data-dismiss="modal"><spring:message code="btn.cancel"/></button>
				      </div>
				    </div><!-- /.modal-content -->
				  </div><!-- /.modal-dialog -->
				</div><!-- /.modal -->
				
				
				</form>
			</div>
			
			<div class="box-footer">
              <button type="button" class="btn btn-sm btn-primary btn-65" onclick="$('#submitModal').modal('show');"><spring:message code="btn.save" /></button>
              <button type="button" class="btn btn-sm btn-primary btn-65 disabled" onclick="cancel();"><spring:message code="btn.cancel" /></button>
            </div>
		</div>
		
	</section>
	
	<div id="shield" style="position: fixed; left: 0px; top: 0px; display: none; z-index: 9998; opacity: 0.8; background: #7D7159; width: 100%; height: 100%;">
		<img src="${ctx}/static/images/loading_s.gif" style="position: absolute; top: 300px; left: 48%;" /></div>
	
	<script type="text/javascript">
	
	 $(function () {
   		$("#menu_client_Module").addClass("active");
  		$("#menu_advertiser_review").addClass("active");  
		$("#primaryForm").validate({
	        	
	    });
        //Flat red color scheme for iCheck
        $('input[type="checkbox"].flat-red, input[type="radio"].flat-red').iCheck({
          	checkboxClass: 'icheckbox_flat-green',
          	radioClass: 'iradio_flat-green'
        });
        
      	//iCheck for checkbox and radio inputs
        $('input[type="radio"].minimal').iCheck({
          	checkboxClass: 'icheckbox_minimal-blue',
          	radioClass: 'iradio_minimal-blue'
        });
        
        // 全选，保留广告主
        $("#primaryForm table thead th input[type='radio'].allcheck").on('ifChecked', function(event){
        	var col_idx = $(this).parents('th').attr('class').replace('merge-head-','');
        	$("#primaryForm table tbody td[class^='merge-body-'] input[type='radio']").iCheck('uncheck');
        	$("#primaryForm table tbody td[class^='merge-body-'] input[type='text']").attr("disabled","disabled");
        	$("#primaryForm table tbody td[class^='merge-body-'] input[type='hidden']").attr("disabled","disabled");
        	$("#primaryForm table tbody td.merge-body-" + col_idx + " input[type='radio']").iCheck('check');
        	$("#primaryForm table tbody td.merge-body-" + col_idx + " input[type='text']").removeAttr("disabled");
        	$("#primaryForm table tbody td.merge-body-" + col_idx + " input[type='hidden']").removeAttr("disabled");
        });
        
        // 单选，保留部分广告主
        $("#primaryForm table tbody td input[type='radio'][name$='_radio']").click( function(){
        	var textname = $(this).attr('name').replace("_radio","");
        	$("div." + textname + "_disabled input").attr("disabled","disabled");
        	$(this).parent().next().find('input').removeAttr('disabled');
        });
        
        // 默认保留第一个
        $(".first-checked").iCheck("check");
        
        // 刷新数据范围
        $(".refreshDataRange").on("ifChecked", function(){
        	if($(this).val() == 2){
        		$("#mergedData").removeAttr("disabled");
        		$("#mergedData").rules('add', {required:true});
        	}else{
        		$("#mergedData").attr("disabled",true);
        		$("#mergedData").rules('remove', "required");
        		$("#mergedData-error").remove();
        		$("#mergedDataDIV").removeClass("has-error");
        	}
        });
        
        // 日期插件
        $("#mergedData").daterangepicker({singleDatePicker:true,opens:"right",cancelClass:"btn-info",format:'YYYY/MM/DD',locale :{
            applyLabel: '<spring:message code="date.apply"/>',
            cancelLabel: '<spring:message code="date.cancel"/>',
            fromLabel: '<spring:message code="date.from"/>',
            toLabel: '<spring:message code="date.to"/>',
            weekLabel: 'W',
            customRangeLabel: '<spring:message code="date.customRange"/>',
            daysOfWeek: moment.weekdaysMin(),
            monthNames: moment.monthsShort(),
            firstDay: moment.localeData()._week.dow
        }});
	 });
	 
	 function cancel(){
		 window.location.href="${ctx}/advertiser/list";
	 };
	 
	 function submitForm(){
		 if($("#primaryForm").valid()){
			 $('#submitModal').modal('hide');
			 $('#shield').show();
			 $('#primaryForm').submit();
		 }
	 };
	</script>
</body>
</html>
