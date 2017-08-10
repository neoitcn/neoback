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
		<jsp:param name="groups" value="aofa-validate,jquery-form,my97DatePicker,jquery-ztree,easyui-cs"/>
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
    var rule = {
   		"name":{required:true},
   		"enabled":{required:true}
   	};
   	var validtor = $('#dataForm').validate({
   		rules:rule
   	});
    var treeObj;
	var setting = {
		async:{
			enable:false
		},
		check:{
			enable:true,
			chkboxType: { "Y": "ps", "N": "ps" }
		},
		data:{
			simpleData:{
				enable:true,
				idKey:'id',
				pIdKey:'parentId',
				rootPId:null
			}
		}
	}
   	$(function(){
   		$.ajax({
   			dataType:'json',
   			url:'${ctx}/menu/all',
   			success:function(data){
   				if(data){
   					data.push({id:'-1',name:'所有功能'});
    				treeObj = $.fn.zTree.init($("#ztree"), setting, data);
    				var ids = $("#menuIds").val();
    				if(ids){
    					var idArr = ids.split(',');
    					for(var i in idArr){
    						var id = idArr[i];
    						var node = treeObj.getNodeByParam("id", id, null);
    						if(node){
	    						treeObj.checkNode(node, true, false);
    						}
    					}
    				}
					treeObj.expandAll(true);
   				}
   			}
   		});
   		$("#add-btn").click(function(){
   			if(!validtor.form()){
				validtor.showMessage();
				return;
			}
   			var nodes = treeObj.getCheckedNodes(true);
  				var ids='';
   			if(nodes && nodes.length > 0){
   				for(var i in nodes){
   					var node = nodes[i];
   					ids+=node.id+',';
   				}
   			}
   			$("#menuIds").val(ids);
   			$("#dataForm").requestSubmit({
   				success:function(data){
   					if(data.success === true){
   						//$($(window).parent()).refreshGrid(null,"listUser");
   						var treeNode = treeObj.getNodesByParam('id',$("#parentMenuId").val());
   						if(treeNode.length>0){
   							treeNode=treeNode[0];
   						}else{
   							treeNode = null;
   						}
   						getData()(data.obj); //如果obj的id存在于tree中，则只是更新，否则在treeNode下添加一个新的节点
   						createDialog({message:'保存成功',type:'success',accept:function(){
       						closeWin();
   						}}).show();
   					}else{
   						createDialog({message:'保存失败:'+data.message,type:'error'}).show();
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
   		$("input[name=permissions]").each(function(i,v){
   			$(v).next().click(function(event){
   				$(v).prop('checked',!$(v).is(':checked'));
   				if($(v).val() === 'a'){
	   				$("input[name=permissions]").each(function(I,V){
	   					if($(V).val()==='a'){
	   						return;
	   					}
							$(V).attr('checked',$(v).is(':checked'));
	   				});
   				}else{
   					var isAll = true;
   	   				$("input[name=permissions]").each(function(I,V){
   	   					if($(V).val()==='a'){
   	   						return;
   	   					}
   						if(!$(V).is(':checked')){
   							isAll = false;
   						}
   	   				});
   	   				try{
	  					$("input[name=permissions]").each(function(I,V){
	  	   					if($(V).val()==='a'){
	  	   						$(V).attr('checked',isAll);
	  	   						throw new Error();
	  	   					}
	  	   				});
   	   				}catch(e){
   	   				}
   				}
   				return event.stopPropagation();
   			});
   			if($(v).val() === 'a'){
	   			$(v).change(function(){
	   				$("input[name=permissions]").each(function(I,V){
	   					if($(V).val()==='a'){
	   						return;
	   					}
   						$(V).attr('checked',$(v).is(':checked'));
	   				});
	   			});
   			}else{
   				$(v).change(function(){
   					var isAll = true;
   	   				$("input[name=permissions]").each(function(I,V){
   	   					if($(V).val()==='a'){
   	   						return;
   	   					}
   						if(!$(V).is(':checked')){
   							isAll = false;
   						}
   	   				});
   	   				try{
	  					$("input[name=permissions]").each(function(I,V){
	  	   					if($(V).val()==='a'){
	  	   						$(V).attr('checked',isAll);
	  	   						throw new Error();
	  	   					}
	  	   				});
   	   				}catch(e){
   	   				}
   				});
   			}
   		});
   	})
   	function openDialog(obj,data){
   		openDialogInRoot(window,5,data,obj);
   	}
   	function getParentNodeIds(node){
   		var ids = '';
   		if(node){
  				ids= node.id+','+getParentNodeIds(node.getParentNode());
   		}
   		return ids;
   	}
   	function beforeClose(){
   		return true;
   	}
    </script>
  </head>
  <body>
  	<div style="width:250px;position:absolute;top:0;bottom:50px;left:0;overflow: auto;">
  		<div class="ztree" id="ztree"></div>
  	</div>
  	<div style="position:absolute;left:250px;top:0;overflow:auto;bottom:50px;border-left:1px solid #DDDDDD;padding:10px;">
  		<form id="dataForm" action="menu/role/save" method="post">
  			<input type="hidden" name="id" id="id" value="${menuRole.id}" />
  			<input type="hidden" name="token" value="${token}" />
  			<table>
  				<tr>
  					<td>角色名称:</td>
  					<input id="menuIds" name="menuIds" type="hidden" value="<c:forEach items="${menuRole.listMenu }" var="m">${m.id},</c:forEach>"/>
  					<td><input type="text" name="name" value="${menuRole.name}"/></td>
  				</tr>
  				<tr>
  					<td>角色描述:</td>
  					<td><input type="text" name="remarks" value="${menuRole.remarks}"/></td>
  				</tr>
  				<tr>
  					<td>角色权限:</td>
  					<td>
  						<c:forEach items="${police:getCodeValues('PERMISSION')}" var="code">
  							<p style="cursor:pointer;"><input type="checkbox" value="${code.value }" name="permissions" <c:if test="${!empty menuRole.permission and fn:contains(menuRole.permission,code.value)}">checked</c:if>/><span>${code.name }</span></p>
  						</c:forEach>
  					</td>
  				</tr>
  				<tr>
  					<td>是否启用:</td>
  					<td>
  						<form:select path="menuRole.enabled">
  							<form:options items="${police:getCodeValues('ENABLED')}" itemLabel="name" itemValue="value" />
  						</form:select>
  					</td>
  				</tr>
  			</table>
  		</form>
  	</div>
  	<div style="height:50px;width:1px;position:relative;bottom:0;z-index:-10;"></div>
  	<div class="footer">
  		<center style="padding-top:10px;">
	  		<span id="add-btn" class="ico-btn ico-hover ico-content ico-save" style="width:auto;">保存</span>
	  		<span id="close-btn" class="ico-btn ico-hover ico-content ico-close" style="width:auto;">关闭</span>
  		</center>
  	</div>
  </body>
</html>
