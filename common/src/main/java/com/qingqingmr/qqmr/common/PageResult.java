package com.qingqingmr.qqmr.common;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 分页数据结果集包装
 * </p>
 *
 * @author ythu
 * @datetime 2018年1月18日 下午12:03:51
 */
public class PageResult<T> implements Serializable {
    private static final long serialVersionUID = 3591583218136297772L;

    public static final Integer DEFAULT_CURRPAGE = 1;
    public static final Integer DEFAULT_PAGESIZE = 10;

    private int currPage = DEFAULT_CURRPAGE; // 当前页面
    private long totalCount; // 总共记录
    private int totalPageCount; // 总共页数
    private int pageSize = DEFAULT_PAGESIZE; // 分页大小
    private List<T> page; // 当前页内容
    private Map<String, Object> conditions; // 当前页条件

    public int getCurrPage() {
        return currPage;
    }

    public void setCurrPage(int currPage) {
        this.currPage = currPage;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public List<T> getPage() {
        return page;
    }

    public void setPage(List<T> page) {
        this.page = page;
    }

    public Map<String, Object> getConditions() {
        return conditions;
    }

    public void setConditions(Map<String, Object> conditions) {
        this.conditions = conditions;
    }

    public void setTotalPageCount(int totalPageCount) {
        this.totalPageCount = totalPageCount;
    }

    public int getTotalPageCount() {
        if (this.totalPageCount > 0) {
            return this.totalPageCount;
        }
        return (int) ((this.totalCount - 1) / this.pageSize + 1);
    }

    /**
     * @param currPage
     * @param totalCount
     * @param pageSize
     * @param page
     * @param conditions
     */
    public PageResult(int currPage, int totalCount, int pageSize, List<T> page, Map<String, Object> conditions) {
        this.currPage = currPage;
        this.totalCount = totalCount;
        this.pageSize = pageSize;
        this.page = page;
        this.conditions = conditions;
    }

    /**
     * @param currPage
     * @param totalCount
     * @param page
     * @param conditions
     */
    public PageResult(int currPage, int totalCount, List<T> page, Map<String, Object> conditions) {
        this.currPage = currPage;
        this.totalCount = totalCount;
        this.page = page;
        this.conditions = conditions;
    }

    /**
     * @param currPage
     * @param totalCount
     * @param page
     */
    public PageResult(int currPage, int totalCount, List<T> page) {
        this.currPage = currPage;
        this.totalCount = totalCount;
        this.page = page;
    }

    /**
     * @param currPage
     * @param pageSize
     */
    public PageResult(int currPage, int pageSize) {
        super();
        this.currPage = currPage;
        this.pageSize = pageSize;
    }

    /**
     *
     */
    public PageResult() {
    }
}
