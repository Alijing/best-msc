package com.jing.msc.binbin.serialize;

import com.alibaba.fastjson.serializer.BeanContext;
import com.alibaba.fastjson.serializer.ContextValueFilter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 处理值大于15位的长整形数组（或List），转换为字符串数组
 * @author : jing
 * @projectName : best-msc
 * @packageName : com.jing.common.swagger.serialize
 * @date : 2023/3/31 13:44
 * @description :
 */
public class JsonLongArrayFilter implements ContextValueFilter {
    @Override
    public Object process(BeanContext context, Object object, String name, Object value) {
        if (value == null) {
            return null;
        }
        Class<?> aClass = value.getClass();
        if (!(value instanceof List) && !aClass.isArray()) {
            return value;
        }

        return convertLong(value);
    }

    /**
     * fastjson无法解析List嵌套List（或数组）的情况，自行遍历转换
     * @param value
     * @return
     */
    private Object convertLong(Object value) {
        Class<?> aClass = value.getClass();
        List _valueList = null;
        if (value instanceof List) {
            _valueList = (List)value;
        } else if (aClass.isArray()) {
            if (Long.class.isAssignableFrom(aClass.getComponentType())) {
                _valueList = Arrays.stream((Long[]) value).collect(Collectors.toList());
            } else if (Long.TYPE.isAssignableFrom(aClass.getComponentType())) {
                _valueList = Arrays.stream((long[]) value).boxed().collect(Collectors.toList());
            } else {
                return value;
            }
        } else {
            return value;
        }

        List result = new ArrayList();
        for (Object _value : _valueList) {
            Object temp = null;
            if (_value == null) {
                result.add(null);
                continue;
            }
            if (Long.class.isAssignableFrom(_value.getClass())
                    || Long.TYPE.isAssignableFrom(_value.getClass())) {
                Long aLong = (Long)_value;
                //大于15位将解析为字符串，处理js解析19位长整形只能解析到15位的bug
                if (aLong != null && aLong.longValue() > 999999999999999l) {
                    temp = _value.toString();
                }
            } else if (_value instanceof List) {
                temp = convertLong(_value);
            } else {
                Class<?> aClass1 = _value.getClass();
                if (aClass1.isArray()) {
                    temp = convertLong(_value);
                }
            }

            result.add(temp != null ? temp : _value);
        }

        return result;
    }
}
