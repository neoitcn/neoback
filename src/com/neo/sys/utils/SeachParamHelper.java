package com.neo.sys.utils;

import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.Statement;
import java.util.*;

/**
 * 未封装完成。
 * 该类将简化getResultByParam的操作。任何一个对数据表的操作需要使用多个参数查询时，可使用该方法来简化操作。
 * 简化后的方法，需要对getResultsByParam()方法做支撑依赖。一切需要使用该方法所对应的xml需要实现getResultsByParam()方法。
 * 虽然依赖于getAll方法，但完全不执行getResultsByParam方法所对应的SQL语句
 * 该类不直接支持对联表查询的自动封装操作。如需实现，需要在xml中对相关属性进行配置。
 * 可以为表手动起别名，比如 user u
 */
@Intercepts({ @Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class, Integer.class }),
        @Signature(type = ResultSetHandler.class, method = "handleResultSets", args = { Statement.class }) })
public class SeachParamHelper implements Interceptor {
    private static final Logger logger = Logger.getLogger(SeachParamHelper.class);

    public static final ThreadLocal<Object[]> localParam = new ThreadLocal<>();
    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        if (localParam.get() == null) {
            return invocation.proceed();
        }
        if (invocation.getTarget() instanceof StatementHandler) {
            StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
            MetaObject metaStatementHandler = SystemMetaObject.forObject(statementHandler);
            // 分离代理对象链(由于目标类可能被多个拦截器拦截，从而形成多次代理，通过下面的两次循环
            // 可以分离出最原始的的目标类)
            while (metaStatementHandler.hasGetter("h")) {
                Object object = metaStatementHandler.getValue("h");
                metaStatementHandler = SystemMetaObject.forObject(object);
            }
            // 分离最后一个代理对象的目标类
            while (metaStatementHandler.hasGetter("target")) {
                Object object = metaStatementHandler.getValue("target");
                metaStatementHandler = SystemMetaObject.forObject(object);
            }
            MappedStatement mappedStatement = (MappedStatement) metaStatementHandler
                    .getValue("delegate.mappedStatement");
            // 重写sql
            String paramSql = buildParamSql();
            // 重写分页sql
            metaStatementHandler.setValue("delegate.boundSql.sql", paramSql);
            Connection connection = (Connection) invocation.getArgs()[0];
            // 将执行权交给下一个拦截器
            return invocation.proceed();
        } else if (invocation.getTarget() instanceof ResultSetHandler) {
            Object result = invocation.proceed();
            return result;
        }
        return null;
    }

    @Override
    public Object plugin(Object o) {
        return null;
    }

    @Override
    public void setProperties(Properties properties) {

    }

    /**
     * 使用前必须调用该方法，以开启创建查询工具
     */
    public void startProcess(){
        Object objArr[] = new Object[3];
        objArr[0]=new ArrayList<Param>();
        objArr[1]= new ArrayList<LinkTable>();
        localParam.set(objArr);
    }

    /**
     * 使用后必须调用该方法，以释放资源
     */
    public void closeProcess(){
        localParam.remove();
    }

    public static void putLinkTable(LinkTable t){
        List<LinkTable> list = (List<LinkTable>) localParam.get()[1];
        list.add(t);
    }
    public static void putLinkTables(LinkTable... ts){
        List<LinkTable> list = (List<LinkTable>) localParam.get()[1];
        list.addAll(Arrays.asList(ts));
    }

    public static void putParam(Param p){
        List<Param> list = (List<Param>) localParam.get()[0];
        list.add(p);
    }

    public static void putParams(Param...ps){
        List<Param> list = (List<Param>) localParam.get()[0];
        list.addAll(Arrays.asList(ps));
    }

    /**
     * 执行指定的方法名语句
     * @param serviceClass
     */
    public static void execute(Class<?> serviceClass,String tableName) throws Exception {
        localParam.get()[2]=tableName;
        Object object = SpringUtils.getApplicationBean(serviceClass);
        InvokeUtil.invokeMethod(object,"getResultsByParam",new HashMap<>());
    }

    /**
     * 创建查询SQL
     * @return
     */
    private String buildParamSql(){
        Object[] objArr = localParam.get();
        List<Param> listParam = (List<Param>) objArr[0];
        List<LinkTable> listLinkTable = (List<LinkTable>) objArr[1];
        String tableName = (String) objArr[2];
        StringBuilder sqlBuilder = new StringBuilder();
        int index = 0;
        String tableAlie = createAlia(index);
        Map<String,String> alieMap = new HashMap<>();
        alieMap.put(tableName,tableAlie);
        sqlBuilder.append("SELECT ").append(tableAlie).append(".* ");
        for(LinkTable linkTable:listLinkTable){
            sqlBuilder.append(",");
            tableName = linkTable.getTableName();
            tableAlie = createAlia(++index);
            alieMap.put(tableName,tableAlie);
            sqlBuilder.append(tableName).append(".* ");
        }
        sqlBuilder.append(" from ").append((String) objArr[2]).append(" ");
        List<Param> listP;
        for(LinkTable linkTable:listLinkTable){
            tableName = linkTable.getTableName();
            tableAlie = alieMap.get(linkTable.getTableName());
            sqlBuilder.append("left join ").append(tableName).append(" ").append(tableAlie).append(" ");
            listP = linkTable.getListParam();
            if(listP!=null && listP.size()>0){
                sqlBuilder.append("on ");
                boolean b = false;
                for(Param p:listP){
                    if(b){
                        if(ParamLogical.AND.equals(p.getLogical())){
                            sqlBuilder.append(" AND ");
                        }else if(ParamLogical.OR.equals(p.getLogical())){
                            sqlBuilder.append(" OR ");
                        }
                    }
                    sqlBuilder.append(p.getTargetColumn());
                    switch(p.getpDo()){
                        case EQ:
                            sqlBuilder.append("= ");
                            break;
                        case LTQ:
                            sqlBuilder.append("<= ");
                            break;
                        case LT:
                            sqlBuilder.append("< ");
                            break;
                        case GTQ:
                            sqlBuilder.append(">= ");
                            break;
                        case GT:
                            sqlBuilder.append("> ");
                        case LIKE:
                            sqlBuilder.append("LIKE ");
                    }
                    if(ParamValue.COLUMN.equals(p.getpValue())){
                        //遇到date类型的问题
//                        sqlBuilder.append();
                    }
                    b=true;
                }

            }
        }
        return null;
    }

    /**
     * 链表操作的类
     * 被链表操作的类，无需添加Table注解，但是需要在xml中配置。
     */
    public static class LinkTable {
        public LinkTable(String tableName, Param... param) {
            this.tableName = tableName;
            if (param != null) {
                listParam = Arrays.asList(param);
            }
        }

        private String tableName;
        private List<Param> listParam;

        public String getTableName() {
            return tableName;
        }

        public List<Param> getListParam() {
            return listParam;
        }
    }

    /**
     * 参数指定类，
     */
    public static class Param {
        private String targetColumn;
        private String value;
        private ParamLogical logical = ParamLogical.AND;
        private ParamValue pValue = ParamValue.VALUE;
        private ParamDo pDo = ParamDo.EQ;

        public Param(String targetColumn, String value) {
            this.targetColumn = targetColumn;
            this.value = value;
        }

        public Param(String targetColumn, String value, ParamLogical logical,ParamValue pValue,ParamDo pDo) {
            this.targetColumn = targetColumn;
            this.value = value;
            this.logical = logical;
            this.pValue=pValue;
            this.pDo = pDo;
        }

        public String getTargetColumn() {
            return targetColumn;
        }

        public String getValue() {
            return value;
        }

        public ParamLogical getLogical() {
            return logical;
        }

        public ParamValue getpValue() {
            return pValue;
        }

        public ParamDo getpDo() {
            return pDo;
        }
    }

    public enum ParamLogical{
        AND,OR
    }
    public enum ParamValue{
        COLUMN,VALUE
    }
    public enum ParamDo{
        GT,GTQ,LT,LTQ,LIKE,EQ
    }


    private static String abc[] = {"a","b","c","d","e","f","g","h","i","j"};
    private static String createAlia(int index){
        String i = String.valueOf(index);
        char[] arr = i.toCharArray();
        StringBuilder sb = new StringBuilder();
        for(char c:arr){
            sb.append(abc[c]);
        }
        return sb.toString();
    }
}
