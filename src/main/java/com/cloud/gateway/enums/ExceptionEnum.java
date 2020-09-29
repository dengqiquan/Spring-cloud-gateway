package com.cloud.gateway.enums;

/**
 * 异常枚举定义
 */
public enum ExceptionEnum {

    /**
     * 成功
     */
    SUCCESS(200,"成功"),
    /**
     * 警告
     */
    WARNING(300, "警告"),
    /**
     * 失败
     */
    FAIL(400, "失败"),
    /**
     * 未认证（未登陆）
     */
    UNAUTHORIZED(401,"未认证"),
    /**
     * token失效
     */
    TOKEN_INVALID(402,"token失效"),
    /**
     * 接口不存在
     */
    NOT_FOUND(404,"接口不存在"),
    /**
     * 请求超时
     */
    REQUEST_TIMEOUT(408,"请求超时"),
    /**
     * 参数错误
     */
    PARAM_ERROR(1000, "参数错误"),
    /**
     * 运行错误
     */
    EXCEPTION_ERROR(2000, "运行错误"),
    /**
     * 网络原因
     */
    CONNECTION_TIMEOUT_ERROR(4000, "网络原因");

    private int code;
    private String msg;

    ExceptionEnum(int rCode,String rMsg){
        this.code = rCode;
        this.msg = rMsg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
