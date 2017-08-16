<<<<<<< HEAD
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="police" uri="http://aofa.com/jsp/jstl/function" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
		<jsp:param name="groups" value="jquery-form,easyui,easyui-cs"/>
	</jsp:include>
    <style>
    	html,body{
    		background-color:#FFFFFF;
    		margin:0;
    		padding:0;
    		height:100%;
    	}
    	
    	.search-pane{
    		box-shadow:0px 0px 1px rgba(0,0,0,.6);
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
    	.search-pane table td input[type='text'],
    	.search-pane table td select{
    		height:20px;
    		border:1px solid #CCCCCC;
    		border-radius:5px;
    		width:200px;
    		box-sizing: content-box;
    		padding:1px;
    	}
    </style>
    <script>
    	$(function(){
    		$("#list").datagrid({
			    columns:[[
                        { field: 'id', align: 'center', width: 80, title: '主键',checkbox:true },
                        { field: 'name', align: 'center', width: 150, title: '角色名称' },
                        { field: 'remarks', align: 'center', width: 140,  title: '角色描述' },
						{ field: 'enabled', align: 'center',width: 100, title: '是否启用' }
				]],
				idField: 'id',
				url: '${ctx}/menu/role/list',
				loadFilter: function (data) {
					if(!data || data.success !== undefined){
						createDialog({message:'获取用户列表失败,'+(data==null?'':data.message),type:'error'}).show();
						return;
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
				pagination: true,//分页
				rownumbers: true//行数
			});
    		$("#add-btn").click(function(){
    			//在父窗口中弹出此窗口
    			var cw = createWindow({title:'添加岗位',url:'${ctx}/menu/role/edit',closeBtn:true,data:refreshTable});
    			cw.open();
    			//openDialog({title:'添加岗位',width:'600px',height:'400px',lock:true,url:'${ctx}/menu/role/edit',type:1},refreshTable);
    		});
    		$("#edit-btn").click(function(){
    			var selObj = selectData('list');
    			if(selObj == null){
    				createDialog({message:'请选择一条记录',type:"alert"}).show();
    				return;
    			}
    			if(selObj.length && selObj.length != 1){
    				createDialog({message:'只能选择一条记录进行编辑',type:"alert"}).show();
    				return;
    			}
    			createWindow({title:'修改岗位',url:'${ctx}/menu/role/edit?id='+selObj.id,data:refreshTable}).open();
    		});
    		$("#del-btn").click(function(){
    			var selObj = $("#list").datagrid("getSelected");
    			if(selObj == null){
    				createDialog({message:'请选择一条记录',type:"alert"}).show();
    				return;
    			}
    			if(selObj.length && selObj.length != 1){
    				createDialog({message:'为了数据安全，一次只能删除一条数据',type:"alert"}).show();
    				return;
    			}
    			createDialog({message:'确定要删除吗？',type:'confirm',accept:function(){
	    				$.ajax({
	        				data:{id:selObj.id},
	        				dataType:'json',
	        				url:'${ctx}/menu/role/delete',
	        				success:function(data){
	        					if(data.success === true){
	        						refreshTable();
	        						createDialog({message:'删除成功',type:'success'}).show();
	        					}else{
	        						createDialog({message:'删除失败：'+data.message,type:'error'}).show();
	        					}
	       					}
	       				});
    				}
    			}).show();
    		});
    		$("#search-btn").click(function(){
    			refreshTable();
    		});
    	})
    	//刷新列表
    	function refreshTable(){
    		refreshTableData('searchForm','list');
    	}
    </script>
  </head>
  
  <body>
  	<div class="search-pane">
	  	<table style="width:100%;">
	  		<tr>
	  			<td>
	  				<form id="searchForm">
			  			<table>
			  				<tr>
			  					<td><input type="text" placeholder="请输入角色名称" name="name"/></td>
			  					<td rowspan="2" valign="middle">
			  						<span id="search-btn" class="ico-btn ico-hover ico-content ico-search">查询</span>
			  					</td>
			  				</tr>
			  			</table>
	  				</form>
	  			</td>
	  			<td style="width:200px;">
	  				<%-- <shirox:permission permissions="admin:w or user:w" name="span" a_add_attr="class='ico-btn ico-hover ico-content ico-add'|id='add-btn'" u_add_attr="style='color:#CCCCCC;'">添加</shirox:permission> --%>
  					<shirox:permission permissions="admin:w or user:w" name="div" className="control">
  						<accept>
  							<span id='add-btn' class="ico-btn ico-hover ico-content ico-add">添加</span>
  							<span id='edit-btn' class="ico-btn ico-hover ico-content ico-edit">修改</span>
  							<span id='del-btn' class="ico-btn ico-hover ico-content ico-remove">删除</span>
  						</accept>
  						<unaccept>
  							<span class="ico-btn ico-content ico-add-disabled" style="color:#AAAAAA;cursor:default;">添加</span>
  							<span class="ico-btn ico-content ico-edit-disabled" style="color:#AAAAAA;cursor:default;">修改</span>
  							<span class="ico-btn ico-content ico-remove-disabled" style="color:#AAAAAA;cursor:default;">删除</span>
  						</unaccept>
  					</shirox:permission>
	  			</td>
	  		</tr>
	  	</table>
  	</div>
  	<div style="position:absolute;top:36px;bottom:0px;left:0px;right:0px;">
  		<table id="list"></table>
  	</div>
  	<div id="pager"></div>
  </body>
</html>
=======
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="police" uri="http://aofa.com/jsp/jstl/function" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
		<jsp:param name="groups" value="jquery-form,easyui,easyui-cs"/>
	</jsp:include>
    <style>
    	html,body{
    		background-color:#FFFFFF;
    		margin:0;
    		padding:0;
    		height:100%;
    	}
    	
    	.search-pane{
    		box-shadow:0px 0px 1px rgba(0,0,0,.6);
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
    	.search-pane table td input[type='text'],
    	.search-pane table td select{
    		height:20px;
    		border:1px solid #CCCCCC;
    		border-radius:5px;
    		width:200px;
    		box-sizing: content-box;
    		padding:1px;
    	}
    </style>
    <script>
    	$(function(){
    		$("#list").datagrid({
			    columns:[[
                        { field: 'id', align: 'center', width: 80, title: '主键',checkbox:true },
                        { field: 'name', align: 'center', width: 150, title: '角色名称' },
                        { field: 'remarks', align: 'center', width: 140,  title: '角色描述' },
						{ field: 'enabled', align: 'center',width: 100, title: '是否启用' }
				]],
				idField: 'id',
				url: '${ctx}/menu/role/list',
				loadFilter: function (data) {
					if(!data || data.success !== undefined){
						createDialog({message:'获取用户列表失败,'+(data==null?'':data.message),type:'error'}).show();
						return;
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
				pagination: true,//分页
				rownumbers: true//行数
			});
    		$("#add-btn").click(function(){
    			//在父窗口中弹出此窗口
    			var cw = createWindow({title:'添加岗位',url:'${ctx}/menu/role/edit',closeBtn:true,data:refreshTable});
    			cw.open();
    			//openDialog({title:'添加岗位',width:'600px',height:'400px',lock:true,url:'${ctx}/menu/role/edit',type:1},refreshTable);
    		});
    		$("#edit-btn").click(function(){
    			var selObj = selectData('list');
    			if(selObj == null){
    				createDialog({message:'请选择一条记录',type:"alert"}).show();
    				return;
    			}
    			if(selObj.length && selObj.length != 1){
    				createDialog({message:'只能选择一条记录进行编辑',type:"alert"}).show();
    				return;
    			}
    			createWindow({title:'修改岗位',url:'${ctx}/menu/role/edit?id='+selObj.id,data:refreshTable}).open();
    		});
    		$("#del-btn").click(function(){
    			var selObj = $("#list").datagrid("getSelected");
    			if(selObj == null){
    				createDialog({message:'请选择一条记录',type:"alert"}).show();
    				return;
    			}
    			if(selObj.length && selObj.length != 1){
    				createDialog({message:'为了数据安全，一次只能删除一条数据',type:"alert"}).show();
    				return;
    			}
    			createDialog({message:'确定要删除吗？',type:'confirm',accept:function(){
	    				$.ajax({
	        				data:{id:selObj.id},
	        				dataType:'json',
	        				url:'${ctx}/menu/role/delete',
	        				success:function(data){
	        					if(data.success === true){
	        						refreshTable();
	        						createDialog({message:'删除成功',type:'success'}).show();
	        					}else{
	        						createDialog({message:'删除失败：'+data.message,type:'error'}).show();
	        					}
	       					}
	       				});
    				}
    			}).show();
    		});
    		$("#search-btn").click(function(){
    			refreshTable();
    		});
    	})
    	//刷新列表
    	function refreshTable(){
    		refreshTableData('searchForm','list');
    	}
    </script>
  </head>
  
  <body>
  	<div class="search-pane">
	  	<table style="width:100%;">
	  		<tr>
	  			<td>
	  				<form id="searchForm">
			  			<table>
			  				<tr>
			  					<td><input type="text" placeholder="请输入角色名称" name="name"/></td>
			  					<td rowspan="2" valign="middle">
			  						<span id="search-btn" class="ico-btn ico-hover ico-content ico-search">查询</span>
			  					</td>
			  				</tr>
			  			</table>
	  				</form>
	  			</td>
	  			<td style="width:200px;">
	  				<%-- <shirox:permission permissions="admin:w or user:w" name="span" a_add_attr="class='ico-btn ico-hover ico-content ico-add'|id='add-btn'" u_add_attr="style='color:#CCCCCC;'">添加</shirox:permission> --%>
  					<shirox:permission permissions="admin:w or user:w" name="div" className="control">
  						<accept>
  							<span id='add-btn' class="ico-btn ico-hover ico-content ico-add">添加</span>
  							<span id='edit-btn' class="ico-btn ico-hover ico-content ico-edit">修改</span>
  							<span id='del-btn' class="ico-btn ico-hover ico-content ico-remove">删除</span>
  						</accept>
  						<unaccept>
  							<span class="ico-btn ico-content ico-add-disabled" style="color:#AAAAAA;cursor:default;">添加</span>
  							<span class="ico-btn ico-content ico-edit-disabled" style="color:#AAAAAA;cursor:default;">修改</span>
  							<span class="ico-btn ico-content ico-remove-disabled" style="color:#AAAAAA;cursor:default;">删除</span>
  						</unaccept>
  					</shirox:permission>
	  			</td>
	  		</tr>
	  	</table>
  	</div>
  	<div style="position:absolute;top:36px;bottom:0px;left:0px;right:0px;">
  		<table id="list"></table>
  	</div>
  	<div id="pager"></div>
  </body>
</html>
>>>>>>> 17ac48076d7a7be7437c8367312af01562698b1c
