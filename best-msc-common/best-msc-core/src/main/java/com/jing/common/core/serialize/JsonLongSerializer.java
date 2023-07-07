package com.jing.common.core.serialize;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ToStringSerializer;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * @author : jing
 * @projectName : best-msc
 * @packageName : com.jing.common.swagger.serialize
 * @date : 2021/4/25 17:26
 * @description :
 */
public class JsonLongSerializer extends ToStringSerializer {

    public final static JsonLongSerializer instance = new JsonLongSerializer();

    public JsonLongSerializer() {
    }

    @Override
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        SerializeWriter out = serializer.out;
        if (object == null) {
            out.writeNull(SerializerFeature.WriteNullNumberAsZero);
        } else {
            String strVal = object.toString();
            if (object instanceof Long) {
                long value = (Long) object;
                if (strVal.length() >= 16) {
                    out.writeString(strVal);
                } else {
                    out.writeLong(value);
                }
                if (out.isEnabled(SerializerFeature.WriteClassName) && value <= 2147483647L && value >= -2147483648L && fieldType != Long.class && fieldType != Long.TYPE) {
                    out.write(76);
                }
            } else {
                out.writeString(strVal);
            }
        }
    }

}
