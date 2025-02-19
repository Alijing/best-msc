package com.jing.msc.cobweb.util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.Calendar;
import java.util.Date;

/**
 * @author : jing
 * @since : 2025/2/19 9:56
 */
public class PoiUtils {


    /**
     * 创建默认单元格样式，水平锤子居中
     *
     * @param wb 工作簿
     * @return XSSFCellStyle
     */
    public static XSSFCellStyle createDefaultCellStyle(XSSFWorkbook wb) {
        XSSFCellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        //下边框
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        //左边框
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        //上边框
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
        //右边框
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
        cellStyle.setWrapText(true);
        return cellStyle;
    }


    public static void setCell(Cell cell, Object value, XSSFCellStyle style) {
        cell.setCellStyle(style);
        if (value == null) {
            return;
        }
        if (value instanceof Double) {
            cell.setCellValue((double) value);
        } else if (value instanceof Date) {
            cell.setCellValue((Date) value);
        } else if (value instanceof Calendar) {
            cell.setCellValue((Calendar) value);
        } else if (value instanceof RichTextString) {
            cell.setCellValue((RichTextString) value);
        } else if (value instanceof String) {
            cell.setCellValue((String) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((boolean) value);
        } else {
            cell.setCellValue(value.toString());
        }
    }

}
