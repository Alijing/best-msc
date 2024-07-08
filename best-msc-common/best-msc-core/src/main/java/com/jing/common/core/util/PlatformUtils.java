package com.jing.common.core.util;

import cn.hutool.core.date.DateUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author : jing
 * @since : 2024/7/3 11:34
 */
@Service("platformUtils")
public class PlatformUtils {


    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 对象转字符串，基本类型、String、Number返回对象的字符串格式，枚举返回name()，日期返回yyyy-MM-dd hh:mm:ss格式字符串，其余对象转为json格式
     *
     * @param value 待转换对象
     * @return 对象的字符串格式
     */
    public static String toString(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof String) {
            return (String) value;
        }
        Class<?> tClass = value.getClass();
        if (ClassUtils.isPrimitiveOrWrapper(tClass)) {
            return String.valueOf(value);
        } else if (Enum.class.isAssignableFrom(tClass)) {
            return ((Enum) value).name();
        } else if (CharSequence.class.isAssignableFrom(tClass)) {
            return String.valueOf(value);
        } else if (Number.class.isAssignableFrom(tClass)) {
            return String.valueOf(value);
        } else if (Date.class.isAssignableFrom(tClass)) {
            return DateUtil.format((Date) value, DATE_FORMAT);
        } else if (LocalDateTime.class.isAssignableFrom(tClass)) {
            return DateUtil.format((LocalDateTime) value, DATE_FORMAT);
        } else {
            return JsonUtils.toJson(value);
        }
    }

    /**
     * 将字符串格式的值转换为指定class的结果对象，注意：如果tClass为集合对象（List等），集合元素则会解析为JSONObject
     *
     * @param valueStr 字符串格式的数据
     * @param tClass   转换后的对象Class，如果valueStr为List格式的字符串，tClass作为
     * @param <T>      结果对象类型，跟tClass一致
     * @return 结果对象
     */
    public static <T> T getValueByClass(String valueStr, Class<T> tClass) {
        return getValueByClass(valueStr, (Type) tClass);
    }

    /**
     * 将字符串格式的值转换为指定class的结果对象，注意：如果tClass为集合对象（List等），集合元素则会解析为JSONObject
     *
     * @param valueStr 字符串格式的数据
     * @param tClass   转换后的对象Class，如果valueStr为List格式的字符串，tClass作为
     * @param <T>      结果对象类型，跟tClass一致
     * @return 结果对象
     */
    public static <T> T getValueByClass(String valueStr, Type tClass) {
        if (valueStr == null) {
            return null;
        }
        if (tClass == String.class) {
            return (T) valueStr;
        }
        if (tClass instanceof Class) {
            Class<T> _tClass = (Class<T>) tClass;
            if (ClassUtils.isPrimitiveOrWrapper(_tClass)) {
                //基本类型的包装类型
                Class<?> primitiveWrapperClass = ClassUtils.resolvePrimitiveIfNecessary(_tClass);
                if (primitiveWrapperClass == Void.class || primitiveWrapperClass == void.class) return null;
                try {
                    return (T) primitiveWrapperClass.getMethod("valueOf", String.class).invoke(null, valueStr);
                } catch (Exception e) {
                    return null;
                }
            } else if (Enum.class.isAssignableFrom(_tClass)) {
                return (T) Enum.valueOf((Class) _tClass, valueStr);
            } else if (CharSequence.class.isAssignableFrom(_tClass)) {
                return (T) valueStr;
            } else if (Number.class.isAssignableFrom(_tClass)) {
                try {
                    //尝试通过构造函数创建
                    Constructor<T> constructor = _tClass.getConstructor(String.class);
                    return constructor.newInstance(valueStr);
                } catch (Exception e) {
                    try {
                        //尝试通过valueOf方法创建
                        return (T) _tClass.getMethod("valueOf", String.class).invoke(null, valueStr);
                    } catch (Exception e1) {
                    }
                }
                //其余情况返回null
                return null;
            } else if (Date.class.isAssignableFrom(_tClass)) {
                return (T) DateUtil.parse(valueStr, DATE_FORMAT);
            } else if (LocalDateTime.class.isAssignableFrom(_tClass)) {
                return (T) DateUtil.parseLocalDateTime(valueStr, DATE_FORMAT);
            }
        }

        return JsonUtils.toBean(valueStr, tClass);
    }


}
