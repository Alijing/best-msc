package com.jing.msc.cobweb.entity.work.bo;

import com.jing.msc.cobweb.annotation.ExcelColumn;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : jing
 * @since : 2025/4/29 16:10
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "加班日志导入实体类")
public class OvertimeImport {

    @ExcelColumn(title = "时间")
    @Schema(description = "时间")
    private String time;

    @ExcelColumn(title = "时长")
    @Schema(description = "时长")
    private String duration;

    @ExcelColumn(title = "项目")
    @Schema(description = "项目")
    private String project;

    @ExcelColumn(title = "工作内容")
    @Schema(description = "工作内容")
    private String content;

}
