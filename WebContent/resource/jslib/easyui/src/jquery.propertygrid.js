<<<<<<< HEAD
/**
 * jQuery EasyUI 1.5.1
 * 
 * Copyright (c) 2009-2016 www.jeasyui.com. All rights reserved.
 *
 * Licensed under the freeware license: http://www.jeasyui.com/license_freeware.php
 * To use it on other terms please contact us: info@jeasyui.com
 *
 */
/**
 * propertygrid - jQuery EasyUI
 * 
 * Dependencies:
 * 	 datagrid
 * 
 */
(function($){
	var currTarget;
	$(document).unbind('.propertygrid').bind('mousedown.propertygrid', function(e){
		var p = $(e.target).closest('div.datagrid-view,div.combo-panel');
		if (p.length){return;}
		stopEditing(currTarget);
		currTarget = undefined;
	});
	
	function buildGrid(target){
		var state = $.data(target, 'propertygrid');
		var opts = $.data(target, 'propertygrid').options;
		$(target).datagrid($.extend({}, opts, {
			cls:'propertygrid',
			view:(opts.showGroup ? opts.groupView : opts.view),
			onBeforeEdit:function(index, row){
				if (opts.onBeforeEdit.call(target, index, row) == false){return false;}
				var dg = $(this);
				var row = dg.datagrid('getRows')[index];
				var col = dg.datagrid('getColumnOption', 'value');
				col.editor = row.editor;
			},
			onClickCell:function(index, field, value){
				if (currTarget != this){
					stopEditing(currTarget);
					currTarget = this;
				}
				if (opts.editIndex != index){
					stopEditing(currTarget);
					$(this).datagrid('beginEdit', index);
					var ed = $(this).datagrid('getEditor', {index:index,field:field});
					if (!ed){
						ed = $(this).datagrid('getEditor', {index:index,field:'value'});
					}
					if (ed){
						var t = $(ed.target);
						var input = t.data('textbox') ? t.textbox('textbox') : t;
						input.focus();
						opts.editIndex = index;
					}
				}
				opts.onClickCell.call(target, index, field, value);
			},
			loadFilter:function(data){
				stopEditing(this);
				return opts.loadFilter.call(this, data);
			}
		}));
	}
	
	function stopEditing(target){
		var t = $(target);
		if (!t.length){return}
		var opts = $.data(target, 'propertygrid').options;
		opts.finder.getTr(target, null, 'editing').each(function(){
			var index = parseInt($(this).attr('datagrid-row-index'));
			if (t.datagrid('validateRow', index)){
				t.datagrid('endEdit', index);
			} else {
				t.datagrid('cancelEdit', index);
			}
		});
		opts.editIndex = undefined;
	}
	
	$.fn.propertygrid = function(options, param){
		if (typeof options == 'string'){
			var method = $.fn.propertygrid.methods[options];
			if (method){
				return method(this, param);
			} else {
				return this.datagrid(options, param);
			}
		}
		
		options = options || {};
		return this.each(function(){
			var state = $.data(this, 'propertygrid');
			if (state){
				$.extend(state.options, options);
			} else {
				var opts = $.extend({}, $.fn.propertygrid.defaults, $.fn.propertygrid.parseOptions(this), options);
				opts.frozenColumns = $.extend(true, [], opts.frozenColumns);
				opts.columns = $.extend(true, [], opts.columns);
				$.data(this, 'propertygrid', {
					options: opts
				});
			}
			buildGrid(this);
		});
	}
	
	$.fn.propertygrid.methods = {
		options: function(jq){
			return $.data(jq[0], 'propertygrid').options;
		}
	};
	
	$.fn.propertygrid.parseOptions = function(target){
		return $.extend({}, $.fn.datagrid.parseOptions(target), $.parser.parseOptions(target,[{showGroup:'boolean'}]));
	};
	
	// the group view definition
	var groupview = $.extend({}, $.fn.datagrid.defaults.view, {
		render: function(target, container, frozen){
			var table = [];
			var groups = this.groups;
			for(var i=0; i<groups.length; i++){
				table.push(this.renderGroup.call(this, target, i, groups[i], frozen));
			}
			$(container).html(table.join(''));
		},
		
		renderGroup: function(target, groupIndex, group, frozen){
			var state = $.data(target, 'datagrid');
			var opts = state.options;
			var fields = $(target).datagrid('getColumnFields', frozen);
			
			var table = [];
			table.push('<div class="datagrid-group" group-index=' + groupIndex + '>');
			if ((frozen && (opts.rownumbers || opts.frozenColumns.length)) ||
					(!frozen && !(opts.rownumbers || opts.frozenColumns.length))){
				table.push('<span class="datagrid-group-expander">');
				table.push('<span class="datagrid-row-expander datagrid-row-collapse">&nbsp;</span>');
				table.push('</span>');
			}
			if (!frozen){
				table.push('<span class="datagrid-group-title">');
				table.push(opts.groupFormatter.call(target, group.value, group.rows));
				table.push('</span>');
			}
			table.push('</div>');
			
			table.push('<table class="datagrid-btable" cellspacing="0" cellpadding="0" border="0"><tbody>');
			var index = group.startIndex;
			for(var j=0; j<group.rows.length; j++) {
				var css = opts.rowStyler ? opts.rowStyler.call(target, index, group.rows[j]) : '';
				var classValue = '';
				var styleValue = '';
				if (typeof css == 'string'){
					styleValue = css;
				} else if (css){
					classValue = css['class'] || '';
					styleValue = css['style'] || '';
				}
				
				var cls = 'class="datagrid-row ' + (index % 2 && opts.striped ? 'datagrid-row-alt ' : ' ') + classValue + '"';
				var style = styleValue ? 'style="' + styleValue + '"' : '';
				var rowId = state.rowIdPrefix + '-' + (frozen?1:2) + '-' + index;
				table.push('<tr id="' + rowId + '" datagrid-row-index="' + index + '" ' + cls + ' ' + style + '>');
				table.push(this.renderRow.call(this, target, fields, frozen, index, group.rows[j]));
				table.push('</tr>');
				index++;
			}
			table.push('</tbody></table>');
			return table.join('');
		},
		
		bindEvents: function(target){
			var state = $.data(target, 'datagrid');
			var dc = state.dc;
			var body = dc.body1.add(dc.body2);
			var clickHandler = ($.data(body[0],'events')||$._data(body[0],'events')).click[0].handler;
			body.unbind('click').bind('click', function(e){
				var tt = $(e.target);
				var expander = tt.closest('span.datagrid-row-expander');
				if (expander.length){
					var gindex = expander.closest('div.datagrid-group').attr('group-index');
					if (expander.hasClass('datagrid-row-collapse')){
						$(target).datagrid('collapseGroup', gindex);
					} else {
						$(target).datagrid('expandGroup', gindex);
					}
				} else {
					clickHandler(e);
				}
				e.stopPropagation();
			});
		},
		
		onBeforeRender: function(target, rows){
			var state = $.data(target, 'datagrid');
			var opts = state.options;
			
			initCss();
			
			var groups = [];
			for(var i=0; i<rows.length; i++){
				var row = rows[i];
				var group = getGroup(row[opts.groupField]);
				if (!group){
					group = {
						value: row[opts.groupField],
						rows: [row]
					};
					groups.push(group);
				} else {
					group.rows.push(row);
				}
			}
			
			var index = 0;
			var newRows = [];
			for(var i=0; i<groups.length; i++){
				var group = groups[i];
				group.startIndex = index;
				index += group.rows.length;
				newRows = newRows.concat(group.rows);
			}
			
			state.data.rows = newRows;
			this.groups = groups;
			
			var that = this;
			setTimeout(function(){
				that.bindEvents(target);
			},0);
			
			function getGroup(value){
				for(var i=0; i<groups.length; i++){
					var group = groups[i];
					if (group.value == value){
						return group;
					}
				}
				return null;
			}
			function initCss(){
				if (!$('#datagrid-group-style').length){
					$('head').append(
						'<style id="datagrid-group-style">' +
						'.datagrid-group{height:'+opts.groupHeight+'px;overflow:hidden;font-weight:bold;border-bottom:1px solid #ccc;}' +
						'.datagrid-group-title,.datagrid-group-expander{display:inline-block;vertical-align:bottom;height:100%;line-height:'+opts.groupHeight+'px;padding:0 4px;}' +
						'.datagrid-group-expander{width:'+opts.expanderWidth+'px;text-align:center;padding:0}' +
						'.datagrid-row-expander{margin:'+Math.floor((opts.groupHeight-16)/2)+'px 0;display:inline-block;width:16px;height:16px;cursor:pointer}' +
						'</style>'
					);
				}
			}
		}
	});

	$.extend($.fn.datagrid.methods, {
		groups:function(jq){
			return jq.datagrid('options').view.groups;
		},
	    expandGroup:function(jq, groupIndex){
	        return jq.each(function(){
	            var view = $.data(this, 'datagrid').dc.view;
	            var group = view.find(groupIndex!=undefined ? 'div.datagrid-group[group-index="'+groupIndex+'"]' : 'div.datagrid-group');
	            var expander = group.find('span.datagrid-row-expander');
	            if (expander.hasClass('datagrid-row-expand')){
	                expander.removeClass('datagrid-row-expand').addClass('datagrid-row-collapse');
	                group.next('table').show();
	            }
	            $(this).datagrid('fixRowHeight');
	        });
	    },
	    collapseGroup:function(jq, groupIndex){
	        return jq.each(function(){
	            var view = $.data(this, 'datagrid').dc.view;
	            var group = view.find(groupIndex!=undefined ? 'div.datagrid-group[group-index="'+groupIndex+'"]' : 'div.datagrid-group');
	            var expander = group.find('span.datagrid-row-expander');
	            if (expander.hasClass('datagrid-row-collapse')){
	                expander.removeClass('datagrid-row-collapse').addClass('datagrid-row-expand');
	                group.next('table').hide();
	            }
	            $(this).datagrid('fixRowHeight');
	        });
	    }
	});

	$.extend(groupview, {
		refreshGroupTitle: function(target, groupIndex){
			var state = $.data(target, 'datagrid');
			var opts = state.options;
			var dc = state.dc;
			var group = this.groups[groupIndex];
			var span = dc.body2.children('div.datagrid-group[group-index=' + groupIndex + ']').find('span.datagrid-group-title');
			span.html(opts.groupFormatter.call(target, group.value, group.rows));
		},
		
		insertRow: function(target, index, row){
			var state = $.data(target, 'datagrid');
			var opts = state.options;
			var dc = state.dc;
			var group = null;
			var groupIndex;
			
			if (!state.data.rows.length){
				$(target).datagrid('loadData', [row]);
				return;
			}
			
			for(var i=0; i<this.groups.length; i++){
				if (this.groups[i].value == row[opts.groupField]){
					group = this.groups[i];
					groupIndex = i;
					break;
				}
			}
			if (group){
				if (index == undefined || index == null){
					index = state.data.rows.length;
				}
				if (index < group.startIndex){
					index = group.startIndex;
				} else if (index > group.startIndex + group.rows.length){
					index = group.startIndex + group.rows.length;
				}
				$.fn.datagrid.defaults.view.insertRow.call(this, target, index, row);
				
				if (index >= group.startIndex + group.rows.length){
					_moveTr(index, true);
					_moveTr(index, false);
				}
				group.rows.splice(index - group.startIndex, 0, row);
			} else {
				group = {
					value: row[opts.groupField],
					rows: [row],
					startIndex: state.data.rows.length
				}
				groupIndex = this.groups.length;
				dc.body1.append(this.renderGroup.call(this, target, groupIndex, group, true));
				dc.body2.append(this.renderGroup.call(this, target, groupIndex, group, false));
				this.groups.push(group);
				state.data.rows.push(row);
			}
			
			this.refreshGroupTitle(target, groupIndex);
			
			function _moveTr(index,frozen){
				var serno = frozen?1:2;
				var prevTr = opts.finder.getTr(target, index-1, 'body', serno);
				var tr = opts.finder.getTr(target, index, 'body', serno);
				tr.insertAfter(prevTr);
			}
		},
		
		updateRow: function(target, index, row){
			var opts = $.data(target, 'datagrid').options;
			$.fn.datagrid.defaults.view.updateRow.call(this, target, index, row);
			var tb = opts.finder.getTr(target, index, 'body', 2).closest('table.datagrid-btable');
			var groupIndex = parseInt(tb.prev().attr('group-index'));
			this.refreshGroupTitle(target, groupIndex);
		},
		
		deleteRow: function(target, index){
			var state = $.data(target, 'datagrid');
			var opts = state.options;
			var dc = state.dc;
			var body = dc.body1.add(dc.body2);
			
			var tb = opts.finder.getTr(target, index, 'body', 2).closest('table.datagrid-btable');
			var groupIndex = parseInt(tb.prev().attr('group-index'));
			
			$.fn.datagrid.defaults.view.deleteRow.call(this, target, index);
			
			var group = this.groups[groupIndex];
			if (group.rows.length > 1){
				group.rows.splice(index-group.startIndex, 1);
				this.refreshGroupTitle(target, groupIndex);
			} else {
				body.children('div.datagrid-group[group-index='+groupIndex+']').remove();
				for(var i=groupIndex+1; i<this.groups.length; i++){
					body.children('div.datagrid-group[group-index='+i+']').attr('group-index', i-1);
				}
				this.groups.splice(groupIndex, 1);
			}
			
			var index = 0;
			for(var i=0; i<this.groups.length; i++){
				var group = this.groups[i];
				group.startIndex = index;
				index += group.rows.length;
			}
		}
	});


	// end of group view definition
	
	$.fn.propertygrid.defaults = $.extend({}, $.fn.datagrid.defaults, {
		groupHeight:21,
		expanderWidth:16,
		singleSelect:true,
		remoteSort:false,
		fitColumns:true,
		loadMsg:'',
		frozenColumns:[[
		    {field:'f',width:16,resizable:false}
		]],
		columns:[[
		    {field:'name',title:'Name',width:100,sortable:true},
		    {field:'value',title:'Value',width:100,resizable:false}
		]],
		
		showGroup:false,
		groupView:groupview,
		groupField:'group',
		groupFormatter:function(fvalue,rows){return fvalue}
	});
})(jQuery);
=======
/**
 * jQuery EasyUI 1.5.1
 * 
 * Copyright (c) 2009-2016 www.jeasyui.com. All rights reserved.
 *
 * Licensed under the freeware license: http://www.jeasyui.com/license_freeware.php
 * To use it on other terms please contact us: info@jeasyui.com
 *
 */
/**
 * propertygrid - jQuery EasyUI
 * 
 * Dependencies:
 * 	 datagrid
 * 
 */
(function($){
	var currTarget;
	$(document).unbind('.propertygrid').bind('mousedown.propertygrid', function(e){
		var p = $(e.target).closest('div.datagrid-view,div.combo-panel');
		if (p.length){return;}
		stopEditing(currTarget);
		currTarget = undefined;
	});
	
	function buildGrid(target){
		var state = $.data(target, 'propertygrid');
		var opts = $.data(target, 'propertygrid').options;
		$(target).datagrid($.extend({}, opts, {
			cls:'propertygrid',
			view:(opts.showGroup ? opts.groupView : opts.view),
			onBeforeEdit:function(index, row){
				if (opts.onBeforeEdit.call(target, index, row) == false){return false;}
				var dg = $(this);
				var row = dg.datagrid('getRows')[index];
				var col = dg.datagrid('getColumnOption', 'value');
				col.editor = row.editor;
			},
			onClickCell:function(index, field, value){
				if (currTarget != this){
					stopEditing(currTarget);
					currTarget = this;
				}
				if (opts.editIndex != index){
					stopEditing(currTarget);
					$(this).datagrid('beginEdit', index);
					var ed = $(this).datagrid('getEditor', {index:index,field:field});
					if (!ed){
						ed = $(this).datagrid('getEditor', {index:index,field:'value'});
					}
					if (ed){
						var t = $(ed.target);
						var input = t.data('textbox') ? t.textbox('textbox') : t;
						input.focus();
						opts.editIndex = index;
					}
				}
				opts.onClickCell.call(target, index, field, value);
			},
			loadFilter:function(data){
				stopEditing(this);
				return opts.loadFilter.call(this, data);
			}
		}));
	}
	
	function stopEditing(target){
		var t = $(target);
		if (!t.length){return}
		var opts = $.data(target, 'propertygrid').options;
		opts.finder.getTr(target, null, 'editing').each(function(){
			var index = parseInt($(this).attr('datagrid-row-index'));
			if (t.datagrid('validateRow', index)){
				t.datagrid('endEdit', index);
			} else {
				t.datagrid('cancelEdit', index);
			}
		});
		opts.editIndex = undefined;
	}
	
	$.fn.propertygrid = function(options, param){
		if (typeof options == 'string'){
			var method = $.fn.propertygrid.methods[options];
			if (method){
				return method(this, param);
			} else {
				return this.datagrid(options, param);
			}
		}
		
		options = options || {};
		return this.each(function(){
			var state = $.data(this, 'propertygrid');
			if (state){
				$.extend(state.options, options);
			} else {
				var opts = $.extend({}, $.fn.propertygrid.defaults, $.fn.propertygrid.parseOptions(this), options);
				opts.frozenColumns = $.extend(true, [], opts.frozenColumns);
				opts.columns = $.extend(true, [], opts.columns);
				$.data(this, 'propertygrid', {
					options: opts
				});
			}
			buildGrid(this);
		});
	}
	
	$.fn.propertygrid.methods = {
		options: function(jq){
			return $.data(jq[0], 'propertygrid').options;
		}
	};
	
	$.fn.propertygrid.parseOptions = function(target){
		return $.extend({}, $.fn.datagrid.parseOptions(target), $.parser.parseOptions(target,[{showGroup:'boolean'}]));
	};
	
	// the group view definition
	var groupview = $.extend({}, $.fn.datagrid.defaults.view, {
		render: function(target, container, frozen){
			var table = [];
			var groups = this.groups;
			for(var i=0; i<groups.length; i++){
				table.push(this.renderGroup.call(this, target, i, groups[i], frozen));
			}
			$(container).html(table.join(''));
		},
		
		renderGroup: function(target, groupIndex, group, frozen){
			var state = $.data(target, 'datagrid');
			var opts = state.options;
			var fields = $(target).datagrid('getColumnFields', frozen);
			
			var table = [];
			table.push('<div class="datagrid-group" group-index=' + groupIndex + '>');
			if ((frozen && (opts.rownumbers || opts.frozenColumns.length)) ||
					(!frozen && !(opts.rownumbers || opts.frozenColumns.length))){
				table.push('<span class="datagrid-group-expander">');
				table.push('<span class="datagrid-row-expander datagrid-row-collapse">&nbsp;</span>');
				table.push('</span>');
			}
			if (!frozen){
				table.push('<span class="datagrid-group-title">');
				table.push(opts.groupFormatter.call(target, group.value, group.rows));
				table.push('</span>');
			}
			table.push('</div>');
			
			table.push('<table class="datagrid-btable" cellspacing="0" cellpadding="0" border="0"><tbody>');
			var index = group.startIndex;
			for(var j=0; j<group.rows.length; j++) {
				var css = opts.rowStyler ? opts.rowStyler.call(target, index, group.rows[j]) : '';
				var classValue = '';
				var styleValue = '';
				if (typeof css == 'string'){
					styleValue = css;
				} else if (css){
					classValue = css['class'] || '';
					styleValue = css['style'] || '';
				}
				
				var cls = 'class="datagrid-row ' + (index % 2 && opts.striped ? 'datagrid-row-alt ' : ' ') + classValue + '"';
				var style = styleValue ? 'style="' + styleValue + '"' : '';
				var rowId = state.rowIdPrefix + '-' + (frozen?1:2) + '-' + index;
				table.push('<tr id="' + rowId + '" datagrid-row-index="' + index + '" ' + cls + ' ' + style + '>');
				table.push(this.renderRow.call(this, target, fields, frozen, index, group.rows[j]));
				table.push('</tr>');
				index++;
			}
			table.push('</tbody></table>');
			return table.join('');
		},
		
		bindEvents: function(target){
			var state = $.data(target, 'datagrid');
			var dc = state.dc;
			var body = dc.body1.add(dc.body2);
			var clickHandler = ($.data(body[0],'events')||$._data(body[0],'events')).click[0].handler;
			body.unbind('click').bind('click', function(e){
				var tt = $(e.target);
				var expander = tt.closest('span.datagrid-row-expander');
				if (expander.length){
					var gindex = expander.closest('div.datagrid-group').attr('group-index');
					if (expander.hasClass('datagrid-row-collapse')){
						$(target).datagrid('collapseGroup', gindex);
					} else {
						$(target).datagrid('expandGroup', gindex);
					}
				} else {
					clickHandler(e);
				}
				e.stopPropagation();
			});
		},
		
		onBeforeRender: function(target, rows){
			var state = $.data(target, 'datagrid');
			var opts = state.options;
			
			initCss();
			
			var groups = [];
			for(var i=0; i<rows.length; i++){
				var row = rows[i];
				var group = getGroup(row[opts.groupField]);
				if (!group){
					group = {
						value: row[opts.groupField],
						rows: [row]
					};
					groups.push(group);
				} else {
					group.rows.push(row);
				}
			}
			
			var index = 0;
			var newRows = [];
			for(var i=0; i<groups.length; i++){
				var group = groups[i];
				group.startIndex = index;
				index += group.rows.length;
				newRows = newRows.concat(group.rows);
			}
			
			state.data.rows = newRows;
			this.groups = groups;
			
			var that = this;
			setTimeout(function(){
				that.bindEvents(target);
			},0);
			
			function getGroup(value){
				for(var i=0; i<groups.length; i++){
					var group = groups[i];
					if (group.value == value){
						return group;
					}
				}
				return null;
			}
			function initCss(){
				if (!$('#datagrid-group-style').length){
					$('head').append(
						'<style id="datagrid-group-style">' +
						'.datagrid-group{height:'+opts.groupHeight+'px;overflow:hidden;font-weight:bold;border-bottom:1px solid #ccc;}' +
						'.datagrid-group-title,.datagrid-group-expander{display:inline-block;vertical-align:bottom;height:100%;line-height:'+opts.groupHeight+'px;padding:0 4px;}' +
						'.datagrid-group-expander{width:'+opts.expanderWidth+'px;text-align:center;padding:0}' +
						'.datagrid-row-expander{margin:'+Math.floor((opts.groupHeight-16)/2)+'px 0;display:inline-block;width:16px;height:16px;cursor:pointer}' +
						'</style>'
					);
				}
			}
		}
	});

	$.extend($.fn.datagrid.methods, {
		groups:function(jq){
			return jq.datagrid('options').view.groups;
		},
	    expandGroup:function(jq, groupIndex){
	        return jq.each(function(){
	            var view = $.data(this, 'datagrid').dc.view;
	            var group = view.find(groupIndex!=undefined ? 'div.datagrid-group[group-index="'+groupIndex+'"]' : 'div.datagrid-group');
	            var expander = group.find('span.datagrid-row-expander');
	            if (expander.hasClass('datagrid-row-expand')){
	                expander.removeClass('datagrid-row-expand').addClass('datagrid-row-collapse');
	                group.next('table').show();
	            }
	            $(this).datagrid('fixRowHeight');
	        });
	    },
	    collapseGroup:function(jq, groupIndex){
	        return jq.each(function(){
	            var view = $.data(this, 'datagrid').dc.view;
	            var group = view.find(groupIndex!=undefined ? 'div.datagrid-group[group-index="'+groupIndex+'"]' : 'div.datagrid-group');
	            var expander = group.find('span.datagrid-row-expander');
	            if (expander.hasClass('datagrid-row-collapse')){
	                expander.removeClass('datagrid-row-collapse').addClass('datagrid-row-expand');
	                group.next('table').hide();
	            }
	            $(this).datagrid('fixRowHeight');
	        });
	    }
	});

	$.extend(groupview, {
		refreshGroupTitle: function(target, groupIndex){
			var state = $.data(target, 'datagrid');
			var opts = state.options;
			var dc = state.dc;
			var group = this.groups[groupIndex];
			var span = dc.body2.children('div.datagrid-group[group-index=' + groupIndex + ']').find('span.datagrid-group-title');
			span.html(opts.groupFormatter.call(target, group.value, group.rows));
		},
		
		insertRow: function(target, index, row){
			var state = $.data(target, 'datagrid');
			var opts = state.options;
			var dc = state.dc;
			var group = null;
			var groupIndex;
			
			if (!state.data.rows.length){
				$(target).datagrid('loadData', [row]);
				return;
			}
			
			for(var i=0; i<this.groups.length; i++){
				if (this.groups[i].value == row[opts.groupField]){
					group = this.groups[i];
					groupIndex = i;
					break;
				}
			}
			if (group){
				if (index == undefined || index == null){
					index = state.data.rows.length;
				}
				if (index < group.startIndex){
					index = group.startIndex;
				} else if (index > group.startIndex + group.rows.length){
					index = group.startIndex + group.rows.length;
				}
				$.fn.datagrid.defaults.view.insertRow.call(this, target, index, row);
				
				if (index >= group.startIndex + group.rows.length){
					_moveTr(index, true);
					_moveTr(index, false);
				}
				group.rows.splice(index - group.startIndex, 0, row);
			} else {
				group = {
					value: row[opts.groupField],
					rows: [row],
					startIndex: state.data.rows.length
				}
				groupIndex = this.groups.length;
				dc.body1.append(this.renderGroup.call(this, target, groupIndex, group, true));
				dc.body2.append(this.renderGroup.call(this, target, groupIndex, group, false));
				this.groups.push(group);
				state.data.rows.push(row);
			}
			
			this.refreshGroupTitle(target, groupIndex);
			
			function _moveTr(index,frozen){
				var serno = frozen?1:2;
				var prevTr = opts.finder.getTr(target, index-1, 'body', serno);
				var tr = opts.finder.getTr(target, index, 'body', serno);
				tr.insertAfter(prevTr);
			}
		},
		
		updateRow: function(target, index, row){
			var opts = $.data(target, 'datagrid').options;
			$.fn.datagrid.defaults.view.updateRow.call(this, target, index, row);
			var tb = opts.finder.getTr(target, index, 'body', 2).closest('table.datagrid-btable');
			var groupIndex = parseInt(tb.prev().attr('group-index'));
			this.refreshGroupTitle(target, groupIndex);
		},
		
		deleteRow: function(target, index){
			var state = $.data(target, 'datagrid');
			var opts = state.options;
			var dc = state.dc;
			var body = dc.body1.add(dc.body2);
			
			var tb = opts.finder.getTr(target, index, 'body', 2).closest('table.datagrid-btable');
			var groupIndex = parseInt(tb.prev().attr('group-index'));
			
			$.fn.datagrid.defaults.view.deleteRow.call(this, target, index);
			
			var group = this.groups[groupIndex];
			if (group.rows.length > 1){
				group.rows.splice(index-group.startIndex, 1);
				this.refreshGroupTitle(target, groupIndex);
			} else {
				body.children('div.datagrid-group[group-index='+groupIndex+']').remove();
				for(var i=groupIndex+1; i<this.groups.length; i++){
					body.children('div.datagrid-group[group-index='+i+']').attr('group-index', i-1);
				}
				this.groups.splice(groupIndex, 1);
			}
			
			var index = 0;
			for(var i=0; i<this.groups.length; i++){
				var group = this.groups[i];
				group.startIndex = index;
				index += group.rows.length;
			}
		}
	});


	// end of group view definition
	
	$.fn.propertygrid.defaults = $.extend({}, $.fn.datagrid.defaults, {
		groupHeight:21,
		expanderWidth:16,
		singleSelect:true,
		remoteSort:false,
		fitColumns:true,
		loadMsg:'',
		frozenColumns:[[
		    {field:'f',width:16,resizable:false}
		]],
		columns:[[
		    {field:'name',title:'Name',width:100,sortable:true},
		    {field:'value',title:'Value',width:100,resizable:false}
		]],
		
		showGroup:false,
		groupView:groupview,
		groupField:'group',
		groupFormatter:function(fvalue,rows){return fvalue}
	});
})(jQuery);
>>>>>>> 17ac48076d7a7be7437c8367312af01562698b1c
