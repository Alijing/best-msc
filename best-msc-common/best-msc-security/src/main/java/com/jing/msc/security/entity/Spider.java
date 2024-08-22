package com.jing.msc.security.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.jing.common.core.base.BaseModel;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;

/**
 * @author : jing
 * @projectName : best-msc
 * @packageName : com.jing.msc.cobweb.entity
 * @date : 2021/4/23 14:52
 * @description :
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_spider")
@Tag(name = "系统用户", description = "系统登录用户实体")
public class Spider extends BaseModel {
    private static final long serialVersionUID = 8779419867388416179L;
    /**
     * 登录账号
     */
    @NotEmpty(message = "登录账号不能为空")
    @Schema(description = "登录账号")
    private String account;
    /**
     * 登录密码
     */
    @Schema(description = "登录密码")
    private String password;
    /**
     * 姓名
     */
    @Schema(description = "姓名")
    private String name;
    /**
     * 电话
     */
    @Schema(description = "电话")
    private String phone;

    @Override
    public String toString() {
        return "Spider{" +
                "account='" + account + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
