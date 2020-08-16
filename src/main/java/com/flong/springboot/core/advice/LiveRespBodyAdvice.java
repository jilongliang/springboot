package com.flong.springboot.core.advice;

import com.alibaba.fastjson.JSONObject;
import com.flong.springboot.core.vo.LiveResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice
@Slf4j
@Order(Integer.MIN_VALUE)
public class LiveRespBodyAdvice implements ResponseBodyAdvice<Object> {

    public static final String LIFE_PACKAGE = "com.flong.springboot.modules";

    @Override
    public boolean supports(MethodParameter methodParameter,
                            Class<? extends HttpMessageConverter<?>> converterType) {
        String className = methodParameter.getContainingClass().getName();
        return className.startsWith(LIFE_PACKAGE) &&
                !ExceptionAdvice.ErrorResp.class.isAssignableFrom(methodParameter.getParameterType()) &&
                !LiveResp.class.isAssignableFrom(methodParameter.getParameterType()) && !String.class
                .isAssignableFrom(methodParameter.getParameterType());
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {

        String path = request.getURI().getPath();
        log.debug("uri:{},response data:{}", path, JSONObject.toJSONString(body));
        return new LiveResp(body != null ? body : "");
    }
}