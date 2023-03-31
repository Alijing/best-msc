package com.jing.common.swagger.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.*;
import com.jing.common.swagger.serialize.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.*;

/**
 * JSON转换工具类
 *
 * @author : jing
 * @projectName : best-msc
 * @packageName : com.jing.common.swagger.util
 * @date : 2023/3/31 12:40
 * @description :
 */
public class JSONUtil {

    private static Logger logger = LoggerFactory.getLogger(JSONUtil.class);

    //自定义序列化对象
    private static SerializeConfig config = SerializeConfig.globalInstance;

    static {
        // 统一处理日期格式
        config.put(Date.class, new JsonDateSerializer());
        config.put(java.sql.Date.class, new JsonDateSerializer());
        config.put(java.sql.Timestamp.class, new JsonDateSerializer());
        config.put(LocalDateTime.class, new JsonDateSerializer());
        // long型转换字符串，解决数据库19位长度，前端js仅支持15位的问题
        config.put(Long.class, JsonLongSerializer.instance);
        config.put(Long.TYPE, JsonLongSerializer.instance);
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
        try {
            return JSON.parseObject(json, clazz, new JsonObjectTypeProcessor(classMapping));
        } catch (Exception e) {
            logger.error("", e);
            return null;
        }

    }

    /**
     * 转换json对象
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
     * 转换json对象，并过滤指定属性
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
     * json字符串转换为List<Object>集合
     *
     * @param jsonStr json字符串
     * @return
     */
    public static List<Object> parseJson2List(String jsonStr) {
        return parseJson2List(jsonStr, null);
    }

    /**
     * json字符串转换为List<clazz>集合
     *
     * @param jsonStr json字符串
     * @param clazz   集中和对象类型
     * @return
     */
    public static <T> List<T> parseJson2List(String jsonStr, Class<T> clazz) {
        if (jsonStr == null) {
            return null;
        }
        List<T> json = JSONArray.parseArray(jsonStr, clazz);
        return json;
    }

    /**
     * json字符串转Map
     *
     * @param jsonStr json字符串
     * @return
     */
    public static Map<String, Object> parseJson2Map(String jsonStr) {
        if (StringUtils.isEmpty(jsonStr)) {
            return null;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        // 最外层解析
        JSONObject json = JSONObject.parseObject(jsonStr);
        for (Object k : json.keySet()) {
            Object v = json.get(k);
            // 如果内层还是数组的话，继续解析
            if (v instanceof JSONArray) {
                List<Object> list = new ArrayList<Object>();
                Iterator<Object> it = ((JSONArray) v).iterator();
                while (it.hasNext()) {
                    Object _obj = it.next();
                    if (_obj instanceof JSONObject) {
                        list.add(parseJson2Map(((JSONObject) _obj).toString()));
                    } else if (_obj instanceof JSONArray) {

                    } else {
                        list.add(_obj);
                    }
                }
                map.put(k.toString(), list);
            } else {
                if (v instanceof JSONObject) {
                    if (((JSONObject) v).isEmpty()) {
                        map.put(k.toString(), null);
                        continue;
                    }
                }
                map.put(k.toString(), v);
            }
        }

        return map;
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

    @SuppressWarnings({"rawtypes", "unused"})
    public static void main(String[] args) {

        System.out.println("转换空对象" + (toJson(null) == null));
//		toBean("sdf",Map.class);

        //        TestPerson json = new TestPerson(19,"李明");
        //        json.setCreateDate(new Date());
        //        json.setUpdateDate(new java.sql.Date(System.currentTimeMillis()));
        //        json.setXxDate(new java.sql.Timestamp(System.currentTimeMillis()));
        //
        ////        System.out.println(JSON.toJSONString(json, true));
        ////        System.out.println(formatJson(JSON.toJSONString(json)));
        //        List<TestPerson> list = new ArrayList<TestPerson>();
        //        list.add(json);
        //        list.add(new TestPerson(12,"张三"));
        //        //将集合或者对象序例化成JSON
        //        System.out.println("JavaBean转JSON: " + toJson(json));
        //        System.out.println("JavaBean转JSON(过滤xxDate): " + toJson(json,"xxDate"));
        //        System.out.println(JSONUtil.toJson(json,"xxDate"));
        //        System.out.println("list<JavaBean>转JSON: " + JSON.toJSON(list) );
        //        //Json串反序列化成对象
        //        TestPerson person = JSON.parseObject("{\"name\":\"李明\",\"age\":19}", TestPerson.class);
        //        System.out.printf("JSON转JavaBean name:%s,age:%d\n",person.getName(),person.getAge());
        //
        //        String str = "[{\"name\":\"李明\",\"age\":19},{\"name\":\"张三\",\"age\":12}]";
        //        //数组对象反序列化成集合
        //        List<TestPerson> listPerson = JSON.parseArray(str,TestPerson.class);
        //
        //        for(TestPerson item : listPerson){
        //            System.out.println("JSON转list<JavaBean>:" + item.getName());
        //            System.out.println("JSON转list<JavaBean>:" + item.getAge());
        //        }
        //
        //        //没有对象直接解析JSON对象
        //        JSONObject jobj = JSON.parseObject("{\"name\":\"李明\",\"age\":19}");
        //        System.out.printf("JSON转JSONObject name:%s,age:%d\n",jobj.getString("name"),jobj.getBigInteger("age"));
        //
        String arrStr = "[{\"name\":\"李明\",\"age\":19},{\"name\":\"张三\",\"age\":12}]";
        //        //没有对象直接解析JSON数组
        //        JSONArray jarr = JSON.parseArray(arrStr);
        //
        //        for(int i=0,len=jarr.size();i<len;i++){
        //            JSONObject temp =  jarr.getJSONObject(i);
        //            System.out.printf("JSON转JSONArray name:%s,age:%d\n",temp.getString("name"),temp.getBigInteger("age"));
        //        }
        //
        //        for(Object obj:jarr){
        //            System.out.println("JSON转JSONArray: " + obj.toString());
        //        }
        //
//		        Map<String, Class<?>> classMapping = new HashMap<String, Class<?>>();
//				classMapping.put("interest", Set.class);
//				classMapping.put("ss", Map.class);
//
//				List _list = toBean(arrStr,List.class,classMapping);
//		        System.out.println("toBean JSON转JSONArray: " + _list);
//		        List _list1 = parseJson2List(arrStr,Map.class);
//		        System.out.println("toBean JSON转JSONArray: " + _list);
//
//		        String jsonStr = "{\"name\":\"李明\",\"age\":19,\"interest\":[\"game\",\"play\",{\"xx\":12}],\"ss\":{\"ee\":1}}";
//		        Map<String,Object> map = parseJson2Map(jsonStr);
//		        System.out.println("parseJson2Map: " + map);
//
//
//
//				JSONObject jsonObj = JSON.parseObject(jsonStr);
//
//				Object m = toBean(jsonStr,Map.class,classMapping);
//
//
//				String fuzaJson = "{\"TYPE\":0,\"unitId\":4,\"aprvUserId\":2,\"instructorId\":4,\"month\":1,\"week\":1,\"listInfo\":[[{\"weekDay\":\"一\",\"planDate\":\"2019-01-07\",\"keshi\":\"早晨\",\"times\":\"13\",\"content\":\"123\",\"way\":\"1234\",\"coaches\":[2,3],\"place\":\"ffas\",\"safetyPrecautions\":\"\"},{\"weekDay\":\"一\",\"planDate\":\"2019-01-07\",\"keshi\":\"上午\",\"times\":\"333\",\"content\":\"444\",\"way\":\"1122\",\"coaches\":[3,4],\"place\":\"das\",\"safetyPrecautions\":\"\"},{\"weekDay\":\"一\",\"planDate\":\"2019-01-07\",\"keshi\":\"下午\",\"times\":\"\",\"content\":\"\",\"way\":\"\",\"coaches\":[],\"place\":\"\",\"safetyPrecautions\":\"\"},{\"weekDay\":\"一\",\"planDate\":\"2019-01-07\",\"keshi\":\"晚上\",\"times\":\"\",\"content\":\"\",\"way\":\"\",\"coaches\":[],\"place\":\"\",\"safetyPrecautions\":\"\"}],[{\"weekDay\":\"二\",\"planDate\":\"2019-01-08\",\"keshi\":\"早晨\",\"times\":\"\",\"content\":\"\",\"way\":\"\",\"coaches\":[],\"place\":\"\",\"safetyPrecautions\":\"\"},{\"weekDay\":\"二\",\"planDate\":\"2019-01-08\",\"keshi\":\"上午\",\"times\":\"\",\"content\":\"\",\"way\":\"\",\"coaches\":[],\"place\":\"\",\"safetyPrecautions\":\"\"},{\"weekDay\":\"二\",\"planDate\":\"2019-01-08\",\"keshi\":\"下午\",\"times\":\"\",\"content\":\"\",\"way\":\"\",\"coaches\":[],\"place\":\"\",\"safetyPrecautions\":\"\"},{\"weekDay\":\"二\",\"planDate\":\"2019-01-08\",\"keshi\":\"晚上\",\"times\":\"\",\"content\":\"\",\"way\":\"\",\"coaches\":[],\"place\":\"\",\"safetyPrecautions\":\"\"}],[{\"weekDay\":\"三\",\"planDate\":\"2019-01-09\",\"keshi\":\"早晨\",\"times\":\"\",\"content\":\"\",\"way\":\"\",\"coaches\":[],\"place\":\"\",\"safetyPrecautions\":\"\"},{\"weekDay\":\"三\",\"planDate\":\"2019-01-09\",\"keshi\":\"上午\",\"times\":\"\",\"content\":\"\",\"way\":\"\",\"coaches\":[],\"place\":\"\",\"safetyPrecautions\":\"\"},{\"weekDay\":\"三\",\"planDate\":\"2019-01-09\",\"keshi\":\"下午\",\"times\":\"\",\"content\":\"\",\"way\":\"\",\"coaches\":[],\"place\":\"\",\"safetyPrecautions\":\"\"},{\"weekDay\":\"三\",\"planDate\":\"2019-01-09\",\"keshi\":\"晚上\",\"times\":\"\",\"content\":\"\",\"way\":\"\",\"coaches\":[],\"place\":\"\",\"safetyPrecautions\":\"\"}],[{\"weekDay\":\"四\",\"planDate\":\"2019-01-10\",\"keshi\":\"早晨\",\"times\":\"\",\"content\":\"\",\"way\":\"\",\"coaches\":[],\"place\":\"\",\"safetyPrecautions\":\"\"},{\"weekDay\":\"四\",\"planDate\":\"2019-01-10\",\"keshi\":\"上午\",\"times\":\"\",\"content\":\"\",\"way\":\"\",\"coaches\":[],\"place\":\"\",\"safetyPrecautions\":\"\"},{\"weekDay\":\"四\",\"planDate\":\"2019-01-10\",\"keshi\":\"下午\",\"times\":\"\",\"content\":\"\",\"way\":\"\",\"coaches\":[],\"place\":\"\",\"safetyPrecautions\":\"\"},{\"weekDay\":\"四\",\"planDate\":\"2019-01-10\",\"keshi\":\"晚上\",\"times\":\"\",\"content\":\"\",\"way\":\"\",\"coaches\":[],\"place\":\"\",\"safetyPrecautions\":\"\"}],[{\"weekDay\":\"五\",\"planDate\":\"2019-01-11\",\"keshi\":\"早晨\",\"times\":\"\",\"content\":\"\",\"way\":\"\",\"coaches\":[],\"place\":\"\",\"safetyPrecautions\":\"\"},{\"weekDay\":\"五\",\"planDate\":\"2019-01-11\",\"keshi\":\"上午\",\"times\":\"\",\"content\":\"\",\"way\":\"\",\"coaches\":[],\"place\":\"\",\"safetyPrecautions\":\"\"},{\"weekDay\":\"五\",\"planDate\":\"2019-01-11\",\"keshi\":\"下午\",\"times\":\"\",\"content\":\"\",\"way\":\"\",\"coaches\":[],\"place\":\"\",\"safetyPrecautions\":\"\"},{\"weekDay\":\"五\",\"planDate\":\"2019-01-11\",\"keshi\":\"晚上\",\"times\":\"\",\"content\":\"\",\"way\":\"\",\"coaches\":[],\"place\":\"\",\"safetyPrecautions\":\"\"}],[{\"weekDay\":\"六\",\"planDate\":\"2019-01-12\",\"keshi\":\"早晨\",\"times\":\"\",\"content\":\"\",\"way\":\"\",\"coaches\":[],\"place\":\"\",\"safetyPrecautions\":\"\"},{\"weekDay\":\"六\",\"planDate\":\"2019-01-12\",\"keshi\":\"上午\",\"times\":\"\",\"content\":\"\",\"way\":\"\",\"coaches\":[],\"place\":\"\",\"safetyPrecautions\":\"\"},{\"weekDay\":\"六\",\"planDate\":\"2019-01-12\",\"keshi\":\"下午\",\"times\":\"\",\"content\":\"\",\"way\":\"\",\"coaches\":[],\"place\":\"\",\"safetyPrecautions\":\"\"},{\"weekDay\":\"六\",\"planDate\":\"2019-01-12\",\"keshi\":\"晚上\",\"times\":\"\",\"content\":\"\",\"way\":\"\",\"coaches\":[],\"place\":\"\",\"safetyPrecautions\":\"\"}],[{\"weekDay\":\"日\",\"planDate\":\"2019-01-13\",\"keshi\":\"早晨\",\"times\":\"\",\"content\":\"\",\"way\":\"\",\"coaches\":[],\"place\":\"\",\"safetyPrecautions\":\"\"},{\"weekDay\":\"日\",\"planDate\":\"2019-01-13\",\"keshi\":\"上午\",\"times\":\"\",\"content\":\"\",\"way\":\"\",\"coaches\":[],\"place\":\"\",\"safetyPrecautions\":\"\"},{\"weekDay\":\"日\",\"planDate\":\"2019-01-13\",\"keshi\":\"下午\",\"times\":\"\",\"content\":\"\",\"way\":\"\",\"coaches\":[],\"place\":\"\",\"safetyPrecautions\":\"\"},{\"weekDay\":\"日\",\"planDate\":\"2019-01-13\",\"keshi\":\"晚上\",\"times\":\"\",\"content\":\"\",\"way\":\"\",\"coaches\":[],\"place\":\"\",\"safetyPrecautions\":\"\"}]],\"fid\":\"resource.integratedbis.system.matterdiary.WeekWork\",\"opt\":\"if4UpdatePlan\",\"lang\":\"zh_CN\"}";
//				Map<String,Object> map1 = parseJson2Map(fuzaJson);
//				System.out.println("parseJson2Map: " + map1);
//				Map<String,Object> map2 = toBean(fuzaJson,Map.class);
//				System.out.println("parseJson2Map: " + map2);

        Map<String, Object> testNull = new HashMap<String, Object>();
        testNull.put("a", 123);
        testNull.put("a1", new Integer(123));
        testNull.put("b", null);
        testNull.put("d", new Date());
        testNull.put("l1", 123456789l);//
        testNull.put("l2", 100000000000000000l);//long转字符串
        testNull.put("generatedKeys", new Long(84404742401359879l));
        testNull.put("generatedKeys2", 84404742401359879l);
        Long[] value = {null, 84404742401359879l, 8440474240l};
        testNull.put("generatedKeys3", value);
        long[] _value = {84404742401359879l, 8440474240l};
        testNull.put("generatedKeys4", _value);
        List<long[]> _value2 = new ArrayList<>();
        _value2.add(_value);
        testNull.put("generatedKeys6", _value2);
        JSONArray keys = new JSONArray();
        keys.add(710701071604030204l);
        testNull.put("generatedKeys5", keys);
        System.out.println(toJson(testNull));
        System.out.println(toJson(_value));

//        String ss = "{\"emsg\":\"人员信息加载失败\",\"data\":[{\"birthday\":\"2021-03-17 00:00:00\",\"sex\":0,\"political\":4,\"inOrganization\":0,\"persontype\":1,\"unit\":[1,3,4,7,710701071604030204],\"phone\":\"18890908765\",\"name\":\"测试人员fff\",\"idCardNum\":\"510123199802038765\",\"rank\":19,\"id\":1,\"attr\":0,\"account\":1}],\"success\":true,\"ecode\":0}";
        String ss = "{\"emsg\":\"人员信息加载失败\",\"data\":[{\"birthday\":\"2021-03-17 00:00:00\",\"sex\":0,\"political\":4,\"inOrganization\":0,\"persontype\":1,\"unit\":[[1,3],[1,3,4,7,710701071604030204]],\"phone\":\"18890908765\",\"name\":\"测试人员fff\",\"idCardNum\":\"510123199802038765\",\"rank\":19,\"id\":1,\"attr\":0,\"account\":1}],\"success\":true,\"ecode\":0}";
//        String ss = "{\"emsg\":\"人员信息加载失败\",\"data\":[{\"birthday\":\"2021-03-17 00:00:00\",\"sex\":0,\"political\":4,\"inOrganization\":0,\"persontype\":1,\"unit\":[{\"ff\":[1,3,4,7,710701071604030204]}],\"phone\":\"18890908765\",\"name\":\"测试人员fff\",\"idCardNum\":\"510123199802038765\",\"rank\":19,\"id\":1,\"attr\":0,\"account\":1}],\"success\":true,\"ecode\":0}";
//        Object test = parseJson2Map(ss);
        Object test = toBean(ss, Map.class);
        List<JSONArray> units = new ArrayList<>();
        JSONArray jsonArray = new JSONArray();
        jsonArray.add(710701071604030204l);
        units.add(jsonArray);
        System.out.println(toJson(jsonArray));
        System.out.println(toJson(test));


        String longTest = "{\"data\":[{\"calendar\":\"工作日历\",\"dispatchDate\":\"2021-09-01至2021-10-31\",\"shift\":\"测试全勤\",\"name\":\"考勤1\",\"rule\":\"xpc\",\"remark\":null,\"id\":9453145711141183,\"people\":1},{\"calendar\":\"工作日历\",\"dispatchDate\":\"2021-09-26至2021-09-30\",\"shift\":\"测试全勤\",\"name\":\"fegfrgfe\",\"rule\":\"xpc\",\"remark\":null,\"id\":9043938405441762,\"people\":1}],\"ecode\":0,\"emsg\":null,\"limit\":30,\"start\":0,\"success\":true,\"total\":2}";

        BigInteger bigInteger = new BigInteger(String.valueOf(9453145711141184l));
        System.out.println(bigInteger.longValue());
        Map longTestMap = toBean(longTest, Map.class);
        longTestMap.put("bigInteger", bigInteger);
        System.out.println(toJson(longTestMap));
    }


}
