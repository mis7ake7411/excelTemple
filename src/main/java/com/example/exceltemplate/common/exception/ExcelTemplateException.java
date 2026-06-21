package com.example.exceltemplate.common.exception;

import org.springframework.http.HttpStatus;

public class ExcelTemplateException extends RuntimeException {

    private final HttpStatus status;
    private final String code;

    public ExcelTemplateException(HttpStatus status, ErrorCode errorCode) {
        this(status, errorCode.getCode(), errorCode.getMessage());
    }

    public ExcelTemplateException(HttpStatus status, ErrorCode errorCode, Throwable cause) {
        this(status, errorCode.getCode(), errorCode.getMessage(), cause);
    }

    public ExcelTemplateException(HttpStatus status, String code, String message) {
        this(status, code, message, null);
    }

    public ExcelTemplateException(HttpStatus status, String code, String message, Throwable cause) {
        super(message, cause);
        this.status = status;
        this.code = code;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }
}
