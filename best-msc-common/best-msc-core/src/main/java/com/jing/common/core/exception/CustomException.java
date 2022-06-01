package com.jing.common.core.exception;

import com.jing.common.core.enums.ResultEnum;

/**
 * @author : jing
 * @projectName : best-msc
 * @packageName : com.jing.common.core.exception
 * @date : 2022/5/31 16:06
 * @description :
 */
public class CustomException extends RuntimeException {

    private static final long serialVersionUID = 4897152402937089644L;
    private Integer code;

    public CustomException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public CustomException(ResultEnum resultEnum) {
        super(resultEnum.getMessage());
        this.code = resultEnum.getCode();
    }

    @Override
    public String toString() {
        return "CustomException{" + "code=" + code + ", message=" + this.getMessage() + '}';
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
