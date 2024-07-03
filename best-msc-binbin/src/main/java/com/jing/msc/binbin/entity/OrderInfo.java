package com.jing.msc.binbin.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;

/**
 * @author : jing
 * @projectName : best-msc
 * @packageName : com.jing.msc.binbin.entity
 * @date : 2024/1/5 22:02
 * @description :
 */
@Data
@ContentRowHeight(25)
@HeadRowHeight(30)
@ColumnWidth(25)
public class OrderInfo {

    @ExcelProperty("序号")
    @ColumnWidth(8)
    private Long serNumber;

    @ExcelProperty("数电票号码")
    @ColumnWidth(20)
    private String code;

    @ExcelProperty("销方识别号")
    @ColumnWidth(20)
    private String saleCode;

    @ExcelProperty("销方名称")
    private String saleName;

    @ExcelProperty("购方识别号")
    @ColumnWidth(20)
    private String buyerCode;

    @ExcelProperty("购买方名称")
    private String buyerName;

    @ExcelProperty("开票日期")
    @ColumnWidth(18)
    private String invoiceDate;

    @ExcelProperty("税收分类编码")
    @ColumnWidth(18)
    private String taxationCode;

    @ExcelProperty("货物或应税劳务名称")
    @ColumnWidth(28)
    private String goodsName;

    @ExcelProperty("规格型号")
    @ColumnWidth(12)
    private String specification;

    @ExcelProperty("单位")
    @ColumnWidth(8)
    private String unit;

    @ExcelProperty("数量")
    @ColumnWidth(8)
    private String number;

    @ExcelProperty("单价")
    @ColumnWidth(14)
    private String unitPrice;

    @ExcelProperty("金额")
    @ColumnWidth(10)
    private String amount;

    @ExcelProperty("税率")
    @ColumnWidth(8)
    private String taxRate;

    @ExcelProperty("税额")
    @ColumnWidth(8)
    private String taxAmount;

    @ExcelProperty("价税合计")
    @ColumnWidth(14)
    private String totalPriceTax;

    @ExcelProperty("备注")
    private String remarks;

}
