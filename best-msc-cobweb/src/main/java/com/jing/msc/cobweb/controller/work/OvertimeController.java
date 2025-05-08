package com.jing.msc.cobweb.controller.work;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.jing.common.core.base.BaseResp;
import com.jing.common.log.aspect.WebLog;
import com.jing.msc.cobweb.service.work.OvertimeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @author : jing
 * @since : 2025/4/29 15:13
 */
@Tag(name = "工作相关接口")
@RestController
@RequestMapping("/work/overtime")
public class OvertimeController {


    @Resource(name = "overtimeService")
    private OvertimeService service;

    @WebLog(description = "加班日志导入")
    @Operation(summary = "加班日志导入")
    @ApiOperationSupport(author = "Jing", order = 1)
    @PostMapping(value = "/import")
    public BaseResp<Boolean> list(@RequestParam(value = "file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return BaseResp.error();
        }
        return BaseResp.ok(service.importOvertime(file));
    }


}
