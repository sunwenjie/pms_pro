package com.asgab.core.pagination;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;

/**
 * 分页对象
 * 
 * @author Jack
 *
 * @param <T>
 */
public class Page<T> implements Serializable {

  private static final long serialVersionUID = 5337828398423415077L;

  private int pageNumber = 1;// 当前页数
  private int pageSize = 20;// 每页记录数
  private int total;// 总记录数

  private Map<String, Object> searchMap;// 查询条件

  private String sort;// 排序条件

  private List<T> content;// 每页的内容

  public RowBounds getRowBounds() {
    return new RowBounds((pageNumber - 1) * pageSize, pageSize);
  }

  public Page() {

  }

  public Page(int pageNumber, int pageSize, Map<String, Object> searchMap) {
    this.pageNumber = pageNumber;
    this.pageSize = pageSize;
    this.searchMap = searchMap;
  }

  public Page(int pageNumber, int pageSize, String sort, Map<String, Object> searchMap) {
    this.pageNumber = pageNumber;
    this.pageSize = pageSize;
    this.sort = sort;
    this.searchMap = searchMap;
  }

  // 当前页
  public int getPageNumber() {
    return pageNumber;
  }

  // 首页
  public int getFirstPage() {
    return 1;
  }

  // 上一页
  public int getPreviousPage() {
    return pageNumber > 0 ? pageNumber - 1 : 0;
  }

  // 下一页
  public int getNextPage() {
    return pageNumber < getPageCount() - 1 ? pageNumber + 1 : getPageCount() - 1;
  }

  // 尾页
  public int getLastPage() {
    return getPageCount() - 1;
  }

  // 总页数
  public int getPageCount() {
    return pageSize == 0 ? 1 : (int) Math.ceil((double) total / (double) pageSize);
  }

  // 是否有上一页
  public boolean hasPrevious() {
    return pageNumber - 1 > 0;
  }

  // 是否第一页
  public boolean isFirst() {
    return !hasPrevious();
  }

  // 是否有下一页
  public boolean hasNext() {
    return pageNumber < getPageCount();
  }

  // 是否尾页
  public boolean isLast() {
    return !hasNext();
  }

  public Map<String, Object> getSearchMap() {
    return searchMap;
  }

  public void setSearchMap(Map<String, Object> searchMap) {
    this.searchMap = searchMap;
  }

  public int getPageSize() {
    return pageSize;
  }

  public void setPageSize(int pageSize) {
    this.pageSize = pageSize;
  }

  public int getTotal() {
    return total;
  }

  public void setTotal(int total) {
    this.total = total;
  }

  public String getSort() {
    return sort;
  }

  public void setSort(String sort) {
    this.sort = sort;
  }

  public List<T> getContent() {
    return content;
  }

  public void setContent(List<T> content) {
    this.content = content;
  }

  public void setPageNumber(int pageNumber) {
    this.pageNumber = pageNumber;
  }

}
