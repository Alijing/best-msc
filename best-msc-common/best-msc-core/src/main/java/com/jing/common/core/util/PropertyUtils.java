package com.jing.common.core.util;



import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * 属性获取和设置工具类，可支持Map以及JavaBean的属性获取与设置，如果不确定对象是什么类型，可使用此工具类进行操作
 * @author : jing
 * @since : 2025/3/6 17:52
 */
public class PropertyUtils {


    /**
     * List下标脚注正则，如colstr[colstr.size()-1][0]中的[colstr.size()-1]和[0]部分
     */
    private static final String LIST_SUBSCRIPT_EXP = "\\[([\\s\\S]+?)\\]";

    /**
     * 使用.分割的查询属性名分段正则
     */
    private static final String PROPERTY_SEGMENT_EXP = "(\\w+(?:\\[[\\s\\S]+?\\])*)\\.?";

    /**
     * 向普通Java bean Object或者Map 中设置属性
     *
     * @param bean  需要设置属性的原始对象，可以为Java bean Object或者Map
     * @param name  属性名
     * @param value 属性值
     * @return 返回赋值结果，如果bean为空或者找不到bean对象的name属性，将返回false
     */
    public static boolean setProperty(Object bean, String name, Object value) {
        if (bean == null) {
            return false;
        }
        if (bean instanceof Map) {
            ((Map) bean).put(name, value);
        } else {
            try {
                org.apache.commons.beanutils.PropertyUtils.setProperty(bean, name, value);
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }

    /**
     * 从普通Java bean Object或者Map 中获取属性，支持链式获取嵌套对象属性的表达式<br>
     * 表达式配置方法：<br>
     * 映射对象（Java bean对象/Map）：支持英文小数点“.”引用，<br>
     * 索引对象（数组/List集合）：支持英文中括号带数字和*的引用，数字表示索引对象元素，从0开始；*表示获取索引对象的全部元素；如 “[0]” “[*]”
     *
     * @param bean 用于提取属性的对象
     * @param name 属性名称，支持链式获取嵌套对象属性的表达式
     * @return 当未找到属性时返回null
     */
    public static Object getProperty(Object bean, String name) {
        if (Objects.isNull(name) || Objects.isNull(bean)) {
            return null;
        }

        List<String> propertySegments = RegUtils.findAllSubSequence(PROPERTY_SEGMENT_EXP, name, true);
        //保存每轮计算结果
        Object result = bean;
        for (int i = 0; i < propertySegments.size(); i++) {
            //当前路径参数临时变量
            String property = propertySegments.get(i);
            List<String> subscriptList = getSubscriptOfList(property);
            if (subscriptList.size() > 0) {
                property = RegUtils.findSubStrUntilExpByCount(property, LIST_SUBSCRIPT_EXP, 1, true);
            }

            //属性是否获取
            boolean propertyGot = false;
            if (isMapped(result)) {
                try {
                    result = getPropertyFromMappedObj(result, property);
                } catch (Exception e) {
                    result = null;
                }
                propertyGot = true;
                if (result == null) {//查找属性值为null，中断后续解析
                    return null;
                }

                if (isMapped(result)) {
                    if (subscriptList.size() > 0) {
                        throw new RuntimeException(property + " 解析结果 " + result + " 不是数组，不能通过下标 " + subscriptList + " 获取，配置错误！");
                    } else {//第一级参数获取到的类型为map和简单属性则为结果 或者 未指定下标，不做list遍历处理
                        continue;
                    }
                }
            }

            if (result != null && result.getClass().isArray()) {//数组转List
                List<Object> _resultList = new ArrayList();
                int length = Array.getLength(result);
                for (int j = 0; j < length; j++) {
                    Object value = Array.get(result, j);
                    _resultList.add(value);
                }
                result = _resultList;
            }

            //List解析
            if (result instanceof List) {
                List<Object> _resultList = (List<Object>) result;
                if (!propertyGot) {//属性未获取，则遍历集合提取属性
                    result = new ArrayList<>();
                    for (Object _result : _resultList) {
                        try {
                            Object propertyFromMappedObj = getPropertyFromMappedObj(_result, property);
                            ((List) result).add(propertyFromMappedObj);
                        } catch (Exception e) {
                            ((List) result).add(null);
                        }
                    }
                    _resultList = (List<Object>) result;
                }

                //集合下标处理
                if (subscriptList.size() > 0) {
                    result = getListElementBySubscript(_resultList, subscriptList, propertySegments.get(i), bean);
                }
            }
        }

        return result;
    }

    /**
     * 从普通Java bean Object或者Map 中获取属性
     *
     * @param bean         待提取属性的Java bean Object或者Map
     * @param propertyName 属性名或者Map的key
     * @return 属性值或者Map的value
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     */
    private static Object getPropertyFromMappedObj(Object bean, String propertyName) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        if (bean instanceof Map) {
            return ((Map<String, Object>) bean).get(propertyName);
        } else if (bean instanceof Map.Entry) {
            Map.Entry<?, ?> entry = (Map.Entry<?, ?>) bean;
            return entry.getKey() != null && entry.getKey().equals(propertyName) ? entry.getValue() : null;
        }

        try {
            //支持属性首字母大写等不规范的JavaBean对象，要求class必须有getter和setter方法
            PropertyDescriptor pd = new PropertyDescriptor(propertyName, bean.getClass());
            return pd.getReadMethod().invoke(bean);
        } catch (IntrospectionException e) {
            return org.apache.commons.beanutils.PropertyUtils.getProperty(bean, propertyName);
        }

    }

    /**
     * 判断Java bean Object或者Map 中是否包含指定的属性名
     *
     * @param bean         Java bean Object或者Map
     * @param propertyName 属性名
     * @return
     */
    public static boolean hasProperty(Object bean, String propertyName) {
        if (bean == null || StringUtils.isBlank(propertyName)) {
            throw new IllegalArgumentException("参数不能为空");
        }
        if (bean instanceof Map) {
            return ((Map<String, Object>) bean).containsKey(propertyName);
        } else if (bean instanceof Map.Entry) {
            Map.Entry<?, ?> entry = (Map.Entry<?, ?>) bean;
            return entry.getKey() != null && entry.getKey().equals(propertyName);
        }

        return ReflectUtils.containsField(bean.getClass(), propertyName);
    }

    /**
     * 检查bean对象是否可索引
     *
     * @param bean 待检查的对象
     * @return
     */
    private static boolean isIndexed(Object bean) {
        if (bean == null) {
            return false;
        }
        if (bean.getClass().isArray()) {
            return true;
        }
        if (bean instanceof List) {
            return true;
        }
        return false;
    }

    /**
     * 检查bean对象是否可映射，普通Java bean Object和Map
     *
     * @param bean 待检查的对象
     * @return
     */
    private static boolean isMapped(Object bean) {
        if (bean == null) {
            return false;
        }
        return !isIndexed(bean);
    }

    /**
     * 获取List下标脚注，如colstr[0]括号中的数字部分，返回 0
     *
     * @param property 解析字符串
     * @return 返回解析下标集合
     */
    public static List<String> getSubscriptOfList(String property) {
        return RegUtils.findFirstSubSequence(LIST_SUBSCRIPT_EXP, property, true);
    }

    /**
     * 从List中通过下标获取元素,支持多维数组
     *
     * @param resultList    List类型数据
     * @param subscriptList 多维数组下标
     * @param property      当前阶段解析属性名
     * @param bean          用于提取属性的顶级对象
     * @return
     */
    private static Object getListElementBySubscript(List<Object> resultList, List<String> subscriptList, String property, Object bean) {
        Object result = null;
        for (int i = 0; i < subscriptList.size(); i++) {
            String subscriptStr = subscriptList.get(i);
            int subscript = -1;
            //*表示取整个List
            if ("*".equals(subscriptStr)) {
                result = resultList;
                continue;
            } else if (StringUtils.isNumber(subscriptStr)) {
                subscript = Integer.parseInt(subscriptStr);
            } else {//计算表达式，通过脚本执行
                //subscript = (int) JexlUtils.executeExpression(subscriptStr, bean);
            }

            if (resultList.size() - 1 < subscript || subscript < 0) {
                throw new RuntimeException("查找字段内容 " + property + " 超出范围，实际结果集为：" + resultList);
            } else {
                result = resultList.get(subscript);
                if (result instanceof List) {
                    resultList = (List) result;
                } else if (i < subscriptList.size() - 1) {//中间出现非List对象，此时下标数组未遍历完，则表示多维下标配置错误
                    throw new RuntimeException(RegUtils.findSubStrUntilExpByCount(property, "\\[", i + 2, true)
                            + " 解析结果 " + result + " 不是数组，不能通过下标 " + subscriptList.get(i + 1) + " 获取，配置错误！");
                }
            }
        }
        return result;
    }

    /**
     * 使用properties的map属性映射填充bean对象，要求map的key和bean的属性名一致，并且map的value值类型必须和bean的属性类型一致
     *
     * @param bean       被填充的对象
     * @param properties 属性值取值map
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     */
    public static void populate(Object bean, Map<String, ? extends Object> properties) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        if (bean != null && properties != null) {
            Iterator var3 = properties.entrySet().iterator();

            while (var3.hasNext()) {
                Map.Entry<String, ? extends Object> entry = (Map.Entry) var3.next();
                String name = entry.getKey();
                if (name != null) {
                    org.apache.commons.beanutils.PropertyUtils.setProperty(bean, name, entry.getValue());
                }
            }

        }
    }


}
