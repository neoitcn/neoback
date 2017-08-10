/*
 * Translated default messages for the jQuery validation plugin.
 * Locale: CN
 */
jQuery.extend(jQuery.validator.messages, {
        required: " 必填字段",
		remote: " 请修正该字段",
		email: " 请输入正确格式的电子邮件",
		url: " 请输入合法的网址",
		date: " 请输入合法的日期",
		dateISO: " 请输入合法的日期 (ISO).",
		datetime:"请正确填写时间日期!例：2011-01-01 01:10:12",
		number: " 请输入合法的数字",
		digits: " 只能输入整数",
		creditcard: " 请输入合法的信用卡号",
		equalTo: " 请再次输入相同的值",
		accept: " 请输入拥有合法后缀名的字符串",
		maxlength: jQuery.validator.format(" 请输入一个长度最多是 {0} 的字符串"),
		minlength: jQuery.validator.format(" 请输入一个长度最少是 {0} 的字符串"),
		rangelength: jQuery.validator.format(" 请输入一个长度介于 {0} 和 {1} 之间的字符串"),
		range: jQuery.validator.format(" 请输入一个介于 {0} 和 {1} 之间的值"),
		max: jQuery.validator.format(" 请输入一个最大为 {0} 的值"),
		min: jQuery.validator.format(" 请输入一个最小为 {0} 的值"),
		//扩展工具   TanSong added
		regexp:jQuery.validator.format(" 请输入满足正则表达式的值"),
		stringMinLength: jQuery.validator.format(" 长度不能小于{0} , 一个汉字占2个字符"), 
		stringMaxLength: jQuery.validator.format(" 长度不能大于{0} , 一个汉字占2个字符"),  
		string: " 含特殊符号!",  
		stringCH: " 只能输入汉字,一个汉字占两个字节",   
		stringEN: " 只能输入字母",
		positiveRealNum:"请输入正数(包括正整数或正小数)"
});