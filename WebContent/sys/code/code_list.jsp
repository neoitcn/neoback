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
		<jsp:param name="groups" value="easyui,easyui-cs"/>
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
    	$(function(){
    		$("#list").jqGrid({
    			url : '${ctx}/code/page',
    			datatype : 'json',
    			mtype : 'POST',
    			colModel : [{name : 'id',index : 'id',label : '主键',checkbox : true},
    						{name : 'name',index : 'name',label : '名称',align : 'center',width : '150px',sortable : false,searchoptions : {clearSearch : false}}, 
    						{name : 'key',index : 'key',label : '关键字',align : 'center',width : '100px',sortable : false,searchoptions : {clearSearch : false}}, 
    						{name : 'enabled',index : 'enabled',label : '是否启用',align : 'center',width : '100px',sortable : false,searchoptions : {clearSearch : false}}
    		    ],
    			pager : '#pager',
    			altRows : true,
    			rownumbers : true,
    			rowNum : 10,
    			rowList : [ 10, 20, 30 ],
    			autowidth : true,
    			shrinkToFit : false,
    			height : 'auto',
    			multiboxonly : true,
    			viewrecords : true,
    			gridview : true,
    			multiselect : true,
    			jsonReader : {
    				repeatitems : false
    			},
    			onSelectRow : function(rowid, status, e) {
    				return true;
    			},
    			onCellSelect : function(rowid,iCol,cellcontent,e){
    				if(iCol!=1){
    					var record = selectData('list');
    					var id = record.id;
    					$("#codeId").val(id);
    					refreshVTable();
    				}
    			}
    		});

    		$("#vList").jqGrid({
    			url : '${ctx}/code/vPage',
    			datatype : 'json',
    			mtype : 'POST',
    			colModel : [{name : 'id',index : 'id',label : '主键',checkbox : true},
    						{name : 'name',index : 'name',label : '名称',align : 'center',width : '150px',sortable : false,searchoptions : {clearSearch : false}}, 
    						{name : 'key',index : 'key',label : '值',align : 'center',width : '100px',sortable : false,searchoptions : {clearSearch : false}}, 
    						{name : 'enabled',index : 'enabled',label : '是否启用',align : 'center',width : '100px',sortable : false,searchoptions : {clearSearch : false}}
    		    ],
    			pager : '#vPager',
    			altRows : true,
    			rownumbers : true,
    			rowNum : 10,
    			rowList : [ 10, 20, 30 ],
    			autowidth : true,
    			shrinkToFit : false,
    			height : 'auto',
    			multiboxonly : true,
    			viewrecords : true,
    			gridview : true,
    			multiselect : true,
    			jsonReader : {
    				repeatitems : false
    			},
    			onSelectRow : function(rowid, status, e) {
    				return true;
    			},
    			onCellSelect : function(rowid,iCol,cellcontent,e){
    				if(iCol!=1){
    				}
    			}
    		});

    		$("#add-btn").click(function(){
    			//在父窗口中弹出此窗口
    			openDialog({title:'添加码表主信息',url:'${ctx}/code/edit',type:1,data:refreshTable});
    		});
    		$("#edit-btn").click(function(){
    			var selObj = selectRecord("list");
    			if(selObj == null){
    				openDialog({title:'请选择一条记录',type:3},null);
    				return;
    			}
    			if(selObj.length && selObj.length != 1){
    				openDialog({title:'只能选择一条记录进行编辑',type:3});
    				return;
    			}
    			openDialog({title:'修改码表主信息',url:'${ctx}/code/edit?id='+selObj.id,type:1,data:refreshTable});
    		});
    		$("#del-btn").click(function(){
    			var selObj = selectRecord("list");
    			if(selObj == null){
    				openDialog({title:'请选择一条记录',type:3});
    				return;
    			}
    			if(selObj.length && selObj.length != 1){
    				openDialog({title:'为了数据安全，一次只能删除一条数据',type:3});
    				return;
    			}
    			openDialog({title:'这将删除包含其在内的所有从字段，确定要删除吗？',type:5,func:function(){
	    				$.requestAjax({
	        				data:{id:selObj.id},
	        				dataType:'json',
	        				url:'${ctx}/code/delete',
	        				success:function(data){
	        					if(data.success === true){
	        						refreshTable();
	        						openDialog({title:'删除成功，3秒后自动关闭',second:3,type:2});
	        					}else{
	        						openDialog({title:'删除失败：'+data.message,type:2});
	        					}
	       					}
	       				});
    				}
    			});
    		});
    		
    		
    		
    		$("#add-btn2").click(function(){
    			var selObj = selectRecord("list");
    			if(selObj == null){
    				openDialog({title:'请选择主表的一条记录后再添加',type:3},null);
    				return;
    			}
    			//在父窗口中弹出此窗口
    			openDialog({title:'添加码表从信息',url:'${ctx}/code/vEdit',type:1,data:{data:selObj.id,func:refreshVTable}});
    		});
    		$("#edit-btn2").click(function(){
    			var selObj = selectRecord("vList");
    			if(selObj == null){
    				openDialog({title:'请选择一条记录',type:3},null);
    				return;
    			}
    			openDialog({title:'修改码表从信息',url:'${ctx}/code/vEdit?id='+selObj.id,type:1,data:{data:selObj.id,func:refreshVTable}});
    		});
    		$("#del-btn2").click(function(){
    			var selObj = selectRecord("vList");
    			if(selObj == null){
    				openDialog({title:'请选择一条记录',type:3});
    				return;
    			}
    			openDialog({title:'确定要删除吗？',type:5,func:function(){
	    				$.requestAjax({
	        				data:{id:selObj.id},
	        				dataType:'json',
	        				url:'${ctx}/code/vDelete',
	        				success:function(data){
	        					if(data.success === true){
	        						refreshVTable();
	        						openDialog({title:'删除成功',second:3,type:2});
	        					}else{
	        						openDialog({title:'删除失败：'+data.message,type:2});
	        					}
	       					}
	       				});
    				}
    			});
    		});
    	})
    	
    	//刷新列表
    	function refreshTable(){
    		refreshTableData("list");
    	}
    	function refreshVTable(){
			refreshTableData("loadForm","vList");
    	}
    </script>
  </head>
  
  <body>
  	<div style="width:500px;overflow:auto;height:100%;">
  		<div class="search-pane">
			<shirox:permission name="div" permissions="admin:w" style="float:right;" className="control">
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
	  	</div>

		<div style="position:absolute;top:50px;bottom:0px;left:0px;width:500px;">
			<table id="list"></table>
		</div>
  	</div>
  	<div style="position:absolute;left:505px;top:0;right:0;height:100%;overflow:auto;">
  		<div class="search-pane">
		<shirox:permission name="div" permissions="admin:w" style="float:right;" className="control">
			<accept>
				<span id="add-btn2" class="ico-btn ico-hover ico-content ico-save" style="margin:0px 2px;width:auto;">添加</span>
				<span id="edit-btn2" class="ico-btn ico-hover ico-content ico-edit" style="margin:0px 2px;width:auto;">修改</span>
				<span id="del-btn2" class="ico-btn ico-hover ico-content ico-remove" style="margin:0px 2px;width:auto;">删除</span>
			</accept>
			<unaccept>
				<span class="ico-btn ico-content ico-save-disabled" style="margin:0px 2px;width:auto;cursor:default;">添加</span>
				<span class="ico-btn ico-content ico-edit-disabled" style="margin:0px 2px;width:auto;cursor:default;">修改</span>
				<span class="ico-btn ico-content ico-remove-disabled" style="margin:0px 2px;width:auto;cursor:default;">删除</span>
			</unaccept>
		</shirox:permission>
	  	</div>
		<div style="position:absolute;top:50px;bottom:0px;left:0px;right:0px;">
  			<table id="vList"></table>
		</div>
  		<form id="loadForm">
  			<input type="hidden" id="codeId" name="codeId"/>
  		</form>
  	</div>
  </body>
</html>
