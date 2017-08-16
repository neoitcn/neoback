<<<<<<< HEAD
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
		<jsp:param name="groups" value="artDialog,jquery-validate,jqGrid,jquery-form,my97DatePicker,jquery-ztree"/>
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
    var treeObj;
	var setting = {
		async:{
			enable:false
		},
		check:{
			enable:true,
			chkboxType: { "Y": "", "N": "" }
		},
		data:{
			simpleData:{
				enable:true,
				idKey:'id',
				pIdKey:'parentId',
				rootPId:null
			}
		},
		callback:{
			beforeCheck:function(treeId,treeNode){
				var checked = !treeNode.checked;
				var children = getAllChildrenNodes(treeNode);
				if(children){
					for(var i in children){
						treeObj.checkNode(children[i],false,false);
					}
				}
				var parents = getAllParentNodes(treeNode);
				if(parents){
					for(var j in parents){
						treeObj.checkNode(parents[j],false,false);
					}
				}
			}
		}
	}
	
	function getAllChildrenNodes(treeNode){
		var nodesArr=[];
        var childrenNodes = treeNode.children;
        if (childrenNodes) {
            for (var i = 0; i < childrenNodes.length; i++) {
            	nodesArr.push(childrenNodes[i]);
                var result = getAllChildrenNodes(childrenNodes[i]);
                if(result){
                	for(var j in result){
                		nodesArr.push(result[j]);
                	}
                }
            }
        }
	    return nodesArr;
	}
	
	function getAllParentNodes(treeNode){
		var nodesArr=[];
        var parent = treeNode.getParentNode();
        if(parent){
        	nodesArr.push(parent);
        	getAllParentNodes(parent);
        }
	    return nodesArr;
	}
	
   	$(function(){
   		$.ajax({
   			dataType:'json',
   			url:'${ctx}/office/user/office',
   			data:{enabled:1,checkDisable:'true',roleId:$("#roleId").val()},
   			success:function(data){
   				if(data){
    				treeObj = $.fn.zTree.init($("#ztree"), setting, data);
    				var ids = $("#officeIds").val();
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
   				}
   			}
   		});
   		$("#add-btn").click(function(){
   			
   			//treeObj.selectNode().getChildren().getCheck()
   			
   			var nodes = treeObj.getCheckedNodes(true);
  				var ids='';
   			if(nodes && nodes.length > 0){
   				for(var i in nodes){
   					var node = nodes[i];
   					ids+=node.id+',';
   				}
   			}
   			$("#officeIds").val(ids);
   			$("#dataForm").ajaxSubmit({
   				success:function(data){
   					if(data.success === true){
   						openDialog({title:'保存成功，3秒后自动关闭',second:3,type:2,func:function(){
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
   </script>
  </head>
  <body>
  	<div style="padding:10px;position:absolute;top:0;bottom:50px;overflow: auto;">
  		<div class="ztree" id="ztree"></div>
  	</div>
  	<form id="dataForm" action="menu/role/use/save" method="post">
	  	<input id="officeIds" name="officeIds" type="hidden" value="<c:forEach items="${offices}" var="m">${m.id},</c:forEach>"/>
	  	<input type="hidden" id="roleId" name="roleId" value="${roleId }"/>
  	</form>
  	<div style="height:50px;width:1px;position:relative;bottom:0;z-index:-10;"></div>
  	<div class="footer">
  		<center style="padding-top:10px;">
	  		<span id="add-btn" class="ico-btn ico-hover ico-content ico-save" style="width:auto;">保存</span>
	  		<span id="close-btn" class="ico-btn ico-hover ico-content ico-close" style="width:auto;">关闭</span>
  		</center>
  	</div>
  </body>
</html>
=======
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
		<jsp:param name="groups" value="artDialog,jquery-validate,jqGrid,jquery-form,my97DatePicker,jquery-ztree"/>
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
    var treeObj;
	var setting = {
		async:{
			enable:false
		},
		check:{
			enable:true,
			chkboxType: { "Y": "", "N": "" }
		},
		data:{
			simpleData:{
				enable:true,
				idKey:'id',
				pIdKey:'parentId',
				rootPId:null
			}
		},
		callback:{
			beforeCheck:function(treeId,treeNode){
				var checked = !treeNode.checked;
				var children = getAllChildrenNodes(treeNode);
				if(children){
					for(var i in children){
						treeObj.checkNode(children[i],false,false);
					}
				}
				var parents = getAllParentNodes(treeNode);
				if(parents){
					for(var j in parents){
						treeObj.checkNode(parents[j],false,false);
					}
				}
			}
		}
	}
	
	function getAllChildrenNodes(treeNode){
		var nodesArr=[];
        var childrenNodes = treeNode.children;
        if (childrenNodes) {
            for (var i = 0; i < childrenNodes.length; i++) {
            	nodesArr.push(childrenNodes[i]);
                var result = getAllChildrenNodes(childrenNodes[i]);
                if(result){
                	for(var j in result){
                		nodesArr.push(result[j]);
                	}
                }
            }
        }
	    return nodesArr;
	}
	
	function getAllParentNodes(treeNode){
		var nodesArr=[];
        var parent = treeNode.getParentNode();
        if(parent){
        	nodesArr.push(parent);
        	getAllParentNodes(parent);
        }
	    return nodesArr;
	}
	
   	$(function(){
   		$.ajax({
   			dataType:'json',
   			url:'${ctx}/office/user/office',
   			data:{enabled:1,checkDisable:'true',roleId:$("#roleId").val()},
   			success:function(data){
   				if(data){
    				treeObj = $.fn.zTree.init($("#ztree"), setting, data);
    				var ids = $("#officeIds").val();
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
   				}
   			}
   		});
   		$("#add-btn").click(function(){
   			
   			//treeObj.selectNode().getChildren().getCheck()
   			
   			var nodes = treeObj.getCheckedNodes(true);
  				var ids='';
   			if(nodes && nodes.length > 0){
   				for(var i in nodes){
   					var node = nodes[i];
   					ids+=node.id+',';
   				}
   			}
   			$("#officeIds").val(ids);
   			$("#dataForm").ajaxSubmit({
   				success:function(data){
   					if(data.success === true){
   						openDialog({title:'保存成功，3秒后自动关闭',second:3,type:2,func:function(){
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
   </script>
  </head>
  <body>
  	<div style="padding:10px;position:absolute;top:0;bottom:50px;overflow: auto;">
  		<div class="ztree" id="ztree"></div>
  	</div>
  	<form id="dataForm" action="menu/role/use/save" method="post">
	  	<input id="officeIds" name="officeIds" type="hidden" value="<c:forEach items="${offices}" var="m">${m.id},</c:forEach>"/>
	  	<input type="hidden" id="roleId" name="roleId" value="${roleId }"/>
  	</form>
  	<div style="height:50px;width:1px;position:relative;bottom:0;z-index:-10;"></div>
  	<div class="footer">
  		<center style="padding-top:10px;">
	  		<span id="add-btn" class="ico-btn ico-hover ico-content ico-save" style="width:auto;">保存</span>
	  		<span id="close-btn" class="ico-btn ico-hover ico-content ico-close" style="width:auto;">关闭</span>
  		</center>
  	</div>
  </body>
</html>
>>>>>>> 17ac48076d7a7be7437c8367312af01562698b1c
