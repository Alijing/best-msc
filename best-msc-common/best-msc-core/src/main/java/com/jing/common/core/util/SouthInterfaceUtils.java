package com.jing.common.core.util;

import com.jing.common.core.vo.ResponseData;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;

/**
 * 用于远程调用http接口的工具类
 * @author : jing
 * @since : 2025/3/6 17:37
 */
public class SouthInterfaceUtils {

    private static final Logger southIfLogger = LoggerFactory.getLogger(SouthInterfaceUtils.class);

    /**
     * 当前线程cookie
     */
    public static final ThreadLocal<List<Cookie>> cookies = new ThreadLocal<List<Cookie>>();

    /**
     * 当前线程执行请求是否需要返回cookie
     */
    public static final ThreadLocal<Boolean> needCookie = new ThreadLocal<Boolean>();

    /**
     * 当前线程执行请求返回的cookie
     */
    public static final ThreadLocal<List<Cookie>> returnCookies = new ThreadLocal<List<Cookie>>();

    /**
     * 当前线程执行请求的cookieStore
     */
    private static final ThreadLocal<CookieStore> COOKIE_STORE = new ThreadLocal<CookieStore>();

    /**
     * 调用请求配置参数对象
     */
    private static RequestConfig requestConfig;

    /**
     * 初始化请求配置参数
     *
     * @param socketTimeout            数据传输过程中数据包之间间隔的最大时间
     * @param connectTimeout           连接建立时间，三次握手完成时间
     * @param connectionRequestTimeout httpclient使用连接池来管理连接，这个时间就是从连接池获取连接的超时时间，可以想象下数据库连接池
     */
    public static void init(Integer socketTimeout, Integer connectTimeout, Integer connectionRequestTimeout) {
        socketTimeout = (Integer) ObjectUtils.defaultIfNull(socketTimeout, 15000);
        connectTimeout = (Integer) ObjectUtils.defaultIfNull(connectTimeout, 15000);
        connectionRequestTimeout = (Integer) ObjectUtils.defaultIfNull(connectionRequestTimeout, 15000);
        requestConfig = RequestConfig.custom().setSocketTimeout(socketTimeout).setConnectTimeout(connectTimeout)
                .setConnectionRequestTimeout(connectionRequestTimeout).build();
    }

    /**
     * 发送http自定义请求，要求params对象为Map或者Java Bean，Map的key或者Bean的属性名作为参数名，Map的value或者Bean的属性值作为参数值；<br/>
     * GET方式的请求参数直接跟在httpUrl路径中；<br/>
     * 除GET方式请求外的请求包体数据格式使用header参数content-type（小写）确定，将直接以params参数作为包体数据，使用此方法需要对HTTP协议内容的编码有较深的理解，如：<br/>
     * 1、application/x-www-form-urlencoded编码包体数据转换为：name=starmo&age=1，<br/>
     * 2、multipart/form-data编码要求params为Map对象，包体数据转换为：name=starmo&age=1，<br/>
     * 3、application/json编码包体数据转换为：{"name":"starmo","age":1}，<br/>
     * 4、其他编码方式请在调用本方法前将参数转换成字符串形式的数据，如：application/xml编码可使用XmlUtil.buildXmlStr(...)方法转换，<br/>
     * 5、本方法还支持自定义content-type（小写）的编码格式，参数转换成对应的自定义编码格式字符串即可 <br/><br/>
     * 执行结果数据类型：ResponseData，对象包含了http状态码，响应头信息和响应包体信息（响应包体数据类型由Class<T>参数指定），如果调用严格模式的restful接口，可使用本方法
     *
     * @param httpUrl     请求地址
     * @param method      请求方法
     * @param headers     请求头参数，头参数中必须包含content-type（小写）字段，需要和params参数数据格式匹配，用于解析params数据;
     *                    另外可设置字符编码格式，通过content-type中的charset指定，不指定默认使用UTF-8
     * @param params      请求参数，支持java bean或者Map，multipart/form-data要求为Map
     * @param resultClass 结果对象Class，指定ResponseData的responseBody字段对象类型
     * @return
     * @throws RuntimeException
     */
    public static <T> ResponseData<T> sendHttpRequest(String httpUrl, HttpMethod method, Map<String, String> headers,
                                                      Object params, Class<T> resultClass) throws RuntimeException {
        return sendHttpRequest(httpUrl, method, headers, params, (Type) resultClass);
    }

    /**
     * 发送http自定义请求，要求params对象为Map或者Java Bean，Map的key或者Bean的属性名作为参数名，Map的value或者Bean的属性值作为参数值；<br/>
     * GET方式的请求参数直接跟在httpUrl路径中；<br/>
     * 除GET方式请求外的请求包体数据格式使用header参数content-type（小写）确定，将直接以params参数作为包体数据，使用此方法需要对HTTP协议内容的编码有较深的理解，如：<br/>
     * 1、application/x-www-form-urlencoded编码包体数据转换为：name=starmo&age=1，<br/>
     * 2、multipart/form-data编码要求params为Map对象，包体数据转换为：name=starmo&age=1，<br/>
     * 3、application/json编码包体数据转换为：{"name":"starmo","age":1}，<br/>
     * 4、其他编码方式请在调用本方法前将参数转换成字符串形式的数据，如：application/xml编码可使用XmlUtil.buildXmlStr(...)方法转换，<br/>
     * 5、本方法还支持自定义content-type（小写）的编码格式，参数转换成对应的自定义编码格式字符串即可 <br/><br/>
     * 执行结果数据类型：ResponseData，对象包含了http状态码，响应头信息和响应包体信息（响应包体数据类型由Class<T>参数指定），如果调用严格模式的restful接口，可使用本方法
     *
     * @param httpUrl     请求地址
     * @param method      请求方法
     * @param headers     请求头参数，头参数中必须包含content-type（小写）字段，需要和params参数数据格式匹配，用于解析params数据;
     *                    另外可设置字符编码格式，通过content-type中的charset指定，不指定默认使用UTF-8
     * @param params      请求参数，支持java bean或者Map，multipart/form-data要求为Map
     * @param resultClass 结果对象Class，指定ResponseData的responseBody字段对象类型，支持泛型
     * @return
     * @throws RuntimeException
     */
    public static <T> ResponseData<T> sendHttpRequest(String httpUrl, HttpMethod method, Map<String, String> headers,
                                                      Object params, Type resultClass) throws RuntimeException {
        String contentType = getContentType(headers);
        if (!HttpMethod.GET.equals(method) && (headers == null || contentType == null)) {
            throw new RuntimeException("headers信息content-type为空！");
        }
        Object _params = "";
        if (params != null) {
            contentType = contentType.toLowerCase();
            if ("".equals(contentType) || contentType.contains("application/x-www-form-urlencoded")
                    || contentType.contains("multipart/form-data")) {
                _params = params instanceof String ? JsonUtils.toBean((String) params, Map.class) : params;
            } else if (contentType.contains("application/json") || StringUtils.contains(contentType, "application/[\\S]+\\+json")) {
                _params = params instanceof String ? params : JsonUtils.toJson(params);
            } else {//其他编码格式由调用方处理为字符串
                _params = params.toString();
            }
        }
        return doSendHttpRequest(httpUrl, method, headers, _params, resultClass);
    }

    /**
     * 发送HTTP POST请求,以application/x-www-form-urlencoded形式发送，默认编码格式为UTF-8，
     * 即<b>headers.put("content-type", "application/x-www-form-urlencoded;charset=UTF-8");</b>
     * 也可自定义数据和编码格式content-type，然后将params自行转换为对应格式的字符串；
     *
     * @param httpUrl     请求地址
     * @param headers     请求头参数
     * @param params      请求参数，支持java bean或者Map
     * @param resultClass 结果对象Class，指定ResponseData的responseBody字段对象类型
     * @return
     */
    public static <T> T sendHttpPost(String httpUrl, Map<String, String> headers, Object params, Class<T> resultClass) {
        return sendHttpPost(httpUrl, headers, params, (Type) resultClass);
    }

    /**
     * 发送HTTP POST请求,以application/x-www-form-urlencoded形式发送，默认编码格式为UTF-8，
     * 即<b>headers.put("content-type", "application/x-www-form-urlencoded;charset=UTF-8");</b>
     * 也可自定义数据和编码格式content-type，然后将params自行转换为对应格式的字符串；
     *
     * @param httpUrl     请求地址
     * @param headers     请求头参数
     * @param params      请求参数，支持java bean或者Map
     * @param resultClass 结果对象Class，指定ResponseData的responseBody字段对象类型，支持泛型
     * @return
     */
    public static <T> T sendHttpPost(String httpUrl, Map<String, String> headers, Object params, Type resultClass) {
        if (headers == null) {
            headers = new HashMap<String, String>();
        }
        if (getContentType(headers) == null) {
            headers.put("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
        }
        return (T) doSendHttpRequest(httpUrl, HttpMethod.POST, headers, params, resultClass).getResponseBody();
    }

    /**
     * 发送HTTP POST请求,以application/json形式发送，params对象自动转化为json，默认编码格式为UTF-8，
     * 可指定header的content-type参数设置兼容json的自定义编码格式如：<b>headers.put("content-type", "application/VIID+JSON;charset=UTF-8");</b>
     *
     * @param httpUrl     请求地址
     * @param headers     请求头参数
     * @param params      请求参数，支持java bean或者Map
     * @param resultClass 结果对象Class，指定ResponseData的responseBody字段对象类型
     * @return
     */
    public static <T> T sendHttpPostWithJson(String httpUrl, Map<String, String> headers, Object params, Class<T> resultClass) {
        return sendHttpPostWithJson(httpUrl, headers, params, (Type) resultClass);
    }

    /**
     * 发送HTTP POST请求,以application/json形式发送，params对象自动转化为json，默认编码格式为UTF-8，
     * 可指定header的content-type参数设置兼容json的自定义编码格式如：<b>headers.put("content-type", "application/VIID+JSON;charset=UTF-8");</b>
     *
     * @param httpUrl     请求地址
     * @param headers     请求头参数
     * @param params      请求参数，支持java bean或者Map
     * @param resultClass 结果对象Class，指定ResponseData的responseBody字段对象类型，支持泛型
     * @return
     */
    public static <T> T sendHttpPostWithJson(String httpUrl, Map<String, String> headers, Object params, Type resultClass) {
        if (headers == null) {
            headers = new HashMap<String, String>();
        }
        if (getContentType(headers) == null) {
            headers.put("Content-Type", "application/json;charset=UTF-8");
        }
        String _params = params instanceof String ? (String) params : JsonUtils.toJson(params);
        return (T) doSendHttpRequest(httpUrl, HttpMethod.POST, headers, _params, resultClass).getResponseBody();
    }

    /**
     * 发送HTTP PUT请求,以application/x-www-form-urlencoded形式发送，默认编码格式为UTF-8，
     * 即<b>headers.put("content-type", "application/x-www-form-urlencoded;charset=UTF-8");</b>
     * 也可自定义数据和编码格式content-type，然后将params自行转换为对应格式的字符串；
     *
     * @param httpUrl     请求地址
     * @param headers     请求头参数
     * @param params      请求参数，支持java bean或者Map
     * @param resultClass 结果对象Class，指定ResponseData的responseBody字段对象类型
     * @return
     */
    public static <T> T sendHttpPut(String httpUrl, Map<String, String> headers, Object params, Class<T> resultClass) {
        return sendHttpPut(httpUrl, headers, params, (Type) resultClass);
    }

    /**
     * 发送HTTP PUT请求,以application/x-www-form-urlencoded形式发送，默认编码格式为UTF-8，
     * 即<b>headers.put("content-type", "application/x-www-form-urlencoded;charset=UTF-8");</b>
     * 也可自定义数据和编码格式content-type，然后将params自行转换为对应格式的字符串；
     *
     * @param httpUrl     请求地址
     * @param headers     请求头参数
     * @param params      请求参数，支持java bean或者Map
     * @param resultClass 结果对象Class，指定ResponseData的responseBody字段对象类型，支持泛型
     * @return
     */
    public static <T> T sendHttpPut(String httpUrl, Map<String, String> headers, Object params, Type resultClass) {
        if (headers == null) {
            headers = new HashMap<String, String>();
        }
        if (getContentType(headers) == null) {
            headers.put("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
        }
        return (T) doSendHttpRequest(httpUrl, HttpMethod.PUT, headers, params, resultClass).getResponseBody();
    }

    /**
     * 发送HTTP PUT请求,以application/json形式发送，params对象自动转化为json，默认编码格式为UTF-8，
     * 可指定header的content-type参数设置兼容json的自定义编码格式如：<b>headers.put("content-type", "application/VIID+JSON;charset=UTF-8");</b>
     *
     * @param httpUrl     请求地址
     * @param headers     请求头参数
     * @param params      请求参数，支持java bean或者Map
     * @param resultClass 结果对象Class，指定ResponseData的responseBody字段对象类型
     * @return
     */
    public static <T> T sendHttpPutWithJson(String httpUrl, Map<String, String> headers, Object params, Class<T> resultClass) {
        return sendHttpPutWithJson(httpUrl, headers, params, (Type) resultClass);
    }

    /**
     * 发送HTTP PUT请求,以application/json形式发送，params对象自动转化为json，默认编码格式为UTF-8，
     * 可指定header的content-type参数设置兼容json的自定义编码格式如：<b>headers.put("content-type", "application/VIID+JSON;charset=UTF-8");</b>
     *
     * @param httpUrl     请求地址
     * @param headers     请求头参数
     * @param params      请求参数，支持java bean或者Map
     * @param resultClass 结果对象Class，指定ResponseData的responseBody字段对象类型，支持泛型
     * @return
     */
    public static <T> T sendHttpPutWithJson(String httpUrl, Map<String, String> headers, Object params, Type resultClass) {
        if (headers == null) {
            headers = new HashMap<String, String>();
        }
        if (getContentType(headers) == null) {
            headers.put("Content-Type", "application/json;charset=UTF-8");
        }
        String _params = params instanceof String ? (String) params : JsonUtils.toJson(params);
        return (T) doSendHttpRequest(httpUrl, HttpMethod.PUT, headers, _params, resultClass).getResponseBody();
    }

    /**
     * 发送HTTP PATCH请求,以application/x-www-form-urlencoded形式发送，默认编码格式为UTF-8，
     * 即<b>headers.put("content-type", "application/x-www-form-urlencoded;charset=UTF-8");</b>
     * 也可自定义数据和编码格式content-type，然后将params自行转换为对应格式的字符串；
     *
     * @param httpUrl     请求地址
     * @param headers     请求头参数
     * @param params      请求参数，支持java bean或者Map
     * @param resultClass 结果对象Class，指定ResponseData的responseBody字段对象类型
     * @return
     */
    public static <T> T sendHttpPatch(String httpUrl, Map<String, String> headers, Object params, Class<T> resultClass) {
        return sendHttpPatch(httpUrl, headers, params, (Type) resultClass);
    }

    /**
     * 发送HTTP PATCH请求,以application/x-www-form-urlencoded形式发送，默认编码格式为UTF-8，
     * 即<b>headers.put("content-type", "application/x-www-form-urlencoded;charset=UTF-8");</b>
     * 也可自定义数据和编码格式content-type，然后将params自行转换为对应格式的字符串；
     *
     * @param httpUrl     请求地址
     * @param headers     请求头参数
     * @param params      请求参数，支持java bean或者Map
     * @param resultClass 结果对象Class，指定ResponseData的responseBody字段对象类型，支持泛型
     * @return
     */
    public static <T> T sendHttpPatch(String httpUrl, Map<String, String> headers, Object params, Type resultClass) {
        if (headers == null) {
            headers = new HashMap<String, String>();
        }
        if (getContentType(headers) == null) {
            headers.put("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
        }
        return (T) doSendHttpRequest(httpUrl, HttpMethod.PATCH, headers, params, resultClass).getResponseBody();
    }

    /**
     * 发送HTTP PATCH请求,以application/json形式发送，params对象自动转化为json，默认编码格式为UTF-8，
     * 可指定header的content-type参数设置兼容json的自定义编码格式如：<b>headers.put("content-type", "application/VIID+JSON;charset=UTF-8");</b>
     *
     * @param httpUrl     请求地址
     * @param headers     请求头参数
     * @param params      请求参数，支持java bean或者Map
     * @param resultClass 结果对象Class，指定ResponseData的responseBody字段对象类型
     * @return
     */
    public static <T> T sendHttpPatchWithJson(String httpUrl, Map<String, String> headers, Object params, Class<T> resultClass) {
        return sendHttpPatchWithJson(httpUrl, headers, params, (Type) resultClass);
    }

    /**
     * 发送HTTP PATCH请求,以application/json形式发送，params对象自动转化为json，默认编码格式为UTF-8，
     * 可指定header的content-type参数设置兼容json的自定义编码格式如：<b>headers.put("content-type", "application/VIID+JSON;charset=UTF-8");</b>
     *
     * @param httpUrl     请求地址
     * @param headers     请求头参数
     * @param params      请求参数，支持java bean或者Map
     * @param resultClass 结果对象Class，指定ResponseData的responseBody字段对象类型，支持泛型
     * @return
     */
    public static <T> T sendHttpPatchWithJson(String httpUrl, Map<String, String> headers, Object params, Type resultClass) {
        if (headers == null) {
            headers = new HashMap<String, String>();
        }
        if (getContentType(headers) == null) {
            headers.put("Content-Type", "application/json;charset=UTF-8");
        }
        String _params = params instanceof String ? (String) params : JsonUtils.toJson(params);
        return (T) doSendHttpRequest(httpUrl, HttpMethod.PATCH, headers, _params, resultClass).getResponseBody();
    }

    /**
     * 发送HTTP DELETE请求,以application/x-www-form-urlencoded形式发送，默认编码格式为UTF-8，
     * 即<b>headers.put("content-type", "application/x-www-form-urlencoded;charset=UTF-8");</b>
     * 也可自定义数据和编码格式content-type，然后将params自行转换为对应格式的字符串；
     *
     * @param httpUrl     请求地址
     * @param headers     请求头参数
     * @param params      请求参数，支持java bean或者Map
     * @param resultClass 结果对象Class，指定ResponseData的responseBody字段对象类型
     * @return
     */
    public static <T> T sendHttpDelete(String httpUrl, Map<String, String> headers, Object params, Class<T> resultClass) {
        return sendHttpDelete(httpUrl, headers, params, (Type) resultClass);
    }

    /**
     * 发送HTTP DELETE请求,以application/x-www-form-urlencoded形式发送，默认编码格式为UTF-8，
     * 即<b>headers.put("content-type", "application/x-www-form-urlencoded;charset=UTF-8");</b>
     * 也可自定义数据和编码格式content-type，然后将params自行转换为对应格式的字符串；
     *
     * @param httpUrl     请求地址
     * @param headers     请求头参数
     * @param params      请求参数，支持java bean或者Map
     * @param resultClass 结果对象Class，指定ResponseData的responseBody字段对象类型，支持泛型
     * @return
     */
    public static <T> T sendHttpDelete(String httpUrl, Map<String, String> headers, Object params, Type resultClass) {
        if (headers == null) {
            headers = new HashMap<String, String>();
        }
        if (getContentType(headers) == null) {
            headers.put("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
        }
        return (T) doSendHttpRequest(httpUrl, HttpMethod.DELETE, headers, params, resultClass).getResponseBody();
    }

    /**
     * 发送HTTP DELETE请求,以application/json形式发送，params对象自动转化为json，默认编码格式为UTF-8，
     * 可指定header的content-type参数设置兼容json的自定义编码格式如：<b>headers.put("content-type", "application/VIID+JSON;charset=UTF-8");</b>
     *
     * @param httpUrl     请求地址
     * @param headers     请求头参数
     * @param params      请求参数，支持java bean或者Map
     * @param resultClass 结果对象Class，指定ResponseData的responseBody字段对象类型
     * @return
     */
    public static <T> T sendHttpDeleteWithJson(String httpUrl, Map<String, String> headers, Object params, Class<T> resultClass) {
        return sendHttpDeleteWithJson(httpUrl, headers, params, (Type) resultClass);
    }

    /**
     * 发送HTTP DELETE请求,以application/json形式发送，params对象自动转化为json，默认编码格式为UTF-8，
     * 可指定header的content-type参数设置兼容json的自定义编码格式如：<b>headers.put("content-type", "application/VIID+JSON;charset=UTF-8");</b>
     *
     * @param httpUrl     请求地址
     * @param headers     请求头参数
     * @param params      请求参数，支持java bean或者Map
     * @param resultClass 结果对象Class，指定ResponseData的responseBody字段对象类型，支持泛型
     * @return
     */
    public static <T> T sendHttpDeleteWithJson(String httpUrl, Map<String, String> headers, Object params, Type resultClass) {
        if (headers == null) {
            headers = new HashMap<String, String>();
        }
        if (getContentType(headers) == null) {
            headers.put("Content-Type", "application/json;charset=UTF-8");
        }
        String _params = params instanceof String ? (String) params : JsonUtils.toJson(params);
        return (T) doSendHttpRequest(httpUrl, HttpMethod.DELETE, headers, _params, resultClass).getResponseBody();
    }

    /**
     * 发送HTTP GET请求
     *
     * @param httpUrl     请求地址
     * @param headers     请求头参数
     * @param resultClass 结果对象Class，指定ResponseData的responseBody字段对象类型
     * @return
     */
    public static <T> T sendHttpGet(String httpUrl, Map<String, String> headers, Class<T> resultClass) {
        return sendHttpGet(httpUrl, headers, (Type) resultClass);
    }

    /**
     * 发送HTTP GET请求
     *
     * @param httpUrl     请求地址
     * @param headers     请求头参数
     * @param resultClass 结果对象Class，指定ResponseData的responseBody字段对象类型，支持泛型
     * @return
     */
    public static <T> T sendHttpGet(String httpUrl, Map<String, String> headers, Type resultClass) {
        return (T) doSendHttpRequest(httpUrl, HttpMethod.GET, headers, null, resultClass).getResponseBody();
    }

    /**
     * 发送Get请求Https
     *
     * @param httpGet
     * @return
     */
    @Deprecated
    private static <T> ResponseData<T> sendHttpsGet(HttpGet httpGet, Type resultClass) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        ResponseData responseData = null;
        try {
            // 创建默认的httpClient实例.
//			PublicSuffixMatcher publicSuffixMatcher = PublicSuffixMatcherLoader.load(new URL(httpGet.getURI().toString()));
//			DefaultHostnameVerifier hostnameVerifier = new DefaultHostnameVerifier(publicSuffixMatcher);
//			httpClient = HttpClients.custom().setSSLHostnameVerifier(hostnameVerifier).build();
            httpClient = getHttpClient(httpGet.getURI().toString());
            httpGet.setConfig(getRequestConfig());
            // 执行请求
            response = httpClient.execute(httpGet);
            responseData = getResponseData(response, resultClass);
        } catch (Exception e) {
            if (southIfLogger.isErrorEnabled()) {
                southIfLogger.error(e.getMessage(), e);
            }
        } finally {
            try {
                // 关闭连接,释放资源
                if (response != null) {
                    response.close();
                }
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException e) {
                if (southIfLogger.isErrorEnabled()) {
                    southIfLogger.error(e.getMessage(), e);
                }
            }
        }
        if (southIfLogger.isDebugEnabled()) {
            southIfLogger.debug("【南向接口返回JSON格式化】" + JsonUtils.toJson(responseData));
        }
        return responseData;
    }

    /**
     * 发送http自定义请求<br/>
     * GET方式的请求参数直接跟在httpUrl路径中；<br/>
     * 除GET方式外的请求将直接以params参数作为包体数据，使用此方法需要对HTTP协议内容的编码有较深的理解，调用本方法前将参数转换成字符串形式的数据,如：<br/>
     * application/json编码数据为：{"name":"starmo","age":1}<br/>
     * application/x-www-form-urlencoded编码数据为：name=starmo&age=1<br/>
     * 其他如application/xml等编码方式同理<br/>
     * 本方法还支持自定义content-type（小写）的编码格式，参数转换成对应的自定义编码格式字符串即可
     *
     * @param httpUrl     请求地址
     * @param method      请求方法
     * @param headers     请求头参数，头参数中必须包含content-type（小写）字段，需要和params参数数据格式匹配，用于解析params数据;另外可设置字符编码格式，通过content-type中的charset指定，不指定默认使用UTF-8
     * @param params      请求参数，支持Java bean或者Map
     * @param resultClass 结果对象Class，指定ResponseData的responseBody字段对象类型，支持泛型
     * @return ResponseData请求结果数据对象
     * @throws RuntimeException
     */
    private static <T> ResponseData<T> doSendHttpRequest(String httpUrl, HttpMethod method, Map<String, String> headers,
                                                         Object params, Type resultClass) throws RuntimeException {
        if (southIfLogger.isDebugEnabled()) {
            String s = "【南向接口调用】 路径：<" + httpUrl + ">，头信息：<" + JsonUtils.toJson(headers) + ">";
            if (params != null) {
                s += "，参数：<" + (params instanceof String ? params : JsonUtils.toJson(params)) + ">";
            }
            if (cookies.get() != null) {
                s += "，cookie：<" + JsonUtils.toJson(cookies.get()) + ">";
            }
            southIfLogger.debug(s);
        }
        String contentType = getContentType(headers);
        if (!HttpMethod.GET.equals(method) && (headers == null || contentType == null)) {
            throw new RuntimeException("headers信息content-type为空！");
        }

        HttpUriRequest httpRequest = null;
        RequestBuilder builder = null;
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        ResponseData responseData = null;
        try {
            //创建http请求构造器
            builder = RequestBuilder.create(method.toString())
                    .setUri(httpUrl)
                    .setConfig(getRequestConfig())
                    .addHeader(HTTP.CONN_DIRECTIVE, HTTP.CONN_CLOSE);

            //设置header
            if (headers != null && headers.size() > 0) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    //通过HttpPost的header参数设置的content-type优先级高于StringEntity.setContentType方法
                    builder.setHeader(entry.getKey(), entry.getValue());
                }
            }

            if (HttpMethod.GET.equals(method)) {

            } else if (params != null && (HttpMethod.POST.equals(method) || HttpMethod.PUT.equals(method) ||
                    HttpMethod.PATCH.equals(method) || HttpMethod.DELETE.equals(method))) {
                String charset = "UTF-8";
                String[] types = contentType.split(";");
                for (String type : types) {
                    String[] typeParam = type.split("=");
                    if (typeParam.length == 2 && "charset".equalsIgnoreCase(typeParam[0])) {//解析content-type中指定的charset编码
                        charset = typeParam[1];
                        break;
                    }
                }
                if (params instanceof String) {//适配content-type为application/json或者application/VIID+JSON
                    //设置参数
                    StringEntity stringEntity = new StringEntity((String) params, charset);
                    stringEntity.setContentType(contentType);
                    stringEntity.setContentEncoding(charset);
                    builder.setEntity(stringEntity);
                } else if (params instanceof Map && contentType.contains("multipart/form-data")) {
                    builder.removeHeaders("content-type");

                    Map<String, Object> _params = (Map<String, Object>) params;
                    Set<Map.Entry<String, Object>> entries = _params.entrySet();

                    MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
                    for (Map.Entry<String, Object> entry : entries) {
                        Object value = entry.getValue();
                        String key = entry.getKey();
                        if (value instanceof InputStream) {
                            Object fileName = _params.get(key + ".name");
                            multipartEntityBuilder.addBinaryBody(key, (InputStream) value, ContentType.MULTIPART_FORM_DATA, fileName != null ? fileName.toString() : key);
                        } else if (value instanceof byte[]) {
                            Object fileName = _params.get(key + ".name");
                            multipartEntityBuilder.addBinaryBody(key, (byte[]) value, ContentType.MULTIPART_FORM_DATA, fileName != null ? fileName.toString() : key);
                        } else if (value instanceof File) {
                            File file = (File) value;
                            multipartEntityBuilder.addBinaryBody(key, file, ContentType.MULTIPART_FORM_DATA, file.getName());
                        } else {
                            multipartEntityBuilder.addPart(key,
                                    new StringBody(value != null ? value.toString() : "",
                                            ContentType.create("text/plain", Consts.UTF_8)));
                        }
                    }
                    builder.setEntity(multipartEntityBuilder.build());
                } else {//java bean 或者Map
                    Collection<String> fieldNames = ReflectUtils.getAllFieldName(params);
                    List<NameValuePair> formParams = new ArrayList<>();
                    for (String field : fieldNames) {
                        Object value = PropertyUtils.getProperty(params, field);
                        formParams.add(new BasicNameValuePair(field, value != null ? value.toString() : ""));
                    }
                    UrlEncodedFormEntity ufe = new UrlEncodedFormEntity(formParams, "UTF-8");
                    builder.setEntity(ufe);
                }
            }
            //创建http请求
            httpRequest = builder.build();
            // 创建默认的httpClient实例.
            httpClient = getHttpClient(httpRequest.getURI().toString());

            // 执行请求
            response = httpClient.execute(httpRequest);
            Boolean needCookie = SouthInterfaceUtils.needCookie.get();
            if (needCookie != null && needCookie) {
                SouthInterfaceUtils.returnCookies.set(COOKIE_STORE.get().getCookies());
            }
            responseData = getResponseData(response, resultClass);
        } catch (Exception e) {
            if (southIfLogger.isErrorEnabled()) {
                southIfLogger.error(e.getMessage(), e);
            }
            responseData = new ResponseData();
        } finally {
            try {
                // 关闭连接,释放资源
                if (response != null) {
                    response.close();
                }
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException e) {
                if (southIfLogger.isErrorEnabled()) {
                    southIfLogger.error(e.getMessage(), e);
                }
            }
            COOKIE_STORE.remove();
        }
        if (southIfLogger.isDebugEnabled()) {
            southIfLogger.debug("【南向接口返回JSON格式化】" + JsonUtils.toJson(responseData));
        }

        return responseData;
    }

    /**
     * 获取Http client，如果接口要求传递cookie，http client将带上cookie
     *
     * @return
     */
    private static CloseableHttpClient getHttpClient(String uri) {
        CloseableHttpClient httpClient = null;
        List<Cookie> cookies = SouthInterfaceUtils.cookies.get();
        CookieStore cookieStore = null;
        Boolean needCookie = SouthInterfaceUtils.needCookie.get();
        if (cookies != null || (needCookie != null && needCookie)) {
            cookieStore = new BasicCookieStore();
            COOKIE_STORE.set(cookieStore);
        }
        if (cookies != null && cookies.size() > 0) {
            addCookies(cookieStore);
        }

        if (uri.toLowerCase().startsWith("https://")) {
            try {
                //暂时忽略所有https证书
                httpClient = getIgnoreSSLClient(cookieStore);
            } catch (Exception e) {
                if (southIfLogger.isErrorEnabled()) {
                    southIfLogger.error(e.getMessage(), e);
                }
            }
        } else if (cookieStore != null) {
            httpClient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
        } else {
            httpClient = HttpClients.createDefault();
        }

        return httpClient;
    }

    /**
     * 获取忽略证书HttpClient
     *
     * @param cookieStore
     * @return
     * @throws Exception
     */
    @SuppressWarnings({"deprecation"})
    private static CloseableHttpClient getIgnoreSSLClient(CookieStore cookieStore) throws Exception {
        SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, new TrustStrategy() {
            @Override
            public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                return true;
            }
        }).build();

        //创建httpClient
        CloseableHttpClient client = HttpClients.custom().setSSLContext(sslContext).setDefaultCookieStore(cookieStore).
                setSSLHostnameVerifier(new NoopHostnameVerifier()).build();

        return client;
    }

    /**
     * 添加cookie
     *
     * @param cookieStore
     */
    private static void addCookies(CookieStore cookieStore) {
        List<Cookie> cookies = SouthInterfaceUtils.cookies.get();
        if (cookieStore != null && cookies != null && cookies.size() > 0) {
            for (Cookie cookie : cookies) {
                cookieStore.addCookie(cookie);
            }
        }
    }

    /**
     * HTTP响应对象转换vo输出
     *
     * @param response
     * @param resultClass 结果对象Class，指定ResponseData的responseBody字段对象类型，支持泛型
     * @return
     */
    private static <T> ResponseData<T> getResponseData(CloseableHttpResponse response, Type resultClass) {
        ResponseData data = new ResponseData();
        try {
            String responseBody = EntityUtils.toString(response.getEntity(), "UTF-8");
            data.setResponseBody(JsonUtils.toBean(responseBody, resultClass));
            Header[] headers = response.getAllHeaders();
            Map<String, String> headerMap = new HashMap<String, String>();
            for (Header header : headers) {
                headerMap.put(header.getName(), header.getValue());
            }
            data.setResponseHeaders(headerMap);
            data.setStatus(response.getStatusLine().getStatusCode());
        } catch (Exception e) {
            if (southIfLogger.isErrorEnabled()) {
                southIfLogger.error(e.getMessage(), e);
            }
        }

        return data;
    }

    /**
     * 不区分大小写查找header中的content-type字段
     *
     * @param headers header集合
     * @return headers为空或者集合中未找到将返回null
     */
    private static String getContentType(Map<String, String> headers) {
        if (headers == null) {
            return null;
        }
        Set<Map.Entry<String, String>> entries = headers.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            if ("content-type".equalsIgnoreCase(entry.getKey())) {
                return entry.getValue();
            }
        }
        return null;
    }

    /**
     * 获取请求配置对象，未初始化，将使用默认配置
     *
     * @return
     */
    public static RequestConfig getRequestConfig() {
        if (requestConfig == null) {
            init(null, null, null);
        }
        return requestConfig;
    }

    /**
     * HTTP请求方法
     *
     * @author caiyj
     * @version 1.0
     * @date 2019年6月29日 下午2:46:52
     */
    public static enum HttpMethod {
        GET,        //请求指定的页面信息，并返回实体主体
        HEAD,        //类似于get请求，只不过返回的响应中没有具体的内容，用于获取报头
        POST,        //向指定资源提交数据进行处理请求（例如提交表单或者上传文件）。数据被包含在请求体中。POST请求可能会导致新的资源的建立和/或已有资源的修改
        PUT,        //从客户端向服务器传送的数据取代指定的文档的内容
        DELETE,        //请求服务器删除指定的页面
        CONNECT,    //HTTP/1.1协议中预留给能够将连接改为管道方式的代理服务器
        OPTIONS,    //允许客户端查看服务器的性能
        TRACE,        //回显服务器收到的请求，主要用于测试或诊断
        PATCH        //从客户端向服务器传送的数据更新指定的局部文档的内容
    }


}
