<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	
	<meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">
    
    <title>
    	<spring:message code='setting.data.sharing.tree' />
    </title>
    
    

</head>

<body>

<style type="text/css">
    
.ztree * {font-size: 10pt;font-family:"Microsoft Yahei",Verdana,Simsun,"Segoe UI Web Light","Segoe UI Light","Segoe UI Web Regular","Segoe UI","Segoe UI Symbol","Helvetica Neue",Arial}
.ztree li ul{ margin:0; padding:0}
.ztree li {line-height:30px;}
.ztree li a {width:auto;height:30px;padding-top: 0px;}
.ztree li a:hover {text-decoration:none; background-color: #E7E7E7;}
.ztree li a span.button.switch {visibility:hidden}
.ztree.showIcon li a span.button.switch {visibility:visible}
.ztree li a.curSelectedNode {background-color:#D4D4D4;border:0;height:30px;}
.ztree li span {line-height:30px;}
.ztree li span.button {margin-top: -7px;}
.ztree li span.button.switch {width: 25px;height: 16px;}

.ztree li a.level0 span {font-size: 150%;font-weight: bold;}
.ztree li span.button {background-image:url("../static/zTree/img/left_menuForOutLook.png"); *background-image:url("../static/zTree/img/left_menuForOutLook.gif")}
.ztree li span.button.switch.level0 {width: 20px; height:20px}
.ztree li span.button.switch.level1 {width: 25px; height:20px}
.ztree li span.button.noline_open {background-position: 0 0;}
.ztree li span.button.noline_close {background-position: -18px 0;}
.ztree li span.button.noline_open.level0 {background-position: 0 -18px;}
.ztree li span.button.noline_close.level0 {background-position: -18px -18px;}

    </style>
	
	<section class="content-header">
		<h1><spring:message code='setting.data.sharing.tree' /></h1>
		<ol class="breadcrumb">
	    	<li><a href="${ctx}/businessOpportunity"><i class="fa fa-dashboard"></i> <spring:message code="opportunity.home" /></a></li>
	        <li class="active">
	        	<spring:message code='setting.data.sharing.tree' />
	        </li>
	    </ol>
	</section>
	
	<section class="content">
		<div class="box box-info" style="min-height: 400px;">
			<div class="box-body">
				<div class="col-md-6">
					<form  class="form-horizontal" >
					<div class="form-group">
						<label class="col-sm-3 control-label"><spring:message code='setting.data.sharing.search.user.info' /></label>
						<div class="col-sm-9">
							<select class="form-control" style="width: 100%;" id="rootId">
								<option value=""></option>
								<c:forEach var="user" items="${users}">
	                     				<option value="${user.id}">${user.name} (${user.email})</option>
	                     		</c:forEach>
	                      	</select>
						</div>
					</div>
					<div class="form-group">
		                  <div class="col-sm-12">
		                    <ul id="treeDemo" class="ztree showIcon" style="margin-top: 10px;border: 1px solid #617775; background: #f0f6e4; height:360px; overflow-y:auto;overflow-x:auto;"></ul>
		                  </div>
                    </div>
					</form>
				</div>	
					
			</div><!-- /.box-body -->
		</div>
	</section>
	

<script type="text/javascript">
	$(document).ready(function() {
		
		// 销售人员select2
        $("#rootId").select2({
        	placeholder: "<spring:message code='setting.data.sharing.search.user.info.remark' />",
		    allowClear: true
		}).on('change', function(){
			getTree($(this).val());
		});
		
       
		
	});
	
	var curMenu = null, zTree_Menu = null;
	var setting = {
		view: {
			showLine: false,
			showIcon: false,
			selectedMulti: false,
			dblClickExpand: false,
			addDiyDom: addDiyDom
		},
		data: {
			simpleData: {
				enable: true
			}
		},
		callback: {
			beforeClick: beforeClick
		}
	};

	function addDiyDom(treeId, treeNode) {
		var spaceWidth = 5;
		var switchObj = $("#" + treeNode.tId + "_switch"),
		icoObj = $("#" + treeNode.tId + "_ico");
		switchObj.remove();
		icoObj.before(switchObj);

		if (treeNode.level > 1) {
			var spaceStr = "<span style='display: inline-block;width:" + (spaceWidth * treeNode.level)+ "px'></span>";
			switchObj.before(spaceStr);
		}
	}

	function beforeClick(treeId, treeNode) {
		if (treeNode.level == 0 ) {
			var zTree = $.fn.zTree.getZTreeObj("treeDemo");
			zTree.expandNode(treeNode);
			return false;
		}
		return true;
	};
	
	function getTree(rootId){
		
		$.ajax({
			type: "get",
			url: "${ctx}/ajax/tree",
			data: {"id": rootId},
			dataType: "json",
			success: function(zNodes){
				if(zNodes!=''){
					$.fn.zTree.init($("#treeDemo"), setting, zNodes);
					zTree_Menu = $.fn.zTree.getZTreeObj("treeDemo");
						if (zTree_Menu && zTree_Menu.getNodes().length>1)
						{
							curMenu = zTree_Menu.getNodes()[0].children[0].children[0];
							zTree_Menu.selectNode(curMenu);
						}
					$(".ztree li a").css('width',$(".ztree li").width());
				}
			}
		}) 
	};
</script>
</body>