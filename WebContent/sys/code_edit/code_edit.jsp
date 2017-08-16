<<<<<<< HEAD
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="police" uri="http://aofa.com/jsp/jstl/function"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML>
<html>
  <head>
  	<meta charset="utf-8">
    <base href="<%=basePath%>">
    <jsp:include page="/common/jsinclude.jsp">
		<jsp:param name="groups" value="easyui-cs,jquery-form"/>
	</jsp:include>
    <style>
    	html,body{
    		background-color:#FFFFFF;
    		margin:0;
    		padding:0;
    		height:100%;
    	}
    	.footer{
    		width:100%;
    		height:50px;
    		position: fixed;
    		bottom:0;
    		box-shadow:0px 0px 1px rgba(0,0,0,.3);
    		background-color:#FFFFFF;
    	}
    </style>
    <script>
    	$(function(){
    	})
    </script>
  </head>
  <body>
  	<div class="ztreeContiner">
  		<div class="ztree" id="ztree"></div>
  	</div>
  </body>
</html>
=======
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="police" uri="http://aofa.com/jsp/jstl/function"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML>
<html>
  <head>
  	<meta charset="utf-8">
    <base href="<%=basePath%>">
    <jsp:include page="/common/jsinclude.jsp">
		<jsp:param name="groups" value="easyui-cs,jquery-form"/>
	</jsp:include>
    <style>
    	html,body{
    		background-color:#FFFFFF;
    		margin:0;
    		padding:0;
    		height:100%;
    	}
    	.footer{
    		width:100%;
    		height:50px;
    		position: fixed;
    		bottom:0;
    		box-shadow:0px 0px 1px rgba(0,0,0,.3);
    		background-color:#FFFFFF;
    	}
    </style>
    <script>
    	$(function(){
    	})
    </script>
  </head>
  <body>
  	<div class="ztreeContiner">
  		<div class="ztree" id="ztree"></div>
  	</div>
  </body>
</html>
>>>>>>> 17ac48076d7a7be7437c8367312af01562698b1c
