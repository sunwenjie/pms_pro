<%@page import="org.apache.shiro.SecurityUtils"%>
<%@page import="com.asgab.service.account.ShiroDbRealm.ShiroUser"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="s" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<%
    ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
%>

<html>
<head>
    <title><spring:message code="menu.agency"/></title>
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
        <spring:message code="menu.agency"/>
    </h1>

    <ol class="breadcrumb">
        <li><a href="${ctx}/channel"><i class="fa fa-dashboard"></i> <spring:message code="opportunity.home" /></a></li>
        <li class="active"><spring:message code="menu.agency" /></li>
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
                            <button class="btn btn-primary btn-sm btn-80" onclick="window.location.href='${ctx}/channel/create';"><i class="fa fa-w fa-plus"></i>&nbsp;<spring:message code="btn.create"/></button>
                            <div class="btn-group">
                                <button class="btn btn-primary btn-sm btn-80" onclick="$('#searchForm').submit();"><i class="fa fa-w fa-search"></i>&nbsp;<spring:message code="btn.search"/></button>
                                <button class="btn btn-warning btn-sm btn-80" onclick="resetForm();"><i class="fa fa-w fa-undo"></i>&nbsp;<spring:message code="btn.reset"/></button>
                            </div>
                        </h3>
                        <div class="box-tools pull-right">
                            <button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>
                        </div>
                    </div><!-- /.box-header -->
                    <form action="${ctx}/channel" method="get" id="searchForm">
                        <div class="box-body" style="display: block;">

                            <div class="row">

                                <div class="col-md-4">
                                    <div class="form-group">
                                        <label><spring:message code="channel.list.name" /></label>
                                        <input type="text" class="form-control" name="name" id="name" value="<c:out value="${pages.searchMap['name']}"/>"
                                               placeholder="<spring:message code="channel.list.name.remark" />">
                                    </div>
                                </div>

                                <div class="col-md-4">
                                    <div class="form-group">
                                        <label><spring:message code="channel.list.sales" /></label>
                                        <input type="text" class="form-control" name="sales" id="sales" value="<c:out value="${pages.searchMap['sales']}"/>"
                                               placeholder="<spring:message code="channel.list.sales.remark" />">
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
                            </div><!-- /.row -->
                        </div><!-- /.box-body -->
                    </form>
                </div>

                <div class="box-body  table-responsive no-padding">

                    <table class="table table-striped table-condensed table-hover">
                        <tbody>
                        <tr>
                            <th class="text-right"><spring:message code="public.oper" /></th>
                            <th style="cursor: pointer;" <tags:sort column="channel_name" page="${pages}"/>><spring:message code="channel.list.name" />&nbsp;<i class="fa fa-w fa-sort"></i></th>
                            <th><spring:message code="channel.list.rebateDate" /></th>
                            <th><spring:message code="channel.list.rebateName" /></th>
                            <th><spring:message code="channel.list.sales" /></th>
                            <th><spring:message code="channel.list.createDate"/></th>
                        </tr>

                        <c:forEach items="${pages.content}" var="channel" varStatus="status">
                            <tr>
                                <td class="text-right">
                                  <span class="glyphicon glyphicon-cog" data-toggle="popover" data-id="${channel.id}"
                                        data-del-msg="<spring:message code='message.confirm.del.channel'
                                                  arguments="${channel.channel_name}"
                                                  argumentSeparator="," />">
                                   </span>
                                </td>
                                <td><a href="javascript:void(0);" onclick="view(${channel.id});" >${channel.channel_name}</a></td>
                                <td>
                                    <a href="javascript:void(0);" style="color: darkgrey;" data-html="true" data-original-title="<spring:message code="channel.list.other_rebate"></spring:message> " data-content="" onmouseover="showOtherRebateDate(this,'${channel.rebate_date_totip}')" onmouseout="hideModal(this)">${channel.current_rebate_date}</a>
                                </td>
                                <c:choose>
                                    <c:when test="${not empty channel.current_rebate}">
                                        <td>${channel.current_rebate}%</td>
                                    </c:when>
                                    <c:otherwise>
                                        <td>${channel.current_rebate}</td>
                                    </c:otherwise>
                                </c:choose>
                                <td>${channel.sales}</td>
                                <td><fmt:formatDate value="${channel.created_at}" pattern="yyyy/MM/dd HH:mm"/> </td>
                            </tr>
                        </c:forEach>

                        </tbody></table>
                </div><!-- /.box-body -->
                <div class="box-footer clearfix">
                    <tags:pagination page="${pages}" paginationSize="10" />
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
        $("#menu_agency").addClass("active");

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

    function  showOtherRebateDate(name,rebateTip) {

           var html = "<div><span>"+rebateTip+"</span></div>";

            $(name).attr('data-content',html);
            $(name).popover('show');
    };

    function hideModal(name) {
        $(name).popover('hide');
    };

    function centent(id,delMsg){
        return '<ul class="list-unstyled text-left">'
            +'<li><a href="javascript:void(0);" onclick="toUpdate(' + id + ')"><spring:message code="btn.edit" /></a></li>'
            +'<li><a href="javascript:void(0);" onclick="del(' + id + ',\''+ delMsg +'\');"><spring:message code="btn.delete" /></a></li>'
            +'<ul>';
    };

    function toUpdate(id){
        window.location.href="${ctx}/channel/update/"+id;
    };

    function view(id){
        window.location.href="${ctx}/channel/view/"+id;
    };

    function del(id,msg){
        bootbox.dialog({
            message: msg,
            title: "<spring:message code='channel.title.delete' />" ,
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
                        window.location.href="${ctx}/channel/delete/"+id;
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
