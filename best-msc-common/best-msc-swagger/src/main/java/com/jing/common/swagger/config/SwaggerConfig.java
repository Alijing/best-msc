package com.jing.common.swagger.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.OperationContext;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

/**
 * @author : jing
 * @projectName : best-msc
 * @packageName : com.jing.common.swagger.config
 * @date : 2021/4/22 15:13
 * @description :
 */
@Configuration
@EnableAutoConfiguration
@ConditionalOnProperty(name = "knife.enable", matchIfMissing = true)
public class SwaggerConfig implements WebMvcConfigurer {

    private static final String BASE_PATH = "/**";
    private static final List<String> DEFAULT_EXCLUDE_PATH = Arrays.asList("/error", "/actuator/**");

    @Bean
    @ConditionalOnMissingBean
    public SwaggerProperties swaggerProperties() {
        return new SwaggerProperties();
    }

    @Bean
    public Docket api(SwaggerProperties swaggerProperties) {
        // basePath 处理
        if (swaggerProperties.getBasePath().isEmpty()) {
            swaggerProperties.getBasePath().add(BASE_PATH);
        }
        // noinspection unchecked
        List<Predicate<String>> basePath = new ArrayList<>();
        swaggerProperties.getBasePath().forEach(it -> basePath.add(PathSelectors.ant(it)));

        if (swaggerProperties.getExcludePath().isEmpty()) {
            swaggerProperties.getExcludePath().addAll(DEFAULT_EXCLUDE_PATH);
        }

        List<Predicate<String>> excludePath = new ArrayList<>();
        swaggerProperties.getExcludePath().forEach(it -> excludePath.add(PathSelectors.ant(it)));

        return new Docket(DocumentationType.OAS_30)
                .host(swaggerProperties.getHost())
                .enable(swaggerProperties.getEnable())
                .apiInfo(apiInfo(swaggerProperties))
                .select()
                // 这里配置要扫描的包，接口在哪个包就配置哪个
                .apis(RequestHandlerSelectors.basePackage(swaggerProperties.getBasePackage()))
                .paths(PathSelectors.any())
                .build()
                .securitySchemes(securitySchemes())
                .securityContexts(securityContexts())
                .pathMapping("/");
    }

    /**
     * 安全上下文
     *
     * @return 安全上下文
     * @author jing
     * @date 2021/4/22 17:35
     */
    private List<SecurityContext> securityContexts() {
        List<SecurityContext> contexts = new ArrayList<>();
        contexts.add(SecurityContext.builder()
                .securityReferences(defaultAuth())
                .build());
        return contexts;
    }

    /**
     * 默认的全局鉴权策略
     *
     * @return 全局鉴权策略
     * @author jing
     * @date 2021/4/22 17:39
     */
    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        List<SecurityReference> securityReferences = new ArrayList<>();
        securityReferences.add(new SecurityReference("Authorization", authorizationScopes));
        return securityReferences;
    }

    private List<SecurityScheme> securitySchemes() {
        List<SecurityScheme> apiKeys = new ArrayList<>();
        apiKeys.add(new ApiKey("Authorization", "Authorization", "header"));
        return apiKeys;
    }

    private ApiInfo apiInfo(SwaggerProperties swaggerProperties) {
        return new ApiInfoBuilder()
                .title(swaggerProperties.getTitle())
                .description(swaggerProperties.getDescription())
                .license(swaggerProperties.getLicense())
                .licenseUrl(swaggerProperties.getLicenseUrl())
                .termsOfServiceUrl(swaggerProperties().getTermsOfServiceUrl())
                .contact(swaggerProperties.getContact())
                .version(swaggerProperties.getVersion())
                .build();
    }

}
