package com.jing.msc.binbin.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @author : jing
 * @projectName : best-msc
 * @packageName : com.jing.msc.binbin.entity
 * @date : 2023/8/12 12:43
 * @description :
 */
@ApiModel(value = "配送单对象", description = "配送单对象")
public class DeliveryOrder {


    @ApiModelProperty("收货方")
    private String shipTo;

    @ApiModelProperty("送货日期")
    private String deliveryDate;

    @ApiModelProperty("订单编号")
    private String orderCode;

    @ApiModelProperty("货物项")
    private List<DeliveryOrderItem> items;

    public String getShipTo() {
        return shipTo;
    }

    public void setShipTo(String shipTo) {
        this.shipTo = shipTo;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public List<DeliveryOrderItem> getItems() {
        return items;
    }

    public void setItems(List<DeliveryOrderItem> items) {
        this.items = items;
    }
}
