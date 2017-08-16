<<<<<<< HEAD
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="police" uri="http://aofa.com/jsp/jstl/function" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
		<jsp:param name="groups" value="easyui,easyui-cs"/>
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
     var dataObj = getData();
     var ids = dataObj.ids;
    	$(function(){
    		$("#list").datagrid({
			    columns:[[
                        { field: 'id', align: 'center', width: 80, title: '主键',checkbox:true },
                        { field: 'name', align: 'center', width: 150, title: '岗位名称' },
                        { field: 'remark', align: 'center', width: 140,  title: '岗位描述' }
				]],
				idField: 'id',
				url: '${ctx}/authority/roles',
				loadFilter: function (data) {
					if(!data||data.success !== undefined){
						createDialog({message:'获取岗位列表失败,'+(data==null?'':data.message),type:'error'}).show();
						return;
					}
					return {  
						"total":data.records,  
						"rows":data.rows  
					}; 
				},
				onLoadSuccess:function(data){
					if(data && data.rows){
						for(var i in data.rows){
							if(rows[i].checked){
								$('#list').datagrid("selectRecord", rows[i].id);
							}
						}
					}
				},
				border:true,
				fit: true,//自动补全 
				fitColumns:false,
				/*onClickRow: DzskDetails,*///行的点击事件
				singleSelect:false,//如果是单选这个属性必须设置，此属性如果不设置就是多选
				pagination: false,//分页
				rownumbers: true//行数
			});
    		$("#search-btn").click(function(){
    			refreshTable();
    		});
    		$("#save-btn").click(function(){
    			var selObj = selectData("list");
    			if(selObj == null){
    				createDialog({message:'请选择一条记录',type:'alert'}).show();
    				return;
    			}
    			var roleIds = '';
    			if(!selObj.length){
    				roleIds = selObj.id;
    			}else{
    				for(var i in selObj){
    					if(roleIds){
    						roleIds+=',';
    					}
    					roleIds+=selObj[i].id;
    				}
    			}
    			$.ajax({
    				url:'${ctx}/authority/grant-roles',
    				data:{userIds:ids,roleIds:roleIds},
    				dataType:'json',
    				type:'post',
    				success:function(data){
    					if(data&&data.success === true){
    						createDialog({message:'授权成功',type:'success',accept:function(){
    							closeWin();
    						}}).show();
    						dataObj.afterSuccess();
    					}else{
    						createDialog({message:'授权失败:'+data.message,type:'error'}).show();
    					}
    				}
    			});
    		});
    		$("#close-btn").click(function(){
    			closeWin();
    		});
    	})
    	//刷新列表
    	function refreshTable(){
    		refreshTableData("searchForm","list");
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
			  					<td><input type="text" placeholder="请输入岗位名称" name="name"/></td>
			  					<td rowspan="2" valign="middle">
			  						<span id="search-btn" class="ico-btn ico-hover ico-content ico-search">查询</span>
			  					</td>
			  				</tr>
			  			</table>
	  				</form>
	  			</td>
	  		</tr>
	  	</table>
  	</div>
  	<div style="position:absolute;top:50px;bottom:0px;left:0px;right:0px;">
  		<table id="list"></table>
  	</div>
  	<div style="height:50px;width:1px;position:relative;bottom:0;z-index:-10;"></div>
  	<div class="footer">
  		<center style="padding-top:10px;">
	  		<span id="save-btn" class="ico-btn ico-hover ico-content ico-save" style="width:auto;">保存</span>
	  		<span id="close-btn" class="ico-btn ico-hover ico-content ico-close" style="width:auto;">关闭</span>
  		</center>
  	</div>
  </body>
</html>
=======
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="police" uri="http://aofa.com/jsp/jstl/function" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
		<jsp:param name="groups" value="easyui,easyui-cs"/>
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
     var dataObj = getData();
     var ids = dataObj.ids;
    	$(function(){
    		$("#list").datagrid({
			    columns:[[
                        { field: 'id', align: 'center', width: 80, title: '主键',checkbox:true },
                        { field: 'name', align: 'center', width: 150, title: '岗位名称' },
                        { field: 'remark', align: 'center', width: 140,  title: '岗位描述' }
				]],
				idField: 'id',
				url: '${ctx}/authority/roles',
				loadFilter: function (data) {
					if(!data||data.success !== undefined){
						createDialog({message:'获取岗位列表失败,'+(data==null?'':data.message),type:'error'}).show();
						return;
					}
					return {  
						"total":data.records,  
						"rows":data.rows  
					}; 
				},
				onLoadSuccess:function(data){
					if(data && data.rows){
						for(var i in data.rows){
							if(rows[i].checked){
								$('#list').datagrid("selectRecord", rows[i].id);
							}
						}
					}
				},
				border:true,
				fit: true,//自动补全 
				fitColumns:false,
				/*onClickRow: DzskDetails,*///行的点击事件
				singleSelect:false,//如果是单选这个属性必须设置，此属性如果不设置就是多选
				pagination: false,//分页
				rownumbers: true//行数
			});
    		$("#search-btn").click(function(){
    			refreshTable();
    		});
    		$("#save-btn").click(function(){
    			var selObj = selectData("list");
    			if(selObj == null){
    				createDialog({message:'请选择一条记录',type:'alert'}).show();
    				return;
    			}
    			var roleIds = '';
    			if(!selObj.length){
    				roleIds = selObj.id;
    			}else{
    				for(var i in selObj){
    					if(roleIds){
    						roleIds+=',';
    					}
    					roleIds+=selObj[i].id;
    				}
    			}
    			$.ajax({
    				url:'${ctx}/authority/grant-roles',
    				data:{userIds:ids,roleIds:roleIds},
    				dataType:'json',
    				type:'post',
    				success:function(data){
    					if(data&&data.success === true){
    						createDialog({message:'授权成功',type:'success',accept:function(){
    							closeWin();
    						}}).show();
    						dataObj.afterSuccess();
    					}else{
    						createDialog({message:'授权失败:'+data.message,type:'error'}).show();
    					}
    				}
    			});
    		});
    		$("#close-btn").click(function(){
    			closeWin();
    		});
    	})
    	//刷新列表
    	function refreshTable(){
    		refreshTableData("searchForm","list");
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
			  					<td><input type="text" placeholder="请输入岗位名称" name="name"/></td>
			  					<td rowspan="2" valign="middle">
			  						<span id="search-btn" class="ico-btn ico-hover ico-content ico-search">查询</span>
			  					</td>
			  				</tr>
			  			</table>
	  				</form>
	  			</td>
	  		</tr>
	  	</table>
  	</div>
  	<div style="position:absolute;top:50px;bottom:0px;left:0px;right:0px;">
  		<table id="list"></table>
  	</div>
  	<div style="height:50px;width:1px;position:relative;bottom:0;z-index:-10;"></div>
  	<div class="footer">
  		<center style="padding-top:10px;">
	  		<span id="save-btn" class="ico-btn ico-hover ico-content ico-save" style="width:auto;">保存</span>
	  		<span id="close-btn" class="ico-btn ico-hover ico-content ico-close" style="width:auto;">关闭</span>
  		</center>
  	</div>
  </body>
</html>
>>>>>>> 17ac48076d7a7be7437c8367312af01562698b1c
