<<<<<<< HEAD
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="shirox" uri="http://shiro.sifa.com/jsp/tag/shirox" %>
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
		<jsp:param name="groups" value="easyui,easyui-cs,jquery-ztree"/>
	</jsp:include>
    <style>
    	html,body{
    		background-color:#FFFFFF;
    		margin:0;
    		padding:0;
    		height:100%;
    	}
    	
    	.search-pane{
    		height:50px;
    		box-shadow:0px 0px 1px rgba(0,0,0,.6);
    		padding:10px;
    		box-sizing:border-box;
    	}
    	
    	.search-pane input[type='text']{
    		height:20px;
    		width:150px;
    	}
    	
    	.left-table{
    		width:100%;
    		height:100%;
    		display: flex;
    		flex-direction:row;
    		flex-wrap:wrap;
    	}
    	.add-btn,.del-btn,.edit-btn{
    		margin:0 2px;
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
   			},
   			data:{
   				simpleData:{
   					enable:true,
   					idKey:'id',
   					pIdKey:'pId',
   					rootPId:null
   				}
   			},
   			callback:{
   				onClick:function(event, treeId, treeNode){
   					$("#userId").val(treeNode.id);
   					refreshTable();
   				}
   			}
    	}
    	$(function(){
    		$("#list").datagrid({
			    columns:[[
                        { field: 'id', align: 'center', width: 80, title: '主键',checkbox:true },
                        { field: 'name', align: 'center', width: 150, title: '岗位名称' },
                        { field: 'remarks', align: 'remarks', width: 150, title: '岗位描述' },
                        { field: 'n', align: 'n', width: 140,  title: '操作' }
				]],
				idField: 'id',
				url: '${ctx}/authority/user-roles',
				loadFilter: function (data) {
					if(!data || data.success !== undefined){
						createDialog({message:'获取岗位列表失败,'+(data==null?'':data.message),type:'error'}).show();
					}
					return {  
						"total":data.records,
						"rows":data.rows  
					};
				},
				border:true,
				fit: true,//自动补全 
				fitColumns:false,
				/*onClickRow: DzskDetails,*///行的点击事件
				singleSelect:true,//如果是单选这个属性必须设置，此属性如果不设置就是多选
				pagination: false,//分页
				rownumbers: true//行数
			});
    		
    		//加载所有的用户
    		innerTree();
    		$("#grent-btn").click(function(){
    			var nodes = treeObj.getCheckedNodes(true);
    			if(!nodes || nodes.length == 0){
    				createDialog({message:'请选择用户',type:'alert'}).show();
    				return;
    			}
    			var ids = '';
    			for(var i in nodes){
    				if(ids){
    					ids+=',';
    				}
    				ids+=nodes[i].id;
    			}
    			createWindow({url:'${ctx}/pages/sys/authority_setting/menu_role_list.jsp',title:'授权',data:{ids:ids,afterSuccess:refreshTable()}}).open();
    		});
    		$("#clear-btn").click(function(){
    			var nodes = treeObj.getCheckedNodes(true);
    			if(!nodes || nodes.length == 0){
    				createDialog({message:'请选择用户',type:'alert'}).show();
    				return;
    			}
    			createDialog({message:'真的要移除这些用户的权限吗？',type:'confirm',accept:function(){
    				var ids = '';
        			for(var i in nodes){
        				if(ids){
        					ids+=',';
        				}
        				ids+=nodes[i].id;
        			}
        			$.requestAjax({
        				url:'${ctx}/authority/remove-roles',
        				data:{userIds:ids},
        				dataType:'json',
        				type:'post',
        				success:function(data){
        					if(data&&data.success === true){
        						createDialog({message:'取消岗位成功',type:'success'}).show();
        						refreshTable();
        					}else{
        						openDialog({message:'取消岗位失败:'+(data==null?'':data.message),type:'success'}).show();
        					}
        				}
        			});
    			}}).show();
    		});
    		$("#view-btn").click(function(){
    			var nodes = treeObj.getSelectedNodes();
    			if(!nodes || nodes.length == 0){
    				createDialog({message:'请选择一个用户',type:'alert'}).show();
    				return;
    			}
    			if(nodes.length>1){
    				createDialog({message:'只能查看一个用户',type:'alert'}).show();
    				return;
    			}
    			createWindow({title:'查看用户'+nodes[0].name+'的菜单',url:'${ctx}/pages/sys/authority_setting/user_menus.jsp',data:{userId:nodes[0].id}}).open();
    		});
    		$("#remove-btn").click(function(){
    			var nodes = treeObj.getSelectedNodes();
       			if(!nodes || nodes.length == 0){
       				createDialog({message:'请选择用户',type:'alert'});
       				return;
       			}
       			if(nodes.length > 1){
       				createDialog({message:'只能选择一个用户',type:'alert'}).show();
       				return;
       			}
       			var userId = nodes[0].id;
       			var roleIds = '';
       			var selectObj = selectData('list');
       			if(!selectObj){
       				createDialog({message:'请选择岗位',type:'alert'}).show();
       				return;
       			}else{
       				if(selectObj.length){
       					for(var i in selectObj){
       						if(roleIds){
       							roleIds+=',';
       						}
       						roleIds+=selectObj.id;
       					}
       				}else{
       					roleIds = selectObj.id;
       				}
       			}
       			removeOneRole(userId,roleIds,nodes[0].name);
    		});
    	})
    	
    	function innerTree(){
    		if(treeObj){
    			treeObj.destory();
    		}
    		$.requestAjax({
    			url:'${ctx}/authority/users',
    			data:{name:$("#userName").val()},
    			dataType:'json',
    			type:'get',
    			success:function(data){
    				if(data && data.success === true){
    					data.obj.push({name:'用户列表',id:'1',nocheck:true});
	    				treeObj = $.fn.zTree.init($("#ztree"),setting,data.obj);
	    				treeObj.expandAll(true);
    				}else{
    					createDialog({message:'用户加载失败',type:'alert'}).show();
    				}
    				
    			}
    		});
    	}
    	
    	//刷新列表
    	function refreshTable(){
    		refreshTableData("roleForm","list");
    	}
    	function showOneRoleMenu(id,name){
			createWindow({title:'查看岗位'+name+'的菜单',url:'${ctx}/pages/sifa/authority_setting/role_menus.jsp',data:{roleId:id}}).show();
    	}
    	function removeOneRole(userId,roleIds,userName){
   			if(!userId){
   				createDialog({message:'未找到用户',type:'alert'}).show();
   				return;
   			}
   			if(!roleIds){
   				createDialog({message:'未找到岗位',type:'alert'}).show();
   				return;
   			}
   			createDialog({title:'真的要移除用户'+(userName||'')+'的岗位吗？',type:'confirm',accept:function(){
       			$.requestAjax({
       				url:'${ctx}/authority/remove-user-role',
       				data:{userId:userId,roleIds:roleIds},
       				dataType:'json',
       				type:'post',
       				success:function(data){
       					if(data&&data.success === true){
       						createDialog({message:'取消岗位成功',type:'success'}).show();
       						refreshTable();
       					}else{
       						createDialog({title:'岗位失败:'+data.message,type:'alert'}).show();
       					}
       				}
       			});
   			}}).show();
    	}
    </script>
  </head>
  
  <body>
  	<div class="search-pane">
  		<div style="float:left;">
	  		<form id="searchForm">
	  			<input type="text" placeholder="请输入账号名称" name="name"/>
	  			<!-- <span id="search-btn" class="ico-btn ico-hover ico-content ico-search">查询</span> -->
	  			<shirox:permission permissions="user:w or admin:w" name="span" a_add_attr="id='add'|style='color:#000000'" u_add_attr="style='color:#EEEEEE'">
	  				<accept>
	  					<span id="grent-btn" class="ico-btn ico-hover ico-content ico-ok">授权</span>
	  					<span id="clear-btn" class="ico-btn ico-hover ico-content ico-remove">取消授权</span>
	  				</accept>
	  				<unaccept>
	  					<span class="ico-btn ico-content ico-ok-disabled" style="color:#AAAAAA;cursor:default;">授权</span>
	  					<span class="ico-btn ico-content ico-remove-disabled" style="color:#AAAAAA;cursor:default;">取消授权</span>
	  				</unaccept>
	  			</shirox:permission>
	  		</form>
	  		<form id="roleForm" style="display:hidden">
	  			<input type="hidden" name="userId" id="userId"/>
	  		</form>
  		</div>
  	</div>
  	<div style="position:absolute;top:50px;bottom:0px;left:0px;width:200px;overflow:scroll;">
  		<div id="ztree" class="ztree"></div>
  	</div>
  	<div style="position:absolute;top:50px;bottom:0px;left:200px;right:0px;height:25px;">
		<shirox:permission name="span" permissions="user:r or admin:r" a_add_attr="id='view-btn'|class='ico-btn ico-hover ico-content ico-view'" u_add_attr="class='ico-btn ico-content ico-remove-disabled'|style='color:#AAAAAA;cursor:default;'">查看菜单</shirox:permission>
		<shirox:permission permissions="user:w or admin:w" name="span">
			<accept>
				<span class='ico-btn ico-hover ico-content ico-remove' id='remove-btn'>移除</span>
			</accept>
			<unaccept>
				<span class='ico-btn ico-content ico-remove-disabled' style='color:#AAAAAA;cursor:default;'>移除</span>
			</unaccept>
		</shirox:permission>
 		</div>
  	<div style="position:absolute;top:75px;bottom:0px;left:200px;right:0px;" id="listRoot">
  		<table id="list"></table>
  	</div>
  </body>
</html>
=======
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="shirox" uri="http://shiro.sifa.com/jsp/tag/shirox" %>
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
		<jsp:param name="groups" value="easyui,easyui-cs,jquery-ztree"/>
	</jsp:include>
    <style>
    	html,body{
    		background-color:#FFFFFF;
    		margin:0;
    		padding:0;
    		height:100%;
    	}
    	
    	.search-pane{
    		height:50px;
    		box-shadow:0px 0px 1px rgba(0,0,0,.6);
    		padding:10px;
    		box-sizing:border-box;
    	}
    	
    	.search-pane input[type='text']{
    		height:20px;
    		width:150px;
    	}
    	
    	.left-table{
    		width:100%;
    		height:100%;
    		display: flex;
    		flex-direction:row;
    		flex-wrap:wrap;
    	}
    	.add-btn,.del-btn,.edit-btn{
    		margin:0 2px;
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
   			},
   			data:{
   				simpleData:{
   					enable:true,
   					idKey:'id',
   					pIdKey:'pId',
   					rootPId:null
   				}
   			},
   			callback:{
   				onClick:function(event, treeId, treeNode){
   					$("#userId").val(treeNode.id);
   					refreshTable();
   				}
   			}
    	}
    	$(function(){
    		$("#list").datagrid({
			    columns:[[
                        { field: 'id', align: 'center', width: 80, title: '主键',checkbox:true },
                        { field: 'name', align: 'center', width: 150, title: '岗位名称' },
                        { field: 'remarks', align: 'remarks', width: 150, title: '岗位描述' },
                        { field: 'n', align: 'n', width: 140,  title: '操作' }
				]],
				idField: 'id',
				url: '${ctx}/authority/user-roles',
				loadFilter: function (data) {
					if(!data || data.success !== undefined){
						createDialog({message:'获取岗位列表失败,'+(data==null?'':data.message),type:'error'}).show();
					}
					return {  
						"total":data.records,
						"rows":data.rows  
					};
				},
				border:true,
				fit: true,//自动补全 
				fitColumns:false,
				/*onClickRow: DzskDetails,*///行的点击事件
				singleSelect:true,//如果是单选这个属性必须设置，此属性如果不设置就是多选
				pagination: false,//分页
				rownumbers: true//行数
			});
    		
    		//加载所有的用户
    		innerTree();
    		$("#grent-btn").click(function(){
    			var nodes = treeObj.getCheckedNodes(true);
    			if(!nodes || nodes.length == 0){
    				createDialog({message:'请选择用户',type:'alert'}).show();
    				return;
    			}
    			var ids = '';
    			for(var i in nodes){
    				if(ids){
    					ids+=',';
    				}
    				ids+=nodes[i].id;
    			}
    			createWindow({url:'${ctx}/pages/sys/authority_setting/menu_role_list.jsp',title:'授权',data:{ids:ids,afterSuccess:refreshTable()}}).open();
    		});
    		$("#clear-btn").click(function(){
    			var nodes = treeObj.getCheckedNodes(true);
    			if(!nodes || nodes.length == 0){
    				createDialog({message:'请选择用户',type:'alert'}).show();
    				return;
    			}
    			createDialog({message:'真的要移除这些用户的权限吗？',type:'confirm',accept:function(){
    				var ids = '';
        			for(var i in nodes){
        				if(ids){
        					ids+=',';
        				}
        				ids+=nodes[i].id;
        			}
        			$.requestAjax({
        				url:'${ctx}/authority/remove-roles',
        				data:{userIds:ids},
        				dataType:'json',
        				type:'post',
        				success:function(data){
        					if(data&&data.success === true){
        						createDialog({message:'取消岗位成功',type:'success'}).show();
        						refreshTable();
        					}else{
        						openDialog({message:'取消岗位失败:'+(data==null?'':data.message),type:'success'}).show();
        					}
        				}
        			});
    			}}).show();
    		});
    		$("#view-btn").click(function(){
    			var nodes = treeObj.getSelectedNodes();
    			if(!nodes || nodes.length == 0){
    				createDialog({message:'请选择一个用户',type:'alert'}).show();
    				return;
    			}
    			if(nodes.length>1){
    				createDialog({message:'只能查看一个用户',type:'alert'}).show();
    				return;
    			}
    			createWindow({title:'查看用户'+nodes[0].name+'的菜单',url:'${ctx}/pages/sys/authority_setting/user_menus.jsp',data:{userId:nodes[0].id}}).open();
    		});
    		$("#remove-btn").click(function(){
    			var nodes = treeObj.getSelectedNodes();
       			if(!nodes || nodes.length == 0){
       				createDialog({message:'请选择用户',type:'alert'});
       				return;
       			}
       			if(nodes.length > 1){
       				createDialog({message:'只能选择一个用户',type:'alert'}).show();
       				return;
       			}
       			var userId = nodes[0].id;
       			var roleIds = '';
       			var selectObj = selectData('list');
       			if(!selectObj){
       				createDialog({message:'请选择岗位',type:'alert'}).show();
       				return;
       			}else{
       				if(selectObj.length){
       					for(var i in selectObj){
       						if(roleIds){
       							roleIds+=',';
       						}
       						roleIds+=selectObj.id;
       					}
       				}else{
       					roleIds = selectObj.id;
       				}
       			}
       			removeOneRole(userId,roleIds,nodes[0].name);
    		});
    	})
    	
    	function innerTree(){
    		if(treeObj){
    			treeObj.destory();
    		}
    		$.requestAjax({
    			url:'${ctx}/authority/users',
    			data:{name:$("#userName").val()},
    			dataType:'json',
    			type:'get',
    			success:function(data){
    				if(data && data.success === true){
    					data.obj.push({name:'用户列表',id:'1',nocheck:true});
	    				treeObj = $.fn.zTree.init($("#ztree"),setting,data.obj);
	    				treeObj.expandAll(true);
    				}else{
    					createDialog({message:'用户加载失败',type:'alert'}).show();
    				}
    				
    			}
    		});
    	}
    	
    	//刷新列表
    	function refreshTable(){
    		refreshTableData("roleForm","list");
    	}
    	function showOneRoleMenu(id,name){
			createWindow({title:'查看岗位'+name+'的菜单',url:'${ctx}/pages/sifa/authority_setting/role_menus.jsp',data:{roleId:id}}).show();
    	}
    	function removeOneRole(userId,roleIds,userName){
   			if(!userId){
   				createDialog({message:'未找到用户',type:'alert'}).show();
   				return;
   			}
   			if(!roleIds){
   				createDialog({message:'未找到岗位',type:'alert'}).show();
   				return;
   			}
   			createDialog({title:'真的要移除用户'+(userName||'')+'的岗位吗？',type:'confirm',accept:function(){
       			$.requestAjax({
       				url:'${ctx}/authority/remove-user-role',
       				data:{userId:userId,roleIds:roleIds},
       				dataType:'json',
       				type:'post',
       				success:function(data){
       					if(data&&data.success === true){
       						createDialog({message:'取消岗位成功',type:'success'}).show();
       						refreshTable();
       					}else{
       						createDialog({title:'岗位失败:'+data.message,type:'alert'}).show();
       					}
       				}
       			});
   			}}).show();
    	}
    </script>
  </head>
  
  <body>
  	<div class="search-pane">
  		<div style="float:left;">
	  		<form id="searchForm">
	  			<input type="text" placeholder="请输入账号名称" name="name"/>
	  			<!-- <span id="search-btn" class="ico-btn ico-hover ico-content ico-search">查询</span> -->
	  			<shirox:permission permissions="user:w or admin:w" name="span" a_add_attr="id='add'|style='color:#000000'" u_add_attr="style='color:#EEEEEE'">
	  				<accept>
	  					<span id="grent-btn" class="ico-btn ico-hover ico-content ico-ok">授权</span>
	  					<span id="clear-btn" class="ico-btn ico-hover ico-content ico-remove">取消授权</span>
	  				</accept>
	  				<unaccept>
	  					<span class="ico-btn ico-content ico-ok-disabled" style="color:#AAAAAA;cursor:default;">授权</span>
	  					<span class="ico-btn ico-content ico-remove-disabled" style="color:#AAAAAA;cursor:default;">取消授权</span>
	  				</unaccept>
	  			</shirox:permission>
	  		</form>
	  		<form id="roleForm" style="display:hidden">
	  			<input type="hidden" name="userId" id="userId"/>
	  		</form>
  		</div>
  	</div>
  	<div style="position:absolute;top:50px;bottom:0px;left:0px;width:200px;overflow:scroll;">
  		<div id="ztree" class="ztree"></div>
  	</div>
  	<div style="position:absolute;top:50px;bottom:0px;left:200px;right:0px;height:25px;">
		<shirox:permission name="span" permissions="user:r or admin:r" a_add_attr="id='view-btn'|class='ico-btn ico-hover ico-content ico-view'" u_add_attr="class='ico-btn ico-content ico-remove-disabled'|style='color:#AAAAAA;cursor:default;'">查看菜单</shirox:permission>
		<shirox:permission permissions="user:w or admin:w" name="span">
			<accept>
				<span class='ico-btn ico-hover ico-content ico-remove' id='remove-btn'>移除</span>
			</accept>
			<unaccept>
				<span class='ico-btn ico-content ico-remove-disabled' style='color:#AAAAAA;cursor:default;'>移除</span>
			</unaccept>
		</shirox:permission>
 		</div>
  	<div style="position:absolute;top:75px;bottom:0px;left:200px;right:0px;" id="listRoot">
  		<table id="list"></table>
  	</div>
  </body>
</html>
>>>>>>> 17ac48076d7a7be7437c8367312af01562698b1c
