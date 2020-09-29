package com.cloud.gateway.filter;

import com.cloud.gateway.enums.ExceptionEnum;
import com.cloud.gateway.exception.CustomException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.server.RequestPath;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.Arrays;

/**
 * @description: 全局过滤器
 * @author: dqq
 * @date: 2020/9/27 18:46
 */
@Component
@RefreshScope
public class CustomerGlobalFilter implements GlobalFilter, Ordered {

    @Value("${ignore.request}")
    public String[] ignoreRequest;

    @Resource
    StringRedisTemplate stringRedisTemplate;

    public static final String TOKEN_FILE = "TOKEN:";




    private static final Logger logger = LoggerFactory.getLogger(CustomerGlobalFilter.class);
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        logger.info("custom global filter");
        /**
         * 1. 获取请求路径
         * 2. 将不需要过滤的请求放在配置文件中，这里取出，存入数组（配置文件存放）
         * 3. 判断请求路径是否在不需要过滤中
         * 4. 不需要过滤
         * 4.1 直接转发，请求接口
         * 5. 需要过滤
         * 5.1 获取请求头中是否包含token，
         * 5.1.1 存在：（redis中判断token是否存在：存在，直接请求，不存在，抛出异常，请求结束）
         * 5.1.2 不存在：（全局异常抛出，结束请求，返回错误信息）
         */

        String requestPath = exchange.getRequest().getURI().getPath();
        logger.info(requestPath);
        boolean contains = Arrays.stream(ignoreRequest).anyMatch(requestPath::equals);
        if (!contains){
            String bearerToken = exchange.getRequest().getHeaders().getFirst("Authorization");
            if (bearerToken == null){
                throw new CustomException(ExceptionEnum.UNAUTHORIZED.getCode(),ExceptionEnum.UNAUTHORIZED.getMsg());
            }
            // 截取请求头中的token信息,去掉"Bearer "
            String token = bearerToken.substring(7);
            logger.error("token信息为："+token);
            if (!stringRedisTemplate.hasKey(TOKEN_FILE + token)){
                logger.info("是否为用户token："+ stringRedisTemplate.hasKey(TOKEN_FILE + token));
                throw new CustomException(ExceptionEnum.TOKEN_INVALID.getCode(),"Token已失效，请重新登陆");
            }
        }
        //将现在的request 变成 change对象
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 1000;
    }
}
