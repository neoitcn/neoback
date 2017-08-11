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
		<jsp:param name="groups" value="easyui-cs,aofa-validate,jqGrid,jquery-form,my97DatePicker,jquery-ztree"/>
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
			enable:false
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
			onClick:function (event, treeId, treeNode) {
				$("#parentMenu").val(treeNode.name);
				$("#parentMenuId").val(treeNode.id);
				$("#parentIds").val(getParentNodeIds(treeNode));
				hideTree();
			}
		}
	}
	
    	$(function(){
    		$.requestAjax({
    			dataType:'json',
    			url:'${ctx}/menu/all',
    			success:function(data){
    				if(data){
    					data.push({id:'-1',name:'顶级菜单'});
	    				treeObj = $.fn.zTree.init($("#ztree"), setting, data);
	    				var node = treeObj.getNodeByParam("id", $("#parentMenuId").val(), null);
	    				if(node){
		    				treeObj.selectNode(node);
		    				$("#parentMenu").val(node.name);
	    				}
	    				var rNode = treeObj.getNodeByParam("id",$("#id").val());
	    				if(rNode){
		    				treeObj.removeNode(rNode);
	    				}
    				}
    			}
    		});
    		$("#add-btn").click(function(){
    			if(!validtor.form()){
    				validtor.showMessage();
    				return;
    			}
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
    		$("#parentMenu").click(function(){
    			var inputObj = $(this);
    			var inputOffset = $(this).offset();
    			$("#sliderPane").css({ left: inputOffset.left + "px", top: inputOffset.top + inputObj.outerHeight() + "px",height:'200px'}).slideDown(2);
    			$("body,html").bind("mousedown",onBodyDown);
    		});
    		$("#selectBtn").click(function(){
    			$("input[name='file']").click();
    		});
    		$("input[name='file']").change(function(){
                //alert($("#filePathText").val());
                //判断是ie浏览器还是火狐还是谷歌浏览器
                if (navigator.appName == "Microsoft Internet Explorer") {
                    $("#image-ico").attr("src", getFullPath(this));
                } else {    //火狐或者谷歌
                    var objUrl = getObjectURL(this.files[0]);
                    if (objUrl) {
                        $("#image-ico").attr("src", objUrl);
                    }
                }
    		});
    	})
    	//建立一個可存取到該file的url(针对火狐谷歌有效)
        function getObjectURL(file) {
            var userAgent = navigator.userAgent.toLowerCase();
            var url = null;
            if (window.createObjectURL != undefined) { // basic
                url = window.createObjectURL(file);
            } else if (window.URL != undefined) { // mozilla(firefox)
                url = window.URL.createObjectURL(file);
            } else if (window.webkitURL != undefined) { // webkit or chrome
                url = window.webkitURL.createObjectURL(file);
            }
            return url;
        }
        //针对ie浏览器有效
        function getFullPath(obj) {
            if (obj) {
                //ie
                if (window.navigator.userAgent.indexOf("MSIE") >= 1 || window.navigator.userAgent.indexOf("Chrome") >= 1) {
                    //alert(456);
                    obj.select();
                    return document.selection.createRange().text;
                }
                    //firefox
                else if (window.navigator.userAgent.indexOf("Firefox") >= 1) {
                    if (obj.files) {
                        return obj.files.item(0).getAsDataURL();
                    }
                    return obj.value;
                }
                return obj.value;
            }
        }
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
	  		<form id="dataForm" action="menu/save" method="post">
	  			<input type="hidden" name="id" id="id" value="${menu.id}" />
	  			<input type="hidden" name="token" value="${token}" />
	  			<table>
	  				<tr>
	  					<td colspan="2"><span style="color:#FF0000;">该功能作为系统维护功能保留，请不要随意修改参数</span></td>
	  				</tr>
	  				<tr>
	  					<td>上级菜单:</td>
	  					<td>
	  						<input type="text" style="cursor:pointer;" id="parentMenu" readonly value="${menu.parentName}"/>
	  						<input type="hidden" id="parentMenuId" name="parentId" value="${menu.parentId }"/>
	  						<input type="hidden" id="parentIds" name="parentIds" value="${menu.parentIds }"/>
	  					</td>
	  				</tr>
	  				<tr>
	  					<td>菜单图标:(16 x 16像素)</td>
	  					<td>
	  						<img id="image-ico" src="${menu.iconUrl}"  width="16" height="16"/><span id="selectBtn" class="ico-btn ico-hover">选择图片</span>&nbsp;<input type="checkbox" name="deleteIcon" value="1"/>清除
	  						<input type="file" name="file" style="display:none;" accept="image/gif,image/x-png"/>
	  					</td>
	  				</tr>
	  				<tr>
	  					<td>菜单名称:</td>
	  					<td><input type="text" name="name" value="${menu.name}"/></td>
	  				</tr>
	  				<tr>
	  					<td>菜单链接:</td>
	  					<td><input type="text" name="url" value="${menu.url}"/></td>
	  				</tr>
	  				<tr>
	  					<td>菜单控制:</td>
	  					<td><input type="text" name="controllerName" value="${menu.controllerName}" placeholder="权限控制名称"/></td>
	  				</tr>
	  				<tr>
	  					<td>是否启用:</td>
	  					<td>
	  						<form:select path="menu.enabled">
	  							<form:options items="${police:getCodeValues('ENABLED')}" itemLabel="name" itemValue="value"/>
	  						</form:select>
	  					</td>
	  				</tr>
	  				<tr>
	  					<td>排序:</td>
	  					<td><input type="text" name="sort" value='${menu.sort}'/></td>
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
		<jsp:param name="groups" value="easyui-cs,aofa-validate,jqGrid,jquery-form,my97DatePicker,jquery-ztree"/>
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
			enable:false
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
			onClick:function (event, treeId, treeNode) {
				$("#parentMenu").val(treeNode.name);
				$("#parentMenuId").val(treeNode.id);
				$("#parentIds").val(getParentNodeIds(treeNode));
				hideTree();
			}
		}
	}
	
    	$(function(){
    		$.requestAjax({
    			dataType:'json',
    			url:'${ctx}/menu/all',
    			success:function(data){
    				if(data){
    					data.push({id:'-1',name:'顶级菜单'});
	    				treeObj = $.fn.zTree.init($("#ztree"), setting, data);
	    				var node = treeObj.getNodeByParam("id", $("#parentMenuId").val(), null);
	    				if(node){
		    				treeObj.selectNode(node);
		    				$("#parentMenu").val(node.name);
	    				}
	    				var rNode = treeObj.getNodeByParam("id",$("#id").val());
	    				if(rNode){
		    				treeObj.removeNode(rNode);
	    				}
    				}
    			}
    		});
    		$("#add-btn").click(function(){
    			if(!validtor.form()){
    				validtor.showMessage();
    				return;
    			}
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
    		$("#parentMenu").click(function(){
    			var inputObj = $(this);
    			var inputOffset = $(this).offset();
    			$("#sliderPane").css({ left: inputOffset.left + "px", top: inputOffset.top + inputObj.outerHeight() + "px",height:'200px'}).slideDown(2);
    			$("body,html").bind("mousedown",onBodyDown);
    		});
    		$("#selectBtn").click(function(){
    			$("input[name='file']").click();
    		});
    		$("input[name='file']").change(function(){
                //alert($("#filePathText").val());
                //判断是ie浏览器还是火狐还是谷歌浏览器
                if (navigator.appName == "Microsoft Internet Explorer") {
                    $("#image-ico").attr("src", getFullPath(this));
                } else {    //火狐或者谷歌
                    var objUrl = getObjectURL(this.files[0]);
                    if (objUrl) {
                        $("#image-ico").attr("src", objUrl);
                    }
                }
    		});
    	})
    	//建立一個可存取到該file的url(针对火狐谷歌有效)
        function getObjectURL(file) {
            var userAgent = navigator.userAgent.toLowerCase();
            var url = null;
            if (window.createObjectURL != undefined) { // basic
                url = window.createObjectURL(file);
            } else if (window.URL != undefined) { // mozilla(firefox)
                url = window.URL.createObjectURL(file);
            } else if (window.webkitURL != undefined) { // webkit or chrome
                url = window.webkitURL.createObjectURL(file);
            }
            return url;
        }
        //针对ie浏览器有效
        function getFullPath(obj) {
            if (obj) {
                //ie
                if (window.navigator.userAgent.indexOf("MSIE") >= 1 || window.navigator.userAgent.indexOf("Chrome") >= 1) {
                    //alert(456);
                    obj.select();
                    return document.selection.createRange().text;
                }
                    //firefox
                else if (window.navigator.userAgent.indexOf("Firefox") >= 1) {
                    if (obj.files) {
                        return obj.files.item(0).getAsDataURL();
                    }
                    return obj.value;
                }
                return obj.value;
            }
        }
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
	  		<form id="dataForm" action="menu/save" method="post">
	  			<input type="hidden" name="id" id="id" value="${menu.id}" />
	  			<input type="hidden" name="token" value="${token}" />
	  			<table>
	  				<tr>
	  					<td colspan="2"><span style="color:#FF0000;">该功能作为系统维护功能保留，请不要随意修改参数</span></td>
	  				</tr>
	  				<tr>
	  					<td>上级菜单:</td>
	  					<td>
	  						<input type="text" style="cursor:pointer;" id="parentMenu" readonly value="${menu.parentName}"/>
	  						<input type="hidden" id="parentMenuId" name="parentId" value="${menu.parentId }"/>
	  						<input type="hidden" id="parentIds" name="parentIds" value="${menu.parentIds }"/>
	  					</td>
	  				</tr>
	  				<tr>
	  					<td>菜单图标:(16 x 16像素)</td>
	  					<td>
	  						<img id="image-ico" src="${menu.iconUrl}"  width="16" height="16"/><span id="selectBtn" class="ico-btn ico-hover">选择图片</span>&nbsp;<input type="checkbox" name="deleteIcon" value="1"/>清除
	  						<input type="file" name="file" style="display:none;" accept="image/gif,image/x-png"/>
	  					</td>
	  				</tr>
	  				<tr>
	  					<td>菜单名称:</td>
	  					<td><input type="text" name="name" value="${menu.name}"/></td>
	  				</tr>
	  				<tr>
	  					<td>菜单链接:</td>
	  					<td><input type="text" name="url" value="${menu.url}"/></td>
	  				</tr>
	  				<tr>
	  					<td>菜单控制:</td>
	  					<td><input type="text" name="controllerName" value="${menu.controllerName}" placeholder="权限控制名称"/></td>
	  				</tr>
	  				<tr>
	  					<td>是否启用:</td>
	  					<td>
	  						<form:select path="menu.enabled">
	  							<form:options items="${police:getCodeValues('ENABLED')}" itemLabel="name" itemValue="value"/>
	  						</form:select>
	  					</td>
	  				</tr>
	  				<tr>
	  					<td>排序:</td>
	  					<td><input type="text" name="sort" value='${menu.sort}'/></td>
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
