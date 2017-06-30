<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="pms" uri="http://i-click/authTag"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	
	<meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>
    	<c:if test="${action eq 'create' }"><spring:message code="opportunity.title.create"/></c:if>
        <c:if test="${action eq 'update' }"><spring:message code="opportunity.title.update"/></c:if>
    </title>


</head>

<body>
 <style>
 	.box{}
 	.box-noborder{border-top: 0}
 	.btn-100{width: 100px;line-height: 1.1}
 	.products{padding: 0 15px 0 15px;}
 	.content{padding-bottom: 0;min-height:0}
 	.select2ErrorClass{border: 1px solid red;}
 	ul li{list-style-type: none;}
    ul.ul-first{padding-left: 0px;}
 </style>
 <!-- Content Header -->
       <section class="content-header">
          <h1>
           	<c:if test="${action eq 'create' }"><spring:message code="opportunity.title.create"/></c:if>
       		<c:if test="${action eq 'update' }"><spring:message code="opportunity.title.update"/></c:if>
          </h1>
          <ol class="breadcrumb">
            <li><a href="${ctx}/businessOpportunity"><i class="fa fa-dashboard"></i> <spring:message code="opportunity.home" /></a></li>
            <li class="active">
            	<c:if test="${action eq 'create' }"><spring:message code="opportunity.title.create"/></c:if>
            	<c:if test="${action eq 'update' }"><spring:message code="opportunity.title.update"/></c:if>
            </li>
          </ol>
        </section>
          <!-- form start -->
          <form action="${ctx}/businessOpportunity/${action}" method="post" id="primaryForm" class="form-horizontal">
          <input type="hidden" name="id" id="id" value="${businessOpportunity.id}" />
        
           <!-- Main content -->
          <section class="content">
         
              <div class="box">
              
                <div class="box-body">
                   <div class="row">
	                   
		                   	<div class="col-md-6">
		                   	<c:if test="${action eq 'update' }">
		           				<div class="form-group">
			                      <label for="number" class="col-md-3 control-label"><spring:message code="opportunity.id" /></label>
			                      <div class="col-md-9">
			                      	<input type="text" disabled="disabled" class="form-control" value="${businessOpportunity.number }" />
			                      </div>
			                    </div>
			                </c:if>  
			                    <div class="form-group">
			                      <label for="name" class="col-md-3 control-label"><spring:message code="business.opportunity.name" /> *</label>
			                      <div class="col-md-9">
			                      	<input type="text" name="name" id="name" class="form-control" value="${businessOpportunity.name }" placeholder="<spring:message code='business.opportunity.input.name'/>">
			                      </div>
			                    </div>
			                </div>
		            </div>
		            <div class="row">
		             
           			
	                	<div class="col-md-6" >
		                    <div class="form-group">
		                      <label for="advertiser_id" class="col-md-3 control-label"><spring:message code="business.opportunity.advertiser" /> *</label>
		                      <div class="col-md-9" >
		                      	<select name="advertiser_id" class="form-control select2 advertiser_id" id="advertiser_id" style="width: 100%;">
	                      			<option value></option>
	                      			<c:forEach var="adv" items="${advertisers}">
	                      				<option value="${adv.id}" data-currency="${adv.tmp}">${adv.value}</option>
	                      			</c:forEach>
	                      		</select>
		                      </div>
		                    </div>
	                    </div>
	                    <div class="col-md-6" style="">
	                    	<div class="form-group">
	                      		<label class="col-md-12" style="padding-top: 7px; margin-bottom: 0"><a target="_blank" href="${ctx}/client/create"><spring:message code="business.opportunity.add.advertiser" /></a></label>
	                     	</div>
	                    </div>
	                </div>
	                
	                <div class="row">
	                <div class="col-md-6">
	                    <div class="form-group">
	                      <label for="deliver_date" class="col-md-3 control-label"><spring:message code="business.opportunity.deliver.date" /> *</label>
	                      <div class="col-md-9">
	                      	<div class="input-group">
	                      		<div class="input-group-addon"><i class="fa fa-calendar"></i></div>
	                      		<input type="text" value="${businessOpportunity.decodeDeliver_date }" class="form-control pull-right" name="deliver_date" id="deliver_date" placeholder="<spring:message code='business.opportunity.input.deliverdate' />">
	                      	</div>
	                      </div>
	                    </div>
	                    
	                    <div class="form-group">
	                      <label for="currency_id" class="col-md-3 control-label"><spring:message code="business.opportunity.currency" /> *</label>
	                      <div class="col-md-9">
	                      		<tags:selectbox name="currency_id" style="select2" list="${currencys}" addNull="true" value="${businessOpportunity.currency_id }"></tags:selectbox>
	                      </div>
	                    </div>
	                    
	                    <div class="form-group">
	                      <label for="budget" class="col-md-3 control-label"><spring:message code="business.opportunity.budget" /> *</label>
	                      <div class="col-md-9">
	                      	<div class="input-group">
	                      		<input type="text" value="${businessOpportunity.budget }"  data-mask data-inputmask="'alias': 'decimal', 'groupSeparator': ',', 'autoGroup': true, 'digits': 2" class="form-control text-right" name="budget_format" id="budget_format" placeholder="<spring:message code='business.opportunity.input.budget' />">
	                      		<div class="input-group-addon"><i class="fa fa-money"></i></div>
	                      	</div>
	                      </div>
	                    </div>
	                    
	                    <div class="form-group">
	                      <label for="progress" class="col-md-3 control-label"><spring:message code="business.opportunity.progress" /></label>
	                      <div class="col-md-9">
				          	<input id="progress" class="form-control" data-slider-id="blue" type="text" name="progress" value="${businessOpportunity.progress}">
				          </div>
	                    </div>
	                    
	                    <div class="form-group">
	                      <label for="progressRemarkPage" class="col-md-3 control-label"><spring:message code="business.opportunity.progress.remark" /></label>
	                      <div class="col-md-9">
	                       <c:if test="${action eq 'update' }">
	                        <a href="javascript:void(0);" onclick="showProgressRemarkList(this);"><spring:message code="business.opportunity.show.history.remark"/></a>
	                        <div style="display: none;" class="hide">
	                        ${businessOpportunity.progressRemarkList}
	                        </div>
	                        </c:if>
                            <textarea rows="3" class="form-control" name="progressRemarkPage" id="progressRemarkPage" placeholder="<spring:message code="business.opportunity.input.progress.remark" />"></textarea>
                          </div>
	                    </div>
	                    
                    </div>
                    
                  </div>
                </div><!-- /.box-body -->
                
              </div>
           
          </section>
          
          <!-- part2 -->
          <section class="content">
              <div class="box box-noborder">
                <div class="box-body">
                   <div class="row">
           			<div class="col-md-6">
	                    <div class="form-group">
	                    	<label for="exist_msa" class="col-md-3 control-label"><spring:message code="business.opportunity.msa" /> *</label>
	                     	<input type="hidden" name="exist_msa" id="exist_msa" value="${businessOpportunity.exist_msa}">
	                      	<div class="col-md-9">
	                      		<input type="button" class="btn <c:choose><c:when test="${businessOpportunity.exist_msa ==1 }">btn-primary</c:when><c:otherwise>btn-default</c:otherwise></c:choose>  btn-flat pull-left btn-sm btn-100" onclick="changeRadio(1,'exist_msa',this);" id="btn_exist_msa_1" value="<spring:message code="business.opportunity.yes" />">
	                      		<input type="button" class="btn <c:choose><c:when test="${businessOpportunity.exist_msa ==0 }">btn-primary</c:when><c:otherwise>btn-default</c:otherwise></c:choose>  btn-flat pull-left btn-sm btn-100" onclick="changeRadio(0,'exist_msa',this);" id="btn_exist_msa_0" value="<spring:message code="business.opportunity.no" />">
	                     	</div>
	                    </div>
	                    
	                    <div class="form-group">
	                    	<label for="exist_service" class="col-md-3 control-label"><spring:message code="business.opportunity.opportunity.type" /> *</label>
	                     	<input type="hidden" name="exist_service" id="exist_service" value="${businessOpportunity.exist_service}">
	                      	<div class="col-md-9">
	                      		<input type="button" class="btn <c:choose><c:when test="${businessOpportunity.exist_service ==1 }">btn-primary</c:when><c:otherwise>btn-default</c:otherwise></c:choose> btn-flat pull-left btn-sm btn-100" onclick="changeRadio(1,'exist_service',this);" value="<spring:message code="business.opportunity.service" />">
	                      		<input type="button" class="btn <c:choose><c:when test="${businessOpportunity.exist_service ==0 }">btn-primary</c:when><c:otherwise>btn-default</c:otherwise></c:choose> btn-flat pull-left btn-sm btn-100" onclick="changeRadio(0,'exist_service',this);" value="<spring:message code="business.opportunity.exec" />">
	                     	</div>
	                    </div>
	                    
	                    <div class="form-group">
	                      <label for="owner_sale" class="col-md-3 control-label"><spring:message code="business.opportunity.sale" /> *</label>
	                      <div class="col-md-9">
	                      	<select class="form-control select2" name="owner_sale" id="owner_sale" style="width: 100%;">
	                      		<option value="${owner_sale_user.id}" selected="selected">${owner_sale_user.name}(${owner_sale_user.agency })</option>
	                      	</select>
	                      </div>
	                    </div>
	                    
	                    <div class="form-group">
	                      <label for="cooperate_sales" class="col-md-3 control-label"><spring:message code="business.opportunity.cooperate" /></label>
	                      <div class="col-md-9">
	                      	<select class="form-control select2" name="cooperate_sales" id="cooperate_sales" style="width: 100%;" multiple="multiple">
	                      		<c:forEach items="${cooperate_sale_users }" var="user">
	                      		   <option value="${user.id}" selected="selected">${user.name}(${user.agency })</option>
	                      		</c:forEach>
	                      	</select>
	                      </div>
	                    </div>
	                    
                    </div>
                  </div>
                </div><!-- /.box-body -->
              </div>
          </section>
          
          <!-- part3 -->
          <section class="content">
              <div class="box box-noborder">
              	<div class="box-header with-border">
					<h3 class="box-title"><spring:message code="business.opportunity.setting" /></h3>
					<i class="fa fa-w fa-caret-down"></i>
				</div>
                <div class="box-body">
                    
                    <!--  -->
                    <div class="products" id="products">
                    	<jsp:include page="productEdit.jsp"></jsp:include>
                    </div>
                    
                   	<div class="row">
           			<div class="col-md-6">
           				<div class="form-group">
	                      <label class="col-md-3 control-label"><a href="javascript:void(0);" class="add_product_link" onclick="addProduct();"><i class="fa fa-w fa-plus-square"></i>&nbsp;<spring:message code="business.opportunity.addproduct" /></a></label>
	                    </div>
           			
	                    <div class="form-group">
	                      <label for="remark" class="col-md-3 control-label"><spring:message code="business.opportunity.remark" /></label>
	                      <div class="col-md-9">
	                      	<textarea rows="3" class="form-control" name="remark" id="remark" placeholder="<spring:message code="business.opportunity.input.remark" />">${businessOpportunity.remark}</textarea>
	                      </div>
	                    </div>
	                  
                    </div>
                  </div>
                </div><!-- /.box-body -->
                <div class="box-footer">
                	<div class="row">
	           			<div class="col-md-6" id="budgetSumError" style="display: none;">
		                	<div class="alert alert-error alert-dismissable" style="margin: 10px 0 0 0">
		                    <button type="button" class="close" onclick="$('#budgetSumError').hide();" aria-hidden="true">×</button>
			                   <spring:message code="business.opportunity.budget.sum.error" />
		            		</div>
	            		</div>
            		</div>
            		<br/>
                    <pms:auth equal1="${businessOpportunity.created_by}" equal2="${businessOpportunity.owner_sale}" array="${businessOpportunity.cooperate_sales}">
                    	<button type="button" class="btn btn-primary btn-sm btn-75" onclick="submitForm();" <c:if test="${businessOpportunity.status == 7 }">disabled="disabled"</c:if>><spring:message code="btn.save"/></button> 
                        <%-- <a href="javascript:void(0);" 
                            onclick="del(${businessOpportunity.id},'<spring:message code='message.confirm.del.opportunity' arguments='${businessOpportunity.name},${businessOpportunity.number}' argumentSeparator="," />');" 
                            class="btn btn-sm btn-danger btn-75 <c:if test="${businessOpportunity.status > 2}">disabled</c:if>">
                            <spring:message code="btn.delete"/>
                        </a> --%>
                    </pms:auth>
                    <button type="button" class="btn btn-primary disabled btn-sm btn-75" onclick="cancel();"><spring:message code="btn.cancel"/></button>
                </div>
              </div>
          </section>
          
          
         </form>
         
         <div id="shield" style="position: fixed; left: 0px; top: 0px; display: none; z-index: 9998; opacity: 0.8; background: #7D7159; width: 100%; height: 100%;">
		<img src="${ctx}/static/images/loading_s.gif" style="position: absolute; top: 300px; left: 48%;" /></div>
          
		<script>    
			$(document).ready(function() {
				
				$("[data-mask]").inputmask();
				
				$("#menu_business_opportunity").addClass("active");
				
				$("#deliver_date").daterangepicker({
					opens:"right",
					cancelClass:"btn-info",
					format:'YYYY/MM/DD',
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
	                endDate: moment().subtract(-30, 'days')
				}).on('change',function(){
	            	$("#primaryForm").validate().element(this);
	            });
				
				$("#btn_exist_service_${businessOpportunity.exist_service}").trigger('click');
				$("#btn_exist_msa_service_${businessOpportunity.exist_msa}").trigger('click');
				
				// 广告主
				$(".advertiser_id").select2({
					placeholder: "<spring:message code='opportunity.adv.name.placeholder' />",
					allowClear: true,
					language: lang_select2()
				}).on('change',function(evt){
					// 货币默认为广告主货币
					if('${action}' == 'create'){
						$("#currency_id").val($(this).find("option:selected").attr("data-currency"));
					}	
					validateSelect2(this);
				});
				
				// 销售
				$("#owner_sale").select2({
					ajax: {
                        url: '${ctx}/ajax/opportunity/users',
                        delay: 250,
                        dataType: 'json',
                        data: function (params) {
                            return {
                                q: params.term
                            };
                        },
                        processResults: function (data) {
                            return {results: data};
                        }
                    },
					placeholder: "<spring:message code='business.opportunity.input.sale' />",
					allowClear: true,
					minimumInputLength: 1,
					language: lang_select2()
				}).on('change',function(evt){
					validateSelect2(this);
				});
				
				// 合作销售
				$("#cooperate_sales").select2({
					ajax: {
                        url: '${ctx}/ajax/opportunity/users',
                        delay: 250,
                        dataType: 'json',
                        data: function (params) {
                            return {
                                q: params.term
                            };
                        },
                        processResults: function (data) {
                            return {results: data};
                        }
                    },
					 placeholder: "<spring:message code='business.opportunity.input.coopsale' />",
					 //allowClear: true,
					 minimumInputLength: 1,
					 language: lang_select2()
				}).on('change',function(evt){
					validateSelect2(this);
				});
				
				$("#primaryForm").validate({
					ignore: "",
					rules:{
						name:"required",
						advertiser_id:"required",
						deliver_date:"required",
						budget_format:{required:true,number:true},
						currency_id:"required",
						owner_sale:"required"
					},
					errorPlacement: function(error, element) {
						if (element.attr("class").indexOf("select2")!=-1) {
							  error.insertAfter( element.next() );
							  element.next().addClass("select2ErrorClass");
						} 
						else if (element.parent('.input-group').length || element.prop('type') === 'checkbox' || element.prop('type') === 'radio') {
				            error.insertAfter(element.parent());
				        } else {
				            error.insertAfter(element);
				        }
				    }
				});
				
				$("#progress").ionRangeSlider({
					min:0,
					max:100,
					force_edges: false,
					values:[0,10,30,50,70,90,100],
					from_fixed: false,
					prettify: function (num) { 
						if(num==0) return '<spring:message code="business.opportunity.status0" />'+": "+num+"%";
						if(num==10) return '<spring:message code="business.opportunity.status10" />'+": "+num+"%";
						if(num==30) return '<spring:message code="business.opportunity.status30" />'+": "+num+"%";
						if(num==50) return '<spring:message code="business.opportunity.status50" />'+": "+num+"%";
						if(num==70) return '<spring:message code="business.opportunity.status70" />'+": "+num+"%";
						if(num==90) return '<spring:message code="business.opportunity.status90" />'+": "+num+"%";
						if(num==100) return '<spring:message code="business.opportunity.status100" />'+": "+num+"%";
				    }
				});
				
				var selected_cooperate_sales = '${businessOpportunity.cooperate_sales}';
				if( selected_cooperate_sales != null && selected_cooperate_sales != ''){
					var array = selected_cooperate_sales.split(',');
					for( var i in array ){
						 $("#cooperate_sales").find("option[value='" + array[i] + "']").attr('selected',true);
					}
					$("#cooperate_sales").change();
				};
				
				<c:if test="${action eq 'create' }">
					addProduct();
				</c:if>
				<c:if test="${action eq 'update' }">
					$("select[name$='.product_category_id']").change();
					$("#advertiser_id").val("${businessOpportunity.advertiser_id}").trigger("change");
					<c:if test="${businessOpportunity.progress eq 100 }">
					$("#advertiser_id").attr("disabled","disabled");
					</c:if>
				</c:if>
			});
			
			function disabled_from(disabled){
				if(disabled){
					$("#primaryForm input[type='text'].form-control, #primaryForm select.form-control, #primaryForm textarea.form-control").attr('disabled',true);
					$("#primaryForm input[type='button'].btn-100").attr('disabled',true);
					$("#primaryForm a.rm_product_link, #primaryForm a.add_product_link").remove();
				}else{
					$("#primaryForm input[type='text'].form-control, #primaryForm select.form-control, #primaryForm textarea.form-control").attr('disabled',false);
					$("#primaryForm input[type='button'].btn-100").attr('disabled',false);
				}
			};
			
			function cancel(){
				window.location.href='${ctx}/businessOpportunity';
			};
			
			function changeRadio(val,id,name){
				$('#'+id).val(val);
				$(name).parent().children().each(function(){
					$(this).removeClass("btn-primary").addClass("btn-default");
				});
				$(name).removeClass("btn-default").addClass("btn-primary");
			};
			
			function removeProduct(index){
				var rm_id = $('#row_product_' + index + ' input[name="businessOpportunityProducts['+index+'].id"]').val();
				if(rm_id != null && rm_id != undefined){
					$("#primaryForm").append('<input type="hidden" name="deleteProductIds" value="' + rm_id + '" />');
				}
				$('#row_product_' + index).remove()
			};
			
			function loadProducts( dom, index, selectedVal ){
				if($("#primaryForm").validate().element(dom)){
					$(dom).next().removeClass('select2ErrorClass');
				}else{
					$(dom).next().addClass('select2ErrorClass');
				}
				var productType = $(dom).val();
				if(productType == null || productType == undefined || productType == '' ){
					$("select[name='businessOpportunityProducts["+index+"].product_id']").html('<option value></option>');
					$("select[name='businessOpportunityProducts["+index+"].product_id']").trigger("change");
				}else{
					$.get("${ctx}/ajax/getProducts",{pt:productType,sv:selectedVal},function(html){
						$("select[name='businessOpportunityProducts["+index+"].product_id']").html(html);
						$("select[name='businessOpportunityProducts["+index+"].product_id']").trigger("change");
					},"html");
					
				}
			};
			
			function addProduct(){
				var productIndex = $(".products").children(".row").length;
				$.post("${ctx}/ajax/addProduct",{index:productIndex},function(html){
					$(".products").append(html);
				},"html");
			};
			
			function submitForm(){
				// 非MSA商机，必须填写一个产品
				add_product_rule();
				// 初始化隐藏错误提示
				$("#budgetSumError").hide()
				var exist_service = $("#exist_service").val();
				// 预算合计
				var budget = Number($("#budget_format").val().split(',').join(''));
				// 产品预算
				var subBudget = 0;
				// 错误计数器
				var flag = 0;
				// 需要验证预算总计和产品预计是否相等
				if( exist_service == '0'){
					// 累加
					$("input[name*='.budget_format']").each(function(i){
						subBudget += Number($(this).val().split(',').join(''));
					});
					// 判断是否相等
					if(budget!=0 && subBudget!=budget){
						$("#budgetSumError").show();
						flag++;
					}
				} 
				// 可填可不填
				else {
					remove_product_rule();
					$("div[id^='row_product']").each(function(i){
						var p1 = $(this).find("select[name='businessOpportunityProducts["+i+"].product_category_id']").val();
						var p2 =  $(this).find("select[name='businessOpportunityProducts["+i+"].product_id']").val();
						var p3 =  $(this).find("select[name='businessOpportunityProducts["+i+"].sale_mode']").val();
						var p4 = $(this).find("input[name='businessOpportunityProducts["+i+"].budget_format']").val();
						if( p1 != '' && p2 != '' && p3 != '' && p4 != '' ){
							// 累加
							$("input[name*='.budget_format']").each(function(i){
								subBudget += Number($(this).val().split(',').join(''));
							});
							// 判断是否相等
							if(budget!=0 && subBudget!=budget){
								$("#budgetSumError").show();
								flag++;
							}
						}
					});	
				}
				
				if(!$("#primaryForm").valid()){
					flag++;
				}
				
				if( flag == 0 ){
					$("#budgetSumError").hide();
					$("#shield").show();
					disabled_from(false);
					$("#primaryForm").submit();
				}
			};

			
			function add_product_rule(){
				// 非MSA商机，必须填写一个产品
				$("div[id^='row_product']").each(function(i){
				    if($(this).find("select[name='businessOpportunityProducts["+i+"].product_category_id']").length > 0){
                        $(this).find("select[name='businessOpportunityProducts["+i+"].product_category_id']").rules( "add", {required:true});
                        $(this).find("select[name='businessOpportunityProducts["+i+"].product_id']").rules( "add", {required:true});
                        $(this).find("select[name='businessOpportunityProducts["+i+"].sale_mode']").rules( "add", {required:true});
                        $(this).find("input[name='businessOpportunityProducts["+i+"].budget_format']").rules( "add", {required:true,number:true});
					}
				});	
			};
			
			function remove_product_rule(){
				// 非MSA商机，必须填写一个产品
				$("div[id^='row_product']").each(function(i){
					$(this).find("select[name='businessOpportunityProducts["+i+"].product_category_id']").rules( "remove", "required");
					$(this).find("select[name='businessOpportunityProducts["+i+"].product_id']").rules( "remove", "required");
					$(this).find("select[name='businessOpportunityProducts["+i+"].sale_mode']").rules( "remove", "required");
					$(this).find("input[name='businessOpportunityProducts["+i+"].budget_format']").rules( "remove", "required");
					$(this).find("input[name='businessOpportunityProducts["+i+"].budget_format']").rules( "remove", "number");
				});	
			};
			
			function del(id,msg){
                bootbox.dialog({
                    message: msg,
                    title: "<spring:message code='opportunity.title.delete' />" ,
                    buttons: {
                        cancel: {
                          label: "<spring:message code='btn.cancel' />",
                          className: "btn-75 btn-default",
                          callback: function() {
                            
                          }
                        },
                        success: {
                          label: "<spring:message code='btn.delete' />",
                          className: "btn-75 btn-danger",
                          callback: function() {
                            window.location.href="${ctx}/businessOpportunity/delete/"+id;
                          }
                        }
                    }
                });
            };
			
			function validateSelect2(name){
				if($("#primaryForm").validate().element( name )){
					$(name).next().removeClass('select2ErrorClass');
				}else{
					$(name).next().addClass('select2ErrorClass');
				}
			};
			
			function showProgressRemarkList(name){
				if($(name).next().hasClass('hide')){
					$(name).next().show();
					$(name).next().removeClass('hide');
				}else{
					$(name).next().hide();
                    $(name).next().addClass('hide');
				}
			};
		</script>
</body>
</html>
 



