package com.jing.msc.binbin.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

/**
 * @author : jing
 * @projectName : best-msc
 * @packageName : com.jing.msc.binbin.entity.vo
 * @date : 2023/8/31 17:37
 * @description :
 */

@Tag(name = "配送单", description = "配送单实体类")
public class DeliveryOrderMergeCfg {

    @Schema(type = "string", description = "ces 收货方")
    private List<String> deliveryOrderPath;

    public List<String> getDeliveryOrderPath() {
        return deliveryOrderPath;
    }

    public void setDeliveryOrderPath(List<String> deliveryOrderPath) {
        this.deliveryOrderPath = deliveryOrderPath;
    }

}
