package com.jing.msc.cobweb.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.jing.msc.cobweb.entity.test.ResGroup;
import com.jing.msc.cobweb.service.SpiderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    private final Map<String, String> region2 = new HashMap<>();

    private final Map<String, String> region3 = new HashMap<>();

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
        String substring = group.getCode().substring(0, 6);
        if ("1102".equals(substring.substring(0, 4))) {
            region2.put(substring, group.getName());
        }
        if ("1103".equals(substring.substring(0, 4))) {
            region3.put(substring, group.getName());
        }
    }

    /**
     * 所有数据解析完成了 都会来调用
     *
     * @param context context
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        List<ResGroup> groups2 = region2.entrySet().stream().map(it -> new ResGroup(it.getValue(), it.getKey()))
                .sorted(Comparator.comparingInt(o -> Integer.parseInt(o.getCode()))).collect(Collectors.toList());
        // 这里也要保存数据，确保最后遗留的数据也存储到数据库
        service.buildInsertSql(groups2, 2000, 200);

        List<ResGroup> groups3 = region3.entrySet().stream().map(it -> new ResGroup(it.getValue(), it.getKey()))
                .sorted(Comparator.comparingInt(o -> Integer.parseInt(o.getCode()))).collect(Collectors.toList());
        // 这里也要保存数据，确保最后遗留的数据也存储到数据库
        service.buildInsertSql(groups3, 3000, 300);
    }

}
