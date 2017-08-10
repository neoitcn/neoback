function refreshTableData(dataTargetId,targetId){
	var param;
	if(arguments.length > 1){
		param = $("#"+dataTargetId).serializeObject();
	}else{
		param={};
		targetId = dataTargetId;
	}
	$( '#'+targetId).datagrid( 'reload',param);
}

function selectData(targetId){
	return $("#"+targetId).datagrid("getSelected");
}