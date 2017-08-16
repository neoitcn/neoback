//package com.neoit.sys.web;
//
//import com.neoit.entity.docm.Drugster;
//import com.neoit.entity.sys.Office;
//import com.neoit.sys.utils.InvokeUtil;
//
//import org.apache.commons.lang.StringUtils;
//
//import java.io.FileInputStream;
//import java.io.ObjectInputStream;
//import java.util.*;
//import java.util.stream.Collectors;
//
//
//public class Test {
//
//    public static void main(String args[]) throws Exception{
//
//        FileInputStream fis = new FileInputStream("C:\\Users\\Shisan\\Desktop/drust.dat");
//        ObjectInputStream ois = new ObjectInputStream(fis);
//
//        List<Drugster> list = (List<Drugster>) ois.readObject();
//        System.out.println(list.size());
//        //list.stream().forEach(o->System.out.println(o.getOfficeId()));
//
//        fis = new FileInputStream("C:\\Users\\Shisan\\Desktop/office.dat");
//        ois = new ObjectInputStream(fis);
//
//        List<Office> list2 = (List<Office>) ois.readObject();
//
//        //list2.stream().forEach(o->System.out.println(o.getName()));
//        Map<String,Object> map = new HashMap<>();
//        List<Map<String,Object>> test2;
//        List<Map<String,Object>> listResult = createContextNode(list2,"name");
////        System.out.println(listResult);
//        List<Map<String,Object>> testList;
//        countSliderNodeForLast(
//                testList=list2.stream().map(o->{
//                Map<String,Object> m = new HashMap<>();
//                m.put("name",o.getName());
//                m.put("id",o.getId());
//                m.put("parentId",o.getParentId());
//                return m;
//            }).collect(Collectors.toList())
//            ,test2=list.stream().map(d->{
//                Map<String,Object> m = new HashMap<>();
//                m.put("name",d.getName());
//                m.put("id",d.getId());
//                m.put("parentId",d.getOfficeId());
//                return m;
//        }).collect(Collectors.toList()),"id","parentId","size");
//        final List<Map<String,Object>> _testList = createContextNode(testList,"name","size");
//        test2.stream().forEach(d->{
//            addContextNode(_testList,d,"id","parentId","children");
//        });
//        System.out.println(_testList);
//    }
//
//    private static ThreadLocal<Map<?,?>> localThread = new ThreadLocal<>();
//
//    /**
//     * 该方法是将对象转换为拥有上下级关系的Map集合
//     * @param originalList 要被转换的平行list集合
//     * @param field 需要被转换的列表。该参数必须被指定。 其中可以使用 别名=原始名的方式指定属性。 field不能为空，否则应用程序不执行解析操作
//     *              其中，Map自身会携带一些参数
//     *              在map集合中，会自身携带一些参数：id，parentId,children
//     *              其中可以使用pid=parentId这样的方式为之起别名。允许的格式：pid=parentId,pid= 和pid 三种格式
//     *              而id是必须值，如果为传入，则自动启用id。对象中必须包含id字段。
//     */
//    public static List<Map<String,Object>> createContextNode(List<?> originalList,String... field){
//        if(field == null || field.length == 0){
//            return null;
//        }
//        if(originalList == null || originalList.size() == 0){
//            return null;
//        }
//        //该集合存放了 原生属性名=别名的键值对
//        Map<String,String> fieldAN = (Map<String, String>) localThread.get();
//        if(localThread.get() == null){
//            fieldAN = new HashMap<>();
//            localThread.set(fieldAN);
//
//            //解析field
//            int index;
//            for(String str:field){
//                str = str.trim();
//                if((index=str.indexOf("="))!=-1 && index > str.length()-1){
//                    fieldAN.put(str.substring(index+1),str.substring(0,index));
//                }else{
//                    if(index == -1){
//                        fieldAN.put(str,str);
//                    }else{
//                        str = str.substring(0,index);
//                        if(StringUtils.isNotBlank(str))
//                            fieldAN.put(str,str);
//                    }
//                }
//            }
//            if(fieldAN.get("parentId") == null){
//                fieldAN.put("parentId","parentId");
//            }
//            if(fieldAN.get("id") == null){
//                fieldAN.put("id","id");
//            }
//            if(fieldAN.get("children") == null){
//                fieldAN.put("children","children");
//            }
//        }
//        List<Map<String,Object>> listResult = new ArrayList<>();
//        for(Object obj:originalList){
//            if(obj!=null){
//                Map<String,Object> result = new HashMap<>();
//                for(String key:fieldAN.keySet()){
//                    String alias = fieldAN.get(key);
//                    if(obj instanceof Map){
//                        Map<String,Object> m = (Map<String, Object>) obj;
//                        result.put(alias,m.get(alias));
//                    }else{
//                        try {
//                            Object value = InvokeUtil.invokeMethod(obj,"get"+key.substring(0,1).toUpperCase()+key.substring(1),null);
//                            result.put(alias,value);
//                        } catch (Exception e) {
//                        }
//                    }
//                }
//                Object o = addContextNode(listResult,result,fieldAN.get("id"),fieldAN.get("parentId"),fieldAN.get("children"));
//                if(o == null){
//                    listResult.add(result);
//                }
//            }
//        }
//
//        localThread.remove();
//
//        return listResult;
//    }
//
//    //这里返回的Object用于标识是否完成添加。如果Object返回空表示未添加成功，不为空则添加成功。
//    //如果返回空，则你可以直接在listResult中添加node了。
//
//    /**
//     * 为List<Map>>添加父/子节点，
//     * @param listResult 已创建的list
//     * @param node 要添加的node
//     * @param idName node中的id名称
//     * @param pidName node中的parentId名字
//     * @param childrenName node中的children的名字
//     * @return 如果添加成功返回值不为空，添加失败返回值为空
//     */
//    public static Object addContextNode(List<Map<String,Object>> listResult,Map<String,Object> node,String idName,String pidName,String childrenName){
//
//        Object id;
//        Object parentId;
//        List<Map<String, Object>> children;
//
//        for(Map<String,Object> map:listResult){
//
//            //获取当前信息，
//            id = map.get(idName);
//            parentId = map.get(pidName);
//            children = (List<Map<String, Object>>) map.get(childrenName);
//            if(children == null){
//                children = new ArrayList<>();
//                map.put(childrenName,children);
//            }else if(children.size() > 0){
//                if(addContextNode(children,node,idName,pidName,childrenName)!=null){
//                    return "";
//                }
//            }
//            //如果被添加的node属于当前被遍历的子机构，则添加
//            if(id.equals(node.get(pidName))){
//                children.add(node);
//                return "";
//            }else if(parentId.equals(node.get(idName))){ //如果被遍历的节点属于node下属节点，则添加，一般情况下，被遍历的node是当前的顶级节点。
//                node.put(childrenName,children);
//                children.addAll(listResult);
//                listResult.clear();
//                listResult.add(node);
//                return "";
//            }
//
//        }
//
//        return null;
//    }
//
//    /**
//     *  平行未分级的集合，添加列，边添加边计算被添加进去的数量，并显示在每一个标签上。
//     * 请注意，此方法只适合统计最后一级数据，也就是说新增的数据不会被统计。统计完成后会添加到数据
//     * @param listResult
//     * @param nodeList
//     * @param idName
//     * @param pidName
//     * @param fieldName
//     */
//    public static void countAddSliderNodeForLast(List<Map<String,Object>> listResult,List<Map<String,Object>> nodeList,String idName,String pidName,String fieldName){
//        countSliderNodeForLast(listResult,nodeList,idName,pidName,fieldName);
//        listResult.addAll(nodeList);
//    }
//
//    /**
//     * 平行未分级的集合，添加列，边添加边计算被添加进去的数量，并显示在每一个标签上。
//     * 请注意，此方法只适合统计最后一级数据，也就是说新增的数据不会被统计。
//     * @param listResult
//     * @param nodeList
//     * @param fieldName
//     */
//    public static void countSliderNodeForLast(List<Map<String,Object>> listResult,List<Map<String,Object>> nodeList,String idName,String pidName,String fieldName){
//
//        List<Map<String,Object>> temp1 = new LinkedList<>();
//        List<Map<String,Object>> temp2 = new LinkedList<>();
//        temp1.addAll(listResult);
//        temp2.addAll(nodeList);
//        Object lpid,tid;
//        Map<String,Object> ltempObj,ttempObj;
//        //先比对新增的数据，将非新增的最底层数据进行一次数据基数添加。
//        for(int i=0;i<temp2.size();){
//            ltempObj = temp2.get(i);
//            lpid = ltempObj.get(pidName);
//            for(int j=0;j<temp1.size();j++){
//                ttempObj = temp1.get(j);
//                tid=ttempObj.get(idName);
//                if(tid.equals(lpid)){
//                    ttempObj.put(fieldName,(int)(ttempObj.getOrDefault(fieldName,0))+1);
//                    continue;
//                }
//            }
//            temp2.remove(i);
//        }
//
//        //然后层层往上叠加。
//        Integer size;
//        boolean isEnd = true;
//        while(isEnd){
//            isEnd = false;
//            there:for(int i=0;i<temp1.size();){
//                ttempObj = temp1.get(i);
//                if((size= (Integer) ttempObj.get(fieldName))!=null){
//                    temp1.remove(i);
//                    for(int j=0;j<temp1.size();j++){
//                        ltempObj = temp1.get(j);
//                        if(ttempObj.get(pidName).equals(ltempObj.get(idName))){
//                            ltempObj.put(fieldName,(int)ltempObj.getOrDefault(fieldName,0)+size);
//                            isEnd = true;
//                            continue there;
//                        }
//                    }
//                }
//                i++;
//            }
//        }
//
//        //最后把所有的信息添加到集合中g
//        //listResult.addAll(nodeList);
//
//    }
//
//}
