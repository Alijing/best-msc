package com.jing.msc.binbin;

import com.jing.msc.binbin.annotation.EnableCustomSwagger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author : jing
 * @projectName : best-msc
 * @packageName : com.jing.msc.binbin
 * @date : 2023/8/12 11:00
 * @description :
 */
@EnableCustomSwagger
@SpringBootApplication(scanBasePackages = {"com.jing"})
public class BinBinApp8520 {

    public static void main(String[] args) {
        SpringApplication.run(BinBinApp8520.class, args);
    }
}
