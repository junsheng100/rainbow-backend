package com.rainbow.base.constant;

import org.springframework.http.HttpStatus;

/**
 * HTTP 状态码枚举
 * 基于 org.springframework.http.HttpStatus 的中文版本
 */
public enum HttpCode {
    // 1xx 信息响应
    CONTINUE(100, "继续"),
    SWITCHING_PROTOCOLS(101, "切换协议"),
    PROCESSING(102, "处理中"),
    CHECKPOINT(103, "检查点"),

    // 2xx 成功
    OK(200, "请求成功"),
    CREATED(201, "已创建"),
    ACCEPTED(202, "已接受"),
    NON_AUTHORITATIVE_INFORMATION(203, "非权威信息"),
    NO_CONTENT(204, "无内容"),
    RESET_CONTENT(205, "重置内容"),
    PARTIAL_CONTENT(206, "部分内容"),
    MULTI_STATUS(207, "多状态"),
    ALREADY_REPORTED(208, "已报告"),
    IM_USED(226, "IM已使用"),

    // 3xx 重定向
    MULTIPLE_CHOICES(300, "多种选择"),
    MOVED_PERMANENTLY(301, "永久移动"),
    FOUND(302, "临时移动"),
    SEE_OTHER(303, "查看其他位置"),
    NOT_MODIFIED(304, "未修改"),
    USE_PROXY(305, "使用代理"),
    TEMPORARY_REDIRECT(307, "临时重定向"),
    PERMANENT_REDIRECT(308, "永久重定向"),

    // 4xx 客户端错误
    BAD_REQUEST(400, "请求错误"),
    UNAUTHORIZED(401, "未授权"),
    PAYMENT_REQUIRED(402, "需要付款"),
    FORBIDDEN(403, "禁止访问"),
    NOT_FOUND(404, "未找到"),
    METHOD_NOT_ALLOWED(405, "方法不允许"),
    NOT_ACCEPTABLE(406, "不可接受"),
    PROXY_AUTHENTICATION_REQUIRED(407, "需要代理认证"),
    REQUEST_TIMEOUT(408, "请求超时"),
    CONFLICT(409, "冲突"),
    GONE(410, "已删除"),
    LENGTH_REQUIRED(411, "需要内容长度"),
    PRECONDITION_FAILED(412, "前提条件失败"),
    PAYLOAD_TOO_LARGE(413, "请求体过大"),
    URI_TOO_LONG(414, "URI过长"),
    UNSUPPORTED_MEDIA_TYPE(415, "不支持的媒体类型"),
    REQUESTED_RANGE_NOT_SATISFIABLE(416, "请求范围不满足"),
    EXPECTATION_FAILED(417, "预期失败"),
    I_AM_A_TEAPOT(418, "我是一个茶壶(愚人节快乐)"),
    UNPROCESSABLE_ENTITY(422, "不可处理的实体"),
    LOCKED(423, "已锁定"),
    FAILED_DEPENDENCY(424, "依赖失败"),
    TOO_EARLY(425, "请求过早"),
    UPGRADE_REQUIRED(426, "需要升级协议"),
    PRECONDITION_REQUIRED(428, "需要前提条件"),
    TOO_MANY_REQUESTS(429, "请求过多"),
    REQUEST_HEADER_FIELDS_TOO_LARGE(431, "请求头字段过大"),
    UNAVAILABLE_FOR_LEGAL_REASONS(451, "因法律原因不可用"),

    // 5xx 服务器错误
    INTERNAL_SERVER_ERROR(500, "服务器内部错误"),
    NOT_IMPLEMENTED(501, "未实现"),
    BAD_GATEWAY(502, "网关错误"),
    SERVICE_UNAVAILABLE(503, "服务不可用"),
    GATEWAY_TIMEOUT(504, "网关超时"),
    HTTP_VERSION_NOT_SUPPORTED(505, "HTTP版本不支持"),
    VARIANT_ALSO_NEGOTIATES(506, "变体也可协商"),
    INSUFFICIENT_STORAGE(507, "存储空间不足"),
    LOOP_DETECTED(508, "检测到循环"),
    BANDWIDTH_LIMIT_EXCEEDED(509, "超出带宽限制"),
    NOT_EXTENDED(510, "未扩展"),
    NETWORK_AUTHENTICATION_REQUIRED(511, "需要网络认证"),
    INTERNAL_DATA_ERROR(512, "数据不可用"),
    INTERNAL_BIZ_ERROR(513, "业务逻辑不可用"),
    WARN(601, "未实现"),;
    private final int value;
    private final String reasonPhrase;

    HttpCode(int value, String reasonPhrase) {
        this.value = value;
        this.reasonPhrase = reasonPhrase;
    }

    /**
     * 获取状态码数值
     */
    public int value() {
        return this.value;
    }

    /**
     * 获取原因短语
     */
    public String getReasonPhrase() {
        return this.reasonPhrase;
    }

    /**
     * 根据状态码获取对应的枚举值
     */
    public static HttpCode valueOf(int statusCode) {
        for (HttpCode status : values()) {
            if (status.value == statusCode) {
                return status;
            }
        }
        throw new IllegalArgumentException("No matching constant for [" + statusCode + "]");
    }



    /**
     * 将 Spring 的 HttpStatus 转换为本地的 HttpCode
     */
    public static HttpCode fromHttpStatus(HttpStatus status) {
        return valueOf(status.value());
    }
}
