package com.cloud.gateway.exception;

import lombok.Data;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.sql.DataSourceDefinition;
import java.util.HashMap;
import java.util.Map;

/**
 * @description: 自定义异常
 * @author: dqq
 * @date: 2020/9/29 12:21
 */
@Data
public class CustomException extends RuntimeException {

    /**
     * 异常状态码
     */

    private int code;

    /**
     * 异常信息
     */
    private String message;


    public CustomException(int code, String message) {
        this.code=code;
        this.message=message;
    }
}
