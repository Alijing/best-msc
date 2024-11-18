package com.jing.common.core.base;

import com.jing.common.core.enums.ResultEnum;

import java.io.Serializable;
import java.util.List;

/**
 * @author : jing
 * @projectName : best-msc
 * @packageName : com.jing.common.core.base
 * @date : 2021/4/23 14:46
 * @description : 接口响应基类
 */
public class BasePageResp<T> extends BaseResp<List<T>> implements Serializable {

    private static final long serialVersionUID = -4621147529659906323L;

    /**
     * 总共多少条记录
     */
    private Long total;
    /**
     * 当前页
     */
    private Long currentPage;
    /**
     * 每页条数
     */
    private Long pageSize;

    public BasePageResp() {
        super();
    }

    /**
     * 通用 返回成功
     *
     * @return 响应结果
     * @author jing
     * @date 2022/5/31 10:27
     */
    public static <T> BasePageResp<T> ok(List<T> data, long total) {
        BasePageResp<T> br = new BasePageResp<>();
        br.setSuccess(ResultEnum.SUCCESS.getSuccess());
        br.setCode(ResultEnum.SUCCESS.getCode());
        br.setMessage(ResultEnum.SUCCESS.getMessage());
        br.setData(data);
        br.setTotal(total);
        return br;
    }

    /**
     * 通用 返回成功
     *
     * @return 响应结果
     * @author jing
     * @date 2022/5/31 10:27
     */
    public static <T> BasePageResp<T> ok(List<T> data, long total, long currentPage, long pageSize) {
        return ok(data, total).setCurrentPage(currentPage).setPageSize(pageSize);
    }

    public Long getTotal() {
        return total;
    }

    public BasePageResp<T> setTotal(Long total) {
        this.total = total;
        return this;
    }

    public Long getCurrentPage() {
        return currentPage;
    }

    public BasePageResp<T> setCurrentPage(Long currentPage) {
        this.currentPage = currentPage;
        return this;
    }

    public Long getPageSize() {
        return pageSize;
    }

    public BasePageResp<T> setPageSize(Long pageSize) {
        this.pageSize = pageSize;
        return this;
    }

}
