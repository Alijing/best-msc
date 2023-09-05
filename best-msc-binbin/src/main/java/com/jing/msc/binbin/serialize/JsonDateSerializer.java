package com.jing.msc.binbin.serialize;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

/**
 * 日期序列化处理类
 *
 * @author : jing
 * @projectName : best-msc
 * @packageName : com.jing.common.swagger.serialize
 * @date : 2023/3/31 12:52
 * @description :
 */
public class JsonDateSerializer extends SimpleDateFormatSerializer {

    private final String dateFormat = "yyyy-MM-dd";
    private final String datetimeFormat = "yyyy-MM-dd HH:mm:ss";

    public JsonDateSerializer() {
        super("yyyy-MM-dd HH:mm:ss");
    }

    @Override
    public void write(JSONSerializer serializer, Object value, Object fieldName, Type fieldType, int arg4) throws IOException {
        SerializeWriter out = serializer.getWriter();
        if (value instanceof java.sql.Timestamp) {
            SimpleDateFormat sdf = new SimpleDateFormat(datetimeFormat, Locale.CHINA);
            out.write("\"" + sdf.format(new Date(((java.sql.Timestamp) value).getTime())) + "\"");
            return;
        } else if (value instanceof java.sql.Date) {
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.CHINA);
            out.write("\"" + sdf.format(new Date(((java.sql.Date) value).getTime())) + "\"");
            return;
        } else if (value instanceof Date) {
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.CHINA);
            out.write("\"" + sdf.format(value) + "\"");
            return;
        } else if (value instanceof LocalDateTime) {
            LocalDateTime localDateTime = (LocalDateTime) value;
            String format = localDateTime.format(DateTimeFormatter.ofPattern(datetimeFormat));
            serializer.write(format);
        }
        out.write(value == null ? null : value.toString());
    }

}
