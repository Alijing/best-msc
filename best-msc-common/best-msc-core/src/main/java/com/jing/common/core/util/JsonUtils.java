package com.jing.common.core.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.*;
import com.alibaba.fastjson.util.ParameterizedTypeImpl;
import com.jing.common.core.serialize.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author : jing
 * @since : 2024/7/3 11:44
 */
public class JsonUtils {


    private static Logger logger = LoggerFactory.getLogger(JsonUtils.class);

    //自定义序列化对象
    private static SerializeConfig config = SerializeConfig.globalInstance;

    static {
        //统一处理日期格式
        //自定义日期格式需要使用：@JSONField(format = "yyyy-MM-dd HH:mm:ss")
        config.put(Date.class, new JsonDateSerializer());
        config.put(java.sql.Date.class, new JsonDateSerializer());
        config.put(java.sql.Timestamp.class, new JsonDateSerializer());
        config.put(LocalDateTime.class, new JsonDateSerializer());
        //long型转换字符串，解决数据库19位长度，前端js仅支持15位的问题
        config.put(Long.class, new JsonLongSerializer());
        config.put(Long.TYPE, new JsonLongSerializer());
        // BigInteger型转换字符串，解决数据库19位长度，前端js仅支持15位的问题
        config.put(BigDecimal.class, BigDecimalCodec.instance);
        // BigInteger型转换字符串，解决数据库19位长度，前端js仅支持15位的问题
        config.put(BigInteger.class, BigIntegerCodec.instance);
    }

    /**
     * 将long的list转换成mongodb的类型
     *
     * @param list
     * @return
     */
    public static String mongo4listToNumberLongJson(List<Long> list) {
        StringBuffer json = new StringBuffer("[");
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                Long one = list.get(i);
                if (i == 0) {
                    json.append("NumberLong('" + one.longValue() + "')");
                } else {
                    json.append(",NumberLong('" + one.longValue() + "')");
                }
            }
        }
        json.append("]");
        return json.toString();
    }

    /**
     * 将json转化为bean，返回结果T跟参数clazz指定的类型相同
     *
     * @param json  json字符串
     * @param clazz 返回结果类型
     * @return
     */
    public static <T> T toBean(String json, Type clazz) {
        return toBean(json, clazz, null);
    }

    /**
     * 将json转化为bean
     *
     * @param json  json字符串
     * @param clazz 返回结果类型
     * @return
     */
    public static <T> T toBean(String json, Class<T> clazz) {
        return toBean(json, clazz, null);
    }

    public static <T> T toBean(String json, Class<T> clazz, final Map<String, Class<?>> classMapping) {
        if (json == null || clazz == null) {
            return null;
        }
        if (clazz == String.class) {
            return (T) json;
        }
        try {
            return JSON.parseObject(json, clazz, new JsonObjectTypeProcessor(classMapping));
        } catch (Exception e) {
            logger.error("", e);
            return null;
        }

    }

    /**
     * json串转换对象，返回结果T跟参数clazz指定的类型相同
     *
     * @param json         json字符串
     * @param clazz        返回结果类型
     * @param classMapping 类转换映射关系
     * @param <T>          结果泛型
     * @return
     */
    public static <T> T toBean(String json, Type clazz, final Map<String, Class<?>> classMapping) {
        if (json == null || clazz == null) {
            return null;
        }
        if (clazz == String.class) {
            return (T) json;
        }
        try {
            return JSON.parseObject(json, clazz, new JsonObjectTypeProcessor(classMapping));
        } catch (Exception e) {
            logger.error("", e);
            return null;
        }

    }

    /**
     * 转换json对象，默认指定了SerializerFeature.WriteMapNullValue特性，输出null字段
     *
     * @param source 数据源对象
     * @return
     */
    public static String toJson(Object source) {
        if (source == null) {
            return null;
        }
        return JSON.toJSONString(source, config, new SerializeFilter[]{new JsonLongArrayFilter()}, SerializerFeature.WriteMapNullValue);
    }

    /**
     * 转换json对象，并过滤指定属性，默认指定了SerializerFeature.WriteMapNullValue特性，输出null字段
     *
     * @param source           数据源对象
     * @param filterProperties 过滤属性数组
     * @return
     */
    public static String toJson(Object source, String... filterProperties) {
        if (source == null) {
            return null;
        }
        SerializeFilter[] filters = {new JsonPropertyFilter(filterProperties), new JsonLongArrayFilter()};
        return JSON.toJSONString(source, config, filters, SerializerFeature.WriteMapNullValue);
    }

    /**
     * 转换json对象，并指定序列化特性
     *
     * @param source   数据源对象
     * @param features 序列化特性配置
     * @return
     */
    public static String toJson(Object source, SerializerFeature... features) {
        if (source == null) {
            return null;
        }
        //特性对象数组或者特性对象都不能为null
        if (features.length == 0 || Arrays.stream(features).anyMatch(feature -> feature == null)) {
            return JSON.toJSONString(source, config, new SerializeFilter[]{new JsonLongArrayFilter()});
        }
        return JSON.toJSONString(source, config, new SerializeFilter[]{new JsonLongArrayFilter()}, features);
    }

    /**
     * json字符串转换为List<Object>集合
     *
     * @param jsonStr json字符串
     * @return
     */
    public static Map<String, Object> toMap(String jsonStr) {
        return toBean(jsonStr, Map.class);
    }

    /**
     * json字符串转换为List<Object>集合
     *
     * @param jsonStr json字符串
     * @return
     */
    public static List<Object> toList(String jsonStr) {
        return toList(jsonStr, null);
    }

    /**
     * json字符串转换为List<elementClass>集合
     *
     * @param jsonStr      json字符串
     * @param elementClass 集合元素对象类型
     * @return
     */
    public static <T> List<T> toList(String jsonStr, Class<T> elementClass) {
        return toList(jsonStr, (Type) elementClass);
    }

    /**
     * json字符串转换为List<elementClass>集合
     *
     * @param jsonStr      json字符串
     * @param elementClass 集合元素对象类型
     * @return
     */
    public static <T> List<T> toList(String jsonStr, Type elementClass) {
        if (jsonStr == null) {
            return null;
        }
        Type listType = new ParameterizedTypeImpl(new Type[]{elementClass}, null, List.class);
        return JsonUtils.toBean(jsonStr, listType);
    }

    /**
     * 格式化Json
     *
     * @param jsonStr json字符串
     * @return
     */
    public static String formatJson(String jsonStr) {
        if (null == jsonStr || "".equals(jsonStr)) {
            return "";
        }
        jsonStr = jsonStr.replace(" ", "");
        StringBuilder sb = new StringBuilder();
        char last = '\0';
        char current = '\0';
        int indent = 0;
        boolean isInQuotationMarks = false;
        for (int i = 0; i < jsonStr.length(); i++) {
            last = current;
            current = jsonStr.charAt(i);
            switch (current) {
                case '"':
                    if (last != '\\') {
                        isInQuotationMarks = !isInQuotationMarks;
                    }
                    sb.append(current);
                    break;
                case '{':
                case '[':
                    sb.append(current);
                    if (!isInQuotationMarks) {
                        sb.append('\n');
                        indent++;
                        addIndentBlank(sb, indent);
                    }
                    break;
                case '}':
                case ']':
                    if (!isInQuotationMarks) {
                        sb.append('\n');
                        indent--;
                        addIndentBlank(sb, indent);
                    }
                    sb.append(current);
                    break;
                case ',':
                    sb.append(current);
                    if (last != '\\' && !isInQuotationMarks) {
                        sb.append('\n');
                        addIndentBlank(sb, indent);
                    }
                    break;
                default:
                    sb.append(current);
            }
        }

        return sb.toString();
    }

    /**
     * 添加space
     *
     * @param sb
     * @param indent
     */
    private static void addIndentBlank(StringBuilder sb, int indent) {
        for (int i = 0; i < indent; i++) {
            sb.append("  ");
        }
    }


}
