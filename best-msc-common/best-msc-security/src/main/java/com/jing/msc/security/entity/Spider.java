package com.jing.msc.security.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.jing.common.core.base.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotEmpty;

/**
 * @author : jing
 * @projectName : best-msc
 * @packageName : com.jing.msc.cobweb.entity
 * @date : 2021/4/23 14:52
 * @description :
 */
@TableName("sys_spider")
@ApiModel(value = "系统登录用户实体", description = "系统登录用户实体")
public class Spider extends BaseModel<Spider> {
    private static final long serialVersionUID = 8779419867388416179L;
    /**
     * 登录账号
     */
    @NotEmpty(message = "登录账号不能为空")
    @ApiModelProperty(value = "登录账号")
    private String account;
    /**
     * 登录密码
     */
    @ApiModelProperty(value = "登录密码")
    private String password;
    /**
     * 姓名
     */
    @ApiModelProperty(value = "姓名")
    private String name;
    /**
     * 电话
     */
    @ApiModelProperty(value = "电话")
    private String phone;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

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
