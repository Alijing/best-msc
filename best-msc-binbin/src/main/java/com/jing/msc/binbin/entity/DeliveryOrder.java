package com.jing.msc.binbin.entity;


import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;

import java.util.List;

/**
 * @author : jing
 * @projectName : best-msc
 * @packageName : com.jing.msc.binbin.entity
 * @date : 2023/8/12 12:43
 * @description :
 */
@Data
@Tag(name = "配送单", description = "配送单实体类")
public class DeliveryOrder {


    @Schema(name = "收货方", type = "string", description = "ces 收货方")
    private String shipTo;

    @Schema(name = "送货日期")
    private String deliveryDate;

    @Schema(name = "订单编号")
    private String orderCode;

    @Schema(name = "货物项")
    private List<DeliveryOrderItem> items;

}
