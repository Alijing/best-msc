package com.jing.common.swagger.serialize;

import com.alibaba.fastjson.parser.deserializer.ExtraTypeProvider;
import com.alibaba.fastjson.parser.deserializer.FieldTypeResolver;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * 对象属性类型获取
 *
 * @author : jing
 * @projectName : best-msc
 * @packageName : com.jing.common.swagger.serialize
 * @date : 2023/3/31 13:43
 * @description :
 */
public class JsonObjectTypeProcessor implements ExtraTypeProvider, FieldTypeResolver {

    private Map<String, Class<?>> classMapping;

    public JsonObjectTypeProcessor() {
        super();
    }

    public JsonObjectTypeProcessor(Map<String, Class<?>> classMapping) {
        this.classMapping = classMapping;
    }

    /**
     * javaBean 无效属性获取类型，仅对一层有效果，嵌套无效
     */
    @Override
    public Type getExtraType(Object object, String key) {
        if (classMapping != null) {
            return classMapping.get(key);
        }

        return null;
    }

    /**
     * JSONObject对象获取类型，仅对一层有效果，嵌套无效
     */
    @Override
    public Type resolve(Object object, String fieldName) {
        if (classMapping != null) {
            return classMapping.get(fieldName);
        }
        return null;
    }

}
