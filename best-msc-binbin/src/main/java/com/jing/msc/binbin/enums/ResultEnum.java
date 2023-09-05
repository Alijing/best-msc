package com.jing.msc.binbin.enums;

/**
 * @author : jing
 * @projectName : best-msc
 * @packageName : com.jing.common.core.constant
 * @date : 2022/5/31 10:14
 * @description :
 */
public enum ResultEnum {
    /**
     * 成功
     */
    SUCCESS(true, 20000, "成功"),
    UNKNOWN_ERROR(false, 20001, "未知错误"),
    PARAM_ERROR(false, 20002, "参数错误"),
    NULL_POINT(false, 20003, "空指针异常"),
    HTTP_CLIENT_ERROR(false, 20004, "空指针异常"),
    SQL_EXCEPTION(false, 20005, "sql执行异常"),
    DUPLICATE_KEY(false, 20006, "sql执行异常"),
    METHOD_NOT_ALLOWED(false, 20007, "请求方式不支持异常"),
    IOException(false, 20008, "IO 异常"),
    User_Not_Found(false, 20009, "用户名或密码错误，请检查是否正确"),
    Login_Failure(false, 20010, "登录失败"),
    Token_Illegal(false, 20011, "token 非法"),
    UN_Login(false, 20012, "用户未登录"),
    ACCESS_DENIED(false, 20013, "用户未授权，不允许访问"),

    ;

    /**
     * 相应是否成功
     */
    private final Boolean success;
    /**
     * 响应状态码
     */
    private final Integer code;
    /**
     * 响应信息
     */
    private final String message;

    ResultEnum(Boolean success, Integer code, String message) {
        this.success = success;
        this.code = code;
        this.message = message;
    }

    public Boolean getSuccess() {
        return success;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
