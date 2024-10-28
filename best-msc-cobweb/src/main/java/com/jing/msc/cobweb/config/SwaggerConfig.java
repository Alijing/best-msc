package com.jing.msc.binbin.config;

import com.jing.msc.cobweb.config.SwaggerProperties;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;

/**
 * @author : jing
 * @projectName : best-msc
 * @packageName : com.jing.common.swagger.config
 * @date : 2021/4/22 15:13
 * @description :
 */
@Configuration
public class SwaggerConfig implements WebMvcConfigurer {

    private static final String BASE_PATH = "/**";
    private static final List<String> DEFAULT_EXCLUDE_PATH = Arrays.asList("/error", "/actuator/**");

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        //registry.addMapping("/**").allowedOriginPatterns("*")
        //        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD")
        //        .allowCredentials(true)
        //        .maxAge(3600);
    }

    @Bean
    public OpenAPI api(SwaggerProperties properties) {
        return new OpenAPI().info(apiInfo(properties));
        //.addSecurityItem(new SecurityRequirement().addList(HttpHeaders.AUTHORIZATION))
        //.components(
        //        new Components()
        //                .addSecuritySchemes(HttpHeaders.AUTHORIZATION, new SecurityScheme().name(HttpHeaders.AUTHORIZATION)
        //                        .type(SecurityScheme.Type.HTTP).scheme("bearer")
        //                )
        //);
    }

    private Info apiInfo(SwaggerProperties properties) {
        return new Info()
                .title(properties.getTitle())
                .description(properties.getDescription())
                .version(properties.getVersion())
                .termsOfService(properties.getTermsOfServiceUrl())
                .contact(properties.getContact())
                .license(properties.getLicense());
    }

}
