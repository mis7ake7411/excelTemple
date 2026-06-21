package com.example.exceltemplate.common.exception;

public enum ErrorCode {

    VALIDATION_ERROR("VALIDATION_ERROR", "輸入資料驗證失敗"),
    MISSING_REQUEST_PARAMETER("MISSING_REQUEST_PARAMETER", "缺少必要請求參數"),
    TYPE_MISMATCH("TYPE_MISMATCH", "請求參數格式錯誤"),
    MESSAGE_NOT_READABLE("MESSAGE_NOT_READABLE", "請求內容格式錯誤"),
    METHOD_NOT_ALLOWED("METHOD_NOT_ALLOWED", "HTTP 方法不支援"),
    UNSUPPORTED_MEDIA_TYPE("UNSUPPORTED_MEDIA_TYPE", "Content-Type 不支援"),
    REPORT_GENERATION_FAILED("REPORT_GENERATION_FAILED", "報表產生失敗"),
    INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR", "系統忙碌，請稍後再試");

    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}