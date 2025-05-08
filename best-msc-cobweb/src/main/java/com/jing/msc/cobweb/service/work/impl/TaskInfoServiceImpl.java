package com.jing.msc.cobweb.service.work.impl;

import cn.hutool.core.util.RandomUtil;
import com.jing.common.core.enums.ResultEnum;
import com.jing.common.core.exception.CustomException;
import com.jing.common.core.util.JsonUtils;
import com.jing.msc.cobweb.entity.work.Overtime;
import com.jing.msc.cobweb.entity.work.Project;
import com.jing.msc.cobweb.entity.work.TaskInfo;
import com.jing.msc.cobweb.entity.work.TaskLog;
import com.jing.msc.cobweb.service.work.TaskInfoService;
import com.jing.msc.cobweb.util.PoiUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.*;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STMerge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author : jing
 * @since : 2024/7/16 14:15
 */
@Service(value = "taskInfoService")
public class TaskInfoServiceImpl implements TaskInfoService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void taskList(String startDate, String endDate, HttpServletRequest request, HttpServletResponse response) {
        List<TaskInfo> taskInfos = readTaskFile(startDate, endDate);
        if (CollectionUtils.isEmpty(taskInfos)) {
            return;
        }
        write2Response(generateWord(taskInfos), "禅道任务_" + RandomUtil.randomNumbers(6) + ".docx", request, response);
    }

    @Override
    public void taskSplit(String startDate, String endDate, HttpServletRequest request, HttpServletResponse response) {
        List<TaskInfo> taskInfos = readTaskFile(startDate, endDate);
        if (CollectionUtils.isEmpty(taskInfos)) {
            return;
        }
        Map<Long, List<TaskInfo>> temp = new HashMap<>();
        Iterator<TaskInfo> iterator = taskInfos.iterator();
        while (iterator.hasNext()) {
            TaskInfo info = iterator.next();
            List<TaskInfo> tasks = temp.get(info.getProjectId());
            if (Objects.isNull(tasks)) {
                tasks = new ArrayList<>();
                temp.put(info.getProjectId(), tasks);
            }
            tasks.add(info);
            iterator.remove();
        }

        List<Project> projects = new ArrayList<>();
        for (Map.Entry<Long, List<TaskInfo>> entry : temp.entrySet()) {
            List<TaskInfo> tasks = entry.getValue();
            tasks.sort(Comparator.comparing(TaskInfo::getStartDate));
            Project project = new Project();
            project.setId(entry.getKey());
            project.setName(tasks.get(0).getProjectName());
            project.setStartDate(tasks.get(0).getStartDate());
            project.setEndDate(tasks.get(tasks.size() - 1).getDeadline());
            project.setTasks(tasks);
            projects.add(project);
        }
        projects.sort(Comparator.comparing(Project::getStartDate));

        String template = "template/禅道任务文档.docx";
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(template);
        if (null == is) {
            throw new CustomException(ResultEnum.TEMPLATE_NOT_EXIST);
        }

        XWPFDocument doc = null;
        ByteArrayOutputStream bao = null;
        OutputStream os = null;
        ByteArrayInputStream bais = null;
        try {
            doc = new XWPFDocument(is);
            XWPFTable table = doc.getTables().get(1);
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            for (Project project : projects) {

                XWPFTableRow row0 = table.createRow();
                XWPFTableCell cell00 = row0.getCell(0);
                setCellText(cell00, "项目名称", "宋体");
                XWPFTableCell cell01 = row0.getCell(1);
                setCellText(cell01, project.getName(), "宋体");

                XWPFTableRow row1 = table.createRow();
                XWPFTableCell cell10 = row1.getCell(0);
                setCellText(cell10, "项目周期", "宋体");
                XWPFTableCell cell11 = row1.getCell(1);
                String projectCycle = project.getStartDate().format(dateFormatter) + " 至 " + project.getEndDate().format(dateFormatter);
                setCellText(cell11, projectCycle, "宋体");

                XWPFTableRow row2 = table.createRow();
                XWPFTableCell cell20 = row2.getCell(0);
                setCellText(cell20, "任务列表", "宋体");
                XWPFTableCell cell21 = row2.getCell(1);
                List<TaskInfo> tasks = project.getTasks();
                if (CollectionUtils.isEmpty(tasks)) {
                    continue;
                }
                List<String> list = new ArrayList<>();
                for (int i = 0; i < tasks.size(); i++) {
                    TaskInfo it = tasks.get(i);
                    list.add((i + 1) + "：" + it.getName() + "（" + it.getStartDate() + " 至 " + it.getDeadline() + "）");
                }
                setCellText(cell21, list, "仿宋", ParagraphAlignment.LEFT);

            }
            bao = new ByteArrayOutputStream();
            doc.write(bao);
            bais = new ByteArrayInputStream(bao.toByteArray());

            write2Response(bais, "禅道任务_" + RandomUtil.randomNumbers(6) + ".docx", request, response);
        } catch (IOException e) {
            throw new CustomException(ResultEnum.FILE_GENERATION_FAILED);
        } finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(doc);
            IOUtils.closeQuietly(bao);
            IOUtils.closeQuietly(os);
            IOUtils.closeQuietly(bais);
        }
    }

    @Override
    public void obtainOvertimeInfo(String startDate, String endDate, HttpServletRequest request, HttpServletResponse response) throws IOException, URISyntaxException {
        Map<LocalDate, List<TaskLog>> workLogs = readTaskLogHtml(startDate, endDate);
        Assert.notEmpty(workLogs, "工作日志为空");
        List<TaskInfo> tasks = readTaskFile(startDate, endDate);
        Assert.notEmpty(tasks, "工作任务为空");

        List<Overtime> overtimes = new ArrayList<>();

        Iterator<Map.Entry<LocalDate, List<TaskLog>>> iterator = workLogs.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<LocalDate, List<TaskLog>> entry = iterator.next();
            List<TaskLog> logs = entry.getValue();
            if (CollectionUtils.isEmpty(logs)) {
                continue;
            }

            List<String> title = new ArrayList<>();
            List<String> execution = new ArrayList<>();

            double consumed = 0;
            for (TaskLog log : logs) {
                consumed += log.getConsumed();

                title.add(log.getTitle());

                TaskInfo task = tasks.stream().filter(t -> t.getId().equals(log.getTaskId())).findFirst().orElse(null);
                if (Objects.nonNull(task)) {
                    execution.add(task.getProjectName());
                }
            }

            // 周末，小于8小时，直接标记加班
            DayOfWeek dayOfWeek = entry.getKey().getDayOfWeek();
            //if (consumed < 8 && (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY)) {
            //    Overtime ot = new Overtime();
            //    ot.setWordDate(entry.getKey().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            //    ot.setOvertime("未知-未知");
            //    ot.setDuration(consumed);
            //    ot.setExecution(execution);
            //    ot.setTitle(title);
            //    overtimes.add(ot);
            //    continue;
            //}

            double overtime = consumed - 8;
            if (overtime <= 0) {
                iterator.remove();
                continue;
            }

            Overtime ot = new Overtime();
            //ot.setWordDate(entry.getKey().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            //
            //// 将 加班时长 小时转换为分钟
            //LocalDateTime end = entry.getKey().atTime(17, 30).plus(Duration.ofMinutes((long) overtime * 60));
            //ot.setOvertime("17:30:00-" + end.format(DateTimeFormatter.ofPattern("HH:mm")));
            //ot.setDuration(overtime);
            //ot.setExecution(execution);
            //ot.setTitle(title);
            overtimes.add(ot);
        }

        //overtimes.sort(Comparator.comparing(Overtime::getWordDate));

        write2Response(generateOvertimeExcel(overtimes, request, response), "加班信息_" + RandomUtil.randomNumbers(6) + ".xlsx", request, response);
    }

    private ByteArrayInputStream generateOvertimeExcel(List<Overtime> overtimes, HttpServletRequest request, HttpServletResponse response) {
        Assert.notEmpty(overtimes, "加班信息为空");

        InputStream is = this.getClass().getClassLoader().getResourceAsStream("template/overtime_template.xlsx");
        Assert.notNull(is, "模板不存在");

        XSSFWorkbook wb = null;
        ByteArrayOutputStream bao = null;
        try {
            wb = new XSSFWorkbook(is);
            // 进行模板的克隆(接下来的操作都是针对克隆后的sheet)
            XSSFSheet sheet = wb.cloneSheet(0);
            wb.setSheetName(1, "加班日志表");

            XSSFCellStyle cellStyle = PoiUtils.createDefaultCellStyle(wb);
            int startIdx = 1;
            for (Overtime ot : overtimes) {
                //logger.info("overtime date: {}, time: {}", ot.getWordDate(), ot.getOvertime());
                //
                //XSSFRow row = sheet.createRow(startIdx);
                ////设置行高
                //row.setHeightInPoints(25);
                //PoiUtils.setCell(row.createCell(0), ot.getWordDate() + " " + ot.getOvertime(), cellStyle);
                //PoiUtils.setCell(row.createCell(1), ot.getDuration(), cellStyle);
                //
                //String execution = StringUtils.join(ot.getExecution(), "、");
                //PoiUtils.setCell(row.createCell(2), execution, cellStyle);
                //
                //String title = StringUtils.join(ot.getTitle(), "、");
                //PoiUtils.setCell(row.createCell(3), title, cellStyle);

                startIdx++;
            }

            // 移除workbook中的模板sheet
            wb.removeSheetAt(0);
            bao = new ByteArrayOutputStream();
            wb.write(bao);
            return new ByteArrayInputStream(bao.toByteArray());
        } catch (IOException e) {
            throw new CustomException(ResultEnum.IOException.getCode(), "加班日志表生成失败");
        } finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(wb);
            IOUtils.closeQuietly(bao);
        }
    }

    private Map<LocalDate, List<TaskLog>> readTaskLogHtml(String startDate, String endDate) throws IOException, URISyntaxException {
        Map<LocalDate, List<TaskLog>> target = new HashMap<>();
        org.jsoup.nodes.Document doc = Jsoup.parse(this.getClass().getClassLoader().getResourceAsStream("data/allTaskLog.html"), "UTF-8", "");
        Elements tr = doc.select("tr");
        for (org.jsoup.nodes.Element value : tr) {
            String dataId = value.attr("data-id");
            if (StringUtils.isBlank(dataId)) {
                continue;
            }
            TaskLog log = new TaskLog();
            log.setId(Long.parseLong(dataId));

            Elements td = value.select("td");
            for (org.jsoup.nodes.Element element : td) {
                String clazz = element.attr("class");
                String text = element.text();

                if ("c-objectType c-name".equals(clazz)) {
                    URI uri = new URI(element.select("a").attr("href"));
                    Map<String, String> collect = URLEncodedUtils.parse(uri, StandardCharsets.UTF_8).stream()
                            .collect(Collectors.toMap(NameValuePair::getName, NameValuePair::getValue));
                    log.setTaskId(Long.parseLong(collect.get("id")));
                }

                if ("c-work c-name".equals(clazz)) {
                    log.setTitle(text.replaceAll("<[^>]*>", ""));
                }

                if ("c-date".equals(clazz)) {
                    LocalDate date = LocalDate.parse(text, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    log.setWorkDate(date);
                }

                if ("c-consumed".equals(clazz)) {
                    log.setConsumed(Double.parseDouble(text));
                }
            }
            List<TaskLog> logs = target.get(log.getWorkDate());
            if (Objects.isNull(logs)) {
                logs = new ArrayList<>();
                target.put(log.getWorkDate(), logs);
            }
            logs.add(log);
        }
        return target;
    }

    @SuppressWarnings("all")
    private List<TaskInfo> readTaskFile(String startDate, String endDate) {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("data/task.json");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            Map<String, Object> map = JsonUtils.toMap(sb.toString());
            List<TaskInfo> taskList = new ArrayList<>();
            // 遍历 map
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                String key = entry.getKey();
                Map<String, Object> value = (Map<String, Object>) entry.getValue();
                LocalDate start = null;
                if ("0000-00-00".equals(value.get("estStarted").toString())) {
                    String realStarted = value.get("realStarted").toString();
                    String[] split = realStarted.split(" ");
                    start = LocalDate.parse(split[0]);
                } else {
                    start = LocalDate.parse(value.get("estStarted").toString());
                }

                LocalDate end = null;
                if ("0000-00-00".equals(value.get("deadline").toString())) {
                    String finishedDate = value.get("finishedDate").toString();
                    String[] split = finishedDate.split(" ");
                    end = LocalDate.parse(split[0]);
                } else {
                    end = LocalDate.parse(value.get("deadline").toString());
                }

                if (start.isBefore(LocalDate.parse(startDate)) || end.isAfter(LocalDate.parse(endDate))) {
                    continue;
                }

                TaskInfo taskInfo = new TaskInfo();
                taskInfo.setId(Long.valueOf(key));
                taskInfo.setName(value.get("name").toString());
                taskInfo.setProjectId(Long.valueOf(value.get("executionID").toString()));
                taskInfo.setProjectName(value.get("executionName").toString());
                taskInfo.setStartDate(start);
                taskInfo.setDeadline(end);
                taskInfo.setDescription(value.get("desc").toString());
                taskList.add(taskInfo);
            }
            taskList.sort(Comparator.comparing(TaskInfo::getId));
            if (CollectionUtils.isEmpty(taskList)) {
                return Collections.emptyList();
            }
            return taskList;
        } catch (IOException e) {
            logger.error("读取文件失败", e);
            throw new CustomException(ResultEnum.FILE_READ_ERROR);
        }
    }

    private ByteArrayInputStream generateWord(List<TaskInfo> taskList) {
        String template = "template/禅道任务文档.docx";
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(template);
        if (null == is) {
            throw new CustomException(ResultEnum.TEMPLATE_NOT_EXIST);
        }

        XWPFDocument doc = null;
        ByteArrayOutputStream bao = null;
        try {
            doc = new XWPFDocument(is);
            XWPFTable table = doc.getTables().get(0);

            for (TaskInfo task : taskList) {
                XWPFTableRow row0 = table.createRow();
                XWPFTableCell cell00 = row0.getCell(0);
                setCellText(cell00, "任务名称", "宋体");

                XWPFTableCell cell01 = row0.getCell(1);
                mergeCellsHorizontal(row0, 1, 3);
                setCellText(cell01, task.getName(), "宋体");

                XWPFTableRow row1 = table.createRow();

                XWPFTableCell cell10 = row1.getCell(0);
                setCellText(cell10, "所属项目", "宋体");

                XWPFTableCell cell11 = row1.getCell(1);
                mergeCellsHorizontal(row1, 1, 3);
                setCellText(cell11, task.getProjectName(), "宋体");

                XWPFTableRow row2 = table.createRow();
                XWPFTableCell cell20 = row2.getCell(0);
                setCellText(cell20, "开始时间", "宋体");

                XWPFTableCell cell21 = row2.getCell(1);
                setCellText(cell21, task.getStartDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), "宋体");

                XWPFTableCell cell22 = row2.getCell(2);
                setCellText(cell22, "截止时间", "宋体");

                XWPFTableCell cell23 = row2.getCell(3);
                setCellText(cell23, task.getDeadline().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), "宋体");

                XWPFTableRow row3 = table.createRow();
                XWPFTableCell cell30 = row3.getCell(0);
                setCellText(cell30, "任务描述", "宋体");

                XWPFTableCell cell31 = row3.getCell(1);
                mergeCellsHorizontal(row3, 1, 3);
                setCellText(cell31, task.getDescription(), "宋体");

            }

            bao = new ByteArrayOutputStream();
            doc.write(bao);
            return new ByteArrayInputStream(bao.toByteArray());
        } catch (IOException e) {
            throw new CustomException(ResultEnum.FILE_GENERATION_FAILED);
        } finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(doc);
            IOUtils.closeQuietly(bao);
        }
    }

    /**
     * 跨列合并
     * table要合并单元格的表格
     * row要合并哪一行的单元格
     * fromCell开始合并的单元格
     * toCell合并到哪一个单元格
     */
    public void mergeCellsHorizontal(XWPFTableRow row, int fromCell, int toCell) {
        for (int cellIndex = fromCell; cellIndex <= toCell; cellIndex++) {
            XWPFTableCell cell = row.getCell(cellIndex);
            if (cellIndex == fromCell) {
                // The first merged cell is set with RESTART merge value
                cell.getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.RESTART);
            } else {
                // Cells which join (merge) the first one, are set with CONTINUE
                cell.getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.CONTINUE);
            }
        }
    }

    /**
     * 跨行合并
     * table要合并单元格的表格
     * col要合并哪一列的单元格
     * fromRow从哪一行开始合并单元格
     * toRow合并到哪一个行
     */
    public void mergeCellsVertically(XWPFTable table, int col, int fromRow, int toRow) {
        for (int rowIndex = fromRow; rowIndex <= toRow; rowIndex++) {
            XWPFTableCell cell = table.getRow(rowIndex).getCell(col);
            if (rowIndex == fromRow) {
                // The first merged cell is set with RESTART merge value
                cell.getCTTc().addNewTcPr().addNewVMerge().setVal(STMerge.RESTART);
            } else {
                // Cells which join (merge) the first one, are set with CONTINUE
                cell.getCTTc().addNewTcPr().addNewVMerge().setVal(STMerge.CONTINUE);
            }
        }
    }


    private void setCellText(XWPFTableCell cell, String val, String fontFamily) {
        // setText底层是追加，所以要删除删除原来的段落
        cell.removeParagraph(0);
        // 设置段落的样式，行间距
        XWPFParagraph paragraph = cell.addParagraph();
        paragraph.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun run = paragraph.createRun();
        run.setText(val);
        run.setFontFamily(StringUtils.isBlank(fontFamily) ? "仿宋" : fontFamily);
        // 是否粗体
        run.setBold(false);
    }

    private void setCellText(XWPFTableRow row, int[] index, String[] val, String fontFamily) {
        for (int i = 0; i < index.length; i++) {
            XWPFTableCell cell = row.getCell(index[i]);
            // setText底层是追加，所以要删除删除原来的段落
            cell.removeParagraph(0);
            // 设置段落的样式，行间距
            XWPFParagraph paragraph = cell.addParagraph();
            paragraph.setAlignment(ParagraphAlignment.CENTER);
            XWPFRun run = paragraph.createRun();
            run.setText(val[i]);
            run.setFontFamily(StringUtils.isBlank(fontFamily) ? "仿宋" : fontFamily);
            // 是否粗体
            run.setBold(false);
        }
    }

    /**
     * 设置单元格文本
     * 如果文本条数大于1，则自动换行
     *
     * @param cell 单元格
     * @param val  文本
     */
    private void setCellText(XWPFTableCell cell, List<String> val, String fontFamily, ParagraphAlignment align) {
        cell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);
        // setText底层是追加，所以要删除删除原来的段落
        cell.removeParagraph(0);
        // 设置段落的样式，行间距
        XWPFParagraph paragraph = cell.addParagraph();
        paragraph.setAlignment(Objects.isNull(align) ? ParagraphAlignment.CENTER : align);
        XWPFRun run = paragraph.createRun();
        for (int i = 0; i < val.size(); i++) {
            String txt = val.get(i);
            run.setText(txt);
            if (i != val.size() - 1) {
                run.addBreak();
            }
        }
        run.setFontFamily(StringUtils.isBlank(fontFamily) ? "仿宋" : fontFamily);
        // 是否粗体
        run.setBold(false);
    }


    private void write2Response(ByteArrayInputStream bais, String fileName, HttpServletRequest request, HttpServletResponse response) {
        OutputStream os = null;
        try {
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
            fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.displayName());
            response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName + ";filename*=utf-8''" + fileName);
            response.addHeader(HttpHeaders.CONTENT_LENGTH, "" + bais.available());
            os = response.getOutputStream();
            byte[] b = new byte[2048];
            int length;
            while ((length = bais.read(b)) > 0) {
                os.write(b, 0, length);
            }
        } catch (IOException e) {
            throw new CustomException(ResultEnum.FILE_GENERATION_FAILED);
        } finally {
            IOUtils.closeQuietly(os);
            IOUtils.closeQuietly(bais);
        }

    }


}
