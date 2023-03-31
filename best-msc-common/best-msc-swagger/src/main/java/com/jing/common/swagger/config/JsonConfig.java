package com.jing.common.swagger.config;

import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.BigDecimalCodec;
import com.alibaba.fastjson.serializer.BigIntegerCodec;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.jing.common.swagger.serialize.JsonLongSerializer;
import com.jing.common.swagger.serialize.JsonDateSerializer;
import com.jing.common.swagger.serialize.LocalDateTimeDeserializer;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author : jing
 * @projectName : best-msc
 * @packageName : com.jing.msc.config
 * @date : 2021/4/20 20:35
 * @description :
 */
@Configuration
public class JsonConfig {

    @Bean
    public HttpMessageConverters httpMessageConverter() {
        List<HttpMessageConverter<?>> converters = new ArrayList<>();
        // 构建一个 HttpMessageConverter FastJson 消息转换器
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
        // 定义配置，设置编码方式和格式化信息
        FastJsonConfig config = new FastJsonConfig();
        // 设置成 PrettyFormat 格式
        config.setSerializerFeatures(SerializerFeature.PrettyFormat);
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
        // 统一处理日期格式
        serializeConfig.put(Date.class, new JsonDateSerializer());
        serializeConfig.put(java.sql.Date.class, new JsonDateSerializer());
        serializeConfig.put(java.sql.Timestamp.class, new JsonDateSerializer());
        serializeConfig.put(LocalDateTime.class, new JsonDateSerializer());

        // long型转换字符串，解决数据库19位长度，前端js仅支持15位的问题
        serializeConfig.put(Long.class, JsonLongSerializer.instance);
        serializeConfig.put(Long.TYPE, JsonLongSerializer.instance);
        // BigInteger型转换字符串，解决数据库19位长度，前端js仅支持15位的问题
        serializeConfig.put(BigDecimal.class, BigDecimalCodec.instance);
        // BigInteger型转换字符串，解决数据库19位长度，前端js仅支持15位的问题
        serializeConfig.put(BigInteger.class, BigIntegerCodec.instance);

        config.setSerializeConfig(serializeConfig);
        // 将 FastJson 加到消息转换器中
        converter.setFastJsonConfig(config);
        converters.add(converter);

        return new HttpMessageConverters(true, converters);
    }
}
