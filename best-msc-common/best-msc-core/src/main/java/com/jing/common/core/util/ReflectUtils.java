package com.jing.common.core.util;

import org.apache.commons.lang3.StringUtils;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 反射工具类
 * @author : jing
 * @since : 2025/3/6 17:52
 */
public class ReflectUtils {



    /**
     * 获取类clazz的所有Field，包括其父类的Field，如果重名，以子类Field为准。
     *
     * @param clazz
     * @return Field数组
     */
    public static Field[] getAllField(Class<?> clazz) {
        List<Field> fieldList = new ArrayList();
        Field[] dFields = clazz.getDeclaredFields();

        if (null != dFields && dFields.length > 0) {
            fieldList.addAll(Arrays.asList(dFields));
        }

        Class<?> superClass = clazz.getSuperclass();
        if (superClass != Object.class) {
            Field[] superFields = getAllField(superClass);
            if (null != superFields && superFields.length > 0) {
                for (Field field : superFields) {
                    if (!isContain(fieldList, field)) {
                        fieldList.add(field);
                    }
                }
            }
        }

        Field[] result = new Field[fieldList.size()];
        fieldList.toArray(result);
        return result;
    }

    /**
     * 获取类clazz的指定名称的Field，包括其父类的Field，如果重名，以子类Field为准。
     *
     * @param clazz
     * @return Field
     */
    public static Field getFieldByName(Class<?> clazz, String fieldName) {
        Field[] dFields = clazz.getDeclaredFields();

        if (null != dFields && dFields.length > 0) {
            Optional<Field> first = Arrays.stream(dFields).filter(field -> field.getName().equals(fieldName)).findFirst();
            if (first.isPresent()) {
                return first.get();
            }
        }

        Class<?> superClass = clazz.getSuperclass();
        if (superClass != Object.class) {
            Field superField = getFieldByName(superClass, fieldName);
            if (superField != null) {
                return superField;
            }
        }

        return null;
    }

    /**
     * 根据Field名称获取bean的Field对应的值
     *
     * @param bean      bean对象
     * @param fieldName 字段名称
     * @return
     */
    public static Object getValueByFieldName(Object bean, String fieldName) {
        if (bean == null) {
            return null;
        }
        Field field = getFieldByName(bean.getClass(), fieldName);
        field.setAccessible(true);
        try {
            return field.get(bean);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("获取bean的Field对应的值失败！", e);
        }
    }

    /**
     * 设置bean的fieldName对应的字段属性值为fieldValue
     *
     * @param bean       bean对象
     * @param fieldName  字段名称
     * @param fieldValue 字段值
     * @return
     */
    public static void setValueByFieldName(Object bean, String fieldName, Object fieldValue) {
        if (bean == null) {
            throw new RuntimeException("用于反射设置实例属性值的bean不能为null！");
        }
        Field field = getFieldByName(bean.getClass(), fieldName);
        field.setAccessible(true);
        try {
            field.set(bean, fieldValue);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("获取bean的Field对应的值失败！", e);
        }
    }

    /**
     * 检测Field List中是否已经包含了目标field
     *
     * @param fieldList 字段集合
     * @param field     带检测field
     * @return
     */
    public static boolean isContain(List<Field> fieldList, Field field) {
        for (Field temp : fieldList) {
            if (temp.getName().equals(field.getName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检测Field List中是否已经包含了目标field
     *
     * @param fieldArray 字段集合
     * @param fieldName  待检测field名称
     * @return
     */
    public static boolean isContain(Field[] fieldArray, String fieldName) {
        for (Field temp : fieldArray) {
            if (temp.getName().equals(fieldName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取bean对象所有字段名，Map对象获取所有key，java bean获取所有属性名
     *
     * @param bean bean对象，支持Map或者Java bean
     * @return bean为null，则返回null
     */
    public static Collection<String> getAllFieldName(Object bean) {
        if (bean == null) {
            return null;
        }
        Collection<String> fieldNames = null;
        if (bean instanceof Map) {
            fieldNames = ((Map<String, Object>) bean).keySet();
        } else {//java bean
            fieldNames = Arrays.stream(ReflectUtils.getAllField(bean.getClass())).map(field -> field.getName()).collect(Collectors.toList());
        }

        return fieldNames;
    }

    /**
     * 判断类clazz的所有Field（包括其父类的Field）是否包含指定属性名称
     *
     * @param clazz     class类
     * @param fieldName 属性名称
     * @return
     */
    public static boolean containsField(Class<?> clazz, String fieldName) {
        boolean contains = false;
        Field[] dFields = clazz.getDeclaredFields();

        if (null != dFields && dFields.length > 0) {
            contains = isContain(dFields, fieldName);
        }

        if (!contains) {
            Class<?> superClass = clazz.getSuperclass();
            if (superClass != Object.class) {
                contains = containsField(superClass, fieldName);
            }
        }
        return contains;
    }

    /**
     * 反射构造实例，Map.class将构造HashMap，List.class将构造ArrayList
     *
     * @param tClass 构造实例的class
     * @param <T>    实例的类型
     * @return 实例对象
     */
    public static <T> T newInstance(Class<T> tClass) {
        if (tClass == null) {
            throw new RuntimeException("用于反射构造实例的class不能为null！");
        }
        try {
            if (tClass == Map.class) {
                tClass = (Class<T>) HashMap.class;
            }
            if (tClass == List.class) {
                tClass = (Class<T>) ArrayList.class;
            }
            return tClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(tClass + " 需要无参构造，用于反射构造实例！", e);
        }
    }

    /**
     * 根据className获取Class对象，className错误将返回null
     *
     * @param className 含包名的class名称
     * @return
     */
    public static Class getClass(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据类型，获取RawClass
     *
     * @param type 对象类型
     * @return
     */
    public static Class getRawClassByType(Type type) {
        if (type == null) {
            return null;
        }
        if (type instanceof Class) {
            return (Class<?>) type;
        }
        if (type instanceof ParameterizedType) {
            return (Class<?>) ((ParameterizedType) type).getRawType();
        }
        return null;
    }

    /**
     * 根据className获取指定的Type，className可包含泛型，例如：<br>
     * GenericT&lt;java.lang.String, java.util.Map&lt;java.lang.Object, java.util.List&lt;java.lang.Integer&gt;&gt;, java.lang.Long&gt;<br>
     * GenericT的class为：public class GenericT&lt;T,A,B&gt; { }
     *
     * @param className 类路径，可包含泛型
     * @return
     */
    public static Type buildTypeFromClassName(String className) {
        if (StringUtils.isBlank(className)) {
            return null;
        }
        className = className.trim();
        Type type = null;
        String _className = RegUtils.findSubStrUntilExpByCount(className, "<", 1, true);
        int i = className.indexOf("<");
        if (i >= 0) {
            String genericStr = className.substring(i + 1, className.length() - 1) + ",";
            List<String> genericClassNameList = RegUtils.findAllSubSequence("([\\s\\S]+?(?:<[\\s\\S]+?>)*?),", genericStr, true);
            List<Type> genericTypeList = new ArrayList<>();
            for (String genericClassName : genericClassNameList) {
                genericTypeList.add(buildTypeFromClassName(genericClassName));
            }
            type = ParameterizedTypeImpl.make(ReflectUtils.getClass(_className), genericTypeList.toArray(new Type[genericTypeList.size()]), null);
        } else {
            type = ReflectUtils.getClass(_className);
        }
        return type;
    }

}
