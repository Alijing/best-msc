package com.jing.msc.binbin.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;

/**
 * @author : jing
 * @projectName : best-msc
 * @packageName : com.jing.msc.binbin.entity
 * @date : 2023/8/12 12:44
 * @description :
 */
@Data
@Tag(name = "配送单", description = "配送单货物项详细信息实体类")
public class DeliveryOrderItem {

    @Schema(name = "序号")
    private String serialNumber;

    @Schema(name = "品名")
    private String name;

    @Schema(name = "规格")
    private String specifications;

    @Schema(name = "单位")
    private String unit;

    @Schema(name = "送货量")
    private String delivery;

    @Schema(name = "收货量")
    private String received;

    @Schema(name = "单价")
    private String unitPrice;

    @Schema(name = "金额")
    private String amount;

    @Schema(name = "备注")
    private String remark;

    @Override
    public String toString() {
        return "DeliveryOrderItem{" +
                "serialNumber='" + serialNumber + '\'' +
                ", name='" + name + '\'' +
                ", specifications='" + specifications + '\'' +
                ", unit='" + unit + '\'' +
                ", delivery='" + delivery + '\'' +
                ", received='" + received + '\'' +
                ", unitPrice='" + unitPrice + '\'' +
                ", amount='" + amount + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}
