//刷新表格
function refreshGrid(form/*被选择的form数据*/,table/*对应的表格*/) {
	if(arguments.length == 0){
		return;
	}
	var param;
	if(arguments.length > 1){
		param = form.serializeObject();
	}else{
		param={};
		table = form;
	}
	var _postData = table.jqGrid("getGridParam", "postData");
	table.jqGrid("setGridParam", {
		postData : $.extend(_postData, param),
		page : 1
	}).trigger("reloadGrid");
}
//选择表格
function selectRecord(id) {
	var rowData = $("#"+id).jqGrid("getGridParam", "selarrrow");
	if (rowData.length) {
		if(rowData.length == 1){
			return $("#"+id).jqGrid('getRowData', rowData[0]);
		}else if(rowData.length > 1){
			var array = [];
			for(var i in rowData){
				array.push($("#"+id).jqGrid('getRowData', rowData[i]));
			}
			return array;
		}
	}
	return null;
}