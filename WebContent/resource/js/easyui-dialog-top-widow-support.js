//easyui dialog without html
//创建一个顶级父窗口的widnow，必须有父窗口的相关js的支持。需要在父窗口引入easyui-dialog-support.js。
function createWindow(data){
	return new window.top.ContentWindow(data);
}
function openWindow(data){
	var w =  createWindow(data);
	w.open();
	return w;
}

//该数据是顶级父窗口共享的数据。
function getData(){
	var arr = $('iframe',window.top.document);
	var length = arr.length;
	for(var i=0;i<length;i++){
		if(window === arr[i].contentWindow){
			return window.top['_data_all_'][$(arr[i]).parent().attr('id')]['data'];
		}
	}
}

//销毁win。
function closeWin(){
	var length = $('iframe',window.top.document).length; 
	var arr = $('iframe',window.top.document);
	for(var i=0;i<length;i++){
		if(window === arr[i].contentWindow){
			return window.top['_data_all_'][$(arr[i]).parent().attr('id')].destory();
		}
	}
}
//返回当前的iframe，不是jquery对象，而是js对象
function getCurrentIFrame(){
	var length = $('iframe',window.top.document).length; 
	var arr = $('iframe',window.top.document);
	for(var i=0;i<length;i++){
		if(window === arr[i].contentWindow){
			return arr[i];
		}
	}
}

function showLoading(){
    	//提交表单前，展示正在提交
    var currentIFrame = getCurrentIFrame();
    if(currentIFrame!=null){
		//$("body",currentIFrame.document).addClass('g-blur');
    	var arr = $(currentIFrame).parent().find('div[data-role=q]');
    	arr.each(function(i,v){
			$(v).show();
		});
    }
}

function hideLoading(){
	//提交表单前，展示正在提交
	var currentIFrame = getCurrentIFrame();
	if(currentIFrame!=null){
		//$("body",currentIFrame.document).removeClass('g-blur');
		var arr = $(currentIFrame).parent().find('div[data-role=q]');
		arr.each(function(i,v){
			$(v).hide();
		});
	}
}

function createDialog(data){
	return new window.top.DialogWindow(data);
}
function openDialog(data){

	//兼容上一版本的openDialog的Window。
	if(data.type && data.type === 1){
		delete data.type;
		return openWindow(data);
	}
	//兼容上一版本的弹框类型
	if(!isNaN(data.type)){
		switch(data.type){
			case 2:
				data.type='success';
				break;
			case 3:
				data.type="alert";
				break;
			case 4:
				data.type="error";
				break;
			case 5:
				data.type="confirm";
				break;
		}
	}
	//兼容上一版的弹框消息
	if(data.title != null && data.message == null){
		data.message = data.title;
		data.title='消息';
	}
	//兼容上一版本的func方法
	if(data.accept == null && data.func != null){
		data.accept = data.func;
		delete data.func;
	}
	var d =  createDialog(data);
	d.show();
	return d;
}