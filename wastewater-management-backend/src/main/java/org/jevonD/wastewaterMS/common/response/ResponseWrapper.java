package org.jevonD.wastewaterMS.common.response;

import lombok.Data;

@Data
public class ResponseWrapper<T> {
    private int code; // 状态码 200-成功 400-参数错误 500-服务器错误
    private String message; // 提示信息
    private T data; // 实际数据

    public ResponseWrapper(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> ResponseWrapper<T> success(T data) {
        return new ResponseWrapper<>(200, "操作成功", data);
    }

    public static <T> ResponseWrapper<T> error(int code, String message) {
        return new ResponseWrapper<>(code, message, null);
    }
}
