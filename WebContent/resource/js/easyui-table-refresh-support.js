<<<<<<< HEAD
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
=======
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
>>>>>>> 17ac48076d7a7be7437c8367312af01562698b1c
}