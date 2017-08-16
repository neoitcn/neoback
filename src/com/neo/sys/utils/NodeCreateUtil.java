package com.neo.sys.utils;

import org.apache.commons.lang.StringUtils;

import com.neo.sys.listener.CountConvert;

import java.util.*;
import java.util.function.BiConsumer;

public class NodeCreateUtil {
    private static ThreadLocal<Map<?,?>> localThread = new ThreadLocal<>();

    /**
     * 该方法是将对象转换为拥有上下级关系的Map集合
     * @param originalList 要被转换的平行list集合，该集合中的元素可以是普通对象，也可以是Map集合，甚至集合类型不同都没有关系。普通对象必须包含被指定的属性名，而map中必须包含被指定的key
     * @param field 需要被转换的列表。该参数必须被指定。 其中可以使用 别名=原始名的方式指定属性。 field不能为空，否则应用程序不执行解析操作
     *              其中，Map自身会携带一些参数
     *              在map集合中，会自身携带一些参数：id，parentId,children
     *              其中可以使用pid=parentId这样的方式为之起别名。允许的格式：pid=parentId,pid= 和pid 三种格式
     *              而id是必须值，如果未传入，则自动启用id。对象中必须包含id字段。
     *
     *              以下算法有待更新，因为多循环了一次，可以放在一次循环中完成，无需分两步操作。有兴趣修改的可以改改。
     *
     */
    public static List<Map<Object,Object>> createContextNode(List<?> originalList,String... field){
        if(field == null || field.length == 0){
            return null;
        }
        if(originalList == null || originalList.size() == 0){
            return null;
        }
        //该集合存放了 原生属性名=别名的键值对
        Map<String,String> fieldAN = (Map<String, String>) localThread.get();
        if(localThread.get() == null){
            fieldAN = new HashMap<>();
            localThread.set(fieldAN);

            //解析field
            int index;
            for(String str:field){
                str = str.trim();
                if((index=str.indexOf("="))!=-1 && index < str.length()-1){
                    fieldAN.put(str.substring(index+1),str.substring(0,index));
                }else{
                    if(index == -1){
                        fieldAN.put(str,str);
                    }else{
                        str = str.substring(0,index);
                        if(StringUtils.isNotBlank(str))
                            fieldAN.put(str,str);
                    }
                }
            }
            if(fieldAN.get("parentId") == null){
                fieldAN.put("parentId","parentId");
            }
            if(fieldAN.get("id") == null){
                fieldAN.put("id","id");
            }
            if(fieldAN.get("children") == null){
                fieldAN.put("children","children");
            }
        }
        List<Map<Object,Object>> listResult = new ArrayList<>();
        List<Map<Object,Object>> faildResult = new ArrayList<>(); //添加失败了的集合
        for(Object obj:originalList){
            if(obj!=null){
                Map<Object,Object> result = new HashMap<>();
                for(String key:fieldAN.keySet()){
                    String alias = fieldAN.get(key);
                    if(obj instanceof Map){
                        Map<Object,Object> m = (Map<Object, Object>) obj;
                        result.put(alias,m.get(key));
                    }else{
                        try {
                            Object value = InvokeUtil.invokeMethod(obj,"get"+key.substring(0,1).toUpperCase()+key.substring(1),null);
                            result.put(alias,value);
                        } catch (Exception e) {
                        }
                    }
                }
                Object o = addContextNode(listResult,result,fieldAN.get("id"),fieldAN.get("parentId"),fieldAN.get("children"));
                if(o == null && listResult.isEmpty()){
                    listResult.add(result);
                }else if(o == null){
                    faildResult.add(result);
                }
            }
        }

        int failCount = faildResult.size();
        if(failCount > 0){
            failCount = -1;
            while(failCount != faildResult.size()){
                for(int i=0;i<faildResult.size();){
                    Object o = addContextNode(listResult,faildResult.get(i),fieldAN.get("id"),fieldAN.get("parentId"),fieldAN.get("children"));
                    if(o!=null){
                        faildResult.remove(i);
                        continue;
                    }
                    i++;
                }
                failCount = faildResult.size();
            }

            if(!faildResult.isEmpty()){
                listResult.addAll(faildResult);
            }
        }

        localThread.remove();

        return listResult;
    }

    /**
     * 循环遍历具备上下级关系的集合，该集合来自createContextNode的转换结果。
     * 请注意，使用该方法遍里时，会自动为Map添加一个属性 _level，它记录当前节点的级别。请确保Map中没有该_level属性，否则会导致值覆盖问题。
     * _level从0开始
     * _level在doWork之前已经被存入，所以可以在doWork方法中获取_level的值
     * @param listMap
     * @param doWork
     */
    public static void foreachContextNode(List<Map<Object,Object>> listMap,BiConsumer<Integer,Map<Object,Object>> doWork){

        foreachContextNode(listMap,doWork,new int[]{0},0);

    }

    /**
     * 循环遍历具备上下级关系的集合，该集合来自createContextNode的转换结果。
     * 这是一个私有方法，如果有必要可以改为public，它用于记录当前节点属于第几级别。最顶级级别的level为0.
     * 请注意，使用该方法遍里时，会自动为Map添加一个属性 _level，它记录当前节点的级别。请确保Map中没有该_level属性，否则会导致值覆盖问题。
     * index为循环下标，从0开始 为了index的准确性，这里使用数组对象引用，获取index[0]即为当前的index.
     * @param listMap
     * @param doWork
     */
    private static void foreachContextNode(List<Map<Object,Object>> listMap, BiConsumer<Integer,Map<Object,Object>> doWork,int []index, int level){
        List<Map<Object,Object>> childMap;
        for(Map<Object,Object> map:listMap){
            map.put("_level",level);
            if(doWork!=null)
                doWork.accept(index[0]++,map);
            else index[0]++;
            if((childMap= ( List<Map<Object,Object>> ) map.get("children"))!=null){
                foreachContextNode(childMap,doWork,index,level+1);
            }
        }

    }

    /**
     * 将具备上下级关系的listMap转换为平行关系
     * 其中，children属性将会被移除。
     * @param listResult 要保存的对象
     * @param listMap 被转换对象
     * @param childrenName 转换依据 子节点
     */
    public static void contextToSliderNode(List<Map<Object,Object>> listResult,List<Map<Object,Object>> listMap,String childrenName){
        if(listMap == null)
            return ;
        Map<Object,Object> toMap;
        for(Map<Object,Object> map:listMap){
            toMap  = new HashMap<>();
            for(Object key:map.keySet()){
                if(childrenName.equals(key)){
                    continue;
                }
                toMap.put(key,map.get(key));
            }
            listResult.add(toMap);
            contextToSliderNode(listResult,(List<Map<Object,Object>>)map.get(childrenName),childrenName);
        }

    }

    //这里返回的Object用于标识是否完成添加。如果Object返回空表示未添加成功，不为空则添加成功。
    //如果返回空，则你可以直接在listResult中添加node了。因为并没有在指定的listResult中找到和node关联的位置，表示node属于顶级的并列节点了。

    /**
     * 为List<Map>>添加父/子节点，
     * @param listResult 已创建的list
     * @param node 要添加的node，注意该node必须是单子节点，它目前不可以有下级，否则如果node属于listResult父节点时，listResult会替换掉node的原来子节点。
     * @param idName node中的id名称
     * @param pidName node中的parentId名字
     * @param childrenName node中的children的名字
     * @return 如果添加成功返回值不为空，添加失败返回值为空
     */
    public static Object addContextNode(List<Map<Object,Object>> listResult,Map<Object,Object> node,String idName,String pidName,String childrenName){

        Object id;
        Object parentId;
        List<Map<Object, Object>> children;

        for(Map<Object,Object> map:listResult){

            //获取当前信息，
            id = map.get(idName);
            parentId = map.get(pidName);
            children = (List<Map<Object, Object>>) map.get(childrenName);
            if(children == null){
                children = new ArrayList<>();
                map.put(childrenName,children);
            }else if(children.size() > 0){
                if(addContextNode(children,node,idName,pidName,childrenName)!=null){
                    return "";
                }
            }
            //如果被添加的node属于当前被遍历的子机构，则添加

            if(id!= null && id.equals(node.get(pidName))){
                children.add(node);
                return "";
            }else if(parentId != null && parentId.equals(node.get(idName))){ //如果被遍历的节点属于node下属节点，则添加，一般情况下，被遍历的node是当前的顶级节点。
                children = new ArrayList<>();
                children.addAll(listResult);
                node.put(childrenName,children);
                listResult.clear();
                listResult.add(node);
                return "";
            }

        }

        return null;
    }

    /**
     *  平行未分级的集合，添加列，边添加边计算被添加进去的数量，并显示在每一个标签上。
     * 请注意，此方法只适合统计最后一级数据，也就是说新增的数据不会被统计。统计完成后会添加到数据
     * 一定要注意，被统计的父节点不能有被统计的字段，如果有则会出现意想不到的错误
     * @param listResult
     * @param nodeList
     * @param idName
     * @param pidName
     * @param fieldNames
     */
    public static void countAddSliderNodeForLast(List<Map<Object,Object>> listResult, List<Map<Object,Object>> nodeList, String idName, String pidName, CountConvert<Object,Object> p, Object...fieldNames){
        countSliderNodeForLast(listResult,nodeList,idName,pidName,p,fieldNames);
        listResult.addAll(nodeList);
    }

    /**
     * 平行未分级的集合，只，并显示在每一个标签上。
     * 请注意，此方法只适合统计最后一级数据，也就是说新增的数据不会被统计。
     * @param listResult
     * @param nodeList
     * @param fieldNames
     */
    public static void countSliderNodeForLast(List<Map<Object,Object>> listResult, List<Map<Object,Object>> nodeList, String idName, String pidName, CountConvert<Object,Object> p, Object...fieldNames){

        List<Map<Object,Object>> temp1 = new LinkedList<>();
        List<Map<Object,Object>> temp2 = new LinkedList<>();
        temp1.addAll(listResult);
        temp2.addAll(nodeList);
        Object lpid,tid;
        Map<Object,Object> ltempObj,ttempObj;
        Map<Object,Object> lastValue = new HashMap<>();
        //先比对新增的数据，将非新增的最底层数据进行一次数据基数添加。
        for(int i=0;i<temp2.size();){ //取新增数据
            ltempObj = temp2.get(i);
            lpid = ltempObj.get(pidName);
            for(int j=0;j<temp1.size();j++){ //取原来数据
                ttempObj = temp1.get(j);
                tid=ttempObj.get(idName);
                if(tid.equals(lpid)){ //新增父id与原来子id相同
                    if(fieldNames != null && fieldNames.length > 0 && p!=null){
                        for(Object fieldName:fieldNames){
                            //ttempObj是原来数据
                            //ltempObj是新增数据
                            ttempObj.put(fieldName,p.apply(ttempObj.get(fieldName) == null,fieldName,ttempObj.get(fieldName),ltempObj.get(fieldName),ttempObj,ltempObj));
                        }
                    }
                    continue;
                }
            }
            temp2.remove(i);
        }


        //该部分是后续优化后修正的部分： 2017/3/2
        int countSize = -1;
        while(countSize != temp1.size()){
            countSize = temp1.size();

            if(countSize > 0){
                List<Integer> inte = countMethod(temp1,temp1.get(0),idName,pidName);
                if(inte != null){
                    //先找到父机构
                    Map<Object,Object> map = temp1.get(inte.get(0));
                    Map<Object,Object> parent = null;
                    for(Map<Object,Object> pa:temp1){
                        if(pa.get(idName).equals(map.get(pidName))){
                            parent = pa;
                            break;
                        }
                    }
                    if(parent == null){ //没有父机构那就没必要再计算了。
                        break;
                    }
                    boolean b = true;
                    for(Integer index:inte){
                        map = temp1.get(index);
                        for(Object fieldName:fieldNames){
                            parent.put(fieldName,p.apply(b,fieldName,parent.get(fieldName),map.get(fieldName),parent,map));
                        }
                        if(b){
                            b = false;
                        }
                    }

                    //移除已经计算过的最底层的元素
                    Collections.sort(inte,(o1,o2)->o1-o2);
                    for(int i=0;i<inte.size();i++){
                        temp1.remove(inte.get(i)-i);
                    }
                }
            }
        }
    }

    /**
     * 查找指定节点的子节点，找到该节点的最底部的子节点，返回这些节点所在list的index值。其中list集合是平行级结合，不存在上下级关系。
     * 如果被查找的节点已经是最底层节点，和它同级别的其他子节点存在下级子节点，则会去查找同级别的下级子节点，直到找到最顶层为止。
     * 2017/3/2
     * @param list
     * @param map
     */
    private static List<Integer> countMethod(List<Map<Object,Object>> list,Map<Object,Object> map,String idName,String pidName){
        Map m;
        boolean isNotFound = true; //是否发现还有子机构
        for(int i = 0;i< list.size();i++){
            m = list.get(i);
            if(map.get(idName).equals(m.get(pidName))){
                return countMethod(list,m,idName,pidName);
            }
        }
        //已经查找到最底层节点的第一步，接下来查找与该节点同级的其他节点，如果其他节点存在子节点，则去查找该子节点。
        List<Integer> temp = new ArrayList<>();
        for(int i = 0;i< list.size();i++){
            m = list.get(i);
            //查找同级别的其他节点。
            if(!m.get(idName).equals(map.get(idName))&&((m.get(pidName) == null && map.get(pidName) == null) || (m.get(pidName)!=null && m.get(pidName).equals(map.get(pidName))) || (map.get(pidName)!=null && map.get(pidName).equals(m.get(pidName))))){
                for(Map<Object,Object> tm:list){
                    if(m.get(idName).equals(tm.get(pidName))){
                        return countMethod(list,tm,idName,pidName);
                    }
                }
                temp.add(i);
            }else if(m.get(idName).equals(map.get(idName))){
                temp.add(i);
            }
        }
        return temp;

    }

    /**
     * 如果值为空，则返回默认值
     * @param t
     * @param def 默认值
     * @return
     */
    public static Object getOrDefault(Object t,Object def){
        return t==null?def:t;
    }



    public static void main(String args[]) throws Exception{

        /*FileInputStream fis = new FileInputStream("D:/test/officeMap.dat");
        ObjectInputStream ois = new ObjectInputStream(fis);
        List<Map<Object,Object>> listMap = (List<Map<Object, Object>>) ois.readObject();
        ois.close();
        fis.close();
        fis = new FileInputStream("D:/test/officePersonMap.dat");
        ois = new ObjectInputStream(fis);
        List<Map<Object,Object>> listPersionMap = (List<Map<Object, Object>>) ois.readObject();
        ois.close();
        fis.close();

        Map<Object,Object> m = listMap.get(0);
        System.out.println("-->"+m.get("id")+"   "+m.get("parentId")+"    "+m.get("name"));
        List<Integer> listInt = countMethod(listMap,m,"id","parentId");
        System.out.println(listInt);
        for(int index:listInt){
            System.out.println(listMap.get(index).get("name"));
        }

        countSliderNodeForLast(listMap,listPersionMap,"id","parentId",(isFirst,fieldName,lastValue,currentValue,lastObj,currentObj)->{
            switch((String)fieldName){
                case "onlineSize":
                    Map<Object,Object> map = (Map<Object, Object>) currentObj;
                    Short online = (Short) map.get("online");
                    return isFirst?((online==null||online!=1)?0:1):(online==null||online!=1?lastValue:(int)lastValue+1);
                case "size":
                    map = (Map<Object, Object>) currentObj;
                    if(map.get("online") != null){
                        return isFirst?1:(int)lastValue+1;
                    }else{
                        return isFirst?currentValue:(int)getOrDefault(lastValue,0)+(int)getOrDefault(currentValue,0);
                    }
            }
            return null;
        },"size","onlineSize");

        System.out.println(listMap);*/
        Short s = 1;
        System.out.println(s == 1);
    }

}
