package com.jing.common.core.serialize;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;

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
public class JsonDateSerializer implements ObjectSerializer {

    private final String dateFormat = "yyyy-MM-dd";

    private final String datetimeFormat = "yyyy-MM-dd HH:mm:ss";

    private final SimpleDateFormat simpleDateTimeFormatter = new SimpleDateFormat(datetimeFormat, Locale.CHINA);

    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, Locale.CHINA);

    private final DateTimeFormatter localDateTimeformatter = DateTimeFormatter.ofPattern(datetimeFormat, Locale.CHINA);

    @Override
    public void write(JSONSerializer serializer, Object value,
                      Object fieldName, Type fieldType, int arg4) throws IOException {

        SerializeWriter out = serializer.getWriter();
        if (value instanceof LocalDateTime) {
            out.write("\"" + localDateTimeformatter.format((LocalDateTime) value) + "\"");
            return;
        } else if (value instanceof java.sql.Timestamp) {
            out.write("\"" + simpleDateTimeFormatter.format(new Date(((java.sql.Timestamp) value).getTime())) + "\"");
            return;
        } else if (value instanceof java.sql.Date) {
            out.write("\"" + simpleDateFormat.format(new Date(((java.sql.Date) value).getTime())) + "\"");
            return;
        } else if (value instanceof Date) {
            out.write("\"" + simpleDateFormat.format(value) + "\"");
            return;
        }
        out.write(value == null ? null : value.toString());
    }

}
