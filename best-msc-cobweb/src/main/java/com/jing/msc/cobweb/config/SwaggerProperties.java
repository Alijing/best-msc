package com.jing.msc.cobweb.config;

import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : jing
 * @projectName : best-msc
 * @packageName : com.jing.common.swagger.config
 * @date : 2021/4/22 16:03
 * @description :
 */
@Data
@Component
@ConfigurationProperties("knife4j")
public class SwaggerProperties {

    /**
     * 是否开启 swagger
     */
    private Boolean enable;
    /**
     * swagger会解析的包路径
     */
    private String basePackage;
    /**
     * swagger会解析的 url 规则
     */
    private List<String> basePath = new ArrayList<>();
    /**
     * 在 basePath 的基础上需要排除的 url 规则
     */
    private List<String> excludePath = new ArrayList<>();
    /**
     * 标题
     */
    private String title;
    /**
     * 描述
     */
    private String description;
    /**
     * 版本
     */
    private String version = "1.0";
    /**
     * 作者
     */
    private Contact contact;
    /**
     * 许可证
     */
    private License license;
    /**
     * 服务条款 url
     */
    private String termsOfServiceUrl;
    /**
     * host
     */
    private String host;
    /**
     * 全局统一鉴权配置
     */
    private Authorization authorization = new Authorization();

    @Data
    public static class Authorization {

        /**
         * 鉴权策略ID，需要和 SecurityReferences ID保持一致
         */
        private String name;
        /**
         * 鉴需要开启鉴权 URL 的正则
         */
        private String authRegex = "^.*$";
        /**
         * 鉴权作用域列表
         */
        private List<AuthorizationScope> authorizationScopes = new ArrayList<>();

        private List<String> tokenUrls = new ArrayList<>();

    }

    @Data
    public static class AuthorizationScope {
        /**
         * 作用域名称
         */
        private String scope;
        /**
         * 作用域描述
         */
        private String description;

    }

}
