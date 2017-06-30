<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<%response.setStatus(200);%>

<!DOCTYPE html>
<html>
<head>
	
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <title>iCRM| 404 Page not found</title>
  <!-- Tell the browser to be responsive to screen width -->
  <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
  <!-- Bootstrap 3.3.6 -->
  <link rel="stylesheet" href="${ctx}/static/AdminLTE-2.3.3/bootstrap/css/bootstrap.min.css">
  
  <link rel="stylesheet" href="${ctx}/static/styles/font-awesome.min.css">
 
  <!-- Theme style -->
  <link rel="stylesheet" href="${ctx}/static/AdminLTE-2.3.3/dist/css/AdminLTE.min.css">
  <!-- AdminLTE Skins. Choose a skin from the css/skins
       folder instead of downloading all of them to reduce the load. -->
  <link rel="stylesheet" href="${ctx}/static/AdminLTE-2.3.3/dist/css/skins/_all-skins.min.css">
</head>


 <body class="hold-transition fixed layout-top-nav skin-black">
    
    
    <div class="wrapper">
   
      <!-- Full Width Column -->
      <div class="content-wrapper" style="padding-top: 50px;">
        <div class="container">
           <!-- Main content -->
    <section class="content">
      <div class="error-page">
        <h2 class="headline text-yellow"> 404</h2>

        <div class="error-content">
           <br/>
          <h3><i class="fa fa-warning text-yellow"></i> <spring:message code="error.404.title"/> </h3>

          <p>
            <spring:message code="error.404.content1"/><a href="${ctx}/businessOpportunity"><spring:message code="error.404.content2"/></a>
          </p>
          <p>
            <spring:message code="error.404.mail"/><a href="#">technology@i-click.com</a>
          </p>
     
        </div>
        <!-- /.error-content -->
      </div>
      <!-- /.error-page -->
    </section>
        </div><!-- /.container -->
      </div><!-- /.content-wrapper -->
      
    </div><!-- ./wrapper -->
    
    
   
  </body>

<body>

</body>
</html>