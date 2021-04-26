package com.jing.common.core.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.type.JdbcType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author : jing
 * @projectName : best-msc
 * @packageName : com.jing.common.core.config
 * @date : 2021/4/21 11:04
 * @description :
 */
@Configuration
public class MybatisPlusConfig {

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 设置 分页插件和数据库
        PaginationInnerInterceptor innerInterceptor = new PaginationInnerInterceptor(DbType.MYSQL);
        // 设置请求的页面大于最大页后，继续操作，true 调回到首页，false 继续请求，默认false
        innerInterceptor.setOverflow(false);
        // 单页分页条数限制，默认 500 条，-1 则不受限制
        innerInterceptor.setMaxLimit(500L);
        interceptor.addInnerInterceptor(innerInterceptor);

        // 乐观锁配置
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());

        return interceptor;
    }

    @Bean
    public ConfigurationCustomizer configurationCustomizer() {
        return configuration -> {
            // 开启二级缓存
            configuration.setCacheEnabled(true);
            //
            configuration.setUseGeneratedShortKey(true);
            // 是否开启自动驼峰命名规则（camel case）映射，即从经典数据库列名 A_COLUMN（下划线命名） 到经典 Java 属性名 aColumn（驼峰命名） 的类似映射。
            configuration.setMapUnderscoreToCamelCase(true);
            // SIMPLE是默认执行器，根据对应的sql直接执行，不会做一些额外的操作。
            // REUSE是可重用执行器，重用对象是Statement（即该执行器会缓存同一个sql的Statement，省去Statement的重新创建，优化性能）（即会重用预处理语句）
            // BATCH执行器会重用预处理语句，并执行批量更新。
            configuration.setDefaultExecutorType(ExecutorType.REUSE);
            configuration.setCallSettersOnNulls(true);
            configuration.setJdbcTypeForNull(JdbcType.NULL);
        };
    }

}
