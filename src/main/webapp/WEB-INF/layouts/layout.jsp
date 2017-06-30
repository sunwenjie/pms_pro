<%@page import="com.asgab.util.CommonUtil"%>
<%@page import="org.springframework.web.servlet.support.RequestContextUtils"%>
<%@page import="org.springframework.web.servlet.LocaleResolver"%>
<%@ page import="org.springframework.context.i18n.LocaleContextHolder"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="sitemesh" uri="http://www.opensymphony.com/sitemesh/decorator"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <link rel="shortcut icon" href="${ctx}/static/images/favicon.ico">
    <title>iCRM | <sitemesh:title/></title>
    
    <style type="text/css">
		table th{word-break: keep-all;white-space:nowrap;}
	</style>
    
    <!-- Tell the browser to be responsive to screen width -->
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
    <!-- Bootstrap 3.3.6 -->
    <link rel="stylesheet" href="${ctx}/static/AdminLTE-2.3.3/bootstrap/css/bootstrap.min.css">
    <!-- Font Awesome -->
    <link rel="stylesheet" href="${ctx}/static/styles/font-awesome.min.css">
    
    <!-- Ion Slider -->
    <link rel="stylesheet" href="${ctx}/static/AdminLTE-2.3.3/plugins/ionslider/css/ion.rangeSlider.css">
    <link rel="stylesheet" href="${ctx}/static/AdminLTE-2.3.3/plugins/ionslider/css/ion.rangeSlider.skinHTML5.css">
    
    <!-- daterange picker -->
    <link rel="stylesheet" href="${ctx}/static/AdminLTE-2.3.3/plugins/daterangepicker/daterangepicker-bs3.css">
    <!-- iCheck for checkboxes and radio inputs -->
    <link rel="stylesheet" href="${ctx}/static/AdminLTE-2.3.3/plugins/iCheck/all.css">
    <!-- Bootstrap Color Picker -->
    <link rel="stylesheet" href="${ctx}/static/AdminLTE-2.3.3/plugins/colorpicker/bootstrap-colorpicker.min.css">
    <!-- Bootstrap time Picker -->
    <link rel="stylesheet" href="${ctx}/static/AdminLTE-2.3.3/plugins/timepicker/bootstrap-timepicker.min.css">
    <!-- Select2 -->
    <link rel="stylesheet" href="${ctx}/static/AdminLTE-2.3.3/plugins/select2/select2.min.css">
    
    <!-- Theme style -->
    <link rel="stylesheet" href="${ctx}/static/AdminLTE-2.3.3/dist/css/AdminLTE.css">
    <link rel="stylesheet" href="${ctx}/static/AdminLTE-2.3.3/dist/css/skins/_all-skins.css">
    
    <!-- daterangepicker -->
    <link rel="stylesheet" href="${ctx}/static/AdminLTE-2.3.3/plugins/daterangepicker/daterangepicker-bs3.css">
    
    <!-- datatable -->
    <link rel="stylesheet" href="${ctx}/static/AdminLTE-2.3.3/plugins/datatables/dataTables.bootstrap.css">
    
    <!-- custom style -->
    <link rel="stylesheet" href="${ctx}/static/styles/custom.css">
  
    <!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
        <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
    
    <!-- jQuery 2.1.4 -->
    <script src="${ctx}/static/AdminLTE-2.3.3/plugins/jQuery/jQuery-2.2.0.min.js"></script>
    <!-- Bootstrap 3.3.5 -->
    <script src="${ctx}/static/AdminLTE-2.3.3/bootstrap/js/bootstrap.min.js"></script>
   <!-- Select2 -->
    <script src="${ctx}/static/AdminLTE-2.3.3/plugins/select2/select2.full.min.js"></script>
    <!-- InputMask -->
    <script src="${ctx}/static/AdminLTE-2.3.3/plugins/input-mask/jquery.inputmask.js"></script>
    <script src="${ctx}/static/AdminLTE-2.3.3/plugins/input-mask/jquery.inputmask.date.extensions.js"></script>
    <script src="${ctx}/static/AdminLTE-2.3.3/plugins/input-mask/jquery.inputmask.extensions.js"></script>
    <script src="${ctx}/static/AdminLTE-2.3.3/plugins/input-mask/jquery.inputmask.numeric.extensions.js"></script>
    <!-- date-range-picker -->
    <script src="${ctx}/static/AdminLTE-2.3.3/plugins/daterangepicker/moment-with-locales.min.js"></script>
    <script src="${ctx}/static/AdminLTE-2.3.3/plugins/daterangepicker/daterangepicker.js"></script>
    <!-- bootstrap color picker -->
    <script src="${ctx}/static/AdminLTE-2.3.3/plugins/colorpicker/bootstrap-colorpicker.min.js"></script>
    <!-- bootstrap time picker -->
    <script src="${ctx}/static/AdminLTE-2.3.3/plugins/timepicker/bootstrap-timepicker.min.js"></script>
    <!-- SlimScroll 1.3.0 -->
    <script src="${ctx}/static/AdminLTE-2.3.3/plugins/slimScroll/jquery.slimscroll.min.js"></script>
    <!-- iCheck 1.0.1 -->
    <script src="${ctx}/static/AdminLTE-2.3.3/plugins/iCheck/icheck.min.js"></script>
    <!-- ChartJS 2.1.4 -->
    <script src="${ctx}/static/chartjs/Chart.js"></script>
    <!-- FastClick -->
    <script src="${ctx}/static/AdminLTE-2.3.3/plugins/fastclick/fastclick.min.js"></script>
    <!-- Ion Slider -->
    <script src="${ctx}/static/AdminLTE-2.3.3/plugins/ionslider/ion.rangeSlider.min.js"></script>
    <!-- bootbox -->
    <script src="${ctx}/static/bootbox/bootbox.js"></script>
    <!-- jquery validate 1.14.0 -->
    <script src="${ctx}/static/jquery-validation/1.14.0/dist/jquery.validate.js" type="text/javascript"></script>
	<script src="${ctx}/static/jquery-validation/1.14.0/dist/jquey.validate.override.js" type="text/javascript"></script>
    <!-- DataTables -->
    <script src="${ctx}/static/AdminLTE-2.3.3/plugins/datatables/jquery.dataTables.min.js"></script>
    <script src="${ctx}/static/AdminLTE-2.3.3/plugins/datatables/dataTables.bootstrap.min.js"></script>

    <!-- AdminLTE App -->
    <script src="${ctx}/static/AdminLTE-2.3.3/dist/js/app.min.js"></script>
    <!-- AdminLTE for demo purposes -->
    <script src="${ctx}/static/AdminLTE-2.3.3/dist/js/demo.js"></script>
    
    <!-- ztree -->
    <link rel="stylesheet" href="${ctx}/static/zTree/css/zTreeStyle/zTreeStyle.css" type="text/css">
    <script type="text/javascript" src="${ctx}/static/zTree/js/jquery.ztree.core.js"></script>
  
    
    <%
    LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver (request);
	String lang =localeResolver.resolveLocale(request).getLanguage();
	if("zh".equals(lang)){
		%>
		<script src="${ctx}/static/jquery-validation/1.14.0/dist/localization/messages_zh.js" type="text/javascript"></script>
		<script src="${ctx}/static/AdminLTE-2.3.3/plugins/select2/i18n/zh-CN.js" type="text/javascript"></script>
		<script type="text/javascript">
			moment.locale("zh-cn");
		</script>
		<%
	}
	%>
	<style type="text/css">
		#cover {
		    background: url("${ctx}/static/images/spin.gif") no-repeat scroll center center #000;
		    position: fixed;
		    top: 0;
		    left: 0;
		    width: 100%;
		    height: 100%;
		    filter:alpha(opacity=50);
		    -moz-opacity:0.5;
		    -khtml-opacity: 0.5;
		    opacity: 0.5;
		    z-index: 10000;
		}
		
		.btn-60 { width: 60px; line-height: 1.1 }
		.btn-65 { width: 65px; line-height: 1.1 }
		.btn-70 { width: 70px; line-height: 1.1 }
		.btn-75 { width: 75px; line-height: 1.1 }
		.btn-80 { width: 80px; line-height: 1.1 }
		.btn-85 { width: 85px; line-height: 1.1 }
		.btn-90 { width: 90px; line-height: 1.1 }
		

		/* select2 placeholder font */
		.select2-selection__placeholder{
			font-size: 14px !important;
		}
		.select2-search__field{
			font-size: 14px !important;
			/* margin-left: 6px; */
		}
		
		/* 双日期看不到yyyy/MM/dd dd 看不到 */
		.daterangepicker .ranges .input-mini {
		      width: 80px !important;
		}
		
		.daterangepicker .ranges {
		      width: 172px !important;
		}
		
		/* select2 multiselect remove selected option */
		.select2-results__options[aria-multiselectable=true] .select2-results__option[aria-selected=true] {
		    display: none;
		}
	</style>
    
  </head>
  <body class="hold-transition fixed layout-top-nav skin-black">
    <div id="cover" style="display: none;"></div>
    
    <div class="wrapper">
	<%@ include file="/WEB-INF/layouts/menu.jsp"%>
      <!-- Full Width Column -->
      <div class="content-wrapper" style="padding-top: 50px;">
        <div class="container">
         <sitemesh:body/>
        </div><!-- /.container -->
      </div><!-- /.content-wrapper -->
      <footer class="main-footer">
        <div class="container">
          <div class="pull-right hidden-xs">
            <b>Version : </b>1.5 - <%=CommonUtil.ip%>
          </div>
          <strong>Copyright &copy; 2016 <a href="http://www.i-click.com/">iClick Interactive Asia Limited</a>.</strong> All rights reserved.
        </div><!-- /.container -->
      </footer>
    </div><!-- ./wrapper -->
    
    
    <script type="text/javascript">
    $(function () {
    	$( document ).ajaxStart(function() {
    		  $( "#cover" ).show();
    	});
    	$( document ).ajaxComplete(function() {
    		  $( "#cover" ).hide();
    	});
    
      var slideToTop = $("<div />");
      slideToTop.html('<i class="fa fa-chevron-up"></i>');
      slideToTop.css({
        position: 'fixed',
        bottom: '20px',
        right: '25px',
        width: '40px',
        height: '40px',
        color: '#eee',
        'font-size': '',
        'line-height': '40px',
        'text-align': 'center',
        'background-color': '#222d32',
        cursor: 'pointer',
        'border-radius': '5px',
        'z-index': '99999',
        opacity: '.7',
        'display': 'none'
      });
      slideToTop.on('mouseenter', function () {
        $(this).css('opacity', '1');
      });
      slideToTop.on('mouseout', function () {
        $(this).css('opacity', '.7');
      });
      $('.wrapper').append(slideToTop);
      $(window).scroll(function () {
        if ($(window).scrollTop() >= 20) {
          if (!$(slideToTop).is(':visible')) {
            $(slideToTop).fadeIn(500);
          }
        } else {
          $(slideToTop).fadeOut(500);
        }
      });
      $(slideToTop).click(function () {
        $("body").animate({
          scrollTop: 0
        }, 500);
      });
    });
    
    function changeLang(lang){
    	url = window.location.href;
    	if(url.indexOf("#")>-1){
    		window.location.href = url.replace("#","");
    	}
    	if(url.indexOf("lang=zh_CN")>-1){
    		window.location.href = url.replace("lang=zh_CN","lang="+lang);
    	}
    	else if(url.indexOf("lang=en_US")>-1){
    		window.location.href = url.replace("lang=en_US","lang="+lang);
    	}else if( url.indexOf("?")==-1){
			window.location.href = url+"?lang="+lang;
		}else {
			window.location.href = url+"&lang="+lang;
		}
    }

   
	function lang_select2() {
	<%if ("zh".equals(lang)) {%>
		return "zh-CN";
	<%}%>
		return "en";
	}
	</script>
  </body>
</html>