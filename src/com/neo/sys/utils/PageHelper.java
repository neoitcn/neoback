package com.neo.sys.utils;

import com.neo.sys.entity.JqGridPage;
import com.neo.sys.entity.Page;
import com.neo.sys.mybatis.plugins.AofaParameterHandler;
import com.neo.sys.listener.JqGridConverter;

import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Mybatis - 通用分页拦截器
 * 
 */
@Intercepts({ @Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class, Integer.class }),
		@Signature(type = ResultSetHandler.class, method = "handleResultSets", args = { Statement.class }) })
public class PageHelper implements Interceptor {
	private static final Logger logger = Logger.getLogger(PageHelper.class);

	public static final ThreadLocal<LocalPageBean> localPage = new ThreadLocal<>();

	public static List nullList = new ArrayList<>(); //easyui中不能识别数据为空的情况，所以必须为查询结果为空的时候返回一个空对象

	/**
	 * 开始分页
	 * 
	 * @param pageIndex
	 * @param pageSize
	 */
	public static void startPage(long pageIndex, int pageSize) {
		if (localPage.get() == null) {
			localPage.set(new LocalPageBean());
		}
		JqGridPage<?> jqGridPage = new JqGridPage<>();
		jqGridPage.setPreNum(pageSize);
		jqGridPage.setPage(pageIndex);
		LocalPageBean lpb = localPage.get();
		lpb.setPage(jqGridPage);
	}
	/**
	 * 开始分页
	 *
	 * @param page
	 */
	public static void startPage(Page page) {
		if (localPage.get() == null) {
			localPage.set(new LocalPageBean());
		}
		JqGridPage<?> jqGridPage = new JqGridPage<>();
		jqGridPage.setPreNum(page.getRows());
		jqGridPage.setPage(page.getPage());
		LocalPageBean lpb = localPage.get();
		lpb.setPage(jqGridPage);
	}

	/**
	 * 期望直接跳转到ID为某一条记录下的页面
	 * 
	 * @param pageIndex
	 * @param pageSize
	 */
	public static void startPage(long pageIndex, int pageSize, String id) {
		if (localPage.get() == null) {
			localPage.set(new LocalPageBean());
		}
		JqGridPage<?> jqGridPage = new JqGridPage<>();
		jqGridPage.setPreNum(pageSize);
		jqGridPage.setPage(pageIndex);
		LocalPageBean lpb = localPage.get();
		lpb.setPage(jqGridPage);
		lpb.setJumpId(id);
	}

	/**
	 * 期望直接跳转到ID为某一条记录下的页面
	 *
	 * @param page
	 */
	public static void startPage(Page page, String id) {
		if (localPage.get() == null) {
			localPage.set(new LocalPageBean());
		}
		JqGridPage<?> jqGridPage = new JqGridPage<>();
		jqGridPage.setPreNum(page.getRows());
		jqGridPage.setPage(page.getPage());
		LocalPageBean lpb = localPage.get();
		lpb.setPage(jqGridPage);
		lpb.setJumpId(id);
	}

	/**
	 * 自定义需要转换展示的属性
	 * 
	 * @param jqc
	 * @param fields
	 */
	public static void setPlugParam(JqGridConverter jqc, String... fields) {
		if (localPage.get() == null) {
			localPage.set(new LocalPageBean());
		}
		LocalPageBean lpb = localPage.get();
		lpb.setFields(fields);
		lpb.setJqc(jqc);
	}

	/**
	 * @param sqlg count语句生成方式
	 * @param jqc
	 * @param fields
	 */
	public static void setPlugParam(SQLGenatorStanded sqlg,JqGridConverter jqc,String... fields) {

		if (localPage.get() == null) {
			localPage.set(new LocalPageBean());
		}
		LocalPageBean lpb = localPage.get();
		lpb.setFields(fields);
		lpb.setJqc(jqc);
		lpb.setAppStartCount(sqlg);
	}



	/**
	 * 结束分页并返回结果，该方法必须被调用，否则localPage会一直保存下去，直到下一次startPage
	 * 
	 * @return
	 */
	public static JqGridPage<?> endPage() {
		JqGridPage<?> page = localPage.get().getPage();
		if(page!=null && (page.getRows() == null || page.getRows().size()==0)){
			page.setRows(nullList);
		}
		localPage.set(null);
		localPage.remove();
		return page;
	}

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		
		if (localPage.get() == null) {
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
			// 分页信息if (localPage.get() != null) {
			JqGridPage page = localPage.get().getPage();
			BoundSql boundSql = (BoundSql) metaStatementHandler.getValue("delegate.boundSql");
			// 分页参数作为参数对象parameterObject的一个属性
			String sql = boundSql.getSql();
			// 重写sql
			// 重写分页sql
			Connection connection = (Connection) invocation.getArgs()[0];
			setPageParameter(sql, connection, mappedStatement, boundSql, page);
			String pageSql = buildPageSql(sql, page);
			metaStatementHandler.setValue("delegate.boundSql.sql", pageSql);
			// 重设分页参数里的总页数等
			// 将执行权交给下一个拦截器
			return invocation.proceed();
		} else if (invocation.getTarget() instanceof ResultSetHandler) {
			Object result = invocation.proceed();
			JqGridPage page = localPage.get().getPage();
			page.setRows(JsonDataUtils.createJqGridResult((List<?>) result, true, localPage.get().getJqc(),
					localPage.get().getFields()));
			return result;
		}
		return null;
	}

	/**
	 * 只拦截这两种类型的 <br>
	 * StatementHandler <br>
	 * ResultSetHandler
	 * 
	 * @param target
	 * @return
	 */
	@Override
	public Object plugin(Object target) {
		if (target instanceof StatementHandler || target instanceof ResultSetHandler) {
			return Plugin.wrap(target, this);
		} else {
			return target;
		}
	}

	@Override
	public void setProperties(Properties properties) {

	}

	/**
	 * 修改原SQL为分页SQL
	 * 
	 * @param sql
	 * @param page
	 * @return
	 */
	private String buildPageSql(String sql, JqGridPage page) {
		StringBuilder pageSql = new StringBuilder();

		pageSql.append(sql).append(" limit " + ((page.getPage()-1)* +page.getPreNum()) +"," + page.getPreNum());
		return pageSql.toString();
	}

	/**
	 * 获取总记录数
	 * 
	 * @param sql
	 * @param connection
	 * @param mappedStatement
	 * @param boundSql
	 * @param page
	 */
	private void setPageParameter(String sql, Connection connection, MappedStatement mappedStatement, BoundSql boundSql,
			JqGridPage page) {
		// 记录总记录数

		LocalPageBean lpb = localPage.get();
		String countSql = splitSqlToCount(sql,lpb.getAppStartCount());

		if (countSql == null) {
			logger.error("自动分页失败，问题位置：获取总记录条数失败，原因：未能成功生成count语句");
			return;
		}

		PreparedStatement countStmt = null;
		ResultSet rs = null;
		try {
			countStmt = connection.prepareStatement(countSql);
			BoundSql countBS = new BoundSql(mappedStatement.getConfiguration(), countSql,
					boundSql.getParameterMappings(), boundSql.getParameterObject());
			setParameters(countStmt, mappedStatement, countBS, boundSql.getParameterObject());
			rs = countStmt.executeQuery();
			long totalCount = 0;
			if (rs.next()) {
				totalCount = rs.getInt(1);
			}
			page.setRecords(totalCount);
			if (totalCount == 0) {
				page.setTotal(1L);
			} else {
				page.setTotal(Long.valueOf((totalCount - 1) / page.getPreNum() + 1));
			}
			if (localPage.get().getJumpId() != null) {
				Object jumpId = localPage.get().getJumpId();
				String searchRowNumSql = "select rownum from ("+
						"select @rownum := @rownum + 1 as rownum,__random_temp_row_index.id from("+
						"select @rownum := 0,id from ("+sql+") __random_temp_row_index_inner )  __random_temp_row_index"+
				") __tt_out_table_number where __tt_out_table_number.id = ";
					if (jumpId instanceof String) {
						searchRowNumSql += "'" + jumpId + "'";
					} else {
						searchRowNumSql += jumpId;
				}
				countStmt = connection.prepareStatement(searchRowNumSql);
				countBS = new BoundSql(mappedStatement.getConfiguration(), searchRowNumSql,
						boundSql.getParameterMappings(), boundSql.getParameterObject());
				setParameters(countStmt, mappedStatement, countBS, boundSql.getParameterObject());
				ResultSet rs2 = countStmt.executeQuery();
				long pageNum = 0;
				if (rs2.next()) {
					long rownum = rs2.getInt("rownum");
					pageNum = (rownum - 1) / page.getPreNum() + 1;					
					page.setPage(pageNum);
					lpb.getPage().setPage(pageNum);
				}
				rs2.close();
			}
		} catch (SQLException e) {
			logger.error("Ignore this exception", e);
		} finally {
			try {
				rs.close();
			} catch (SQLException e) {
				logger.error("Ignore this exception", e);
			}
			try {
				countStmt.close();
			} catch (SQLException e) {
				logger.error("Ignore this exception", e);
			}
		}
	}

	/**
	 * 代入参数值
	 * 
	 * @param ps
	 * @param mappedStatement
	 * @param boundSql
	 * @param parameterObject
	 * @throws SQLException
	 */
	private void setParameters(PreparedStatement ps, MappedStatement mappedStatement, BoundSql boundSql,
			Object parameterObject) throws SQLException {
		ParameterHandler parameterHandler = new AofaParameterHandler(mappedStatement, parameterObject, boundSql);
		parameterHandler.setParameters(ps);
	}

	public enum SQLGenatorStanded{
		/**替换查询条件*/REPLACE,/**前追加并消除查询条件*/APPSTART
	}

	static class LocalPageBean {
		/**
		 * 分页信息，由前台传入
		 */
		private JqGridPage<?> page;
		/**
		 * Jqgrid生成器的转换器
		 */
		private JqGridConverter jqc;
		/**
		 * 需要映射的bean中的属性，由Jqgrid生成工具生成。
		 */
		private String fields[];
		/**
		 * 要跳转的位置记录条目的ID
		 */
		private Object jumpId;
		/**
		 * 是否是前置追加sql语句
		 * 如果设置为true，则select count(0) from (正式的SQL) abcd，
		 * 默认为空,由系统依据SQL自动识别做判断。
		 * 此参数依照性能和使用情况而定。比如遇到group by语句可以将参数设置为true，以避免一些count错误问题。
		 */
		private SQLGenatorStanded appStartCount = null;

		public JqGridPage<?> getPage() {
			return page;
		}

		public void setPage(JqGridPage<?> page) {
			this.page = page;
		}

		public JqGridConverter getJqc() {
			return jqc;
		}

		public void setJqc(JqGridConverter jqc) {
			this.jqc = jqc;
		}

		public String[] getFields() {
			return fields;
		}

		public void setFields(String[] fields) {
			this.fields = fields;
		}

		public Object getJumpId() {
			return jumpId;
		}

		public void setJumpId(Object jumpId) {
			this.jumpId = jumpId;
		}

		public SQLGenatorStanded getAppStartCount() {
			return appStartCount;
		}

		public void setAppStartCount(SQLGenatorStanded appStartCount) {
			this.appStartCount = appStartCount;
		}
	}

	// ================华丽的分割线=========================//
	//////// ====================================/////////
	// 这里的代码用作PageHelper专门需要的工具封装
	
	/**
	 * 将分页查询或非分页查询的语句转换为查询记录条数的语句。
	 * @param str
	 * @return
	 */
	private String splitSqlToCount(String str,SQLGenatorStanded generatorStanded) {
		//str+=" ";
		return "select count(0) from ( "+str+") random_table_name_uuid_aubcqumn";
		/*int index = 0;

		int temp = 0;

		boolean b = false;
		
		str = str.replaceAll("\r\n", " ").replaceAll("\n"," ").replaceAll("\t"," ");

		do{

			index = str.toLowerCase().indexOf("from", index);

			temp = index;

			while (temp >= 0) {

				char c = str.charAt(--temp);
				if (c != ' ') {
					if (c == ',') {
						b = true;
					} else if (c != '.') {
						if (index > temp + 1) {
							b = true;
						}
					}
					break;
				}

			}

			if (b) {
				temp = index + 4;
				b = false;
				while (temp < str.length()) {

					char c = str.charAt(temp++);
					if (c == ' ') {
						b = true;
						break;
					}
				}
			}

			if (b) {
				break;
			}

			index++;

		}while(index > 0);
		if(b){
			Pattern p = Pattern.compile(" group +by +");
			Matcher m = p.matcher(str);
			if(generatorStanded == null){
				//检查语句是否具有group by语句。如果有，则SQL另行拼凑发送。
				if(m.find()){
					if(str.substring(m.start()).contains(")")){

						return "select count(0) from " + str.substring(index + 5);
					}
					return "select count(0) from ( select count(0) from " + str.substring(index + 5) +") random_table_name_uuid_aubcqumn";
				}else{
					System.out.println(str.substring(index+5));
					return "select count(0) from " + str.substring(index + 5);
				}
			}else{
				if(generatorStanded.equals(SQLGenatorStanded.APPSTART)){
					return "select count(0) from ( select count(0) from " + str.substring(index + 5) +") random_table_name_uuid_aubcqumn";
				}else{
					return "select count(0) from " + str.substring(index + 5);
				}
			}

		}*/

		//return null;
	}

}
