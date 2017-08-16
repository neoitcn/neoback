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
		<jsp:param name="groups" value="easyui-cs,easyui,jqGrid,jquery-validate,jquery-ztree"/>
	</jsp:include>
    <style>
    	html,body{
    		background-color:#FFFFFF;
    		margin:0;
    		padding:0;
    		height:100%;
    	}
    	.containter{
    		width:100%;
    		height:100%;
    		display: flex;
    		flex-direction:row;
    		flex-wrap:wrap;
    	}
    	.add-btn,.del-btn,.edit-btn{
    		margin:0 2px;
    	}
    	.menu-table{
    		border-collapse: collapse;
    		border:1px solid #DDDDDD;
    	}
    	.menu-table th,
    	.menu-table td{
    		padding:4px 6px;
    		font-weight:lighter;
    	}
    	.menu-table th{
    		background:linear-gradient(to bottom,#FFFFFF 0,#F0F0F0 100%);
    	}
    	tr:nth-child(2n){
    		background-color:#EEEEEE;
    	}
    </style>
    <script>
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
					if(treeNode.id==null){
						$("#menu-table tbody").html('');
						return;
					}
					$.requestAjax({
		    			dataType:'json',
		    			url:'${ctx}/menu/child',
		    			data:{id:treeNode.id},
		    			success:function(data){
		    				try{
		    					if(data.success === false){
			    					throw new Error();
			    				}
		    					var html='';
		    					var obj = data.obj;
		    					if(obj){
		    						for(var key in obj){
		    							var de = obj[key];
		    							html+='<tr>';
		    							html+="<td align='center'><span style='width:16px;height:16px;display:block;background-size:16px 16px;background-repeat:no-repeat;background-image:url("+de.iconUrl+")'></span></td>";
		    							html+="<td>"+de.name+"</td>";
		    							html+="<td>"+de.controllerName+"</td>";
		    							html+="<td>"+(de.url==null?'':de.url)+"</td>";
		    							html+="<td>"+de.enabled+"</td>";
		    							html+="<td>"+de.sort+"</td>";
		    							html+="<td>"+de.createrName+"</td>";
		    							html+='</tr>';
		    						}
		    					}
		    					$("#menu-table tbody").html(html);
		    				}catch(e){
		    					openDialog({title:'加载失败',type:4});
		    				}
		    			}
		    		});
				}
			}
		}
    	$(function(){
    		initTree();
    		$("#add-btn").click(function(){
    			openDialog({title:'添加菜单',url:'${ctx}/menu/edit',type:1,data:refreshTable});
    		});
    		$("#edit-btn").click(function(){
    			var treeNodes = treeObj.getSelectedNodes();
    			if(treeNodes.length == 1){
	    			openDialog({title:'编辑菜单',url:'${ctx}/menu/edit?id='+treeNodes[0].id,type:1,data:refreshTable});
    			}else{
    				openDialog({title:'只能选择一条记录进行编辑',type:3});
    			}
    		});
    		$("#del-btn").click(function(){
    			openDialog({title:'确定要删除吗？',type:5,func:function(){
	    			var treeNodes = treeObj.getSelectedNodes();
	    			if(treeNodes.length == 1){
	    				$.requestAjax({
	            			dataType:'json',
	            			url:'${ctx}/menu/delete',
	            			data:{id:treeNodes[0].id},
	            			success:function(data){
	            				if(data.success === true){
	            					openDialog({title:'删除成功',type:2});
	            					treeObj.removeNode(treeNodes[0]);
	            				}
	            				else{
	            					openDialog({title:'删除失败:'+data.message,type:4});
	            				}
	            			}
	            		});
	    			}else{
	    				openDialog({title:'只能选择一条记录进行删除',type:3});
	    			}
    			}});
    		});
    	})
    	function initTree(obj){
			if(treeObj){
				treeObj.destroy();
			}
			$.requestAjax({
    			dataType:'json',
    			url:'${ctx}/menu/all',
    			success:function(data){
    				treeObj = $.fn.zTree.init($("#ztree"), setting, data);
    				if(obj){
	    				var node = treeObj.getNodesByParam('id',obj.id);
	    	   			treeObj.selectNode(node);
    				}
    			}
    		});
		}
    	//刷新列表
    	function refreshTable(obj){
    		initTree(obj);
    	}
    </script>
  </head>
  
  <body>
   	<div class="containter">
   		<aside style="flex-basis:200px;box-shadow: 1px 0px 0px rgba(0,0,0,.3);position:relative;">
   			<div style="height:5px;"></div>
			<shirox:permission name="div" permissions="admin:w" style="height:30px;">
				<accept>
					<span id="add-btn" class="ico-btn ico-hover ico-content ico-add" style="margin:0px 2px;width:auto;">添加</span>
					<span id="edit-btn" class="ico-btn ico-hover ico-content ico-edit" style="margin:0px 2px;width:auto;">修改</span>
					<span id="del-btn" class="ico-btn ico-hover ico-content ico-remove" style="margin:0px 2px;width:auto;">删除</span>
				</accept>
				<unaccept>
					<span class="ico-btn ico-content ico-add-disabled" style="margin:0px 2px;width:auto;cursor:default;">添加</span>
					<span class="ico-btn ico-content ico-edit-disabled" style="margin:0px 2px;width:auto;cursor:default;">修改</span>
					<span class="ico-btn ico-content ico-remove-disabled" style="margin:0px 2px;width:auto;cursor:default;">删除</span>
				</unaccept>
			</shirox:permission>
   			<div style="position:absolute;top:35px;bottom:0px;left:0;right:0;overflow:auto;">
   				<div class="ztree" id="ztree"></div>
   			</div>
   		</aside>
   		<div style="flex-grow:3;">
   			<div style="overflow:auto;position:absolute;left:210px;right:10px;bottom:0px;top:10px;">
   				<table class="menu-table" id="menu-table" border="1">
   					<thead>
   						<tr>
   							<th width="80">菜单图标</th>
   							<th width="150">菜单名称</th>
   							<th width="120">菜单控制</th>
   							<th width="200">链接地址</th>
   							<th width="80">是否启用</th>
   							<th width="50">排序</th>
   							<th width="80">创建者</th>
   						</tr>
   					</thead>
   					<tbody></tbody>
   				</table>
   			</div>
   		</div>
   	</div>
   	<div></div>
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
		<jsp:param name="groups" value="easyui-cs,easyui,jqGrid,jquery-validate,jquery-ztree"/>
	</jsp:include>
    <style>
    	html,body{
    		background-color:#FFFFFF;
    		margin:0;
    		padding:0;
    		height:100%;
    	}
    	.containter{
    		width:100%;
    		height:100%;
    		display: flex;
    		flex-direction:row;
    		flex-wrap:wrap;
    	}
    	.add-btn,.del-btn,.edit-btn{
    		margin:0 2px;
    	}
    	.menu-table{
    		border-collapse: collapse;
    		border:1px solid #DDDDDD;
    	}
    	.menu-table th,
    	.menu-table td{
    		padding:4px 6px;
    		font-weight:lighter;
    	}
    	.menu-table th{
    		background:linear-gradient(to bottom,#FFFFFF 0,#F0F0F0 100%);
    	}
    	tr:nth-child(2n){
    		background-color:#EEEEEE;
    	}
    </style>
    <script>
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
					if(treeNode.id==null){
						$("#menu-table tbody").html('');
						return;
					}
					$.requestAjax({
		    			dataType:'json',
		    			url:'${ctx}/menu/child',
		    			data:{id:treeNode.id},
		    			success:function(data){
		    				try{
		    					if(data.success === false){
			    					throw new Error();
			    				}
		    					var html='';
		    					var obj = data.obj;
		    					if(obj){
		    						for(var key in obj){
		    							var de = obj[key];
		    							html+='<tr>';
		    							html+="<td align='center'><span style='width:16px;height:16px;display:block;background-size:16px 16px;background-repeat:no-repeat;background-image:url("+de.iconUrl+")'></span></td>";
		    							html+="<td>"+de.name+"</td>";
		    							html+="<td>"+de.controllerName+"</td>";
		    							html+="<td>"+(de.url==null?'':de.url)+"</td>";
		    							html+="<td>"+de.enabled+"</td>";
		    							html+="<td>"+de.sort+"</td>";
		    							html+="<td>"+de.createrName+"</td>";
		    							html+='</tr>';
		    						}
		    					}
		    					$("#menu-table tbody").html(html);
		    				}catch(e){
		    					openDialog({title:'加载失败',type:4});
		    				}
		    			}
		    		});
				}
			}
		}
    	$(function(){
    		initTree();
    		$("#add-btn").click(function(){
    			openDialog({title:'添加菜单',url:'${ctx}/menu/edit',type:1,data:refreshTable});
    		});
    		$("#edit-btn").click(function(){
    			var treeNodes = treeObj.getSelectedNodes();
    			if(treeNodes.length == 1){
	    			openDialog({title:'编辑菜单',url:'${ctx}/menu/edit?id='+treeNodes[0].id,type:1,data:refreshTable});
    			}else{
    				openDialog({title:'只能选择一条记录进行编辑',type:3});
    			}
    		});
    		$("#del-btn").click(function(){
    			openDialog({title:'确定要删除吗？',type:5,func:function(){
	    			var treeNodes = treeObj.getSelectedNodes();
	    			if(treeNodes.length == 1){
	    				$.requestAjax({
	            			dataType:'json',
	            			url:'${ctx}/menu/delete',
	            			data:{id:treeNodes[0].id},
	            			success:function(data){
	            				if(data.success === true){
	            					openDialog({title:'删除成功',type:2});
	            					treeObj.removeNode(treeNodes[0]);
	            				}
	            				else{
	            					openDialog({title:'删除失败:'+data.message,type:4});
	            				}
	            			}
	            		});
	    			}else{
	    				openDialog({title:'只能选择一条记录进行删除',type:3});
	    			}
    			}});
    		});
    	})
    	function initTree(obj){
			if(treeObj){
				treeObj.destroy();
			}
			$.requestAjax({
    			dataType:'json',
    			url:'${ctx}/menu/all',
    			success:function(data){
    				treeObj = $.fn.zTree.init($("#ztree"), setting, data);
    				if(obj){
	    				var node = treeObj.getNodesByParam('id',obj.id);
	    	   			treeObj.selectNode(node);
    				}
    			}
    		});
		}
    	//刷新列表
    	function refreshTable(obj){
    		initTree(obj);
    	}
    </script>
  </head>
  
  <body>
   	<div class="containter">
   		<aside style="flex-basis:200px;box-shadow: 1px 0px 0px rgba(0,0,0,.3);position:relative;">
   			<div style="height:5px;"></div>
			<shirox:permission name="div" permissions="admin:w" style="height:30px;">
				<accept>
					<span id="add-btn" class="ico-btn ico-hover ico-content ico-add" style="margin:0px 2px;width:auto;">添加</span>
					<span id="edit-btn" class="ico-btn ico-hover ico-content ico-edit" style="margin:0px 2px;width:auto;">修改</span>
					<span id="del-btn" class="ico-btn ico-hover ico-content ico-remove" style="margin:0px 2px;width:auto;">删除</span>
				</accept>
				<unaccept>
					<span class="ico-btn ico-content ico-add-disabled" style="margin:0px 2px;width:auto;cursor:default;">添加</span>
					<span class="ico-btn ico-content ico-edit-disabled" style="margin:0px 2px;width:auto;cursor:default;">修改</span>
					<span class="ico-btn ico-content ico-remove-disabled" style="margin:0px 2px;width:auto;cursor:default;">删除</span>
				</unaccept>
			</shirox:permission>
   			<div style="position:absolute;top:35px;bottom:0px;left:0;right:0;overflow:auto;">
   				<div class="ztree" id="ztree"></div>
   			</div>
   		</aside>
   		<div style="flex-grow:3;">
   			<div style="overflow:auto;position:absolute;left:210px;right:10px;bottom:0px;top:10px;">
   				<table class="menu-table" id="menu-table" border="1">
   					<thead>
   						<tr>
   							<th width="80">菜单图标</th>
   							<th width="150">菜单名称</th>
   							<th width="120">菜单控制</th>
   							<th width="200">链接地址</th>
   							<th width="80">是否启用</th>
   							<th width="50">排序</th>
   							<th width="80">创建者</th>
   						</tr>
   					</thead>
   					<tbody></tbody>
   				</table>
   			</div>
   		</div>
   	</div>
   	<div></div>
  </body>
</html>
>>>>>>> 17ac48076d7a7be7437c8367312af01562698b1c
