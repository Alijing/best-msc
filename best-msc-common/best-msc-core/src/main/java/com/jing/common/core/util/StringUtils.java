package com.jing.common.core.util;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具，包含：驼峰和下划线格式互转，基于正则的字符串开头、结尾匹配，
 * 基于正则的掐头去尾，基于正则的包含，整型数字，指定索引的字符串截取，浮点型数字的判断，获取随机字符串等
 *
 * @author : jing
 * @since : 2025/3/6 18:06
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils {


    public static void main(String[] args) {
        System.out.println(toCamelCase("user_name"));
        System.out.println(toCamelCase("userName"));
        System.out.println(toPascalCase("user_name"));
        System.out.println(toPascalCase("userName"));
        System.out.println(toUnderlineCase("userName"));
        System.out.println(toCamelCase("wsBasePath"));
        System.out.println(StringUtils.join(new String[]{"33", "fd"}, ","));
        System.out.println(String.join(",", new String[]{"33", "fd"}));
        String sql = "REPLACE into xxxx";
        System.out.println(startsWith(sql.toUpperCase(), "INSERT |REPLACE "));

        System.out.println(substringAfter("/service-bus-security/a/b", "/"));
        System.out.println(substringAfterLast("/service-bus-security/a/b", "/"));
        System.out.println(substringAfter("/service-bus-security/a/b", "/", 2));
        System.out.println(substringStart("/service-bus-security/a/b", "/", 2));

        System.out.println(substringBefore("/service-bus-security/a/b", "/"));
        System.out.println(substringBeforeLast("/service-bus-security/a/b", "/"));
        System.out.println(ordinalIndexOf("/service-bus-security/a/b", "/", 2));

    }


    /**
     * 首字母大写的驼峰转换
     * user_name  ---->  UserName
     * userName   --->  UserName
     *
     * @param underlineStr 带有下划线的字符串，或者驼峰字符串
     * @return 驼峰字符串
     */
    public static String toPascalCase(String underlineStr) {
        String camelCase = toCamelCase(underlineStr);
        return Character.toUpperCase(camelCase.charAt(0)) + camelCase.substring(1);
    }

    /**
     * 下划线转驼峰
     * user_name  ---->  userName
     * userName   --->  userName
     *
     * @param underlineStr 带有下划线的字符串
     * @return 驼峰字符串
     */
    public static String toCamelCase(String underlineStr) {
        if (underlineStr == null) {
            return null;
        }
        // 分成数组
        char[] charArray = underlineStr.toCharArray();
        // 判断上次循环的字符是否是"_"
        boolean underlineBefore = false;
        StringBuffer buffer = new StringBuffer();
        for (int i = 0, l = charArray.length; i < l; i++) {
            // 判断当前字符是否是"_",如果跳出本次循环
            if (charArray[i] == 95) {
                underlineBefore = true;
            } else if (underlineBefore) {
                // 如果为true，代表上次的字符是"_",当前字符需要转成大写
                buffer.append(charArray[i] -= 32);
                underlineBefore = false;
            } else {
                // 不是"_"后的字符就直接追加
                buffer.append(charArray[i]);
            }
        }
        return buffer.toString();
    }

    /**
     * 驼峰转 下划线
     * userName  ---->  user_name
     * user_name  ---->  user_name
     *
     * @param camelCaseStr 驼峰字符串
     * @return 带下滑线的String
     */
    public static String toUnderlineCase(String camelCaseStr) {
        if (camelCaseStr == null) {
            return null;
        }
        // 将驼峰字符串转换成数组
        char[] charArray = camelCaseStr.toCharArray();
        StringBuffer buffer = new StringBuffer();
        //处理字符串
        for (int i = 0, l = charArray.length; i < l; i++) {
            if (charArray[i] >= 65 && charArray[i] <= 90) {
                buffer.append("_").append(charArray[i] += 32);
            } else {
                buffer.append(charArray[i]);
            }
        }
        return buffer.toString();
    }

    /**
     * 首字母转小写
     *
     * @param name 字符串
     * @return
     */
    public static String firstCharToLowerCase(String name) {
        if (isEmpty(name)) {
            return name;
        }
        return String.valueOf(name.charAt(0)).toLowerCase() + name.substring(1);
    }

    /**
     * 判断字符串是否以正则表达式规则开始，区分大小写
     *
     * @param message 待检索的字符串
     * @param exp     正则表达式，不含开始符^
     * @return
     */
    public static boolean startsWith(String message, String exp) {
        return startsWith(message, exp, false);
    }

    /**
     * 判断字符串是否以正则表达式规则开始
     *
     * @param message         待检索的字符串
     * @param exp             正则表达式，不含开始符^
     * @param caseInsensitive 是否忽略大小写
     * @return
     */
    public static boolean startsWith(String message, String exp, boolean caseInsensitive) {
        Pattern pattern = null;
        if (caseInsensitive) {//忽略大小写
            pattern = Pattern.compile("^(" + exp + ")[\\s\\S]*$", Pattern.CASE_INSENSITIVE);
        } else {
            pattern = Pattern.compile("^(" + exp + ")[\\s\\S]*$");
        }
        Matcher matcher = pattern.matcher(message);
        return matcher.matches();
    }

    /**
     * 判断字符串是否以正则表达式规则结束，区分大小写
     *
     * @param message 待检索的字符串
     * @param exp     正则表达式，不含结束符$
     * @return
     */
    public static boolean endsWith(String message, String exp) {
        return endsWith(message, exp, false);
    }

    /**
     * 判断字符串是否以正则表达式规则结束
     *
     * @param message         待检索的字符串
     * @param exp             正则表达式，不含结束符$
     * @param caseInsensitive 是否忽略大小写
     * @return
     */
    public static boolean endsWith(String message, String exp, boolean caseInsensitive) {
        Pattern pattern = null;
        if (caseInsensitive) {//忽略大小写
            pattern = Pattern.compile("^[\\s\\S]*(" + exp + ")$", Pattern.CASE_INSENSITIVE);
        } else {
            pattern = Pattern.compile("^[\\s\\S]*(" + exp + ")$");
        }
        Matcher matcher = pattern.matcher(message);
        return matcher.matches();
    }

    /**
     * 移除字符串中匹配正则表达式的头部，忽略大小写
     *
     * @param message 待检索的字符串
     * @param exp     正则表达式，不含开始符^
     * @return
     */
    public static String stripStart(String message, String exp) {
        return message.replaceFirst("^(?i)(" + exp + ")", "");
    }

    /**
     * 移除字符串中匹配正则表达式的头部，忽略大小写
     *
     * @param message 待检索的字符串
     * @param exp     正则表达式，不含结束符$
     * @return
     */
    public static String stripEnd(String message, String exp) {
        return message.replaceFirst("(?i)(" + exp + ")$", "");
    }


    /**
     * 判断某个字符串是否包含正则表达式
     *
     * @param message 待检查字符串
     * @param exp     正则表达式
     * @return
     * @throws IllegalArgumentException message和exp为空或空串将抛出异常
     */
    public static boolean contains(String message, String exp) throws IllegalArgumentException {
        if (StringUtils.isBlank(message)) {
            throw new IllegalArgumentException("字符串不能为null或者空串");
        }
        if (StringUtils.isBlank(exp)) {
            throw new IllegalArgumentException("表达式不能为null或者空串");
        }
        //Pattern.DOTALL模式,保证'.'可以匹配所有的字符串，包括结束符和换行符
        Pattern pattern = Pattern.compile(exp, Pattern.DOTALL);
        Matcher matcher = pattern.matcher(message);
        return matcher.find();
    }

    /**
     * <p>Gets the substring after the first occurrence of a separator.
     * The separator is not returned.</p>
     *
     * <p>A <code>null</code> string input will return <code>null</code>.
     * An empty ("") string input will return the empty string.
     * A <code>null</code> separator will return the empty string if the
     * input string is not <code>null</code>.</p>
     *
     * <p>If nothing is found, the empty string is returned.</p>
     *
     * <pre>
     * StringUtils.substringAfter(null, *)      = null
     * StringUtils.substringAfter("", *)        = ""
     * StringUtils.substringAfter(*, null)      = ""
     * StringUtils.substringAfter("abc", "a")   = "bc"
     * StringUtils.substringAfter("abcba", "b") = "cba"
     * StringUtils.substringAfter("abc", "c")   = ""
     * StringUtils.substringAfter("abc", "d")   = ""
     * StringUtils.substringAfter("abc", "")    = "abc"
     * </pre>
     *
     * @param str       the String to get a substring from, may be null
     * @param separator the String to search for, may be null
     * @param ordinal   指定索引的子序列，从1开始
     * @return the substring after the first occurrence of the separator,
     * <code>null</code> if null String input
     * @since 2.0
     */
    public static String substringAfter(String str, String separator, int ordinal) {
        if (isEmpty(str)) {
            return str;
        }
        if (separator == null) {
            return EMPTY;
        }
        int pos = ordinalIndexOf(str, separator, ordinal);
        if (pos == INDEX_NOT_FOUND) {
            return EMPTY;
        }
        return str.substring(pos + separator.length());
    }

    /**
     * str从第ordinal个分隔符separator的开始位置向后截取字符串
     *
     * @param str       待截取的字符串
     * @param separator 截取分隔符
     * @param ordinal   截取分隔符的个数，从1开始
     * @return
     */
    public static String substringStart(String str, String separator, int ordinal) {
        if (isEmpty(str)) {
            return str;
        }
        if (separator == null) {
            return EMPTY;
        }
        int pos = ordinalIndexOf(str, separator, ordinal);
        if (pos == INDEX_NOT_FOUND) {
            return EMPTY;
        }
        return str.substring(pos);
    }

    /**
     * 字符串是否为整型数字
     *
     * @param str 待判断的文本
     * @return
     */
    public static boolean isIntegerOrLong(String str) {
        return RegUtils.match("^[\\-0-9][0-9]*$", str, true);
    }

    /**
     * 是否为有效的数字，含小数
     *
     * @param str 待判断的文本
     * @return
     */
    public static boolean isNumber(String str) {
        if (str == null) {
            return false;
        }
        try {
            Double.parseDouble(str);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 获取指定位数的随机数
     *
     * @param digit 位数
     * @return
     */
    public static String getRandomString(int digit) {
        String sources = "0123456789abzdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random rand = new Random();
        StringBuffer flag = new StringBuffer();
        for (int j = 0; j < digit; j++) {
            flag.append(sources.charAt(rand.nextInt(sources.length())) + "");
        }

        return flag.toString();
    }
}
