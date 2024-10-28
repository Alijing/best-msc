package com.jing.msc.cobweb.controller.dashboard;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.jing.common.core.base.BaseResp;
import com.jing.common.log.aspect.WebLog;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : jing
 * @since : 2024/8/23 10:02
 */
@Tag(name = "分析页相关接口")
@RestController
@RequestMapping("/analysis")
public class AnalysisController {

    @WebLog(description = "统计所有")
    @Operation(summary = "统计所有")
    @ApiOperationSupport(author = "Jing", order = 1)
    @GetMapping("/total")
    public BaseResp<Map<String, Integer>> total() {
        Map<String, Integer> total = new HashMap<>();
        total.put("users", 102400);
        total.put("messages", 81212);
        total.put("moneys", 9280);
        total.put("shoppings", 13600);
        return BaseResp.ok(total);
    }

    @WebLog(description = "用户访问源")
    @Operation(summary = "用户访问源")
    @ApiOperationSupport(author = "Jing", order = 1)
    @GetMapping("/userAccessSource")
    public BaseResp<List<UserAccessSource>> userAccessSource() {
        List<UserAccessSource> data = new ArrayList<>();
        data.add(new UserAccessSource("analysis.directAccess", "1000"));
        data.add(new UserAccessSource("analysis.mailMarketing", "310"));
        data.add(new UserAccessSource("analysis.allianceAdvertising", "234"));
        data.add(new UserAccessSource("analysis.videoAdvertising", "135"));
        data.add(new UserAccessSource("analysis.searchEngines", "1548"));
        return BaseResp.ok(data);
    }

    @WebLog(description = "每周用户活动")
    @Operation(summary = "每周用户活动")
    @ApiOperationSupport(author = "Jing", order = 1)
    @GetMapping("/weeklyUserActivity")
    public BaseResp<List<UserAccessSource>> weeklyUserActivity() {
        List<UserAccessSource> data = new ArrayList<>();
        data.add(new UserAccessSource("analysis.monday", "13253"));
        data.add(new UserAccessSource("analysis.tuesday", "34235"));
        data.add(new UserAccessSource("analysis.wednesday", "26321"));
        data.add(new UserAccessSource("analysis.thursday", "12340"));
        data.add(new UserAccessSource("analysis.friday", "24643"));
        data.add(new UserAccessSource("analysis.saturday", "1322"));
        data.add(new UserAccessSource("analysis.sunday", "1324"));
        return BaseResp.ok(data);
    }

    @WebLog(description = "每周用户活动")
    @Operation(summary = "每周用户活动")
    @ApiOperationSupport(author = "Jing", order = 1)
    @GetMapping("/monthlySales")
    public BaseResp<List<MonthlySales>> monthlySales() {
        List<MonthlySales> data = new ArrayList<>();
        data.add(new MonthlySales("100", "120", "analysis.january"));
        data.add(new MonthlySales("120", "82", "analysis.february"));
        data.add(new MonthlySales("161", "91", "analysis.march"));
        data.add(new MonthlySales("134", "154", "analysis.april"));
        data.add(new MonthlySales("105", "162", "analysis.may"));
        data.add(new MonthlySales("160", "140", "analysis.june"));
        data.add(new MonthlySales("165", "145", "analysis.july"));
        data.add(new MonthlySales("114", "250", "analysis.august"));
        data.add(new MonthlySales("163", "134", "analysis.september"));
        data.add(new MonthlySales("185", "56", "analysis.october"));
        data.add(new MonthlySales("118", "99", "analysis.november"));
        data.add(new MonthlySales("123", "123", "analysis.december"));
        return BaseResp.ok(data);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserAccessSource {

        private String name;

        private String value;

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MonthlySales {

        private String estimate;

        private String actual;

        private String name;

    }


}
