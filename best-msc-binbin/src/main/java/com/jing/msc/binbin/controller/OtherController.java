package com.jing.msc.binbin.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author : jing
 * @date : 2024/4/23 13:46
 * @description :
 */
@RestController
@RequestMapping("/other")
@Tag(name = "其他接口")
public class OtherController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Operation(summary = "upload")
    @ApiOperationSupport(author = "Jing", order = 1)
    @PostMapping(value = "/backProxyApi/defense/saas/v1/file/image/upload")
    public void upload(@RequestParam(value = "file") MultipartFile file, HttpServletRequest request, HttpServletResponse response) {
        logger.info("file:{}", file.getOriginalFilename());
    }


    @Operation(summary = "file")
    @ApiOperationSupport(author = "Jing", order = 1)
    @GetMapping(value = "/defense/saas/v1/file/image")
    public void read(@RequestParam(value = "id") String id) {
        logger.info("id:{}", id);
    }


}
