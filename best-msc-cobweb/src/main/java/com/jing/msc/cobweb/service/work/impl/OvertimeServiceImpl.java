package com.jing.msc.cobweb.service.work.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jing.common.core.util.StringUtils;
import com.jing.msc.cobweb.entity.work.Overtime;
import com.jing.msc.cobweb.entity.work.bo.OvertimeImport;
import com.jing.msc.cobweb.mapper.work.OvertimeMapper;
import com.jing.msc.cobweb.service.work.OvertimeService;
import com.jing.msc.cobweb.util.PoiUtils;
import io.jsonwebtoken.lang.Collections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author : jing
 * @since : 2025/4/29 15:08
 */
@Service(value = "overtimeService")
public class OvertimeServiceImpl extends ServiceImpl<OvertimeMapper, Overtime> implements OvertimeService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    @Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
    public boolean importOvertime(MultipartFile file) throws IOException {
        Assert.notNull(file, "file is null");

        String suffix = Objects.requireNonNull(file.getOriginalFilename()).substring(file.getOriginalFilename().lastIndexOf(".") + 1);

        List<OvertimeImport> rowDataList = PoiUtils.readExcel(file.getInputStream(), suffix, null, OvertimeImport.class, rowData -> rowData);

        if (Collections.isEmpty(rowDataList)) {
            throw new RuntimeException("导入数据为空");
        }
        // 构建灵活的日期时间解析器
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                // 年
                .appendPattern("yyyy/")
                // 单月
                .optionalStart().appendPattern("M").optionalEnd()
                // 双月
                .optionalStart().appendPattern("MM").optionalEnd()
                // 分隔符
                .appendPattern("/")
                // 单日
                .optionalStart().appendPattern("d").optionalEnd()
                // 双日
                .optionalStart().appendPattern("dd").optionalEnd()
                // 时间部分
                .appendPattern(" HH:mm:ss")
                // 默认月份为1
                .parseDefaulting(ChronoField.MONTH_OF_YEAR, 1)
                // 默认日期为1
                .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
                .toFormatter();
        String regex = "\\b\\d{4}/\\d{1,2}/\\d{1,2}\\b";
        Pattern pattern = Pattern.compile(regex);

        List<Overtime> overtimes = new ArrayList<>();
        for (OvertimeImport rowData : rowDataList) {
            Overtime overtime = new Overtime();

            String date = null;
            String[] timeSlot = null;
            Matcher matcher = pattern.matcher(rowData.getTime());
            if (matcher.find()) {
                date = matcher.group();
                String times = rowData.getTime().substring(date.length());
                times = times.replaceAll("\\s+", "");
                timeSlot = times.split("-");
            }
            if (Objects.isNull(timeSlot)) {
                continue;
            }

            if (timeSlot[0].length() != 8) {
                timeSlot[0] += ":00";
            }
            overtime.setStartTime(LocalDateTime.parse(date + " " + timeSlot[0], formatter));

            if (timeSlot[1].length() != 8) {
                timeSlot[1] += ":00";
            }
            overtime.setEndTime(LocalDateTime.parse(date + " " + timeSlot[1], formatter));

            overtime.setDuration(Double.parseDouble(rowData.getDuration()));

            if (StringUtils.isNoneBlank(rowData.getProject())) {
                int idx = rowData.getProject().indexOf("（");
                if (idx > 0) {
                    String substring = rowData.getProject().substring(0, idx);
                    // 正则表达式：匹配数字、字母和 -
                    String codeRegex = "^[a-zA-Z0-9-]+$";
                    boolean matches = substring.matches(codeRegex);
                    if (matches) {
                        overtime.setProjectCode(substring);
                    }
                    overtime.setProject(rowData.getProject().substring(idx));
                } else {
                    overtime.setProject(rowData.getProject());
                }
            }

            overtime.setWork(rowData.getContent());
            overtimes.add(overtime);
        }
        return saveBatch(overtimes);
    }


}
