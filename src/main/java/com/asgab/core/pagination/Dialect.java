package com.asgab.core.pagination;



public abstract class Dialect {

  public static enum Type {
    MYSQL, ORACLE, SQLSERVER
  }

  public abstract String getLimitString(String sql, int skipResults, int maxResults);

}
