package com.flong.springboot.core.config;

import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Configuration
@ConditionalOnClass(WebMvcConfigurer.class)
@Order(Ordered.HIGHEST_PRECEDENCE)
public class WebConfig implements WebMvcConfigurer {


  @Bean
  public HttpMessageConverters customConverters() {
    //创建fastJson消息转换器
    FastJsonHttpMessageConverter fastJsonConverter = new FastJsonHttpMessageConverter();
    //创建配置类
    FastJsonConfig fastJsonConfig = new FastJsonConfig();
    //修改配置返回内容的过滤
    fastJsonConfig.setSerializerFeatures(
        // 格式化
        SerializerFeature.PrettyFormat,
        // 可解决long精度丢失 但会有带来相应的中文问题
        //SerializerFeature.BrowserCompatible,
        // 消除对同一对象循环引用的问题，默认为false（如果不配置有可能会进入死循环）
        SerializerFeature.DisableCircularReferenceDetect,
        // 是否输出值为null的字段,默认为false
        SerializerFeature.WriteMapNullValue,
        // 字符类型字段如果为null,输出为"",而非null
        SerializerFeature.WriteNullStringAsEmpty,
        // List字段如果为null,输出为[],而非null
        SerializerFeature.WriteNullListAsEmpty
    );
    // 日期格式
    fastJsonConfig.setDateFormat("yyyy-MM-dd HH:mm:ss");
    // long精度问题
    SerializeConfig serializeConfig = SerializeConfig.globalInstance;
    serializeConfig.put(BigInteger.class, ToStringSerializer.instance);
    serializeConfig.put(Long.class, ToStringSerializer.instance);
    serializeConfig.put(Long.TYPE, ToStringSerializer.instance);
    fastJsonConfig.setSerializeConfig(serializeConfig);
    //处理中文乱码问题
    List<MediaType> fastMediaTypes = new ArrayList<>();
    fastMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
    fastJsonConverter.setSupportedMediaTypes(fastMediaTypes);
    fastJsonConverter.setFastJsonConfig(fastJsonConfig);
    //将fastjson添加到视图消息转换器列表内
    return new HttpMessageConverters(fastJsonConverter);
  }


  /**
   * 拦截器
   */
  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    //registry.addInterceptor(logInterceptor).addPathPatterns("/**");
    //registry.addInterceptor(apiInterceptor).addPathPatterns("/**");
  }

  /**
   * cors 跨域支持 可以用@CrossOrigin在controller上单独设置
   */
  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**")
        //设置允许跨域请求的域名
        .allowedOrigins("*")
        //设置允许的方法
        .allowedMethods("*")
        //设置允许的头信息
        .allowedHeaders("*")
        //是否允许证书 不再默认开启
        .allowCredentials(Boolean.TRUE);
  }
}