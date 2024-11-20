package com.jing.common.core.base;

/**
 * @author : jing
 * @projectName : best-msc
 * @packageName : com.jing.common.core.base
 * @date : 2022/5/31 16:53
 * @description : 分页参数相关实体类
 */
public class BasePage {
    /**
     * 总共多少条记录
     */
    private Integer total;
    /**
     * 当前页
     */
    private Integer pageIndex;
    /**
     * 每页条数
     */
    private Integer pageSize;

    public BasePage() {
    }

    public BasePage(Integer total, Integer pageIndex, Integer pageSize) {
        this.total = total;
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

}
