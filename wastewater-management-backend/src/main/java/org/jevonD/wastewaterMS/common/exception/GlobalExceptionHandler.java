package org.jevonD.wastewaterMS.common.exception;

import org.jevonD.wastewaterMS.common.response.ResponseWrapper;
import org.jevonD.wastewaterMS.common.utils.ResponseResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AuthException.class) // 使用自定义的 AuthException
    public ResponseResult<Void> handleAuthException(AuthException e) {
        return new ResponseResult<>(HttpStatus.UNAUTHORIZED.value(), e.getMessage(), null); // 将数据部分传递为null
    }
    @ExceptionHandler(PasswordMismatchException.class)
    public ResponseEntity<ResponseWrapper<Void>> handlePasswordMismatchException(PasswordMismatchException ex) {
        return ResponseEntity.badRequest().body(ResponseWrapper.error(400, ex.getMessage()));
    }

    @ExceptionHandler(SamePasswordException.class)
    public ResponseEntity<ResponseWrapper<Void>> handleSamePasswordException(SamePasswordException ex) {
        return ResponseEntity.badRequest().body(ResponseWrapper.error(400, ex.getMessage()));
    }

    private ResponseEntity<ResponseWrapper<Object>> buildErrorResponse(HttpStatus status, String message, String errorMessage) {
        ResponseWrapper<Object> response = ResponseWrapper.error(status.value(), errorMessage);
        response.setMessage(message);
        return ResponseEntity.status(status).body(response);
    }
}
