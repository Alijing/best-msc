package com.jing.msc.cobweb;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author : jing
 * @projectName : best-msc
 * @packageName : com.jing.msc.cobweb
 * @date : 2021/4/22 17:59
 * @description :
 */
@EnableScheduling
@SpringBootApplication(scanBasePackages = {"com.jing"})
@MapperScan({"com.jing.msc.security.mapper", "com.jing.msc.cobweb.mapper"})
public class CobwebApp8135 {

    public static void main(String[] args) {
        SpringApplication.run(CobwebApp8135.class, args);
    }

}
