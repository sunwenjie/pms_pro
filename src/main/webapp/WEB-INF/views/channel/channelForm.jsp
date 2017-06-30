<%@page import="org.springframework.web.servlet.support.RequestContextUtils"%>
<%@page import="org.springframework.web.servlet.LocaleResolver"%>
<%@page import="org.apache.shiro.SecurityUtils"%>
<%@page import="com.asgab.service.account.ShiroDbRealm.ShiroUser"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="pms" uri="http://i-click/authTag"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<%
	ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
	LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver (request);
	request.setAttribute("lang",localeResolver.resolveLocale(request).getLanguage());
	request.setAttribute("country", localeResolver.resolveLocale(request).getCountry());
%>
<html>
<head>
<title><spring:message code="menu.agency"/></title>
</head>
<body>
	<style type="text/css">
		.btn-sm{line-height: 1.1;}
		.btn-50{width: 50px;}
		.dateclear {
		    position: absolute;
		    right: 7px;
		    top: 16px;
		    margin: auto;
		    line-height:0px;
		    cursor: pointer;
		    font-weight: bold;
		    z-index: 3;
		}
		ul li{list-style-type: none;}
        ul.ul-first{padding-left: 0px;}
	</style>
	 <!-- Content Header -->
       <section class="content-header">
          <h1>
            <spring:message code="menu.agency"/>
          </h1>
          
          <ol class="breadcrumb">
            <li><a href="${ctx}/businessOpportunity"><i class="fa fa-dashboard"></i> <spring:message code="opportunity.home" /></a></li>
            <li class="active"><spring:message code="menu.agency"/></li>
          </ol>
		
		<c:if test="${message != null}">
			<div class="alert alert-success alert-dismissable" style="margin: 10px 0 0 0">
                    <button type="button" class="close" data-dismiss="alert" aria-hidden="true">×</button>
                    <h4>	<i class="icon fa fa-check"></i> <spring:message code="message.alert.success"/>!</h4>
                    <c:if test="${successId != null }">
                   	 &lt;${successId}&gt;
                    </c:if>
                    ${message}
            </div>
        </c:if>
        
        <c:if test="${message_del != null}">
            <div class="alert ${alert_type} alert-dismissable" style="margin: 10px 0 0 0; ${success == true ? 'background-color: green;' : 'background-color: red;'}">
                    <button type="button" class="close" data-dismiss="alert" aria-hidden="true">×</button>
                    <c:if test="${success == true}">
                         <h4><i class="icon fa fa-check"></i> <spring:message code="message.alert.success"/>!</h4>
                    </c:if>
                    <c:if test="${success != true}">
                         <h4><i class="icon fa fa-close"></i> <spring:message code="message.alert.fail"/>!</h4>
                    </c:if>
                   
                    ${message_del}
                    <c:if test="${orderMessage != null}"><br/>${orderMessage}</c:if>
            </div>
        </c:if>
		          
        </section>

          <!-- form start -->
          <form action="${ctx}/channel/save" method="post" id="channelForm" class="form-horizontal">
	          <input type="hidden" name="id" id="id" value="${channel.id}" />
	          <!-- Main content -->
	          <section class="content">
              	<div class="box">
	                <div class="box-body">
	                	<div class="row">
		                   	<div class="col-md-6">
			                    <c:if test="${not empty channel.id}">
		           				<div class="form-group">
			                      <label for="number" class="col-md-3 control-label"><spring:message code="channel.id" /></label>
			                      <div class="col-md-9">
			                      	<input type="text" disabled="disabled" class="form-control" value="AG<fmt:formatNumber value="${channel.id}" pattern="000000000"/>">
			                      </div>
			                    </div>
			                    </c:if>
			                  
			                    <div class="form-group">
			                      <label for="channel_name" class="col-md-3 control-label"><spring:message code="channel.name" /> *</label>
			                      <div class="col-md-9">
			                      	<input type="text" name="channel_name" id="channel_name" class="form-control" value="${channel.channel_name}" placeholder="<spring:message code="channel.name.placehold" />">
			                      </div>
			                    </div>
			                    
			                    <div class="form-group">
			                      <label for="qualification_name" class="col-md-3 control-label"><spring:message code="channel.qualification" /> *</label>
			                      <div class="col-md-9">
			                      	<input type="text" name="qualification_name" id="qualification_name" class="form-control" value="${channel.qualification_name}" placeholder="<spring:message code="channel.qualification.placehold" />">
			                      </div>
			                    </div>
			                    
			                    <div class="form-group">
			                      <label for="currency_id" class="col-md-3 control-label"><spring:message code="channel.currency" /> *</label>
			                      <div class="col-md-9">
			                      		<tags:selectbox name="currency_id" style="select2" list="${currencys}" addNull="true" value="${channel.currency_id }"></tags:selectbox>
			                      </div>
			                    </div>
			                    
			                    <div class="form-group">
			                      <label for="contact_person" class="col-md-3 control-label"><spring:message code="channel.contact.name" /> *</label>
			                      <div class="col-md-9">
			                      	<input type="text" name="contact_person" id="contact_person" class="form-control" value="${channel.contact_person}" placeholder="<spring:message code="channel.contact.name.placehold" />">
			                      </div>
			                    </div>
			                    
			                    <div class="form-group">
			                      <label for="position" class="col-md-3 control-label"><spring:message code="channel.contact.position" /> *</label>
			                      <div class="col-md-9">
			                      	<input type="text" name="position" id="position" class="form-control" value="${channel.position}" placeholder="<spring:message code="channel.contact.position.placehold" />">
			                      </div>
			                    </div>
			                    
			                    <div class="form-group">
			                      <label for="phone" class="col-md-3 control-label"><spring:message code="channel.contact.number" /> *</label>
			                      <div class="col-md-9">
			                      	<input type="text" name="phone" id="phone" class="form-control" value="${channel.phone}" placeholder="<spring:message code="channel.contact.number.placehold" />">
			                      </div>
			                    </div>
			                    
			                    <div class="form-group">
			                      <label for="email" class="col-md-3 control-label"><spring:message code="channel.email" /> *</label>
			                      <div class="col-md-9">
			                      	<input type="text" name="email" id="email" class="form-control" value="${channel.email}" placeholder="<spring:message code="channel.email.placehold" />">
			                      </div>
			                    </div>
			                    
			                    <div class="form-group">
			                      <label for="company_adress" class="col-md-3 control-label"><spring:message code="channel.company" /> *</label>
			                      <div class="col-md-9">
			                      	<input type="text" name="company_adress" id="company_adress" class="form-control" value="${channel.company_adress}" placeholder="<spring:message code="channel.company.placehold" />">
			                      </div>
			                    </div>
			                    
			                    <div class="form-group">
			                      <label for="rebates" class="col-md-3 control-label"><spring:message code="channel.rebate" /> *</label>
			                      <div class="col-md-9" id="rebate_div">
			                        <c:forEach items="${channelRebates}" varStatus="stat" var="rebate">
			                      	<div class="input-group">
			                      	  <%-- <c:if test="${(empty rebate.start_date) and (empty rebate.start_date)}">
                      				  	<input type="text" value="" pattern="yyyy-MM-dd"/>" class="form-control pull-right" name="deliver_date[${stat.index}]" id="deliver_date">
			                      	  </c:if>
			                      	  <c:if test="${((empty rebate.start_date) and (empty rebate.start_date)) == false}">
                      				  	<input type="text" value="<fmt:formatDate value="${rebate.start_date}" pattern="yyyy-MM-dd"/> ~ <fmt:formatDate value="${rebate.end_date}" pattern="yyyy-MM-dd"/>" class="form-control pull-right" name="deliver_date[${stat.index}]" id="deliver_date">
			                      	  </c:if> --%>
			                      	  <div class="input-group-addon">
			                      	  <c:choose>
			                      	  	<c:when test="${(empty rebate.start_date) and (empty rebate.start_date)}">
                      				  		<input type="text" value="" class="form-control pull-right deliver_date" name="deliver_date" style="width:200px;">
			                      	  	</c:when>
			                      	  	<c:otherwise>
                      				  		<input type="text" value="<fmt:formatDate value="${rebate.start_date}" pattern="yyyy-MM-dd"/> - <fmt:formatDate value="${rebate.end_date}" pattern="yyyy-MM-dd"/>" class="form-control pull-right deliver_date" name="deliver_date" style="width:200px;">
			                      	  	</c:otherwise>
			                      	  </c:choose></div>
							          <div class="input-group-addon"><i class="fa fa-calendar"></i></div>
							          <!-- <input type="text" value="  " name="nonuniform_date_range" id="nonuniforms_appendedInput" class="span2 required_input xmoCalendarInputSchedule proof_ready_unmodify">
							          <span class="add-on"><i class="icon-calendar icon_trigger r_margin_0"></i></span> -->
			                      	  <div class="input-group-addon">
							          <input class="align_right numeric rebate_radio proof_ready_unmodify" id="nonuniform_rebate" name="nonuniform_rebate" type="text" value="${rebate.rebate}" style="width:60px;" onkeyup="value=value.replace(/[^\d\.]/g,'')">
							          <span class="add-on currency_unit_budget" id="nonuniform_rebate_unit" name="nonuniform_rebate_unit">%</span>
							          </div>
						              <span class="delete_one_rebate" style="display: flex;margin: 5px 0;">
						              	<a href="javascript:void(0);" class="rm_product_link" onclick="delete_one_rebate(this)" style="margin-left: 10px;margin-top: 10px;color: red;"><i class="fa fa-w fa-minus-square"></i><%-- &nbsp;<spring:message code="business.opportunity.product.remove" /> --%></a>
						              </span>
								    </div>
								    </c:forEach>
			                      </div>
							      <div>
							    	<label class="col-md-3 control-label" style="margin-left: 70px;"><a href="javascript:void(0);" class="add_product_link" onclick="addRebate();"><i class="fa fa-w fa-plus-square"></i>&nbsp;Add</a></label>
							      </div>
			                    </div>
	                    
			                    <div class="form-group">
			                      <label for="sale_ids" class="col-md-3 control-label"><spring:message code="channel.salesperson" /> *</label>
			                      <div class="col-md-9">
			                      	<select class="form-control select2" name="sale_ids" id="sale_ids" style="width: 100%;" multiple="multiple">
			                      		<c:forEach items="${sale_users}" var="user">
			                      		   <option value="${user.id}" selected="selected">${user.name}(${user.agency })</option>
			                      		</c:forEach>
			                      	</select>
			                      </div>
			                    </div>
			                    
			                </div>
		            	</div>
		            	
	                </div>
	                
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
	            		<shiro:hasPermission name="agency">
	                    	<button type="button" class="btn btn-primary btn-sm btn-75" onclick="submitForm();"><spring:message code="btn.save"/></button>
	                    </shiro:hasPermission>
	                    <button type="button" class="btn btn-primary disabled btn-sm btn-75" onclick="cancel();"><spring:message code="btn.cancel"/></button>
	                </div>
                </div>
	          </section>
          </form>
          
          <div class="modal fade" id="remarkModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
			    <div class="modal-dialog">
			        <div class="modal-content">
			            <div class="modal-header">
			                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
			                <h4 class="modal-title" id="myModalLabel"><spring:message code="business.opportunity.progress.remark" /></h4>
			            </div>
			            <div class="modal-body"></div>
			        </div><!-- /.modal-content -->
			    </div><!-- /.modal -->
			</div>
          
          
          
          <script type="text/javascript">
          
          	$(function(){
                $("#menu_agency").addClass("active");
          	});

			function submitForm(){
				var hasError = false;
				$("[name='deliver_date']").each(function(){
					if($(this).val() == ''){
						hasError = true;
						$(this).css("border-color", "red");
					}
					else{
						$(this).css("border-color", "gainsboro");
					}
				})
				$("[name='nonuniform_rebate']").each(function(){
					if($(this).val() == ''){
						hasError = true;
						$(this).css("border-color", "red");
					}
					else{
						$(this).css("border-color", "gainsboro");
					}
				})
				if (!hasError){
					$("#channelForm").submit();
				}
			}
          
			function cancel(){
				window.location.href='${ctx}/businessOpportunity';
			};
			
			$("#sale_ids").select2({
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
				 placeholder: "<spring:message code='channel.salesperson.placehold' />",
				 //allowClear: true,
				 minimumInputLength: 1,
				 language: "zh-CN"
			}).on('change',function(evt){
				validateSelect2(this);
			});
			
			function validateSelect2(name){
				if($("#channelForm").validate().element( name )){
					$(name).next().removeClass('select2ErrorClass');
				}else{
					$(name).next().addClass('select2ErrorClass');
				}
			};
			
			$(function(){
				$(".deliver_date").daterangepicker({
					opens:"right",
					cancelClass:"btn-info",
					format:'YYYY-MM-DD',
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
	            	$("#channelForm").validate().element(this);
	            });
			})
			
			$("#channelForm").validate({
					ignore: "",
					rules:{
						channel_name:"required",
						qualification_name:"required",
						deliver_date:"required",
						nonuniform_rebate: "required",
						currency_id:"required",
						contact_person:"required",
						position:"required",
						phone:"required",
						company_adress:"required",
						sale_ids:"required",
						email:"required"
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
			
			function delete_one_rebate(thi){
				if ($("#rebate_div").find(".input-group").length > 1)
					$(thi).parent().parent().remove();
			}

			var index = ${channelRebates.size()};
			function addRebate(){
				var div_string = '<div class="input-group"><div class="input-group-addon"><input type="text" value="" class="form-control pull-right" name="deliver_date" id="deliver_date'+index+'" aria-required="true" style="width:200px;"></div><div class="input-group-addon"><i class="fa fa-calendar"></i></div><div class="input-group-addon"><input class="align_right numeric rebate_radio proof_ready_unmodify" id="nonuniform_rebate'+index+'" name="nonuniform_rebate" type="text" aria-required="true" value="" onkeyup="value=value.replace(/[^\\d\.]/g,\'\')" style="width:60px;"><span class="add-on currency_unit_budget" id="nonuniform_rebate_unit" name="nonuniform_rebate_unit"> %</span></div><span class="delete_one_rebate" style="display: flex;margin: 5px 0;"><a href="javascript:void(0);" class="rm_product_link" onclick="delete_one_rebate(this)" style="margin-left: 10px;margin-top: 10px;color: red;"><i class="fa fa-w fa-minus-square"></i></a></span></div>';
				$("#rebate_div").append(div_string);
				$("#deliver_date"+index).daterangepicker({
					opens:"right",
					cancelClass:"btn-info",
					format:'YYYY-MM-DD',
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
	            	$("#channelForm").validate().element(this);
	            });
				index++;
			}
          </script>
</body>
</html>
