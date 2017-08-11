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
    	$(function(){
    		$("#codeId").val(getData()['data']);
    		$("#add-btn").click(function(){
    			$("#dataForm").ajaxSubmit({
    				success:function(data){
    					if(data.success === true){
    						getData()['func']();
    						openDialog({title:'保存成功',type:2,func:function(){
        						closeWin();
    						}});
    					}else{
    						openDialog({title:'保存失败:'+data.message,type:4});
    					}
    				}
    			});
    		});
    		$("#close-btn").click(function(){
				closeWin();
    		});
    		$("#parentMenu").click(function(){
    			var inputObj = $(this);
    			var inputOffset = $(this).offset();
    			$("#sliderPane").css({ left: inputOffset.left + "px", top: inputOffset.top + inputObj.outerHeight() + "px",height:'200px'}).slideDown(2);
    			$("body,html").bind("mousedown",onBodyDown);
    		});
    	})
    	function hideTree(){
			$("#sliderPane").fadeOut("fast");
			$("body,html").unbind("mousedown",onBodyDown);
		}
		function onBodyDown(event){
			if (!event.target.id == "sliderPane" ||!($(event.target).parents("#sliderPane").length>0)) {
				hideTree();
			}
		}
    	function getParentNodeIds(node){
    		var ids = '';
    		if(node){
   				ids= node.id+','+getParentNodeIds(node.getParentNode());
    		}
    		return ids;
    	}
    </script>
  </head>
  <body>
  	
  	<div>
	  	<div style="overflow:auto;width:300px;margin:auto;">
	  		<form id="dataForm" action="code/vSave" method="post">
	  			<input type="hidden" name="id" id="id" value="${cValue.id}" />
	  			<input type="hidden" name="codeId" id="codeId" value="${cValue.codeId}" />
	  			<input type="hidden" name="token" value="${token}" />
	  			<table>
	  				<tr>
	  					<td colspan="2"><span style="color:#FF0000;">该功能作为系统维护功能保留，请不要随意修改参数</span></td>
	  				</tr>
	  				<tr>
	  					<td>名称:</td>
	  					<td><input type="text" name="name" value="${cValue.name}"/></td>
	  				</tr>
	  				<tr>
	  					<td>值:</td>
	  					<td><input type="text" name="value" value="${cValue.value}"/></td>
	  				</tr>
	  				<tr>
	  					<td>是否启用:</td>
	  					<td>
	  						<form:select path="cValue.enabled">
	  							<form:options items="${police:getCodeValues('ENABLED')}" itemLabel="name" itemValue="value"/>
	  						</form:select>
	  					</td>
	  				</tr>
	  			</table>
	  		</form>
	  	</div>
  	</div>
  	<div style="height:50px;width:1px;position:relative;bottom:0;z-index:-10;"></div>
  	<div class="footer">
  		<center style="padding-top:10px;">
	  		<span id="add-btn" class="ico-btn ico-hover ico-content ico-save" style="width:auto;">保存</span>
	  		<span id="close-btn" class="ico-btn ico-hover ico-content ico-close" style="width:auto;">关闭</span>
  		</center>
  	</div>
  	<div id="sliderPane" style="background-color:#FFFFFF;border:1px solid #EEEEEE;position:absolute;width:200px;height:200px;overflow:auto;display:none;">
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
    	$(function(){
    		$("#codeId").val(getData()['data']);
    		$("#add-btn").click(function(){
    			$("#dataForm").ajaxSubmit({
    				success:function(data){
    					if(data.success === true){
    						getData()['func']();
    						openDialog({title:'保存成功',type:2,func:function(){
        						closeWin();
    						}});
    					}else{
    						openDialog({title:'保存失败:'+data.message,type:4});
    					}
    				}
    			});
    		});
    		$("#close-btn").click(function(){
				closeWin();
    		});
    		$("#parentMenu").click(function(){
    			var inputObj = $(this);
    			var inputOffset = $(this).offset();
    			$("#sliderPane").css({ left: inputOffset.left + "px", top: inputOffset.top + inputObj.outerHeight() + "px",height:'200px'}).slideDown(2);
    			$("body,html").bind("mousedown",onBodyDown);
    		});
    	})
    	function hideTree(){
			$("#sliderPane").fadeOut("fast");
			$("body,html").unbind("mousedown",onBodyDown);
		}
		function onBodyDown(event){
			if (!event.target.id == "sliderPane" ||!($(event.target).parents("#sliderPane").length>0)) {
				hideTree();
			}
		}
    	function getParentNodeIds(node){
    		var ids = '';
    		if(node){
   				ids= node.id+','+getParentNodeIds(node.getParentNode());
    		}
    		return ids;
    	}
    </script>
  </head>
  <body>
  	
  	<div>
	  	<div style="overflow:auto;width:300px;margin:auto;">
	  		<form id="dataForm" action="code/vSave" method="post">
	  			<input type="hidden" name="id" id="id" value="${cValue.id}" />
	  			<input type="hidden" name="codeId" id="codeId" value="${cValue.codeId}" />
	  			<input type="hidden" name="token" value="${token}" />
	  			<table>
	  				<tr>
	  					<td colspan="2"><span style="color:#FF0000;">该功能作为系统维护功能保留，请不要随意修改参数</span></td>
	  				</tr>
	  				<tr>
	  					<td>名称:</td>
	  					<td><input type="text" name="name" value="${cValue.name}"/></td>
	  				</tr>
	  				<tr>
	  					<td>值:</td>
	  					<td><input type="text" name="value" value="${cValue.value}"/></td>
	  				</tr>
	  				<tr>
	  					<td>是否启用:</td>
	  					<td>
	  						<form:select path="cValue.enabled">
	  							<form:options items="${police:getCodeValues('ENABLED')}" itemLabel="name" itemValue="value"/>
	  						</form:select>
	  					</td>
	  				</tr>
	  			</table>
	  		</form>
	  	</div>
  	</div>
  	<div style="height:50px;width:1px;position:relative;bottom:0;z-index:-10;"></div>
  	<div class="footer">
  		<center style="padding-top:10px;">
	  		<span id="add-btn" class="ico-btn ico-hover ico-content ico-save" style="width:auto;">保存</span>
	  		<span id="close-btn" class="ico-btn ico-hover ico-content ico-close" style="width:auto;">关闭</span>
  		</center>
  	</div>
  	<div id="sliderPane" style="background-color:#FFFFFF;border:1px solid #EEEEEE;position:absolute;width:200px;height:200px;overflow:auto;display:none;">
  		<div class="ztree" id="ztree"></div>
  	</div>
  </body>
</html>
>>>>>>> 17ac48076d7a7be7437c8367312af01562698b1c
