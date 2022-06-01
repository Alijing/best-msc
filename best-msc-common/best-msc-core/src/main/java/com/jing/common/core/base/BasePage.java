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
    private Integer currentPage;
    /**
     * 每页条数
     */
    private Integer pageSize;

    public BasePage() {
    }

    public BasePage(Integer total, Integer currentPage, Integer pageSize) {
        this.total = total;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

}
