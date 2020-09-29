package com.cloud.gateway.config;

import com.alibaba.fastjson.JSONObject;
import com.cloud.gateway.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * @description: 全局异常拦截器
 * @author: dqq
 * @date: 2020/9/29 12:28
 */
@Component
@Slf4j
public class GlobalExceptionHandler implements ErrorWebExceptionHandler {
    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        JSONObject object = new JSONObject();
        Map<String,Object> map = new HashMap<>();
        if (ex instanceof CustomException) {
            map.put("code",((CustomException) ex).getCode());
            map.put("message",ex.getMessage());
            object.put("meta",map);
            object.put("data",null);
        }
        DataBufferFactory bufferFactory = exchange.getResponse().bufferFactory();
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        return exchange.getResponse().writeWith(Flux.just(bufferFactory.wrap(object.toJSONString().getBytes())));
    }
}
