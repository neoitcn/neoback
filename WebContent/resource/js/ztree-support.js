<<<<<<< HEAD
var tree;
var setting = {
	async:{
		enable:false
	},
	check:{
		chkboxType:{'Y':'ps','N':'ps'},
		enable:true,
	},
	data:{
		simpleData:{
			enable:true,
			idKey:'id',
			pIdKey:'parentId',
			rootPId:null
		}
	}
};

function initZTree(pane,u,set,finished){
	if(tree){
		tree.destory();
	}
	if(set){
		$.ajax({
			dataType:'json',
			url:u,
			success:function(data){
				var tree = $.fn.zTree.init(pane, set, data);
				if(finished && typeof finished === 'function'){
					finished(tree);
				}
			}
		});
	}else{
		$.ajax({
			dataType:'json',
			url:u,
			success:function(data){
				var tree = $.fn.zTree.init(pane, setting, data);
				if(finished && typeof finished === 'function'){
					finished(tree);
				}
			}
		});
	}
=======
var tree;
var setting = {
	async:{
		enable:false
	},
	check:{
		chkboxType:{'Y':'ps','N':'ps'},
		enable:true,
	},
	data:{
		simpleData:{
			enable:true,
			idKey:'id',
			pIdKey:'parentId',
			rootPId:null
		}
	}
};

function initZTree(pane,u,set,finished){
	if(tree){
		tree.destory();
	}
	if(set){
		$.ajax({
			dataType:'json',
			url:u,
			success:function(data){
				var tree = $.fn.zTree.init(pane, set, data);
				if(finished && typeof finished === 'function'){
					finished(tree);
				}
			}
		});
	}else{
		$.ajax({
			dataType:'json',
			url:u,
			success:function(data){
				var tree = $.fn.zTree.init(pane, setting, data);
				if(finished && typeof finished === 'function'){
					finished(tree);
				}
			}
		});
	}
>>>>>>> 17ac48076d7a7be7437c8367312af01562698b1c
}