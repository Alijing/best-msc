package com.jing.msc.binbin.service.impl;

import com.jing.msc.binbin.entity.DeliveryOrder;
import com.jing.msc.binbin.entity.DeliveryOrderItem;
import com.jing.msc.binbin.entity.vo.DeliveryOrderMergeCfg;
import com.jing.msc.binbin.service.BinBinService;
import com.jing.msc.binbin.util.DateUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

/**
 * @author : jing
 * @projectName : best-msc
 * @packageName : com.jing.msc.binbin.service
 * @date : 2023/8/12 11:16
 * @description :
 */
@Service("binBinService")
public class BinBinServiceImpl implements BinBinService {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Excel 中的各种时间格式
     */
    public static final String[] dateFormatStr = new String[]{
            "yyyy/mm;@", "m/d/yy", "yy/m/d", "mm/dd/yy", "dd-mmm-yy", "yyyy/m/d", "yyyy\\-mm\\-dd;@", "yyyy\\-mm\\-dd", "m/d/yy h:mm",
            "YYYY\\-MM\\-DD;@", "yyyy/mm/dd",
    };

    private static FormulaEvaluator evaluator;

    /**
     * 判断是否是文件夹路径正则
     */
    private static final String WIN_FILE_FOLDER_PATTERN = "^([a-zA-Z]:(([\\\\/])[^\\\\/:*?<>|]+)*([\\\\/])[^\\\\/:*?<>|]+\\.[^\\\\/:*?<>|]+,)*[a-zA-Z]:(([\\\\/])[^\\\\/:*?<>|]+)*([\\\\/])[^\\\\/:*?<>|.]+(/[^\\\\/:*?\"<>.|]|[/w,/s]*|[\\/])$";

    @Override
    public void mergeDeliveryOrder(DeliveryOrderMergeCfg cfg, HttpServletRequest request, HttpServletResponse response) {
        String outputEncoding = "GBK";
        Enumeration<String> headers = request.getHeaderNames();
        while (headers.hasMoreElements()) {
            String headKey = headers.nextElement();
            if (request.getHeader(headKey) != null && request.getHeader(headKey).toLowerCase().contains("firefox")) {
                outputEncoding = "UTF-8";
                break;
            }
        }
        response.setCharacterEncoding(outputEncoding);

        List<String> deliveryOrderPath = cfg.getDeliveryOrderPath();
        if (CollectionUtils.isEmpty(deliveryOrderPath)) {
            throw new RuntimeException("配送单文件夹路径不能为空");
        }

        String template = "export/开票明细.xlsx";
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(template);
        if (null == is) {
            throw new RuntimeException("开票明细模板不存在");
        }

        List<File> files = new ArrayList<>();
        for (String path : deliveryOrderPath) {
            boolean matches = path.matches(WIN_FILE_FOLDER_PATTERN);
            if (!matches) {
                continue;
            }
            File dir = new File(path);
            if (!dir.exists()) {
                return;
            }
            File[] temp = dir.listFiles();
            if (null == temp || 0 == temp.length) {
                continue;
            }
            files.addAll(Arrays.asList(temp));
        }

        List<DeliveryOrder> orders = new ArrayList<>();
        for (File file : files) {
            boolean isExcel = file.getName().endsWith(".xls") || file.getName().endsWith(".xlsx");
            if (file.isDirectory() || !isExcel) {
                continue;
            }
            DeliveryOrder order = readDeliveryOrderExcel(file);
            if (Objects.isNull(order)) {
                continue;
            }
            orders.add(order);
        }

        writeDeliveryOrder2Excel(is, orders, response, outputEncoding);
    }

    private void writeDeliveryOrder2Excel(InputStream is, List<DeliveryOrder> orders, HttpServletResponse response, String outputEncoding) {
        XSSFWorkbook wb = null;
        ByteArrayOutputStream bao = null;
        OutputStream os = null;
        ByteArrayInputStream bai = null;
        try {
            ZipSecureFile.setMinInflateRatio(-1.0d);
            ZipSecureFile.setMinInflateRatio(0);
            wb = new XSSFWorkbook(is);
            // 进行模板的克隆(接下来的操作都是针对克隆后的sheet)
            XSSFSheet sheet = wb.getSheetAt(0);
            evaluator = wb.getCreationHelper().createFormulaEvaluator();
            int lastRowNum = 1;
            for (DeliveryOrder order : orders) {
                logger.info("开始写入 : {},{}", order.getShipTo(), order.getOrderCode());
                List<DeliveryOrderItem> items = order.getItems();
                for (DeliveryOrderItem it : items) {
                    XSSFRow row = sheet.createRow(lastRowNum);
                    row.createCell(0).setCellValue(it.getSerialNumber());

                    if (!NumberUtils.isCreatable(it.getSerialNumber())) {
                        lastRowNum++;
                        continue;
                    }

                    row.createCell(1).setCellValue(it.getName());
                    row.createCell(2).setCellValue(it.getSpecifications());
                    row.createCell(3).setCellValue(it.getUnit());
                    row.createCell(4).setCellValue(it.getDelivery());
                    row.createCell(5).setCellValue(Double.parseDouble(it.getReceived()));
                    row.createCell(6).setCellValue(Double.parseDouble(it.getUnitPrice()));

                    XSSFCell cell7 = row.createCell(7);
                    cell7.setCellFormula("ROUND(G" + (lastRowNum + 1) + "*0.85,2)");

                    XSSFCell cell8 = row.createCell(8);
                    cell8.setCellFormula("ROUND(F" + (lastRowNum + 1) + "*H" + (lastRowNum + 1) + ",2)");

                    lastRowNum++;
                }
            }

            bao = new ByteArrayOutputStream();
            wb.write(bao);
            bai = new ByteArrayInputStream(bao.toByteArray());

            String filename = "开票明细导出-" + DateUtils.formatDate(new Date(), "yyyyMMddHHmmss") + ".xlsx";
            response.reset();

            filename = java.net.URLEncoder.encode(filename, "UTF-8").replace("+", "%20");
            response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
            response.setHeader("Content-Disposition", "attachment; filename=" + filename);
            response.addHeader("Content-Length", "" + bai.available());
            response.setContentType("application/octet-stream;charset=" + outputEncoding);
            os = response.getOutputStream();
            IOUtils.copy(bai, os);
            response.flushBuffer();
        } catch (IOException e) {
            logger.error("读取失败 : " + e.getMessage(), e);
        } finally {
            IOUtils.closeQuietly(wb);
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(bao);
            IOUtils.closeQuietly(os);
            IOUtils.closeQuietly(bai);
        }
    }

    private DeliveryOrder readDeliveryOrderExcel(File file) {
        XSSFWorkbook wb = null;
        InputStream is = null;
        try {
            is = new FileInputStream(file);
            ZipSecureFile.setMinInflateRatio(-1.0d);
            ZipSecureFile.setMinInflateRatio(0);
            wb = new XSSFWorkbook(is);
            // 进行模板的克隆(接下来的操作都是针对克隆后的sheet)
            XSSFSheet sheet = wb.getSheetAt(0);
            evaluator = wb.getCreationHelper().createFormulaEvaluator();

            int lastRowNum = sheet.getLastRowNum();
            int startIdx = 0;
            for (int i = 0; i < lastRowNum; i++) {
                XSSFRow row = sheet.getRow(i);
                if (!row.getCTRow().getHidden()) {
                    startIdx = i;
                    break;
                }
            }
            startIdx += 1;
            XSSFRow row = sheet.getRow(startIdx);

            DeliveryOrder order = new DeliveryOrder();
            String page = parseCell(row.getCell(0)).toString();
            String total = page.substring(page.length() - 2, page.length() - 1);
            String date = parseCell(row.getCell(7)).toString();
            order.setDeliveryDate(date.split("：")[1]);

            startIdx += 1;
            row = sheet.getRow(startIdx);
            String shipTo = parseCell(row.getCell(0)).toString();
            order.setShipTo(shipTo);
            String orderCode = parseCell(row.getCell(7)).toString();
            order.setOrderCode(orderCode);

            List<DeliveryOrderItem> items = new ArrayList<>();
            for (int i = startIdx + 2; i < lastRowNum; i++) {
                row = sheet.getRow(i);
                String cell1 = parseCell(row.getCell(0)).toString();
                String cell2 = parseCell(row.getCell(1)).toString();
                if (StringUtils.isBlank(cell1) && StringUtils.isBlank(cell2)) {
                    continue;
                }
                if (cell1.contains("序号") || cell1.contains("送货") || cell1.contains("配送单") || cell1.contains("页/共") || cell1.contains("收货方")) {
                    continue;
                }
                DeliveryOrderItem it = new DeliveryOrderItem();
                it.setSerialNumber(cell1);
                it.setName(cell2);
                it.setSpecifications(parseCell(row.getCell(2)).toString());
                it.setUnit(parseCell(row.getCell(3)).toString());
                it.setDelivery(parseCell(row.getCell(4)).toString());
                it.setReceived(parseCell(row.getCell(5)).toString());
                it.setUnitPrice(parseCell(row.getCell(6)).toString());
                it.setAmount(parseCell(row.getCell(8)).toString());
                it.setRemark(parseCell(row.getCell(9)).toString());
                items.add(it);
            }
            order.setItems(items);
            return order;
        } catch (IOException e) {
            logger.error("读取失败 : " + e.getMessage(), e);
        } finally {
            IOUtils.closeQuietly(wb);
            IOUtils.closeQuietly(is);
        }
        return null;
    }


    private static Object parseCell(Cell cell) {
        Object cellValue = null;
        switch (cell.getCellType()) {
            case NUMERIC:
                String formatString = cell.getCellStyle().getDataFormatString();
                for (String str : dateFormatStr) {
                    if (str.equals(formatString)) {
                        cellValue = DateUtils.formatDate(cell.getDateCellValue(), DateUtils.FORMAT_STRING_1);
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
                cellValue = getCellValue(evaluator.evaluate(cell));
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

    private static String getCellValue(CellValue cell) {
        String cellValue = null;
        switch (cell.getCellType()) {
            case STRING:
                cellValue = cell.getStringValue();
                break;
            case NUMERIC:
                cellValue = String.valueOf(cell.getNumberValue());
                break;
            default:
                break;
        }
        return cellValue;
    }

}
