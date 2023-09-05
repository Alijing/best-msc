package com.jing.msc.binbin.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author : jing
 * @projectName : best-msc
 * @packageName : com.jing.msc.binbin.entity
 * @date : 2023/8/12 12:44
 * @description :
 */
@ApiModel(value = "配送单货物项详细信息对象", description = "配送单货物项详细信息对象")
public class DeliveryOrderItem {

    @ApiModelProperty("序号")
    private String serialNumber;

    @ApiModelProperty("品名")
    private String name;

    @ApiModelProperty("规格")
    private String specifications;

    @ApiModelProperty("单位")
    private String unit;

    @ApiModelProperty("送货量")
    private String delivery;

    @ApiModelProperty("收货量")
    private String received;

    @ApiModelProperty("单价")
    private String unitPrice;

    @ApiModelProperty("金额")
    private String amount;

    @ApiModelProperty("备注")
    private String remark;

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecifications() {
        return specifications;
    }

    public void setSpecifications(String specifications) {
        this.specifications = specifications;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getDelivery() {
        return delivery;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }

    public String getReceived() {
        return received;
    }

    public void setReceived(String received) {
        this.received = received;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

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
