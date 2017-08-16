<<<<<<< HEAD
/**
 * Created by Shisan on 2017/1/19.
 */

$(function(){
    $.requestAjax=function(options){
        if(options.before){
            var b = options.before();
            if(b === false || b == null){
                return;
            }
        }
        if(options.after){
            b = options.after();
            if(b === false || b == null){
                return;
            }
        }
        options.beforeSend = function(){
            showLoading();
        }
        options.complete = function(){
            hideLoading();
        }
        if(!options.error){
            options.error = function(xhr,status,errMsg){
                openDialog({title:'请求出错',message:'遇到一些问题,错误代码：'+xhr.status,type:'error'});
            }
        }
        $.ajax(options);
    }
=======
/**
 * Created by Shisan on 2017/1/19.
 */

$(function(){
    $.requestAjax=function(options){
        if(options.before){
            var b = options.before();
            if(b === false || b == null){
                return;
            }
        }
        if(options.after){
            b = options.after();
            if(b === false || b == null){
                return;
            }
        }
        options.beforeSend = function(){
            showLoading();
        }
        options.complete = function(){
            hideLoading();
        }
        if(!options.error){
            options.error = function(xhr,status,errMsg){
                openDialog({title:'请求出错',message:'遇到一些问题,错误代码：'+xhr.status,type:'error'});
            }
        }
        $.ajax(options);
    }
>>>>>>> 17ac48076d7a7be7437c8367312af01562698b1c
})