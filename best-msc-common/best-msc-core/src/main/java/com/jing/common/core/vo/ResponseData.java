package com.jing.common.core.vo;

import java.util.Map;

/**
 * 返回http请求返回数据
 *
 * @author : jing
 * @since : 2025/3/6 17:40
 */
public class ResponseData<T> {


    /**
     * 响应状态码，如地址错误或者链接不上，此值为-1
     */
    private int status = -1;

    /**
     * 响应头
     */
    private Map<String, String> responseHeaders;

    /**
     * 响应载荷
     */
    private T responseBody;

    public ResponseData() {
        super();
    }

    public int getStatus() {
        return status;
    }

    public Map<String, String> getResponseHeaders() {
        return responseHeaders;
    }

    public T getResponseBody() {
        return responseBody;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setResponseHeaders(Map<String, String> responseHeaders) {
        this.responseHeaders = responseHeaders;
    }

    public void setResponseBody(T responseBody) {
        this.responseBody = responseBody;
    }


}
