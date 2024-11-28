package com.jing.msc.cobweb.entity.sys.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author : jing
 * @since : 2024/11/26 11:04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class CascaderDictItem extends NormalDictItem {

    @Schema(description = "子节点")
    private List<CascaderDictItem> children;

}
