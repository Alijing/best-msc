package com.jing.msc.binbin.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author : jing
 * @date : 2024/4/23 13:46
 * @description :
 */
@RestController
@Api(tags = "接口")
public class OtherController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @ApiOperation(value = "合并配送单信息")
    @ApiOperationSupport(author = "Jing", order = 1)
    @PostMapping(value = "/backProxyApi/defense/saas/v1/file/image/upload")
    public void upload(@RequestParam(value = "file") MultipartFile file, HttpServletRequest request, HttpServletResponse response) {
        logger.info("file:{}", file.getOriginalFilename());
    }


    @ApiOperation(value = "合并配送单信息")
    @ApiOperationSupport(author = "Jing", order = 1)
    @GetMapping(value = "/defense/saas/v1/file/image")
    public void read(@RequestParam(value = "id") String id) {
        logger.info("id:{}", id);
    }


}
