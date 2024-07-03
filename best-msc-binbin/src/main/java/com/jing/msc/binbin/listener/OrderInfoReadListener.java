package com.jing.msc.binbin.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.jing.msc.binbin.entity.OrderInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author : jing
 * @projectName : best-msc
 * @packageName : com.jing.msc.binbin.listener
 * @date : 2024/1/8 11:10
 * @description :
 */
public class OrderInfoReadListener implements ReadListener<OrderInfo> {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final Map<String, OrderInfo> sumed = new HashMap<>();

    private List<String> descriptors = Arrays.asList("加工节", "加工", "洗净", "去头", "去皮", "红心", "白心");

    @Override
    public void invoke(OrderInfo orderInfo, AnalysisContext analysisContext) {
        for (String desc : descriptors) {
            if (desc.equals(orderInfo.getGoodsName())) {
                String replace = orderInfo.getGoodsName().replaceAll(desc, "");
                orderInfo.setGoodsName(replace);
            }
        }


        if (StringUtils.isNotBlank(orderInfo.getSpecification())) {
            String spec = orderInfo.getSpecification();
            if (spec.contains("*")) {
                spec = orderInfo.getSpecification().split("\\*")[0].toLowerCase(Locale.ROOT);
            }
            if (spec.contains("/")) {
                spec = orderInfo.getSpecification().split("/")[0].toLowerCase(Locale.ROOT);
            }
            orderInfo.setGoodsName(orderInfo.getGoodsName().toLowerCase(Locale.ROOT).replaceAll(spec, ""));
        }
        orderInfo.setGoodsName(orderInfo.getGoodsName().split("（")[0]);
        orderInfo.setGoodsName(orderInfo.getGoodsName().split("【")[0]);
        if (sumed.get(orderInfo.getGoodsName()) != null) {
            OrderInfo original = sumed.get(orderInfo.getGoodsName());

            BigDecimal ori = new BigDecimal(original.getNumber());
            BigDecimal cur = new BigDecimal(orderInfo.getNumber());
            original.setNumber(ori.add(cur).toString());

            BigDecimal amount1 = new BigDecimal(original.getAmount());
            BigDecimal amount2 = new BigDecimal(orderInfo.getAmount());
            original.setAmount(amount1.add(amount2).toString());

            sumed.put(original.getGoodsName(), original);
        } else {
            sumed.put(orderInfo.getGoodsName(), orderInfo);
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        logger.info(" --- >>>>> 读取数据数量 ：{} ", sumed.size());
    }

    public Map<String, OrderInfo> getSumed() {
        return sumed;
    }

}
