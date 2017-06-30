<%@page import="org.apache.shiro.SecurityUtils"%>
<%@page import="com.asgab.service.account.ShiroDbRealm.ShiroUser"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<%
	ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
%>

<html>
<head>
<title><spring:message code="menu.advertiser"/></title>
</head>
<body>
	<style type="text/css">
		.btn-sm {line-height: 1.1}
		table .btn-default{ background: #d2d6de }
		
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
	</style>
	 <!-- Content Header -->
       <section class="content-header">
          <h1>
            <spring:message code="menu.advertiser"/>
          </h1>
          
          <ol class="breadcrumb">
            <li><a href="${ctx}/businessOpportunity"><i class="fa fa-dashboard"></i> <spring:message code="opportunity.home" /></a></li>
            <li class="active"><spring:message code="menu.advertiser" /></li>
          </ol>
		
		<c:if test="${message != null}">
			<div class="alert alert-success alert-dismissable" style="margin: 10px 0 0 0">
                    <button type="button" class="close" data-dismiss="alert" aria-hidden="true">×</button>
                    <h4>	<i class="icon fa fa-check"></i> <spring:message code="message.alert.success"/>!</h4>
                    <c:if test="${successId != null}">
                    	 &lt;${successId}&gt;
                    </c:if>
                    ${message}
            </div>
        </c:if>
        
        <c:if test="${message_del != null}">
            <c:if test="${success == true}">
                <div class="alert alert-success alert-dismissable" style="margin: 10px 0 0 0">
                    <button type="button" class="close" data-dismiss="alert" aria-hidden="true">×</button>
                    <h4>    <i class="icon fa fa-check"></i> <spring:message code="message.alert.success"/>!</h4>
                    ${message_del}
                </div>
            </c:if>
            <c:if test="${success != true}">
                <div class="alert alert-danger alert-dismissable" style="margin: 10px 0 0 0">
                    <button type="button" class="close" data-dismiss="alert" aria-hidden="true">×</button>
                    <h4>    <i class="icon fa fa-close"></i> <spring:message code="message.alert.fail"/>!</h4>
                    ${message_del}
                </div>
            </c:if>
        </c:if>
        
        <c:if test="${msg_modal != null}">
        	<div class="alert ${msg_modal['success'] == true ? 'alert-success' : 'alert-danger'} alert-dismissable" style="margin: 10px 0 0 0" id="modal-error-alert">
                    <button type="button" class="close" data-dismiss="alert" aria-hidden="true">×</button>
                    <h4>	
                    	<i class="icon fa ${msg_modal['success'] == true ? 'fa-check' : 'fa-close'}"></i> 
                    	<c:if test="${msg_modal['success'] == true}">
                    		<spring:message code="message.alert.success" />
                    	</c:if> 
                    	<c:if test="${msg_modal['success'] != true}">
                    		<spring:message code="message.alert.fail" />
                    	</c:if>
                    	!</h4>
                    ${msg_modal['message_modal']}
            </div>
        </c:if>
		          
        </section>

          <!-- Main content -->
          <section class="content">
          	<div class="nav-tabs-custom">
                <div class="tab-content">
                  <div class="tab-pane active" id="activity">
                  <div class="box box-primary">
		            <div class="box-header with-border">
		              <h3 class="box-title">
		              <!-- 没有title -->
		             	<button class="btn btn-primary btn-sm btn-80" onclick="window.location.href='${ctx}/client/create';"><i class="fa fa-w fa-plus"></i>&nbsp;<spring:message code="btn.create"/></button>
                		<div class="btn-group">
                		<button class="btn btn-primary btn-sm btn-80" onclick="$('#searchForm').submit();"><i class="fa fa-w fa-search"></i>&nbsp;<spring:message code="btn.search"/></button>
		              	<button class="btn btn-warning btn-sm btn-80" onclick="resetForm();"><i class="fa fa-w fa-undo"></i>&nbsp;<spring:message code="btn.reset"/></button>
		              	</div>
		              </h3>
		              <div class="box-tools pull-right">
		                <button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>
		              </div>
		            </div><!-- /.box-header -->
		            <form action="${ctx}/client" method="get" id="searchForm">
		            <div class="box-body" style="display: block;">
		            	
						<div class="row">
						
							<div class="col-md-4">
								<div class="form-group">
									<label><spring:message code="client.name.brand.channel" /></label>
									<input type="text" class="form-control" name="name_brand_channel" id="name_brand_channel" value="<c:out value="${pages.searchMap['name_brand_channel']}"/>" 
									placeholder="<spring:message code="client.name.brand.channel.remark" />">
								</div>
							</div>
							
							<div class="col-md-4">
								<div class="form-group">
									<label><spring:message code="client.industry" /></label>
									<select name="industry_id" class="form-control industry_id select2" id="industry_id" style="width: 100%;">
										<option value></option>
										<c:forEach var="it" items="${industryTypes}">
											<option value="${it.id}">${it.value}</option>
										</c:forEach>
									</select>
								</div>
							</div>
							
							<div class="col-md-4">
                                <div class="form-group">
                                    <label><spring:message code="client.number" /></label>
                                    <input type="text" name="clientNumber" id="clientNumber" class="form-control" value="<c:out value="${pages.searchMap['clientNumber']}"/>" 
                                    placeholder="<spring:message code="client.number.remark" />">
                                </div>
                            </div>
							
							 <div class="col-md-4">
                                 <div class="form-group">
                                    <label><spring:message code="business.opportunity.created.by"/></label>
                                    <select class="form-control select2 created_by" name="created_by" id="created_by" style="width: 100%;">
                                        <option value></option>
                                        <c:forEach var="user" items="${users}">
                                            <option value="${user.id}">${user.name}</option>
                                        </c:forEach>
                                    </select>
                                 </div>
                             </div>
                             <div class="col-md-4">  
                                 <label><spring:message code="business.opportunity.created.date"/></label>  
                                 <div class="form-group input-group">
                                    <lable class="input-group-addon"><i class="fa fa-calendar"></i></lable>
                                    <input type="text" class="form-control" value="${pages.searchMap['created_period']}" 
                                    name="created_period" id="created_period" placeholder="<spring:message code='business.opportunity.created.date.remark' />">
                                    <span class="dateclear">×</span>
                                 </div>
                            </div>
							
							<div class="col-md-4">
								<div class="form-group">
									<label><spring:message code="public.status"/></label>
									<tags:selectbox name="state" value="${pages.searchMap['state']}" list="${states}" />
								</div>
							</div>	
							
						</div><!-- /.row -->
					</div><!-- /.box-body -->
					</form>
					</div>
                	
                	<div class="box-body  table-responsive no-padding">
          
	                  <table class="table table-striped table-condensed table-hover">
	                    <tbody>
		                    <tr>
		                        <th class="text-right"><spring:message code="public.oper" /></th>
		                    	<th style="cursor: pointer;" <tags:sort column="id" page="${pages}"/>><spring:message code="client.number" />&nbsp;<i class="fa fa-w fa-sort"></i></th>
		                    	<th style="cursor: pointer;" <tags:sort column="clientname" page="${pages}"/>><spring:message code="client" />&nbsp;<i class="fa fa-w fa-sort"></i></th>
		                    	<th><spring:message code="client.brand" /></th>
		                    	<th><spring:message code="client.agency" /></th>
		                    	<th><spring:message code="client.industry" /></th>
		                    	<th><spring:message code="public.created.by"/></th>
		                    	<th><spring:message code="public.created.at"/></th>
		                    	<th><spring:message code="public.status"/></th>
		                    	<th><spring:message code="client.approval.mgt" /></th>
		                    </tr>
                    
	                    <c:forEach items="${pages.content}" var="client" varStatus="status">
	                    	<tr>
	                    	    <td class="text-right">
                                  <span class="glyphicon glyphicon-cog" data-toggle="popover" data-id="${client.id}"
                                  data-del-msg="<spring:message code='message.confirm.del.advertisers' 
                                                  arguments="${client.clientname},${client.number}"
                                                  argumentSeparator="," />">
                                   </span>
                                </td>
	                    		<td>${client.number}</td>
		                     	<td><a href="javascript:void(0);" onclick="view(${client.id});" >${client.clientname}</a></td>
		                     	<td>${client.brand}</td>
		                     	<td>${client.channel_name}</td>
		                     	<td><tags:decodeList list="${industryTypes}" value="${client.industry_id}"></tags:decodeList></td>
		                     	<td>${client.created_user}</td>
		                     	<td><fmt:formatDate value="${client.created_at}" pattern="yyyy/MM/dd HH:mm"/> </td>
		                     	<td><tags:decodeList list="${states}" value="${client.state}" /></td>
		                     	<td style="word-break: keep-all;white-space:nowrap;">
		                     		<button class="btn btn-sm btn-${client.buttonStyle} btn-75" data-client_id="${client.id}" id="client_popover_${client.id}" 
		                     		onclick="showModal('${ctx}/ajax/client/approval/modal/${client.id}')" style="text-align:center; padding-left: 0px;padding-right: 0px;">
		                     		<spring:message code="btn.client.approve.${client.buttonStyle}" />
		                     		</button>
		                     	</td>
		                    </tr>
	                    </c:forEach>
                    
                  </tbody></table>
                </div><!-- /.box-body -->
                <div class="box-footer clearfix">
                    <tags:pagination page="${pages}" paginationSize="5" />
                	<ul class="pagination pull-left hidden-sm hidden-xs" style="margin: 0px 0px 10px 10px">
						<span class="label label-default"><spring:message code="client.list.label.draft" /></span>
						<span class="label label-primary"><spring:message code="client.list.label.waitForApproval" /></span>
						<span class="label label-success"><spring:message code="client.list.label.approved" /></span>
						<span class="label label-danger"><spring:message code="client.list.label.rejected" /></span>
					</ul>
					<ul class="pagination pull-left hidden-md hidden-lg" style="margin: 5px 0px 10px 0px">
                        <span class="label label-default"><spring:message code="client.list.label.draft" /></span>
                        <span class="label label-primary"><spring:message code="client.list.label.waitForApproval" /></span>
                        <span class="label label-success"><spring:message code="client.list.label.approved" /></span>
                        <span class="label label-danger"><spring:message code="client.list.label.rejected" /></span>
                    </ul>
                </div>
              </div><!-- /.box -->

              
            </div>
            </div>
          </section>

			<div class="modal fade" id="myModal" tabindex="-1" role="dialog" 
			   aria-labelledby="myModalLabel" aria-hidden="true">
			   <div class="modal-dialog">
			      <div class="modal-content">
			         
			      </div><!-- /.modal-content -->
			   </div><!-- /.modal-dialog -->
			</div><!-- /.modal -->        
          
          <script type="text/javascript">
          	$(document).ready(function() {
          		$("#menu_client_Module").addClass("active");
        		$("#menu_client").addClass("active");  
        		$("#modal-error-alert a").attr("href","${ctx}/client/update/${msg_modal.back_edit_client_id}")
        		
        		$("#created_by").val("${pages.searchMap['created_by']}").trigger("change");
                
                $("#created_by").select2({
                     placeholder: "<spring:message code='business.opportunity.created.by.remark' />",
                     allowClear: true,
                     language: lang_select2()
                }).on('change', function(){
                    $("#searchForm").submit();
                });
                
                $(".dateclear").click(function(){
                    $("#created_period").val('');
                });
                
                $("#created_period").daterangepicker({
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
                });
        		
        		$("#industry_id").select2({
                    placeholder: '<spring:message code="client.industry.remark" />',
                    allowClear: true,
                    language: lang_select2()
                });
        		
        		// 操作弹出窗
                $('[data-toggle="popover"]').each(function(){
                     var element = $(this);
                     var id = element.attr('data-id');
                     var delMsg = element.attr('data-del-msg');
                     element.popover({
                         trigger: 'click',
                         placement: 'right',
                         html: 'true',
                         content: centent(id, delMsg)
                     }).on("mouseenter", function () {
                         var _this = this;
                         $(this).popover("show");
                         $(this).siblings(".popover").on("mouseleave", function () {
                             $(_this).popover('hide');
                         });
                     }).on("mouseleave", function () {
                         var _this = this;
                         setTimeout(function () {
                             if (!$(".popover:hover").length) {
                                 $(_this).popover("hide")
                             }
                         }, 100);
                     });
                });
        	});
          
          	$("#industry_id").val("${pages.searchMap['industry_id']}").trigger("change");
          	
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
        			keyboard: false
        		});
        	};
        	
        	function centent(id,delMsg){
                return '<ul class="list-unstyled text-left">'
                       +'<li><a href="javascript:void(0);" onclick="toUpdate(' + id + ')"><spring:message code="btn.edit" /></a></li>'
                       +'<li><a href="javascript:void(0);" onclick="del(' + id + ',\''+ delMsg +'\');"><spring:message code="btn.delete" /></a></li>'
                  +'<ul>';
            };
        	
        	function toUpdate(id){
                window.location.href="${ctx}/client/update/"+id;
            };
        	
          	function view(id){
          		window.location.href="${ctx}/client/view/"+id;
          	};
          	
          	function del(id,msg){
          		bootbox.dialog({
          			message: msg,
          		  	title: "<spring:message code='client.title.delete' />" ,
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
	          		    	window.location.href="${ctx}/client/delete/"+id;
	          		      }
	          		    }
          		  	}
          		});
          	};
          	
          	function resetForm(){
          		$("#searchForm input").val('');
          		$("#searchForm select").val(null).trigger("change");
          		$('#searchForm').submit();
          	};
          </script>
</body>
</html>
