package org.jevonD.wastewaterMS.common.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseResult<T> {
    private int code;
    private String message;
    private T data;
}
