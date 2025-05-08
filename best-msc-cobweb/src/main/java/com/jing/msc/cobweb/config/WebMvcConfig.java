package com.jing.msc.cobweb.config;

import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.jing.common.core.serialize.LocalDateTimeDeserializer;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : jing
 * @since : 2024/7/30 14:44
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        // 此配置非常重要，否则 controller 返回的字符串，会被序列化两次
        converters.clear();
        converters.add(new ByteArrayHttpMessageConverter());
        // @ResponseBody 解决乱码
        converters.add(new StringHttpMessageConverter(StandardCharsets.UTF_8));
        converters.add(new ResourceHttpMessageConverter());
        converters.add(new AllEncompassingFormHttpMessageConverter());
        converters.add(fastJsonHttpMessageConverter());
    }

    private FastJsonHttpMessageConverter fastJsonHttpMessageConverter() {
        // 构建一个 HttpMessageConverter FastJson 消息转换器
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
        converter.setDefaultCharset(StandardCharsets.UTF_8);
        // 定义配置，设置编码方式和格式化信息
        FastJsonConfig config = new FastJsonConfig();
        // 处理中文乱码
        List<MediaType> mediaTypes = new ArrayList<>();
        mediaTypes.add(MediaType.APPLICATION_JSON);
        converter.setSupportedMediaTypes(mediaTypes);
        // 全局 json 请求日期反序列化 --> LocalDateTime
        ParserConfig parserConfig = new ParserConfig();
        parserConfig.putDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer());
        config.setParserConfig(parserConfig);
        // 全局 json 响应日期序列化 --> LocalDateTime
        SerializeConfig serializeConfig = new SerializeConfig();
        config.setSerializeConfig(serializeConfig);
        config.setSerializerFeatures(
                // 设置成 PrettyFormat 格式
                SerializerFeature.PrettyFormat,
                // 保留map空的字段
                SerializerFeature.WriteMapNullValue,
                // 将List类型的null转成[]
                SerializerFeature.WriteNullListAsEmpty,
                // 将Boolean类型的null转成false
                SerializerFeature.WriteNullBooleanAsFalse,
                // 日期格式转换
                SerializerFeature.WriteDateUseDateFormat,
                // 避免循环引用
                SerializerFeature.DisableCircularReferenceDetect,
                // 较大整数javascript丢失精度问题
                SerializerFeature.BrowserCompatible,
                // 枚举转字符串
                SerializerFeature.WriteEnumUsingToString
        );

        // 将 FastJson 加到消息转换器中
        converter.setFastJsonConfig(config);
        return converter;
    }

}
