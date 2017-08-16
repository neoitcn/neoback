//设置当前窗口为根窗口
function setRoot(win){
	if(!win){
		throw new Error("错误，window对象不存在！");
	}
	win._root_='****root****';
}

//在父窗口中打开一个弹框，该弹框依赖于art.dialog。
function openDialogInRoot(win,times,data,info){
	if(!win){
		win = window;
	}
	if(!times||typeof times !== 'number'){
		times = 2;
	}
	if(!art){
		throw new Error('art插件不存在');
	}
	art.dialog.data('data',data);
	for(var i=0;i<times;i++){
		if(i>0){
			win = win.parent;
		}
		if(win._root_ === '****root****'){
			if(info){
				if(info.type === 1){
					art.dialog.open(info.url,{title:info.title,width:info.width,height:info.height,lock:info.lock});
				}else if(info.type === 2){
					art.dialog.success(info.title,info.func);
					if(!isNaN(info.second)){
						setTimeout("closeDialog()",info.second*1000);
					}
				}else if(info.type === 4){
					art.dialog.fail(info.title,info.func);
					if(!isNaN(info.second)){
						setTimeout("closeDialog()",info.second*1000);
					}
				}else if(info.type === 5){
					art.dialog.confirm(info.title,info.func,info.canc);
				}else if(info.type === 6){
					art.dialog.tips(info.title);
				}else{
					art.dialog.alert(info.title,info.func);
					if(!isNaN(info.second)){
						setTimeout("closeDialog()",info.second*1000);
					}
				}
			}else{
				art.dialog.alert("没有找到顶级父页面");
			}
			break;
		}
	}
}
function dialogData(){
	return art.dialog.data('data');
}
function closeDialog(){
	art.dialog.close();
}
