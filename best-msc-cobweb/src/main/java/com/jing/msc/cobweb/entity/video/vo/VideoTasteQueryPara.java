package com.jing.msc.cobweb.entity.video.vo;

import com.jing.common.core.base.BasePage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author : jing
 * @since : 2024/10/25 11:39
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class VideoTasteQueryPara extends BasePage {

    @Schema(description = "车牌号")
    private String number;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "演员")
    private String performer;

}
