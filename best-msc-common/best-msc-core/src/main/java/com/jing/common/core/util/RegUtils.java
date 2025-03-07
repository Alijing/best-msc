package com.jing.common.core.util;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则表达式工具类，包含：获取正则模式对象，字符串的正则匹配、查找满足正则的子串、
 * 查找满足条件子串中的子序列()、
 *
 * @author : jing
 * @since : 2025/3/6 17:56
 */
public class RegUtils {


    /**
     * 用于缓存已经编译的正则表达式
     */
    private static final Map<String, Pattern> patterns = new HashMap<String, Pattern>();

    /**
     * 获取正则模式对象，如果cachePattern=true，将从缓存中获取，否则新建模式对象
     *
     * @param exp          正则表达式
     * @param cachePattern 是是否缓存通过exp创建的模式对象，如果频繁使用的正则表达式，可设置为true，降低创建模式对象的开销，提高性能；如果正则不常用，可不设置缓存
     * @return
     */
    public static final Pattern getPattern(String exp, boolean cachePattern) {
        Pattern pattern = null;
        if (cachePattern) {
            pattern = patterns.get(exp);
        }
        if (pattern == null) {
            pattern = Pattern.compile(exp, Pattern.DOTALL);//Pattern.DOTALL模式,保证'.'可以匹配所有的字符串，包括结束符和换行符
            if (cachePattern) {
                patterns.put(exp, pattern);
            }
        }
        return pattern;
    }

    /**
     * 判断某个字符串是否满足正则表达式；本方法不缓存正则模式对象，
     * 如果频繁使用的正则表达式，可调用方法{@link #match(String, String, boolean)}缓存正则模式对象，降低创建模式对象的开销
     *
     * @param exp     正则表达式
     * @param message 需要验证的字符串
     * @return
     */
    public static boolean match(String exp, String message) {
        return match(exp, message, false);
    }

    /**
     * 判断某个字符串是否满足正则表达式
     *
     * @param exp          正则表达式
     * @param message      需要验证的字符串
     * @param cachePattern 是否缓存通过exp创建的模式对象，如果频繁使用的正则表达式，可设置为true，降低创建模式对象的开销，提高性能；如果正则不常用，可不设置缓存
     * @return
     */
    public static boolean match(String exp, String message, boolean cachePattern) {
        if (exp == null || "".equals(exp.trim())) {
            return false;
        }
        if (message == null) {
            message = "";
        }
        Pattern pattern = getPattern(exp, cachePattern);
        Matcher matcher = pattern.matcher(message);
        return matcher.matches();
    }

    /**
     * 从字符串中message查找满足exp条件的字符串；本方法不缓存正则模式对象，
     * 如果频繁使用的正则表达式，可调用方法{@link #findAllSubStr(String, String, boolean)}缓存正则模式对象，降低创建模式对象的开销<br>
     * 例： <br>
     * message为：java.util.List&lt;com.UserVo&gt; <br>
     * exp为：<([\s\S]+)> <br>
     * 查找结果（json格式）为：["&lt;com.UserVo&gt;"]
     *
     * @param exp     子串正则表达式
     * @param message 待检索的字符串
     * @return
     */
    public static List<String> findAllSubStr(String exp, String message) {
        return findAllSubStr(exp, message, false);
    }

    /**
     * 从字符串中message查找满足exp条件的字符串<br>
     * 例： <br>
     * message为：java.util.List&lt;com.UserVo&gt; <br>
     * exp为：<([\s\S]+)> <br>
     * 查找结果（json格式）为：["&lt;com.UserVo&gt;"]
     *
     * @param exp          子串正则表达式
     * @param message      待检索的字符串
     * @param cachePattern 是否缓存通过exp创建的模式对象，如果频繁使用的正则表达式，可设置为true，降低创建的模式对象的开销，提高性能；如果正则不常用，可不设置缓存
     * @return
     */
    public static List<String> findAllSubStr(String exp, String message, boolean cachePattern) {
        List<String> list = new ArrayList<String>();
        Pattern pattern = getPattern(exp, cachePattern);
        Matcher matcher = pattern.matcher(message);
        while (matcher.find()) {
            list.add(matcher.group());
        }
        return list;
    }

    /**
     * 从字符串中查找满足条件的所有子序列()中的字符串；本方法不缓存正则模式对象，
     * 如果频繁使用的正则表达式，可调用方法{@link #findAllSubSequence(String, String, boolean)}缓存正则模式对象，降低创建模式对象的开销<br>
     * 例：<br>
     * message为：@if(:inviteCode != null) { and invite_code = :inviteCode and name = :name } <br>
     * exp为：\(([\s\S]*?)\)|\{([\s\S]*([#|\$]\{[\s\S]+?\})*[\s\S]*)\} <br>
     * 查找结果（json格式）为：[:inviteCode != null,  and invite_code = :inviteCode and name = :name ]
     *
     * @param exp     待匹配的正则表达式
     * @param message 待检索字符串
     * @return 子序列()中的字符串
     */
    public static List<String> findAllSubSequence(String exp, String message) {
        return findAllSubSequence(exp, message, false);
    }

    /**
     * 从字符串中查找满足条件的所有子序列()中的字符串<br>
     * 例：<br>
     * message为：@if(:inviteCode != null) { and invite_code = :inviteCode and name = :name } <br>
     * exp为：\(([\s\S]*?)\)|\{([\s\S]*([#|\$]\{[\s\S]+?\})*[\s\S]*)\} <br>
     * 查找结果（json格式）为：[:inviteCode != null,  and invite_code = :inviteCode and name = :name ]
     *
     * @param exp          待匹配的正则表达式
     * @param message      待检索字符串
     * @param cachePattern 是否缓存通过exp创建的模式对象，如果频繁使用的正则表达式，可设置为true，降低创建的模式对象的开销，提高性能；如果正则不常用，可不设置缓存
     * @return 子序列()中的字符串
     */
    public static List<String> findAllSubSequence(String exp, String message, boolean cachePattern) {
        List<String> list = new ArrayList<>();
        Pattern pattern = getPattern(exp, cachePattern);
        Matcher matcher = pattern.matcher(message);
        while (matcher.find()) {
            for (int i = 1; i <= matcher.groupCount(); i++) {
                String group = matcher.group(i);
                if (group != null) {
                    list.add(group);
                }
            }
        }
        return list;
    }

    /**
     * 从字符串中查找匹配正则表达式的子串中的第一个子序列(正则表达式中第一个使用（）括起来的子序列)，平台使用的工具类，禁止修改；本方法不缓存正则模式对象，
     * 如果频繁使用的正则表达式，可调用方法{@link #findFirstSubSequence(String, String, boolean)}缓存正则模式对象，降低创建模式对象的开销<br>
     * 例： <br>
     * message为：java.util.List&lt;com.UserVo&gt; <br>
     * exp为：<([\s\S]+)> <br>
     * 查找结果（json格式）为：["com.UserVo"]
     *
     * @param exp     子串正则表达式
     * @param message 需要查找的字符串
     * @return 查询到的子串中的第一个子序列
     */
    public static final List<String> findFirstSubSequence(String exp, String message) {
        return findFirstSubSequence(exp, message, false);
    }

    /**
     * 从字符串中查找匹配正则表达式的子串中的第一个子序列(正则表达式中第一个使用（）括起来的子序列)，平台使用的工具类，禁止修改<br>
     * 例： <br>
     * message为：java.util.List&lt;com.UserVo&gt; <br>
     * exp为：<([\s\S]+)> <br>
     * 查找结果（json格式）为：["com.UserVo"]
     *
     * @param exp          子串正则表达式
     * @param message      需要查找的字符串
     * @param cachePattern 是否缓存通过exp创建的模式对象，如果频繁使用的正则表达式，可设置为true，降低创建的模式对象的开销，提高性能；如果正则不常用，可不设置缓存
     * @return 查询到的子串中的第一个子序列
     */
    public static List<String> findFirstSubSequence(String exp, String message, boolean cachePattern) {
        List<String> list = new ArrayList<>();
        Pattern pattern = getPattern(exp, cachePattern);
        Matcher matcher = pattern.matcher(message);
        while (matcher.find()) {
            list.add(matcher.group(1));
        }
        return list;
    }

    /**
     * 从字符串中查找匹配正则表达式的子串中的指定索引位置子序列(正则表达式中第index个使用（）括起来的子序列)，平台使用的工具类，禁止修改；本方法不缓存正则模式对象，
     * 如果频繁使用的正则表达式，可调用方法{@link #findSubSequenceByExpAndIndex(String, String, int, boolean)}缓存正则模式对象，降低创建模式对象的开销<br>
     * 例： <br>
     * message为：java.util.List&lt;com.UserVo&gt; <br>
     * exp为：<([\s\S]+)> <br>
     * index为：1 <br>
     * 查找结果（json格式）为：["com.UserVo"]
     *
     * @param exp     子串正则表达式
     * @param message 需要查找的字符串
     * @param index   指定索引的子序列，从1开始
     * @return 查询到的子串中的第index个子序列
     */
    public static final List<String> findSubSequenceByExpAndIndex(String exp, String message, int index) {
        return findSubSequenceByExpAndIndex(exp, message, index, false);
    }

    /**
     * 从字符串中查找匹配正则表达式的子串中的指定索引位置子序列(正则表达式中第index个使用（）括起来的子序列)，平台使用的工具类，禁止修改<br>
     * 例： <br>
     * message为：java.util.List&lt;com.UserVo&gt; <br>
     * exp为：<([\s\S]+)> <br>
     * index为：1 <br>
     * 查找结果（json格式）为：["com.UserVo"]
     *
     * @param exp          子串正则表达式
     * @param message      需要查找的字符串
     * @param index        指定索引的子序列，从1开始
     * @param cachePattern 是否缓存通过exp创建的模式对象，如果频繁使用的正则表达式，可设置为true，降低创建的模式对象的开销，提高性能；如果正则不常用，可不设置缓存
     * @return 查询到的子串中的第index个子序列
     */
    public static List<String> findSubSequenceByExpAndIndex(String exp, String message, int index, boolean cachePattern) {
        List<String> list = new ArrayList<>();
        Pattern pattern = getPattern(exp, cachePattern);
        Matcher matcher = pattern.matcher(message);
        while (matcher.find()) {
            list.add(matcher.group(index));
        }
        return list;
    }

    /**
     * 截取满足正则表达式之前的子串，如果未找到，则返回原始字符串message；本方法不缓存正则模式对象，
     * 如果频繁使用的正则表达式，可调用方法{@link #findSubStrUntilExpByCount(String, String, int, boolean)}缓存正则模式对象，降低创建模式对象的开销<br>
     * 例： <br>
     * message为：java.util.List&lt;com.UserVo&gt; <br>
     * 例1：<br>
     * exp为：< <br>
     * count为：1 <br>
     * 查找结果为：java.util.List  <br>
     * <p>
     * 例2：<br>
     * exp为：& <br>
     * count为：1 <br>
     * 查找结果为：java.util.List&lt;com.UserVo&gt;
     *
     * @param message 需要截取的字符串
     * @param exp     正则表达式
     * @param count   满足第几个
     * @return
     */
    public static String findSubStrUntilExpByCount(String message, String exp, int count) {
        return findSubStrUntilExpByCount(message, exp, count, false);
    }

    /**
     * 截取满足正则表达式之前的子串，如果未找到，则返回原始字符串message<br>
     * 例： <br>
     * message为：java.util.List&lt;com.UserVo&gt; <br>
     * 例1：<br>
     * exp为：< <br>
     * count为：1 <br>
     * 查找结果为：java.util.List  <br>
     * <p>
     * 例2：<br>
     * exp为：& <br>
     * count为：1 <br>
     * 查找结果为：java.util.List&lt;com.UserVo&gt;
     *
     * @param message      需要截取的字符串
     * @param exp          正则表达式
     * @param count        满足第几个
     * @param cachePattern 是否缓存通过exp创建的模式对象，如果频繁使用的正则表达式，可设置为true，降低创建的模式对象的开销，提高性能；如果正则不常用，可不设置缓存
     * @return
     */
    public static String findSubStrUntilExpByCount(String message, String exp, int count, boolean cachePattern) {
        String result = null;
        Pattern pattern = getPattern(exp, cachePattern);
        Matcher matcher = pattern.matcher(message);
        int i = 0;
        int endIndex = -1;
        while (matcher.find() && i < count) {
            endIndex = matcher.start();
            i++;
        }
        if (endIndex > -1) {
            result = message.substring(0, endIndex);
        }

        if (result == null) {//未找到exp，则返回原始字符串
            result = message;
        }
        return result;
    }

    /**
     * 转义正则特殊字符 （$()*+.[]?\^{},|）
     *
     * @param keyword 待转义字符串
     * @return
     */
    public static String escapeRegExpr(String keyword) {
        if (StringUtils.isNotBlank(keyword)) {
            String[] fbsArr = {"\\", "$", "(", ")", "*", "+", ".", "[", "]", "?", "^", "{", "}", "|"};
            for (String key : fbsArr) {
                if (keyword.contains(key)) {
                    keyword = keyword.replace(key, "\\" + key);
                }
            }
        }
        return keyword;
    }

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        String msg = "asdf Windows 3.1";
        String exp = "Windows (?!95|98|NT|2000)";
//        String msg = "Windows 956";
        System.out.println(findAllSubStr(exp, msg));
        System.out.println(findAllSubSequence(exp, msg));

        String exp1 = ":[A-Za-z0-9_\\[\\]]+[^\\(]*(?!\\()";
        String msg1 = ":name.a.contains(";
        System.out.println(findAllSubStr(exp1, msg1));
        System.out.println(findAllSubSequence(exp1, msg1));

        String message = ":inviteCode != null && (:name.a.contains(\") {\") && :id.contains (\") \\\" {\")) ";
        String paramMacroExp = "\\:[A-Za-z0-9_\\[\\]]+(?:\\.[A-Za-z0-9_\\[\\]]+)*(\\s*\\()?";
        System.out.println(findAllSubStr(paramMacroExp, message));
        System.out.println(findAllSubSequence(paramMacroExp, message));

        String className = "java.util.List<com.UserVo><f>";
        System.out.println(RegUtils.findSubStrUntilExpByCount(className, "<", 1));
        System.out.println(RegUtils.findSubStrUntilExpByCount(className, "<", 2));
        System.out.println(RegUtils.findFirstSubSequence("<([\\s\\S]+?)>", className));
        System.out.println(RegUtils.findAllSubStr("<([\\s\\S]+?)>", className));

        String ifStr = "@if(:inviteCode != null) { and invite_code = #{inviteCode} and name LIKE CONCAT('%',:name) }";
        String sqlSegmentExp = "\\(([\\s\\S]*?)\\)|\\{([\\s\\S]*([#|\\$]\\{[\\s\\S]+?\\})*[\\s\\S]*)\\}";
        System.out.println(RegUtils.findAllSubSequence(sqlSegmentExp, ifStr));
        System.out.println(RegUtils.findAllSubStr(sqlSegmentExp, ifStr));

        String exp2 = "(Person.Name LIKE '张三%')&(Person.Name NOT LIKE '%张三%')&(Person.age BETWEEN 18 AND 35)&(Person.age NOT BETWEEN 18 AND 35)";
        String regExp = "(('[\\s\\S]*?'){1}|[^ \\f\\n\\r\\t\\v=<>!*/%\\+\\-&\\(\\),']+)";
        System.out.println("sql===》" + RegUtils.findAllSubStr(regExp, exp2));

        //国际化替换开始
//		String url = "http://localhost:8080/SecurtyCloudPlatform/assets/js/chunk-commons.js";
//		List<String> url_patterns = new ArrayList<>();
//		url_patterns.add("^.+\\.html$");
//		url_patterns.add("^.+\\.js$");
//		url_patterns.add("^.+\\.css$");
//		url_patterns.add("^.+\\.jsp$");
//		url_patterns.add("^/function/operation.action$");
//		url_patterns.add("^/function/batchoperation.action$");
//
//		List<String> url_ignore_patterns = new ArrayList<>();
//		url_ignore_patterns.add("^.+\\.file$");
//		url_ignore_patterns.add("^.+vendor\\.js$");
//		url_ignore_patterns.add("^.+chunk\\-elementUI\\.js$");
//		url_ignore_patterns.add("^.+chunk\\-libs\\.js$");
//
//		//进行正则表达式验证
//		boolean doI18n = false;
//		if(url_patterns != null && url_patterns.size() > 0){
//			for(String pattern : url_patterns){
//				if(pattern == null || "".equals(pattern.trim())){continue;}
//				if(RegUtil.match(pattern, url)){doI18n = true;break;}
//			}
//		}
//		//判断忽略的路径
//		if(doI18n && url_ignore_patterns != null && url_ignore_patterns.size() > 0){
//			for(String pattern : url_ignore_patterns){
//				if(pattern == null || "".equals(pattern.trim())){continue;}
//				if(RegUtil.match(pattern, url)){doI18n = false;break;}
//			}
//		}
//
//		System.out.println(doI18n);
//		//国际化替换结束
//		String listSubscriptExp = "\\[(\\d+)\\]";
//		String property = "$sdate[44].ss[22].sd";
//		List<String> strings = RegUtil.find(listSubscriptExp, property);
//		System.out.println(strings.get(0));

        String reg = "com.starmo.platform.[\\.\\w]*.controller.[\\.\\w]*";
        String path = "com.starmo.platform.core.workflow.controller.ProcessController";
        String path1 = "com.starmo.platform.core.function.controller.impl.FunctionController";

        System.out.println(match(reg, path));
        System.out.println(match(reg, path1));

        String ipAndPortRegex = "(([1-9]|([1-9]\\d)|(1\\d{1,2})|(2[0-4]\\d)|(25[0-5]))\\.){1}(([0-9]|([1-9]\\d)|(1\\d{1,2})|(2[0-4]\\d)|(25[0-5]))\\.){2}([1-9]|([1-9]\\d)|(1\\d{1,2})|(2[0-4]\\d)|(25[0-5]))(:([0-9]|[1-9]\\d{1,3}|[1-5]\\d{4}|6[0-4]\\d{4}|65[0-4]\\d{2}|655[0-2]\\d|6553[0-5]))";
        System.out.println(RegUtils.match(ipAndPortRegex, "192.160.1.11:8080"));
    }

}
