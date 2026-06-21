package com.example.exceltemplate.common.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class ErrorCodeTest {

    @Test
    void shouldExposeCodeAndDefaultMessage() {
        assertEquals("VALIDATION_ERROR", ErrorCode.VALIDATION_ERROR.getCode());
        assertEquals("輸入資料驗證失敗", ErrorCode.VALIDATION_ERROR.getMessage());
        assertEquals("MISSING_REQUEST_PARAMETER", ErrorCode.MISSING_REQUEST_PARAMETER.getCode());
        assertEquals("缺少必要請求參數", ErrorCode.MISSING_REQUEST_PARAMETER.getMessage());
        assertEquals("REPORT_GENERATION_FAILED", ErrorCode.REPORT_GENERATION_FAILED.getCode());
        assertEquals("報表產生失敗", ErrorCode.REPORT_GENERATION_FAILED.getMessage());
        assertEquals("INTERNAL_SERVER_ERROR", ErrorCode.INTERNAL_SERVER_ERROR.getCode());
        assertEquals("系統忙碌，請稍後再試", ErrorCode.INTERNAL_SERVER_ERROR.getMessage());
    }
}