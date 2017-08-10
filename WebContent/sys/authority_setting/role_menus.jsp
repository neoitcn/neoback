<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="police" uri="http://aofa.com/jsp/jstl/function"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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
		<jsp:param name="groups" value="easyui-cs,jquery-ztree"/>
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
    	#dataForm table{
    		border-collapse: collapse;
    		width:100%;
    	}
    	#dataForm table td{
    		border-width:0px 0px 1px 0px;
    		border-style:solid;
    		border-color: rgba(0,0,0,.05);
    		height:40px;
    		vertical-align: center;
    	}
    	#dataForm table td input[type='text'],
    	#dataForm table td select,
    	#dataForm table td textarea{
    		height:20px;
    		border:1px solid #CCCCCC;
    		border-radius:5px;
    	}
    </style>
    <script>
    var roleId = getData().roleId;
    var treeObj;
	var setting = {
		async:{
			enable:false
		},
		check:{
			enable:false
		},
		data:{
			simpleData:{
				enable:true,
				idKey:'id',
				pIdKey:'pId',
				rootPId:null
			}
		}
	}
	
   	$(function(){
   		$.requestAjax({
   			dataType:'json',
   			url:'${ctx}/authority/role-menus',
   			data:{roleId:roleId},
   			success:function(data){
   				if(data && data.obj && data.obj.length>0){
    				treeObj = $.fn.zTree.init($("#ztree"), setting, data.obj);
    				treeObj.expandAll(true);
   				}else{
   					$("#ztree").text('没有菜单');
   				}
   			}
   		});
   		$("#close-btn").click(function(){
   			closeWin();
   		});
   	})
   </script>
  </head>
  <body>
  	<div style="padding:10px;position:absolute;top:0;bottom:50px;left:0px;right:0px;overflow: auto;">
  		<div class="ztree" id="ztree"></div>
  	</div>
  	<form id="dataForm" action="menu/role/use/save" method="post">
	  	<input id="officeIds" name="officeIds" type="hidden" value="<c:forEach items="${offices}" var="m">${m.id},</c:forEach>"/>
	  	<input type="hidden" id="roleId" name="roleId" value="${roleId }"/>
  	</form>
  	<div style="height:50px;width:1px;position:relative;bottom:0;z-index:-10;"></div>
  	<div class="footer">
  		<center style="padding-top:10px;">
	  		<span id="close-btn" class="ico-btn ico-hover ico-content ico-close" style="width:auto;">关闭</span>
  		</center>
  	</div>
  </body>
</html>
