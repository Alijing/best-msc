package com.jing.msc.cobweb.util;

import cn.hutool.core.date.DateUtil;
import com.jing.common.core.enums.ResultEnum;
import com.jing.common.core.exception.CustomException;
import com.jing.common.core.util.PropertyUtils;
import com.jing.common.core.util.ReflectUtils;
import com.jing.common.core.util.StringUtils;
import com.jing.msc.cobweb.annotation.ExcelColumn;
import org.apache.commons.collections.MapUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;

/**
 * @author : jing
 * @since : 2025/2/19 9:56
 */
public class PoiUtils {

    /**
     * Excel 中的各种时间格式
     */
    public static final String[] DATE_FORMAT_STR = new String[]{
            "yyyy/mm;@", "m/d/yy", "yy/m/d", "mm/dd/yy", "dd-mmm-yy", "yyyy/m/d", "yyyy\\-mm\\-dd;@", "yyyy\\-mm\\-dd",
            "YYYY\\-MM\\-DD;@", "yyyy/mm/dd",
    };

    /**
     * 下标
     */
    private static final String SUB_START = "<sub>";
    private static final String SUB_END = "</sub>";

    /**
     * 上标
     */
    private static final String SUP_START = "<sup>";
    private static final String SUP_END = "</sup>";

    /**
     * 下划线
     */
    private static final String U_START = "<u>";
    private static final String U_END = "</u>";

    /**
     * 粗体
     */
    private static final String STRONG_START = "<strong>";
    private static final String STRONG_END = "</strong>";

    /**
     * 斜体
     */
    private static final String EM_START = "<em>";
    private static final String EM_END = "</em>";

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

    /**
     * fetch data by rule id
     *
     * @param stream          excel 文件流
     * @param fileType        excel 文件后缀名
     * @param sheetAt         表格sheet索引
     * @param titleMap        表头映射，如果elClass为Map，将 titleMap.getValue(表头) 设置为结果map的key；如果elClass为javaBean，将 titleMap.getValue(表头) 设置为结果对象的属性名
     * @param elClass         excel数据读取封装的对象Class
     * @param <T>             excel数据读取封装的对象类型
     * @param rowDataFunction 行数据封装对象解析代码函数式接口，可用于行数据对象创建后的自定义处理
     * @return 导入的数据
     * @author jing
     * @author caiyj
     * @date 2021/9/1 16:54
     */
    public static <T> List<T> readExcel(InputStream stream, String fileType, int sheetAt,
                                        Map<String, String> titleMap, Class<T> elClass, Function<T, T> rowDataFunction) throws IOException {
        List<T> result = new ArrayList<>();
        Workbook wb = null;
        if ("xls".equals(fileType)) {
            wb = new HSSFWorkbook(stream);
        } else if ("xlsx".equals(fileType)) {
            wb = new XSSFWorkbook(stream);
        } else {
            throw new CustomException(ResultEnum.FILE_READ_ERROR.getCode(), "您导入的Excel格式不正确");
        }
        Sheet sheet1 = wb.getSheetAt(sheetAt);
        List<String> titles = new ArrayList<>();
        int rowIndex = 0;

        for (Iterator<Row> var7 = sheet1.iterator(); var7.hasNext(); ++rowIndex) {
            Row row = var7.next();
            T dataInstance = ReflectUtils.newInstance(elClass);
            boolean isBlank = true;
            Cell cell;
            if (rowIndex == 0) {
                for (Cell value : row) {
                    cell = value;
                    Object cellValue = parseCell(cell);
                    String title = cellValue != null ? cellValue.toString() : null;
                    if (StringUtils.isBlank(title)) {
                        continue;
                    }
                    boolean find = false;
                    Field[] declaredFields = elClass.getDeclaredFields();
                    for (Field field : declaredFields) {
                        ExcelColumn annotation = field.getAnnotation(ExcelColumn.class);
                        if (Objects.isNull(annotation)) {
                            continue;
                        }
                        if (StringUtils.isBlank(annotation.title())) {
                            continue;
                        }
                        if (title.equals(annotation.title())) {
                            titles.add(field.getName());
                            find = true;
                            break;
                        }
                    }

                    if (find) {
                        continue;
                    }

                    title = MapUtils.getString(titleMap, title, title);
                    titles.add(title);
                }
            } else {
                for (int i = 0; i < titles.size(); ++i) {
                    cell = row.getCell(i);
                    Object cellValue = null;
                    if (cell != null) {
                        cellValue = parseCell(cell);
                        if ("".equals(cellValue)) {
                            cellValue = null;
                        }
                    }
                    if (cellValue != null) {
                        isBlank = false;
                    }
                    PropertyUtils.setProperty(dataInstance, titles.get(i), cell != null && cellValue != null ? cellValue.toString() : null);
                }
                // 行数据对象创建后的自定义处理，如：设置额外字段值，原字段属性值转换等
                if (rowDataFunction != null) {
                    dataInstance = rowDataFunction.apply(dataInstance);
                }
            }
            if (rowIndex > 0 && !isBlank) {
                result.add(dataInstance);
            }
        }
        wb.close();
        stream.close();
        return result;
    }

    /**
     * fetch data by rule id
     *
     * @param stream          excel 文件流
     * @param fileType        excel 文件后缀名
     * @param titleMap        表头映射，如果elClass为Map，将 titleMap.getValue(表头) 设置为结果map的key；如果elClass为javaBean，将 titleMap.getValue(表头) 设置为结果对象的属性名
     * @param elClass         excel数据读取封装的对象Class
     * @param <T>             excel数据读取封装的对象类型
     * @param rowDataFunction 行数据封装对象解析代码函数式接口，可用于行数据对象创建后的自定义处理
     * @return 导入的数据
     * @author jing
     * @author caiyj
     * @date 2021/9/1 16:54
     */
    public static <T> List<T> readExcel(InputStream stream, String fileType, Map<String, String> titleMap,
                                        Class<T> elClass, Function<T, T> rowDataFunction) throws IOException {
        return readExcel(stream, fileType, 0, titleMap, elClass, rowDataFunction);
    }

    private static Object parseCell(Cell cell) {
        Object cellValue = null;
        switch (cell.getCellType()) {
            case NUMERIC:
                String formatString = cell.getCellStyle().getDataFormatString();
                for (String str : DATE_FORMAT_STR) {
                    if (str.equals(formatString)) {
                        cellValue = DateUtil.format(cell.getDateCellValue(), "yyyy-MM-dd HH:mm:ss");
                        break;
                    }
                }
                if (null == cellValue) {
                    Long longVal = Math.round(cell.getNumericCellValue());
                    double doubleVal = cell.getNumericCellValue();
                    // 判断是否含有小数位.0
                    if (Double.parseDouble(longVal + ".0") == doubleVal) {
                        cellValue = longVal;
                    } else {
                        cellValue = doubleVal;
                    }
                }
                break;
            case STRING:
                cellValue = cell.getStringCellValue();
                break;
            case BOOLEAN:
                cellValue = cell.getBooleanCellValue();
                break;
            case FORMULA:
                cellValue = cell.getCellFormula();
                break;
            case BLANK:
                cellValue = "";
                break;
            case ERROR:
                cellValue = "非法字符";
                break;
            default:
                cellValue = "未知类型";
        }
        return cellValue;
    }

    /**
     * 获取下一对标签的index，不存在这些标签就返回null
     *
     * @param s
     * @param tag SUB_START或者SUP_START或者U_START或者STRONG_START或者EM_START
     * @return int[]中有两个元素，第一个是开始标签的index，第二个元素是结束标签的index
     */
    public static int[] getNextTagsIndex(String s, String tag) {
        int firstStart = s.indexOf(tag);
        if (firstStart > -1) {
            int firstEnd = 0;
            switch (tag) {
                case SUB_START:
                    firstEnd = s.indexOf(SUB_END);
                    break;
                case SUP_START:
                    firstEnd = s.indexOf(SUP_END);
                    break;
                case U_START:
                    firstEnd = s.indexOf(U_END);
                    break;
                case STRONG_START:
                    firstEnd = s.indexOf(STRONG_END);
                    break;
                case EM_START:
                    firstEnd = s.indexOf(EM_END);
                    break;
            }
            if (firstEnd > firstStart) {
                return new int[]{firstStart, firstEnd};
            }
        }
        return null;
    }

    /**
     * 移除下一对sub或者sup或者u或者strong或者em标签，返回移除后的字符串
     *
     * @param s
     * @param tag SUB_START或者SUP_START或者U_START或者STRONG_START或者EM_START
     * @return
     */
    public static String removeNextTags(String s, String tag) {
        s = s.replaceFirst(tag, "");
        switch (tag) {
            case SUB_START:
                s = s.replaceFirst(SUB_END, "");
                break;
            case SUP_START:
                s = s.replaceFirst(SUP_END, "");
                break;
            case U_START:
                s = s.replaceFirst(U_END, "");
                break;
            case STRONG_START:
                s = s.replaceFirst(STRONG_END, "");
                break;
            case EM_START:
                s = s.replaceFirst(EM_END, "");
                break;
        }
        return s;
    }

    /**
     * 判断是不是包含sub、sup、u、strong、em标签
     *
     * @param s
     * @return
     */
    public static boolean containTag(String s) {
        return (s.contains(SUB_START) && s.contains(SUB_END)) || (s.contains(SUP_START) && s.contains(SUP_END))
                || (s.contains(U_START) && s.contains(U_END)) || (s.contains(STRONG_START) && s.contains(STRONG_END))
                || (s.contains(EM_START) && s.contains(EM_END));
    }


    /**
     * 设置单元格值和样式
     *
     * @param cell  单元格
     * @param value 值
     * @param style 单元格样式
     */
    public static Cell handleCell(Cell cell, Object value, XSSFCellStyle style) {
        cell.setCellStyle(style);
        if (value == null) {
            return cell;
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
        return cell;
    }

}
