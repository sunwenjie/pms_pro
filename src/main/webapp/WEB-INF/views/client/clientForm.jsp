<%@page import="org.springframework.web.servlet.support.RequestContextUtils"%>
<%@page import="org.springframework.web.servlet.LocaleResolver"%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<%
LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver (request);
String lang =localeResolver.resolveLocale(request).getLanguage();
request.setAttribute("lang", lang);
%>
<html>
<head>
	
	<meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">
    
    <title>
    	<c:if test="${action eq 'create' }"><spring:message code="client.title.create" /></c:if>
        <c:if test="${action eq 'update' }"><spring:message code="client.title.edit" /></c:if>
    </title>

</head>



<body>

<style>
	.box{}
	.box-noborder{border-top: 0}
	.btn-100{width: 100px;line-height: 1.1}
	.client_contacts{padding: 0 15px 0 15px;}
	.content{padding-bottom: 0;min-height:0}
	.select2ErrorClass{border: 1px solid #dd4b39;box-shadow: none}
</style>

<div class="modal fade" id="myModal" tabindex="-1" role="dialog" 
   aria-labelledby="myModalLabel" aria-hidden="true">
   <div class="modal-dialog">
      <div class="modal-content">
         
      </div><!-- /.modal-content -->
   </div><!-- /.modal-dialog -->
</div><!-- /.modal -->       

<!-- Content Header -->
<section class="content-header">
	<h1>
       	<c:if test="${action eq 'create' }"><spring:message code="client.title.create" /></c:if>
   		<c:if test="${action eq 'update' }"><spring:message code="client.title.edit" /></c:if>
    </h1>	
    <ol class="breadcrumb">
    <c:if test="${action ne 'create' }">
    	<li>
    		<button class="btn btn-sm btn-${client.buttonStyle}" data-client_id="${client.id}" id="client_popover_${client.id}" 
		     onclick="showModal('${ctx}/ajax/client/approval/modal/${client.id}')"><spring:message code="btn.approval" /></button>
    	</li>
    </c:if>	
    	<li><a href="${ctx}/businessOpportunity"><i class="fa fa-dashboard"></i> <spring:message code="opportunity.home" /></a></li>
        <li><a href="${ctx}/client"><spring:message code="menu.advertiser" /></a></li>
        <li class="active">
        	<c:if test="${action eq 'create' }"><spring:message code="navigat.new" /></c:if>
   			<c:if test="${action eq 'update' }"><spring:message code="navigat.edit" /></c:if>
        </li>
    </ol>
    
    
    
</section> 

<!-- form -->
<form role="form" action="${ctx}/client/${action}" method="post" id="primaryForm" class="form-horizontal">
<input type="hidden" name="id" id="id" value="${client.id}" />

		<!-- Main content -->
		<section class="content">
     		 <div class="box box-info">
      			 <div class="box-body">
      			 	<div class="row">
					 	<div class="col-md-6">
					 	
					 	<c:if test="${action eq 'update' }">
					 		<div class="form-group">
								<label for="number" class="col-md-3 control-label"><spring:message code="client.number" /></label>
			                    <div class="col-md-9">
			                      	<input class="form-control" disabled="disabled" type="text" value="${client.number}" />
			                    </div>
							</div>
						</c:if>	
						
							<div class="form-group">
								<label for="clientname" class="col-md-3 control-label"><spring:message code="client.name" /><em> *</em></label>
			                    <div class="col-md-9">
			                      	<input id="clientname" class="form-control" type="text" name="clientname" value="${client.clientname}" 
			                      	placeholder="<spring:message code="client.name.remark" />"/>
			                    </div>
							</div>
						
	                		<div class="form-group">
	                			<label for="brand" class="col-md-3 control-label"><spring:message code="client.brand.name" /><em> *</em></label>
		                      	<div class="col-md-9">
		                      		<input id="brand" class="form-control" type="text" name="brand" value="${client.brand}"
		                      		placeholder="<spring:message code="client.brand.remark" />"/>
		                      	</div>
	                		</div>
		                	
	                	  </div>	
		             </div>
		             <div class="row">
		                  <div class="col-md-6">
		                      <div class="form-group">
                                <label for="channel" class="col-md-3 control-label"><spring:message code="client.channel" /><em> *</em></label>
                                <div class="col-md-9 ">
                                    <div class="input-group channel-parent-append-error">
                                        <label class="form-label input-group-addon">
                                            <c:if test="${client.whether_channel == 1}">
                                                <input type="radio" name="whether_channel" checked="checked" class="whether_channel_yes" value="1" />
                                            </c:if>
                                            <c:if test="${client.whether_channel != 1}">
                                                <input type="radio" name="whether_channel" class="whether_channel_yes" value="1" />
                                            </c:if>
                                            &nbsp;<spring:message code="client.channel.yes" />
                                        </label>
                                        <div class="channel-parent-has-error">
                                            <select class="form-control select2 channel" name="channel" disabled="disabled" id="channel" style="width: 100%; display: none;">
                                                <option value></option>
                                                <c:forEach var="agency" items="${agencys}">
                                                    <option value="${agency.id}">${agency.channel_name}</option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                        
                                        <label class="form-label input-group-addon">
                                            <c:if test="${client.whether_channel == 1}">
                                                <input type="radio" name="whether_channel" class="whether_channel_no" value="0" />
                                            </c:if>
                                            <c:if test="${client.whether_channel != 1}">
                                                <input type="radio" name="whether_channel" checked="checked" class="whether_channel_no" value="0" />
                                            </c:if>
                                            &nbsp;<spring:message code="client.channel.no" />
                                        </label>
                                    </div>
                                </div>
                            </div>  
		                  </div>
		                  
		                  <div class="col-md-6">
		                      <div class="form-group">
		                        <c:if test="${zh eq lang}">
                                    <label class="col-md-12" style="padding-top: 0px; margin-bottom: 0"><a href="http://sales.optimix.asia/zh-cn/channels" target="_blank"><spring:message code="business.opportunity.add.agency" /></a><br><spring:message code="business.opportunity.add.agency2" /></label>
                                </c:if>
                                <c:if test="${zh ne lang}">
                                    <label class="col-md-12" style="padding-top: 0px; margin-bottom: 0"><a href="http://sales.optimix.asia/en/channels" target="_blank"><spring:message code="business.opportunity.add.agency" /></a><br><spring:message code="business.opportunity.add.agency2" /></label>
                                </c:if>
                              </div>
		                  </div>
		             
		             </div>
		             
		             <div class="row">
                          <div class="col-md-6">
                                <div class="form-group">
                                <label for="industry_id" class="col-md-3 control-label"><spring:message code="client.industry" /><em> *</em></label>
                                <div class="col-md-9">
                                    <select name="industry_id" class="form-control select2 industry_id" id="industry_id" style="width: 100%;">
                                        <option value></option>
                                        <c:forEach var="it" items="${industryTypes}">
                                            <option value="${it.id}">${it.value}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>
                            
                            <div class="form-group">
                                <label for="currency_id" class="col-md-3 control-label"><spring:message code="client.currency" /><em> *</em></label>
                                <div class="col-md-9">
                                    <tags:selectbox name="currency_id" list="${currencyTypes}" value="${client.currency_id }" addNull="true"  />
                                </div>
                            </div>
                          </div>
                     </div>
		             
		             
      			</div><!-- /.box-body -->
     		 </div><!-- /.box-info -->
		 </section><!--  /Main content -->  
		
		 <!-- part2 -->
		 <section class="content">
		 		<div class="box box-solid">
		 				<div class="box-header with-border">
							<h3 class="box-title"><spring:message code="client.contact.setting" /></h3>
							<i class="fa fa-w fa-caret-down"></i>
						</div>
		 				<div class="box-body">
					 			<div class="client_contacts" id="client_contacts">
			                    	<jsp:include page="include/clientContactEdit.jsp"></jsp:include>
			                    </div>
		 						
		 						<div class="row">
				           			<div class="col-md-6">
				           				<div class="form-group">
					                      	<label class="col-md-3 control-label"><a href="javascript:void(0);" onclick="add_client_contacts();">
					                      		<i class="fa fa-w fa-plus-square"></i>&nbsp;<spring:message code="client.contact.add" /></a>
					                      	</label>
					                    </div>
				                    </div>
				                </div>
		 				</div><!-- /.box-body -->
		 		</div><!-- /.box-noborder -->
		 </section><!-- /part2 -->
		 
		 
		 <!-- part3 -->
		 <section class="content">
		 		<div class="box box-noborder">
		 				<div class="box-body">
		 						<div class="row">
		 						
		 							<div class="col-md-6">
		 								<div class="form-group">
											<label for="address" class="col-md-3 control-label"><spring:message code="client.contact.address" /></label>
											<div class="col-md-9">
					                      		<input id="address" class="form-control" type="text" name="address" value="${client.address}" />
					                      	</div>
										</div>
		                
					                	<div class="form-group">
											<label for="name" class="col-md-3 control-label"><spring:message code="client.contact.salesperson" /></label>
						                    <div class="col-md-9">
						                    	<select class="form-control saleIds" name="saleIds" id="saleIds" multiple="multiple" style="width:100%;">
					                      			<c:forEach var="user" items="${users}">
					                      				<option value="${user.id}">${user.name}&nbsp;(${user.agency})</option>
					                      			</c:forEach>
						                      	</select>
						                    </div>
										</div>
										
										<div class="form-group">
											<label for="name" class="col-md-3 control-label"><spring:message code="client.cross.regional" /></label>
						                    <div class="col-md-9">
						                    		<input type="hidden" name="whether_cross_district" id="whether_cross_district" value="${client.whether_cross_district}">
						                    		<input type="button" class="btn tooltip-toggle <c:choose><c:when test="${client.whether_cross_district == 1 }">btn-primary</c:when><c:otherwise>btn-default</c:otherwise></c:choose>  btn-flat pull-left btn-sm btn-100" onclick="changeRadio(1,'whether_cross_district',this);" id="btn_whether_cross_district_1" 
						                    		value="<spring:message code="client.channel.yes" />" data-toggle="tooltip" data-placement="bottom" title="<spring:message code="client.cross.regional.remark" />" />
	                      							<input type="button" class="btn <c:choose><c:when test="${client.whether_cross_district != 1 }">btn-primary</c:when><c:otherwise>btn-default</c:otherwise></c:choose>  btn-flat pull-left btn-sm btn-100" onclick="changeRadio(0,'whether_cross_district',this);" id="btn_whether_cross_district_0" 
	                      							value="<spring:message code="client.channel.no" />">
						                    </div>
										</div>
					                
					                </div>
		 						
		 						</div><!-- /.row -->
		 				</div><!-- /.box-body -->
		 				
		      		 	<div class="box-footer">
				          	 <button type="submit" class="btn btn-primary btn-sm btn-65" onclick="submitForm();"><spring:message code="btn.save"/></button>
				          	 <button type="button" class="btn btn-primary btn-sm btn-65 disabled" onclick="window.location.href='${ctx}/client'"><spring:message code="btn.cancel"/></button>
				    	</div>
		 		</div><!-- /.box-noborder -->
		 </section><!-- /part3 -->
		 

</form><!-- /form -->

<div id="shield" style="position: fixed; left: 0px; top: 0px; display: none; z-index: 9998; opacity: 0.8; background: #7D7159; width: 100%; height: 100%;">
		<img src="${ctx}/static/images/loading_s.gif" style="position: absolute; top: 300px; left: 48%;" /></div>

          
<script>    
	$(document).ready(function() {
		$("#menu_client_Module").addClass("active");
		$("#menu_client").addClass("active");  

		if('${client.whether_channel}' == 1){
			$('#channel').attr('disabled',false);
		}
		
		$("#primaryForm").validate({
			ignore: "",
			rules:{
				clientname: "required",
				brand: "required",
				industry_id: "required",
				currency_id: "required",
				channel: "validate_channel"
			},
			errorPlacement: function(error, element) {
				if( element.hasClass("select2") ){
					if( element.hasClass("channel") ){
						error.insertAfter($('.channel-parent-append-error'));
					}else{
						error.insertAfter( element.next() );
						element.next().addClass("select2ErrorClass");
					}
				}else{
					error.insertAfter(element);
				} 
			} 
		});
		// 如果是修改.  首次加载. 那么不要赋值agency联系人
		var firstLoad = true;
		// 代理下单，代理
		$("#channel").select2({
		    placeholder: '<spring:message code="public.please.select" />',
		    allowClear: true,
		    language: lang_select2()
		}).on('change',function(evt){
			$("#primaryForm").validate().element(this);
			var isUpdate = "${action eq 'update'}";
			if($(this).val()!='' && isUpdate && !firstLoad){
				$.post("${ctx}/client/channel/"+$(this).val(),{},function(data){
					if(data!=''){
						$("#contact_person").val(data.contact_person);
						$("#position").val(data.position);
						$("#phone").val(data.phone);
						$("#email").val(data.email);
					}
				},"json");
			}
			firstLoad = false;
		});
		
		$("#saleIds").select2({
		    placeholder: "<spring:message code='business.opportunity.input.coopsale' />",
		    language: lang_select2()
		});
		
      	$("#industry_id").select2({
      		placeholder: '<spring:message code="client.industry.remark" />',
      		allowClear: true,
      		language: lang_select2()
      	}).on('change',function(evt){
      		validateSelect2(this);
		});
      	
		$('#whether_cross_district_cbk').click( function(){
			if($(this).is(':checked')){
				$('#whether_cross_district').val(1);
			}else{
				$('#whether_cross_district').val(0);
			}
		});
		
		// 单选框触发验证
		$('.whether_channel_yes').click( function(){
			$("#channel").attr("disabled",false);
			$("#primaryForm").validate().element($('#channel'));
		});
		
		$('.whether_channel_no').click( function(){
			$("#channel").attr("disabled",true);
			$("#channel").select2("val", "");
			$("#primaryForm").validate().element($('#channel'));
		});
		
		// 验证代理下单
		jQuery.validator.addMethod("validate_channel", function(value, element){
			$(".channel-parent-has-error").removeClass("select2ErrorClass");
			if( $('.whether_channel_yes').is(":checked") ){
				if( value == null || value == '' ){
					$(".channel-parent-has-error").addClass("select2ErrorClass");
					return false;
				}
			}
			return true;
		}, "<spring:message code='client.channel.remark' />");
		
		<c:if test="${action eq 'create' }">add_client_contacts();</c:if>
		<c:if test="${action eq 'update' }">
			bind_hover();
			bind_remove();
			$("#industry_id").val("${client.industry_id}").trigger("change");
			$("#channel").val("${client.channel}").trigger("change");
		</c:if>
		var selected_saleIds = '${client.saleIds}';
		if( selected_saleIds != null && selected_saleIds != ''){
			var array = selected_saleIds.split(',');
			for( var i in array ){
				 $("#saleIds").find("option[value='" + array[i] + "']").attr('selected',true);
			}
			$("#saleIds").change();
		};
		
		// 动态验证广告主联系人
		$("div[id^='client_contact_']").each(function(i){
			$(this).find("input[name='contacts["+i+"].contact_person']").rules('add',{required:true});
			$(this).find("input[name='contacts["+i+"].position']").rules('add',{required:true});
			$(this).find("input[name='contacts["+i+"].phone']").rules('add',{required:true,maxlength:20});
			$(this).find("input[name='contacts["+i+"].email']").rules('add',{required:true,email:true});
		});
	});
	
	function changeRadio(val,id,name){
		$('#'+id).val(val);
		$(name).parent().children().each(function(){
			$(this).removeClass("btn-primary").addClass("btn-default");
		});
		$(name).removeClass("btn-default").addClass("btn-primary");
	};
	
	// 再添加一个联系人
	function add_client_contacts(){
		var size = $("#client_contacts div.contacts").size();
		var index = -1;
		if(size != 0 ){
			index = $("#client_contacts div.contacts").last().attr("id").replace("client_contact_","");
		}
		$.post("${ctx}/ajax/addContact",{index:Number(index)+1},function(html){
			$("#client_contacts").append(html);
			bind_hover();
			bind_remove();
		},"html");
	};
	
	// 删除联系人
	function bind_remove(){
		$("label[id^='rm-contact-']").click( function(){
				var index = $(this).attr('id').split('-')[2];
				var rm_id = $('#client_contact_' + index + ' .contact_id').val();
				if(rm_id != null && rm_id != undefined){
					$("#primaryForm").append('<input type="hidden" name="deleteContactIds" value="' + rm_id + '" />');
				}
				var contact_size = $("input[name$='.contact_person']").size();
				if( contact_size == 1){
					$("input[name='contacts["+index+"].contact_person']").val(null);
					$("input[name='contacts["+index+"].position']").val(null);
					$("input[name='contacts["+index+"].phone']").val(null);
					$("input[name='contacts["+index+"].email']").val(null);
				}else{
					$('#client_contact_' + index).remove();
				}
		});
	};
	
	// 删除联系人提示
	function bind_hover(){
		$("label[id^='rm-contact-']").mouseover( function(){
			var index = $(this).attr('id').split('-')[2];
			$("#client_contact_" + index +" input").css('border-color','red');
		});
		
		$("label[id^='rm-contact-']").mouseout( function(){
			var index = $(this).attr('id').split('-')[2];
			$("#client_contact_" + index +" input").css('border-color','#d2d6de');
		});
	};
	
	function showModal( url ){
		$.ajax({
			url: url,
			type: 'GET',
			async : false,
			dataType: 'html',
			success: function(html){
				 $('#myModal .modal-content').html(html)
			}
		});
		
		$("#myModal").modal({
			keyboard: true
		});
	};
	
	function validateSelect2(name){
		if($("#primaryForm").validate().element( name )){
			$(name).next().removeClass('select2ErrorClass');
		}else{
			$(name).next().addClass('select2ErrorClass');
		}
	};
	
	function submitForm(){
		 if($("#primaryForm").valid()){
			 $('#submitModal').modal('hide');
			 $('#shield').show();
			 $('#primaryForm').submit();
		 }
	 };
	 
	 function addClient(lang){
		 if(lang == "zh"){
			 window.location.href="http://sales.optimix.asia/zh-cn/channels";
		 }else{
			 window.location.href="http://sales.optimix.asia/en/channels";
		 }
	 };
</script>
</body>
</html>