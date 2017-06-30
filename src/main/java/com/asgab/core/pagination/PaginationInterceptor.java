package com.asgab.core.pagination;


import java.sql.Connection;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.RowBounds;

@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class})})
public class PaginationInterceptor implements Interceptor {

  private static final ObjectFactory DEFAULT_OBJECT_FACTORY = new DefaultObjectFactory();
  private static final ObjectWrapperFactory DEFAULT_OBJECT_WRAPPER_FACTORY = new DefaultObjectWrapperFactory();
  private final static Log log = LogFactory.getLog(PaginationInterceptor.class);

  @Override
  public Object intercept(Invocation invocation) throws Throwable {
    StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
    MetaObject metaStatementHandler = MetaObject.forObject(statementHandler, DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY);

    // 分离代理对象链(由于目标类可能被多个拦截器拦截，从而形成多次代理，通过下面的两次循环可以分离出最原始的的目标类)
    while (metaStatementHandler.hasGetter("h")) {
      Object object = metaStatementHandler.getValue("h");
      metaStatementHandler = MetaObject.forObject(object, DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY);
    }
    // 分离最后一个代理对象的目标类
    while (metaStatementHandler.hasGetter("target")) {
      Object object = metaStatementHandler.getValue("target");
      metaStatementHandler = MetaObject.forObject(object, DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY);
    }
    RowBounds rowBounds = (RowBounds) metaStatementHandler.getValue("delegate.rowBounds");
    if (rowBounds == null || rowBounds == RowBounds.DEFAULT) {
      return invocation.proceed();
    }
    Configuration configuration = (Configuration) metaStatementHandler.getValue("delegate.configuration");
    Dialect.Type databaseType = null;
    try {
      databaseType = Dialect.Type.valueOf(configuration.getVariables().getProperty("dialect").toUpperCase());
    } catch (Exception e) {
    }
    if (databaseType == null) {
      throw new RuntimeException(
          "the value of the dialect property in configuration.xml is not defined : " + configuration.getVariables().getProperty("dialect"));
    }
    Dialect dialect = null;
    switch (databaseType) {
      case ORACLE:
        break;
      case MYSQL:
        dialect = new MySql5Dialect();
        break;
      case SQLSERVER:
        break;
      default:
        break;

    }

    String originalSql = (String) metaStatementHandler.getValue("delegate.boundSql.sql");
    metaStatementHandler.setValue("delegate.boundSql.sql", dialect.getLimitString(originalSql, rowBounds.getOffset(), rowBounds.getLimit()));
    metaStatementHandler.setValue("delegate.rowBounds.offset", RowBounds.NO_ROW_OFFSET);
    metaStatementHandler.setValue("delegate.rowBounds.limit", RowBounds.NO_ROW_LIMIT);

    return invocation.proceed();
  }

  @Override
  public Object plugin(Object target) {
    return Plugin.wrap(target, this);
  }

  @Override
  public void setProperties(Properties properties) {}

}
