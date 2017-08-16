//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.neo.sys.mybatis.plugins;

import com.neo.sys.utils.InvokeUtil;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeException;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@Deprecated
public class AofaParameterHandler implements ParameterHandler {
    private final TypeHandlerRegistry typeHandlerRegistry;
    private final MappedStatement mappedStatement;
    private final Object parameterObject;
    private BoundSql boundSql;
    private Configuration configuration;

    public AofaParameterHandler(MappedStatement mappedStatement, Object parameterObject, BoundSql boundSql) {
        this.mappedStatement = mappedStatement;
        this.configuration = mappedStatement.getConfiguration();
        this.typeHandlerRegistry = mappedStatement.getConfiguration().getTypeHandlerRegistry();
        this.parameterObject = parameterObject;
        this.boundSql = boundSql;
    }
//_frch_xx_index
    public Object getParameterObject() {
        return this.parameterObject;
    }

    public void setParameters(PreparedStatement ps) {
        ErrorContext.instance().activity("setting parameters").object(this.mappedStatement.getParameterMap().getId());
        List parameterMappings = this.boundSql.getParameterMappings();
        //MyBatis在参数封装时，遇到list，list的key不会展现在ParameterObjects中，但item的key会以_frch_xx_index存入或直接item存入。
        //所以在处理时，list的key后缀加s，item去掉s，方便分页处理。
        //过于复杂的方式，需要使用人家的插件。这里没有使用。
        int strIndex = 0;
        int index = 0; //mybatis会将list中的参数添加index的后缀
        List list;
        String ends = null; //解析属性中的值。
        if(parameterMappings != null) {
            for(int i = 0; i < parameterMappings.size(); ++i) {
                ParameterMapping parameterMapping = (ParameterMapping)parameterMappings.get(i);
                if(parameterMapping.getMode() != ParameterMode.OUT) {
                    String propertyName = parameterMapping.getProperty();
                    Object value;
                    if(this.boundSql.hasAdditionalParameter(propertyName)) {
                        value = this.boundSql.getAdditionalParameter(propertyName);
                    } else if(this.parameterObject == null) {
                        value = null;
                    } else if(this.typeHandlerRegistry.hasTypeHandler(this.parameterObject.getClass())) {
                        value = this.parameterObject;
                    } else {
                        MetaObject typeHandler = this.configuration.newMetaObject(this.parameterObject);
                        value = typeHandler.getValue(propertyName);
                        if(value == null){
                            //如果属性以s结尾，则撤掉s，查找。这是list的设定规则。
                            if((strIndex = propertyName.indexOf("."))!=-1){
                                ends = propertyName.substring(strIndex+1);
                                propertyName = propertyName.substring(0,strIndex);
                            }
                            //将真实的key分离出来
                            if(propertyName.matches("__frch_([a-zA-Z0-9]+)_\\d+")){
                                strIndex = propertyName.lastIndexOf("_");
                                index = Integer.parseInt(propertyName.substring(strIndex+1));
                                propertyName = propertyName.substring(7,strIndex);
                                value = typeHandler.getValue(propertyName);
                                if(value == null){
                                    value = typeHandler.getValue(propertyName+"s");
                                }
                                if(value != null && value instanceof List){
                                    list = (List) value;
                                    if(list.size() > index){
                                        value = list.get(index);
                                        //拆解value，将value中的值获取出来。
                                        if(ends != null){
                                            String fields[] = ends.split("[.]");
                                            for(String fieldName : fields){
                                                try {
                                                    value = InvokeUtil.invokeMethod(value,"get"+fieldName.substring(0,1).toUpperCase()+fieldName.substring(1),null);
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        ends = null;
                    }

                    TypeHandler var12 = parameterMapping.getTypeHandler();
                    JdbcType jdbcType = parameterMapping.getJdbcType();
                    if(value == null && jdbcType == null) {
                        jdbcType = this.configuration.getJdbcTypeForNull();
                    }

                    try {
                        var12.setParameter(ps, i + 1, value, jdbcType);
                    } catch (TypeException var10) {
                        throw new TypeException("Could not set parameters for mapping: " + parameterMapping + ". Cause: " + var10, var10);
                    } catch (SQLException var11) {
                        throw new TypeException("Could not set parameters for mapping: " + parameterMapping + ". Cause: " + var11, var11);
                    }
                }
            }
        }

    }
}
