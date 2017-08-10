var mode = '<div id="****id****" role="cd0051vbbppps" style="position:absolute;right:0px;width:400px;bottom:****bottom****;height:100px;overflow:hidden;">'
		+ '<div style="height:100px;background-color:#53b9e3;z-index:9999999;box-shadow:inset 0px 0px 40px #FF0000;margin-left:400px;width:400px;position:relative;">'
		+ '<div style="padding-top:10px;padding-left:10px;padding-right:10px;">'
		+ '<span style="font: bold 16px \'微软雅黑\',\'黑体\';color:#FF0000;display:inline-block;">****title****</span>'
		+ '<span style="float:right;display:inline-block;color:#000000;font-size:14px;cursor:pointer;" onclick="****close****">x</span>'
		+ '</div>'
		+ '<div id="test" style="padding:0px 10px;color:#FFFFFF;max-height:70px;vertical-align:middle;  overflow: hidden;">****content****</div>'
		+ '</div></div>';

var messageDiv = {};
var length = 0;
var objectManager = {};

var MessageBody = function(title, message, timeout) {
	this.title = title;
	this.message = message;
	this.id = new Date().getTime() + parseInt(Math.random() * 100000);
	this.timeout = timeout | 3000;

	this.mode = mode.replace('****title****', title).replace('****content****',
			message).replace('****id****', this.id);
	var obj = this;
	this.showMessage = function() {
		var layoutY = length * 100;
		this.mode = this.mode.replace("****bottom****", layoutY + "px")
				.replace('****close****', 'removeMessage(\'' + obj.id + '\')');
		length++;
		messageDiv[this.id] = layoutY;
		$("body").append(this.mode);
		$("#" + this.id).children().eq(0).animate({
			marginLeft : '0px'
		}, function() {
			obj.winTime = window.setTimeout(function() {
				obj.removeMessage();
			}, obj.timeout);
		});
		$("#" + this.id).mouseenter(function() {
			// messageDiv[obj.id+'pushed'] = true;
			window.clearTimeout(obj.winTime);
		});
		$("#" + this.id).mouseleave(function() {
			// messageDiv[obj.id+'pushed'] = false;
			obj.winTime = window.setTimeout(function() {
				obj.removeMessage();
			}, obj.timeout);
		});
	};

	this.removeMessage = function() {
		length--;
		var obj = this;
		var tempL = 100;
		$("div[role='cd0051vbbppps']")
				.each(
						function(i, v) {
							if ($(v).attr('id') != obj.id
									&& $(v).offset().top < $("#" + obj.id)
											.offset().top) {
								/*
								 * if(messageDiv[$(v).attr('id')+'pushed']){
								 * tempL += 100; return; }
								 */
								var to = messageDiv[$(v).attr('id')] -= tempL;
								$(v).animate({
									bottom : to + "px"
								});
							}
						});
		$("#" + this.id).children().eq(0).animate({
			marginLeft : '400px'
		}, function() {
			$("#" + obj.id).remove();
			delete messageDiv[obj.id];
			delete objectManager[obj.id];
			// delete messageDiv[obj.id+'pushed'];
		});
	}
	objectManager[this.id] = this;
}

function removeMessage(id) {
	if (objectManager[id]) {
		objectManager[id].removeMessage();
	}
}

function playWarn(){
	if(!$('#audio').attr('src')){
		$('#audio').attr("src","resource/audio/alarmSound.wav");
	}
	$('#audio')[0].play();
}