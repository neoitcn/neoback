/**
 * Created by Shisan on 2017/1/24.
 */
//为了方便修改，将jqgrid的属性转换为easyui的属性。
//页面修改只需要将easyui引入，剔除jqgrid的引入，如果两者同时引入可能会导致问题。
$.fn.extend($.fn,{
    jqGrid:function(options){
        if(options.colModel){
            for(var i in options.colModel){
                options.colModel[i].field = options.colModel[i].name;
                delete options.colModel[i].name;
                delete options.colModel[i].index;
                options.colModel[i].title = options.colModel[i].label;
                delete options.colModel[i].label;
                delete options.colModel[i].searchoptions;
                delete options.colModel[i].sortable;
                if(options.colModel[i].width!== undefined){
                    if(options.colModel[i].width.lastIndexOf &&  options.colModel[i].width.lastIndexOf('%')==-1){
                        options.colModel[i].width = parseInt(options.colModel[i].width);
                    }
                }else{
                    options.colModel[i].width = 80;
                }
                if(options.colModel[i].height!== undefined){
                    if(options.colModel[i].height.lastIndexOf &&  options.colModel[i].height.lastIndexOf('%')==-1){
                        options.colModel[i].height = parseInt(options.colModel[i].height);
                    }
                }
            }
        }
        this.datagrid({
            columns:[options.colModel],
            idField: 'id',
            url:options.url,
            loadFilter: function (data) {
                if(!data || data.success !== undefined){
                    createDialog({message:'获取数据失败,'+(data==null?'':data.message),type:'error'}).show();
                    return;
                }
                return {
                    "total":data.records,
                    "rows":data.rows
                };
            },
            border:true,
            fit: true,//自动补全
            fitColumns:false,
            /*onClickRow: DzskDetails,*///行的点击事件
            singleSelect:!!options.multiselect,//如果是单选这个属性必须设置，此属性如果不设置就是多选
            pagination: !!options.pager,//分页
            rownumbers: options.rownumbers,//行数
            onSelect:options.onCellSelect,
            pageList:options.rowList,
            pageSize:options.rowNum
        });
    }
});

function selectRecord(id){
    return selectData(id);
}