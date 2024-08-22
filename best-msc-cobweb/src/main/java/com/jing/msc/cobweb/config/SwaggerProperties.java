package com.jing.msc.cobweb.config;

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
     * 许可证
     */
    private String license;
    /**
     * 许可证 url
     */
    private String licenseUrl;
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

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public String getBasePackage() {
        return basePackage;
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }

    public List<String> getBasePath() {
        return basePath;
    }

    public void setBasePath(List<String> basePath) {
        this.basePath = basePath;
    }

    public List<String> getExcludePath() {
        return excludePath;
    }

    public void setExcludePath(List<String> excludePath) {
        this.excludePath = excludePath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getLicenseUrl() {
        return licenseUrl;
    }

    public void setLicenseUrl(String licenseUrl) {
        this.licenseUrl = licenseUrl;
    }

    public String getTermsOfServiceUrl() {
        return termsOfServiceUrl;
    }

    public void setTermsOfServiceUrl(String termsOfServiceUrl) {
        this.termsOfServiceUrl = termsOfServiceUrl;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Authorization getAuthorization() {
        return authorization;
    }

    public void setAuthorization(Authorization authorization) {
        this.authorization = authorization;
    }

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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAuthRegex() {
            return authRegex;
        }

        public void setAuthRegex(String authRegex) {
            this.authRegex = authRegex;
        }

        public List<AuthorizationScope> getAuthorizationScopes() {
            return authorizationScopes;
        }

        public void setAuthorizationScopes(List<AuthorizationScope> authorizationScopes) {
            this.authorizationScopes = authorizationScopes;
        }

        public List<String> getTokenUrls() {
            return tokenUrls;
        }

        public void setTokenUrls(List<String> tokenUrls) {
            this.tokenUrls = tokenUrls;
        }
    }


    public static class AuthorizationScope {
        /**
         * 作用域名称
         */
        private String scope;
        /**
         * 作用域描述
         */
        private String description;

        public String getScope() {
            return scope;
        }

        public void setScope(String scope) {
            this.scope = scope;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }

}
