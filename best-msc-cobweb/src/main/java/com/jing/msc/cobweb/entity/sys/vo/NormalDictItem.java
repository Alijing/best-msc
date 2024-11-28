package com.jing.msc.cobweb.entity.sys.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

/**
 * 前端显示的字典项
 *
 * @author : jing
 * @since : 2024/11/26 10:57
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "前端显示的字典项")
public class NormalDictItem {

    @Schema(description = "字典项Id")
    private Long id;

    @Schema(description = "字典项名称")
    private String name;

    @Schema(description = "是否选中")
    private Boolean checked;

    @Schema(description = "是否禁用")
    private Boolean disabled;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        NormalDictItem that = (NormalDictItem) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

}
