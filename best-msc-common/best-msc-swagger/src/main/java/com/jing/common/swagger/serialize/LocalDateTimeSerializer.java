package com.jing.common.swagger.serialize;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author : jing
 * @projectName : best-msc
 * @packageName : com.jing.common.swagger.serialize
 * @date : 2021/4/23 17:39
 * @description : json响应 将日期字符串序列化为定制日期字符串响应
 */
public class LocalDateTimeSerializer extends SimpleDateFormatSerializer {

    private final String pattern;

    public LocalDateTimeSerializer(String pattern) {
        super(pattern);
        this.pattern = pattern;
    }

    @Override
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        if (null == object) {
            serializer.out.writeNull();
            return;
        }
        if (object instanceof LocalDateTime) {
            LocalDateTime localDateTime = (LocalDateTime) object;
            String format = localDateTime.format(DateTimeFormatter.ofPattern(pattern));
            serializer.write(format);
        }
    }
}
