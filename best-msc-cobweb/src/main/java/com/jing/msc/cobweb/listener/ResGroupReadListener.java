package com.jing.msc.cobweb.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.jing.msc.cobweb.entity.test.ResGroup;
import com.jing.msc.cobweb.service.SpiderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author : jing
 * @projectName : best-msc
 * @packageName : com.jing.msc.cobweb.listener
 * @date : 2023/4/19 10:20
 * @description :
 */
public class ResGroupReadListener implements ReadListener<ResGroup> {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final Map<String, Map<String, String>> region = new HashMap<>();

    /**
     * 假设这个是一个DAO，当然有业务逻辑这个也可以是一个service。当然如果不用存储这个对象没用。
     */
    private final SpiderService service;

    /**
     * 如果使用了spring,请使用这个构造方法。每次创建Listener的时候需要把spring管理的类传进来
     *
     * @param service 服务类
     */
    public ResGroupReadListener(SpiderService service) {
        this.service = service;
    }

    /**
     * 这个每一条数据解析都会来调用
     *
     * @param group   one row value. Is is same as {@link AnalysisContext#readRowHolder()}
     * @param context context
     */
    @Override
    public void invoke(ResGroup group, AnalysisContext context) {
        String level2 = group.getCode().substring(0, 4);
        String level3 = group.getCode().substring(0, 6);
        String suffix = group.getCode().substring(6, 8);
        Map<String, String> children = region.get(level2);
        if (null == children) {
            children = new HashMap<>();
        }
        if (0 == Integer.parseInt(suffix)) {
            children.put(level3, group.getName());
        }
        region.put(level2, children);
    }

    /**
     * 所有数据解析完成了 都会来调用
     *
     * @param context context
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        List<String> keys = new ArrayList<>();
        region.forEach((key, value) -> keys.add(key));
        keys.sort(Comparator.comparingInt(Integer::parseInt));
        keys.remove(0);
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            List<ResGroup> groups = region.get(key).entrySet().stream().map(it -> new ResGroup(it.getValue(), it.getKey()))
                    .sorted(Comparator.comparingInt(o -> Integer.parseInt(o.getCode()))).collect(Collectors.toList());
            service.buildInsertSql(groups, Integer.parseInt((i + 1) + "000"), Integer.parseInt((i + 1) + "00"));
        }

    }

}
