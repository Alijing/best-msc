package com.jing.common.core.base;

import com.jing.common.core.enums.ResultEnum;

import java.io.Serializable;

/**
 * @author : jing
 * @projectName : best-msc
 * @packageName : com.jing.common.core.base
 * @date : 2021/4/23 14:46
 * @description : 接口响应基类
 */
public class BaseResp<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private Boolean success;

    private Integer code;

    private String message;

    private T data;

    BaseResp() {
    }

    /**
     * 通用 返回成功
     *
     * @return 响应结果
     * @author jing
     * @date 2022/5/31 10:27
     */
    public static <T> BaseResp<T> ok() {
        BaseResp<T> br = new BaseResp<>();
        br.setSuccess(ResultEnum.SUCCESS.getSuccess());
        br.setCode(ResultEnum.SUCCESS.getCode());
        br.setMessage(ResultEnum.SUCCESS.getMessage());
        return br;
    }

    /**
     * 通用 返回成功
     *
     * @return 响应结果
     * @author jing
     * @date 2022/5/31 10:27
     */
    public static <T> BaseResp<T> ok(T data) {
        BaseResp<T> br = new BaseResp<>();
        br.setSuccess(ResultEnum.SUCCESS.getSuccess());
        br.setCode(ResultEnum.SUCCESS.getCode());
        br.setMessage(ResultEnum.SUCCESS.getMessage());
        br.setData(data);
        return br;
    }

    /**
     * 通用 返回成功
     *
     * @return 响应结果
     * @author jing
     * @date 2022/5/31 10:27
     */
    public static <T> BaseResp<T> ok(T data, String message) {
        BaseResp<T> br = new BaseResp<>();
        br.setSuccess(ResultEnum.SUCCESS.getSuccess());
        br.setCode(ResultEnum.SUCCESS.getCode());
        br.setMessage(message);
        br.setData(data);
        return br;
    }

    /**
     * 通用返回失败，未知错误
     *
     * @return 失败，未知错误
     * @author jing
     * @date 2022/5/31 10:30
     */
    public static <T> BaseResp<T> error() {
        BaseResp<T> br = new BaseResp<>();
        br.setSuccess(ResultEnum.UNKNOWN_ERROR.getSuccess());
        br.setCode(ResultEnum.UNKNOWN_ERROR.getCode());
        br.setMessage(ResultEnum.UNKNOWN_ERROR.getMessage());
        return br;
    }

    /**
     * 通用返回失败，未知错误
     *
     * @return 失败，未知错误
     * @author jing
     * @date 2022/5/31 10:30
     */
    public static <T> BaseResp<T> error(Integer code) {
        BaseResp<T> br = new BaseResp<>();
        br.setSuccess(ResultEnum.UNKNOWN_ERROR.getSuccess());
        br.setCode(code);
        br.setMessage(ResultEnum.UNKNOWN_ERROR.getMessage());
        return br;
    }

    /**
     * 通用返回失败，未知错误
     *
     * @return 失败，未知错误
     * @author jing
     * @date 2022/5/31 10:30
     */
    public static <T> BaseResp<T> error(String message) {
        BaseResp<T> br = new BaseResp<>();
        br.setSuccess(ResultEnum.UNKNOWN_ERROR.getSuccess());
        br.setCode(ResultEnum.UNKNOWN_ERROR.getCode());
        br.setMessage(message);
        return br;
    }

    /**
     * 通用返回失败，未知错误
     *
     * @return 失败，未知错误
     * @author jing
     * @date 2022/5/31 10:30
     */
    public static <T> BaseResp<T> error(Integer code, String message) {
        BaseResp<T> br = new BaseResp<>();
        br.setSuccess(ResultEnum.UNKNOWN_ERROR.getSuccess());
        br.setCode(code);
        br.setMessage(message);
        return br;
    }

    /**
     * 设置结果，形参为结果枚举
     *
     * @param result 结果枚举
     * @return 响应
     * @author jing
     * @date 2022/5/31 10:32
     */
    public static <T> BaseResp<T> setResult(ResultEnum result) {
        BaseResp<T> br = new BaseResp<>();
        br.setSuccess(result.getSuccess());
        br.setCode(result.getCode());
        br.setMessage(result.getMessage());
        return br;
    }

    /**
     * 自定义返回结果
     *
     * @param success 结果
     * @return 类本身
     * @author jing
     * @date 2022/5/31 10:44
     */
    public BaseResp<T> success(Boolean success) {
        this.setSuccess(success);
        return this;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public BaseResp<T> setData(T data) {
        this.data = data;
        return this;
    }

    @Override
    public String toString() {
        return "BaseResp{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
