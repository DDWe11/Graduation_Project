package org.jevonD.wastewaterMS.common.exception;

import org.jevonD.wastewaterMS.common.utils.ResponseResult;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AuthException.class) // 使用自定义的 AuthException
    public ResponseResult<Void> handleAuthException(AuthException e) {
        return new ResponseResult<>(HttpStatus.UNAUTHORIZED.value(), e.getMessage(), null); // 将数据部分传递为null
    }
}
