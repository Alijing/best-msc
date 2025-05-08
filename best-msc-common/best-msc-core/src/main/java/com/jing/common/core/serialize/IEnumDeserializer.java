package com.jing.common.core.serialize;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.baomidou.mybatisplus.annotation.IEnum;

import java.lang.reflect.Type;

/**
 * @author : jing
 * @since : 2025/3/26 14:37
 */
public class IEnumDeserializer implements ObjectDeserializer {


    @Override
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        // 获取枚举值
        Object value = parser.parseObject(Object.class);
        if (value == null) {
            return null;
        }

        // 获取枚举类
        Class<?> clazz = (Class<?>) type;
        if (!IEnum.class.isAssignableFrom(clazz)) {
            return null;
        }
        // 通过枚举值获取对应的枚举实例
        for (Object enumConstant : clazz.getEnumConstants()) {
            IEnum<?> iEnum = (IEnum<?>) enumConstant;
            if (value instanceof String && iEnum.toString().equals(value)) {
                return (T) iEnum;
            }
            if (value instanceof Integer && iEnum.getValue().equals(value)) {
                return (T) iEnum;
            }
        }
        return null;
    }

    @Override
    public int getFastMatchToken() {
        return 0;
    }
}
