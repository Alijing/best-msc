package com.jing.common.core.constant;

/**
 * @author : jing
 * @projectName : best-msc
 * @packageName : com.jing.common.core.constant
 * @date : 2021/4/23 14:47
 * @description :
 */
public class Constants {

    /**
     * 成功标记
     */
    public static final Integer SUCCESS = 200;

    /**
     * 失败标记
     */
    public static final Integer FAIL = 500;


    /**
     * 存储登录成功用户信息的 redis key
     */
    public static final String LOGIN_USER_KEY = "msc:login:";

    /**
     * token 过期时间 秒
     */
    public static final int TOKEN_EXPIRES_TIME = 3000;

}
