package com.neo.sys.utils;

import com.neo.sys.entity.JqGridPage;
import com.neo.sys.entity.Page;
import com.neo.sys.listener.AccessInterface;
import com.neo.sys.listener.JqGridConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class ListPageHelper {

    private static List<Map<String,Object>> emptyList = new ArrayList<>();

    /**
     * 创建分页对象
     * @param page 前台传入的分页对象
     * @param originalList 从数据库或其他数据源查询出的原生List集合
     * @param jqc 转换器
     * @param fields 需要转换的属性列表
     * @return 返回分页对象
     */
    public static JqGridPage<?> createResult(Page page, List<?> originalList, JqGridConverter jqc,String... fields){
        return createResult(page,originalList,jqc,null,fields);
    }
    /**
     * 创建分页对象
     * @param page 前台传入的分页对象
     * @param originalList 从数据库或其他数据源查询出的原生List集合
     * @param jqc 转换器
     * @param fields 需要转换的属性列表
     * @return 返回分页对象
     */
    public static JqGridPage<?> createResult(Page page, List<?> originalList, JqGridConverter jqc, AccessInterface p, String... fields){

        JqGridPage<?> jqPage = new JqGridPage<>();
        List<Map<String,Object>> listResult = JsonDataUtils.createJqGridResult(originalList,true,jqc,p,fields);
        if(listResult == null || listResult.isEmpty()){
            jqPage.setRows(emptyList);
            jqPage.setRecords(0L);
            jqPage.setPage(1L);
        }else{
            jqPage.setRecords((long)listResult.size());
            if(page != null){
                jqPage.setPage(page.getPage());
                long start = (page.getPage()<=0?0:page.getPage()-1)*page.getRows();
                long end = start+page.getRows();
                end = end>listResult.size()?listResult.size():end;
                jqPage.setRows(listResult.subList((int)start,(int)end));
            }else{
                jqPage.setRows(listResult);
            }
        }
        return jqPage;
    }

    /**
     * 对生成结果直接返回一个分页对象
     * @param page
     * @param listResult
     * @return
     */
    public static JqGridPage<?> createResult(Page page, List<Map<String,Object>> listResult){
        return createResult(page,listResult,null);
    }
    public static JqGridPage<?> createResult(Page page, List<Map<String,Object>> listResult,AccessInterface p){

        JqGridPage<?> jqPage = new JqGridPage<>();
        if(listResult == null){
        	listResult = emptyList;
        }
        if(listResult == null || listResult.isEmpty()){
            jqPage.setRows(emptyList);
            jqPage.setRecords(0L);
            jqPage.setPage(1L);
        }else{
            listResult = listResult.stream().filter(o->{
                if(p!=null){
                    return p.test(o);
                }
                return true;
            }).collect(Collectors.toList());
            jqPage.setRecords((long)listResult.size());
            jqPage.setPage(page.getPage());
            long start = (page.getPage()<=0?0:page.getPage()-1)*page.getRows();
            long end = start+page.getRows();
            end = end>listResult.size()?listResult.size():end;
            jqPage.setRows(listResult.subList((int)start,(int)end));
        }
        return jqPage;
    }
}
