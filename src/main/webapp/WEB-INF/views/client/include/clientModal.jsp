<%@ page import="org.apache.shiro.SecurityUtils"%>
<%@ page import="com.asgab.service.account.ShiroDbRealm.ShiroUser"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<style type="text/css">

	
</style>

<form role="form" action="${ctx}/client/approval" method="post" id="modalForm" class="form-horizontal">
	<input type="hidden" name="id" value="${client.id}" />
	<input type="hidden" name="modal_status" id="modal_status" value="" />
	<input type="hidden" name="modal_state" id="modal_state" value="${client.state}" />
	<input type="hidden" name="node_id" id="node_id" value="" />
		<div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" 
               aria-hidden="true">×
            </button>
            <h4 class="modal-title" id="myModalLabel" style="padding-left: 15px;">
               <spring:message code="client.approval.modal.title" /> - 
               <span>${client.clientname}</span>
               <span>(${client.number})</span>
            </h4>
         </div>
		 <div class="modal-body clearfix">
         		<div class="col-md-12 clearfix" style="margin-bottom: 20px;">
					<input type="button" class="btn btn-flat pull-left btn-sm btn-primary btn-tabs-sub" 
					onclick="changeTabs(1,2);" value="<spring:message code="client.approval.modal.tab1" />" style="width:50%;" />
					<input type="button" class="btn btn-flat pull-left btn-sm btn-default btn-tabs-app" 
					onclick="changeTabs(2,1);" value="<spring:message code="client.approval.modal.tab2" />" style="width:50%;" />
				</div>
				
				<div class="popover-body-1">
				
					<div id="process-detail-1">
						<div class="col-md-12">
					   		<div class="col-md-4"><spring:message code="client.approval.modal.submitter" /></div>
					   		<div class="col-md-8">
					   			<c:if test="${client.state eq 'cli_saved'}">
					   				- 
					   			</c:if>
					   			<c:if test="${client.state ne 'cli_saved' }">
					   				<c:if test="${submitedBy != null}">
					   					${submitedBy.created_user}
					   				</c:if>
					   				<c:if test="${submitedBy == null}">
					   					-
					   				</c:if>
					   			</c:if>
					   		</div>
						 </div>
						 <div class="col-md-12" style="padding-top: 5px;">
					   		<div class="col-md-4"><spring:message code="client.approval.modal.submission.time" /></div>
					   		<div class="col-md-8">
					   			<c:if test="${client.state eq 'cli_saved'}">
				   					- 
				   				</c:if>
				   				<c:if test="${client.state ne 'cli_saved' }">
					   				<c:if test="${submitedBy != null}">
					   					<fmt:formatDate value="${submitedBy.created_at}" pattern="yyyy/MM/dd HH:mm" />
					   				</c:if>
					   				<c:if test="${submitedBy == null}">
					   					-
					   				</c:if>
					   			</c:if>
					   		</div>
						 </div>
						 <div class="col-md-12"><hr></div>
					</div>	
					
					<div id="process-detail-2">
						 <div class="col-md-12">
					   		<div class="col-md-4"><spring:message code="client.approval.modal.approver" /></div>
					   		<div class="col-md-8">
					   			<c:if test="${client.state eq 'cli_saved'}">
				   					- 
				   				</c:if>
				   				<c:if test="${client.state ne 'cli_saved' }">
					   				<c:if test="${approvedBy != null}">
					   					${approvedBy.created_user}
					   				</c:if>
					   				<c:if test="${approvedBy == null}">
					   					-
					   				</c:if>
					   			</c:if>
					   		</div>
						 </div>
						 <div class="col-md-12" style="padding-top: 5px;">
						   		<div class="col-md-4"><spring:message code="client.approval.modal.approval.time" /></div>
						   		<div class="col-md-8">
									<c:if test="${client.state eq 'cli_saved'}">
					   					- 
					   				</c:if>
					   				<c:if test="${client.state ne 'cli_saved' }">
						   				<c:if test="${approvedBy != null}">
					   						<fmt:formatDate value="${approvedBy.created_at}" pattern="yyyy/MM/dd HH:mm" />
						   				</c:if>
						   				<c:if test="${approvedBy == null}">
						   					-
						   				</c:if>
						   			</c:if>						   			
						   		</div>
						 </div>
						 
						 <div class="col-md-12"><hr></div>
					</div>
					
					<div id="process-detail-3">	
						 <div class="col-md-12">
					   		<div class="col-md-4"><spring:message code="client.approval.modal.cross.approver" /></div>
					   		<div class="col-md-8">
					   			<c:if test="${client.state eq 'cli_saved'}">
				   					- 
				   				</c:if>
				   				<c:if test="${client.state ne 'cli_saved' }">
					   				<c:if test="${cross_approvedBy != null}">
					   					${cross_approvedBy.created_user}
					   				</c:if>
					   				<c:if test="${cross_approvedBy == null}">
					   					-
					   				</c:if>
					   			</c:if>
					   		</div>
						 </div>
						 <div class="col-md-12" style="padding-top: 5px;">
						   		<div class="col-md-4"><spring:message code="client.approval.modal.cross.approver.time" /></div>
						   		<div class="col-md-8">
						   			<c:if test="${cross_approvedBy != null}">
					   					<fmt:formatDate value="${cross_approvedBy.created_at}" pattern="yyyy/MM/dd HH:mm" />
					   				</c:if>
					   				<c:if test="${cross_approvedBy == null}">
					   					-
					   				</c:if>
						   		</div>
						 </div>
						 
						 <div class="col-md-12"><hr></div>
					</div>
					
					<div class="hide" id="submited-btn">
						<div class="col-md-12" style="text-align:center; padding-bottom: 20px;">
					   		<div class="col-md-12">
					   			 <input type="button" class="btn resetBtn btn-flat btn-sm btn-65" value="<spring:message code="btn.cancel" />" data-dismiss="modal" />&nbsp;&nbsp;&nbsp;
					   			 <input type="button" class="btn btn-primary btn-flat btn-sm btn-65" value="<spring:message code="btn.submit" />" onclick="submit_form(1);" />
					   		</div>
						</div>		
					</div>   
					
					
					<div class="hide" id="released-cbk">
						<div class="col-md-12">
					   		<div class="col-md-12">
						   		<div class="checkbox">
						   			<label><input type="checkbox" class="release_adv" />
						   				<spring:message code="client.approval.modal.release.advertiser.permission" />
						   			</label>
						   		</div>
					   		</div>
					 	</div>
					 	<div class="col-md-12"><hr></div>
					</div>
					
					<div class="hide" id="released-btn">
						<div class="col-md-12" style="text-align:center; padding-bottom: 20px; border-bottom: none;">
					   		<div class="col-md-12">
					   			 <input type="button" class="btn resetBtn btn-flat btn-sm btn-65" value="<spring:message code="btn.cancel" />" data-dismiss="modal" />&nbsp;&nbsp;&nbsp;
					   			 <input type="button" class="btn btn-primary btn-flat btn-sm btn-65" value="<spring:message code="btn.release" />" onclick="submit_form(4);"/>
					   		</div>
						</div>		
					</div>   		
					 
					    			 
					 <div class="col-md-12">
					   		<div class="col-md-12" style="background: #f1f1f2; text-align: center">
					   				<span style="line-height: 30px!important;height: 30px; color: #336dc6;">
					   					<c:if test="${pageContext.response.locale.language=='zh' }">
			                     			<tags:decodeList list="${client.STATES_ZH}" value="${client.state}" />
			                     		</c:if>
			                     		<c:if test="${pageContext.response.locale.language=='en' }">
			                     			<tags:decodeList list="${client.STATES_EN}" value="${client.state}" />
			                     		</c:if>
		                     		</span>
					   		</div>	
					 </div>
			
				</div>

				<div class="popover-body-2" style="display:none;">
					<div class="col-md-12" id="approval-rdo" style="padding-bottom: 20px;">
			   			 <div class="col-md-6 form-group">
				   			 <div class="radio">
				   			 	<label><input type="radio" name="state_rdo" class="minimal" checked="checked" value="2">&nbsp;
				   			 	<spring:message code="client.approval.modal.rdo1" /></label>
				   			 </div>
				   			 <div class="radio">
				   			 	<label><input type="radio" name="state_rdo" class="minimal-red" value="3">&nbsp;
				   			 	<spring:message code="client.approval.modal.rdo2" /></label>
				   			 </div>
			   			 </div>
					</div>
					<div class="col-md-12" id="approval-trea" style="padding-bottom: 20px;">
						 <div class="col-md-12">
					   	 		<textarea name="comment" class="form-control" rows="8" cols="20" placeholder="<spring:message code="client.approval.modal.common.remark" />"
					   	 		 style="width:100%;box-sizing:border-box;"></textarea>
					     </div>
					</div>
					<div class="col-md-12 hide" id="approval-btn" style="text-align:center; padding-bottom: 20px;">
				   			 <input type="button" class="btn resetBtn btn-flat btn-sm btn-65" value="<spring:message code="btn.cancel" />" data-dismiss="modal" />&nbsp;&nbsp;&nbsp;
				   			 <input type="button" class="btn btn-primary btn-flat btn-sm btn-65" value="<spring:message code="btn.submit" />" id="approval_btn" />
					 </div>		 			 
					 <div class="col-md-12">
					   		<div class="col-md-12" style="background: #f1f1f2; text-align: center">
					   				<span style="line-height: 30px!important;height: 30px; color: #336dc6;">
						   				<c:if test="${pageContext.response.locale.language=='zh' }">
			                     			<tags:decodeList list="${client.STATES_ZH}" value="${client.state}" />
			                     		</c:if>
			                     		<c:if test="${pageContext.response.locale.language=='en' }">
			                     			<tags:decodeList list="${client.STATES_EN}" value="${client.state}" />
			                     		</c:if>
		                     		</span>
					   		</div>	
					 </div>
				</div>
				
         </div>
         
         <div class="modal-footer">
            <button type="button" class="btn btn-default btn-sm btn-flat" 
               data-dismiss="modal"><spring:message code="btn.close" />
            </button>
         </div>
</form>         
<script type="text/javascript">

	//iCheck for checkbox and radio inputs
	$('input[type="checkbox"].minimal, input[type="radio"].minimal').iCheck({
	  checkboxClass: 'icheckbox_minimal-blue',
	  radioClass: 'iradio_minimal-blue'
	});
	//Red color scheme for iCheck
    $('input[type="checkbox"].minimal-red, input[type="radio"].minimal-red').iCheck({
      checkboxClass: 'icheckbox_minimal-red',
      radioClass: 'iradio_minimal-red'
    });
		
	if("cli_rejected" == "${client.state}"){
		$("input[type='radio'][name='state_rdo'][value='3']").iCheck("check");
	}

	$(".resetBtn").button("reset");

	$("#approval_btn").click( function(){
		var state = $("input[type='radio'][name='state_rdo']:checked").val();
		submit_form(state);
	});

	function submit_form(state){
		$("#modal_status").val(state);
		$('#modalForm').submit();
	};

	$(".release_adv").click( function(){
		if($(this).is(':checked')){
			$("#released-btn").removeClass("hide");
		}else{
			$("#released-btn").addClass("hide");
		}
	});
	
	control('${client.state}');
	
	function control(state){
		// 草稿
		if( state == 'cli_saved'){
			// 可以提交
			$("#submited-btn").removeClass("hide");
			// 不能释放
			$("#released-cbk").remove();
			$("#released-btn").remove();
			// 不能审批
			$("#approval-rdo input[name='state_rdo']").attr("disabled",true);
			$("#approval-trea textarea").attr("disabled",true);
			$("#approval-btn").remove();
		}
		// 审批中，跨区特批中
		else if(state == 'unapproved' || state == 'cross_unapproved'){
			// 不能提交
			$("#submited-btn").remove();
			// 不能释放
			$("#released-cbk").remove();
			$("#released-btn").remove();
			// 可以审批
			$("#approval-btn").removeClass("hide");
		}
		// 审批通过
		else if( state == 'approved' ){
			// 不能提交
			$("#submited-btn").remove();
			// 可以释放
			$("#released-cbk").removeClass("hide");
			// 不能审批
			$("#approval-rdo input[name='state_rdo']").attr("disabled",true);
			$("#approval-trea textarea").attr("disabled",true);
			$("#approval-btn").remove();
		}
		// 审批未通过
		else if( state == 'cli_rejected'){
			// 不能提交
			$("#submited-btn").remove();
			// 不能释放
			$("#released-cbk").remove();
			$("#released-btn").remove();
			// 不能审批
			$("#approval-rdo input[name='state_rdo']").attr("disabled",true);
			$("#approval-trea textarea").attr("disabled",true);
			$("#approval-btn").remove();
		}
		// 已释放
		else if( state == 'released' ){
			// 可以提交
			$("#submited-btn").removeClass("hide");
			// 不能释放
			$("#released-cbk").remove();
			$("#released-btn").remove();
			// 不能审批
			$("#approval-rdo input[name='state_rdo']").attr("disabled",true);
			$("#approval-trea textarea").attr("disabled",true);
			$("#approval-btn").remove();
		}
	};
	

	function changeTabs(showIdx,dispIdx){
		$('.popover-body-' +showIdx).css('display','block');
		$('.popover-body-' +dispIdx).css('display','none');
		if(showIdx == 1){
			$('.btn-tabs-sub').removeClass('btn-default').addClass('btn-primary');
			$('.btn-tabs-app').removeClass('btn-primary').addClass('btn-default');
		}else{
			$('.btn-tabs-sub').removeClass('btn-primary').addClass('btn-default');
			$('.btn-tabs-app').removeClass('btn-default').addClass('btn-primary');
		}
	};

</script>         