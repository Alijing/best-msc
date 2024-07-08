package com.jing.msc.security.service;

import com.jing.common.core.base.BaseResp;
import com.jing.msc.security.entity.Spider;
import com.jing.msc.security.entity.TokenInfo;

/**
 * @author : jing
 * @projectName : best-msc
 * @packageName : com.jing.msc.security.service.impl
 * @date : 2023/7/4 14:06
 * @description :
 */
public interface SecurityUserService {
    /**
     * 通过账号和密码登录
     *
     * @param spider 用户信息
     * @return 登录结果
     * @author jing
     * @date 2023/7/4 14:09
     */
    BaseResp<TokenInfo> login(Spider spider);

    /**
     * 通过 token 注销退出登录
     *
     * @return 结果
     * @author jing
     * @date 2023/7/4 14:09
     */
    BaseResp<Boolean> logout();
}
