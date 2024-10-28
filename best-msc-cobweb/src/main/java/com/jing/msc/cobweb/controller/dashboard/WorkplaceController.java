package com.jing.msc.cobweb.controller.dashboard;

import cn.hutool.core.date.DateUtil;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.jing.common.core.base.BaseResp;
import com.jing.common.core.util.JsonUtils;
import com.jing.common.log.aspect.WebLog;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * @author : jing
 * @since : 2024/8/23 11:00
 */
@Tag(name = "工作台相关接口")
@RestController
@RequestMapping("/workplace")
public class WorkplaceController {

    @WebLog(description = "统计所有")
    @Operation(summary = "统计所有")
    @ApiOperationSupport(author = "Jing", order = 1)
    @GetMapping("/total")
    public BaseResp<Map<String, Integer>> total() {
        Map<String, Integer> total = new HashMap<>();
        total.put("project", 102400);
        total.put("access", 81212);
        total.put("todo", 9280);
        return BaseResp.ok(total);
    }

    @WebLog(description = "获取项目")
    @Operation(summary = "获取项目")
    @ApiOperationSupport(author = "Jing", order = 1)
    @GetMapping("/project")
    public BaseResp<List<Project>> project() {
        String json = "[\n" +
                "  {\n" +
                "    \"name\": \"Github\",\n" +
                "    \"icon\": \"akar-icons:github-fill\",\n" +
                "    \"message\": \"workplace.introduction\",\n" +
                "    \"personal\": \"Archer\",\n" +
                "    \"time\": \"2023-09-18T14:00:00\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\": \"Vue\",\n" +
                "    \"icon\": \"logos:vue\",\n" +
                "    \"message\": \"workplace.introduction\",\n" +
                "    \"personal\": \"Archer\",\n" +
                "    \"time\": \"2023-09-18T14:00:00\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\": \"Angular\",\n" +
                "    \"icon\": \"logos:angular-icon\",\n" +
                "    \"message\": \"workplace.introduction\",\n" +
                "    \"personal\": \"Archer\",\n" +
                "    \"time\": \"2023-09-18T14:00:00\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\": \"React\",\n" +
                "    \"icon\": \"logos:react\",\n" +
                "    \"message\": \"workplace.introduction\",\n" +
                "    \"personal\": \"Archer\",\n" +
                "    \"time\": \"2023-09-18T14:00:00\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\": \"Webpack\",\n" +
                "    \"icon\": \"logos:webpack\",\n" +
                "    \"message\": \"workplace.introduction\",\n" +
                "    \"personal\": \"Archer\",\n" +
                "    \"time\": \"2023-09-18T14:00:00\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\": \"Vite\",\n" +
                "    \"icon\": \"vscode-icons:file-type-vite\",\n" +
                "    \"message\": \"workplace.introduction\",\n" +
                "    \"personal\": \"Archer\",\n" +
                "    \"time\": \"2023-09-18T14:00:00\"\n" +
                "  }\n" +
                "]";
        return BaseResp.ok(JsonUtils.toList(json, Project.class));
    }

    @WebLog(description = "获取动态")
    @Operation(summary = "获取动态")
    @ApiOperationSupport(author = "Jing", order = 1)
    @GetMapping("/dynamic")
    public BaseResp<List<Map<String, Object>>> dynamic() {
        Map<String, Object> total = new HashMap<>();
        total.put("keys", Arrays.asList("workplace.push", "Github"));
        total.put("time", DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));

        List<Map<String, Object>> data = new ArrayList<>();

        data.add(total);
        data.add(total);
        data.add(total);
        data.add(total);
        return BaseResp.ok(data);
    }

    @WebLog(description = "获取团队信息")
    @Operation(summary = "获取团队信息")
    @ApiOperationSupport(author = "Jing", order = 1)
    @GetMapping("/team")
    public BaseResp<List<Team>> team() {
        List<Team> team = new ArrayList<>();
        team.add(new Team("Github", "akar-icons:github-fill"));
        team.add(new Team("Vue", "logos:vue"));
        team.add(new Team("Angular", "logos:angular-icon"));
        team.add(new Team("React", "logos:react"));
        team.add(new Team("Webpack", "logos:webpack"));
        team.add(new Team("Vite", "vscode-icons:file-type-vite"));
        return BaseResp.ok(team);
    }

    @WebLog(description = "统计所有")
    @Operation(summary = "统计所有")
    @ApiOperationSupport(author = "Jing", order = 1)
    @GetMapping("/radar")
    public BaseResp<List<Radar>> radar() {
        List<Radar> radars = new ArrayList<>();
        radars.add(new Radar("workplace.quote", 65, 42, 50));
        radars.add(new Radar("workplace.contribution", 160, 30, 140));
        radars.add(new Radar("workplace.hot", 300, 20, 28));
        radars.add(new Radar("workplace.yield", 130, 35, 35));
        radars.add(new Radar("workplace.follow", 100, 80, 90));
        return BaseResp.ok(radars);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Project {

        private String name;

        private String icon;

        private String message;

        private String personal;

        private String time;

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Team {

        private String name;

        private String icon;

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Radar {

        private String name;

        private Integer max;

        private Integer personal;

        private Integer team;

    }

}
