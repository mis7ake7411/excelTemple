package com.example.exceltemplate.common.exception;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = GlobalExceptionHandlerTestController.class)
@Import(GlobalExceptionHandler.class)
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnStructuredBodyForBusinessException() throws Exception {
        mockMvc.perform(get("/test/business-error"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value("REPORT_001"))
            .andExpect(jsonPath("$.message").value("報表參數錯誤"))
            .andExpect(jsonPath("$.path").value("/test/business-error"))
            .andExpect(jsonPath("$.errors").isEmpty());
    }

    @Test
    void shouldReturnFieldErrorsForValidationFailure() throws Exception {
        mockMvc.perform(post("/test/validate")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
            .andExpect(jsonPath("$.errors", hasSize(1)))
            .andExpect(jsonPath("$.errors[0]").value("name: name is required"));
    }

    @Test
    void shouldReturnGenericResponseForUnexpectedException() throws Exception {
        mockMvc.perform(get("/test/runtime-error"))
            .andExpect(status().isInternalServerError())
            .andExpect(jsonPath("$.code").value("INTERNAL_SERVER_ERROR"))
            .andExpect(jsonPath("$.message").value("系統忙碌，請稍後再試"))
            .andExpect(jsonPath("$.path").value("/test/runtime-error"));
    }

    @Test
    void shouldReturnBadRequestForMissingRequestParameter() throws Exception {
        mockMvc.perform(get("/test/required-param"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value("MISSING_REQUEST_PARAMETER"))
            .andExpect(jsonPath("$.message").value("缺少必要請求參數"))
            .andExpect(jsonPath("$.path").value("/test/required-param"))
            .andExpect(jsonPath("$.errors[0]").value("name: required"));
    }

    @Test
    void shouldReturnBadRequestForTypeMismatch() throws Exception {
        mockMvc.perform(get("/test/type-mismatch").param("id", "abc"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value("TYPE_MISMATCH"))
            .andExpect(jsonPath("$.message").value("請求參數格式錯誤"))
            .andExpect(jsonPath("$.path").value("/test/type-mismatch"))
            .andExpect(jsonPath("$.errors[0]").value("id: type mismatch"));
    }

    @Test
    void shouldReturnBadRequestForUnreadableRequestBody() throws Exception {
        mockMvc.perform(post("/test/validate")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value("MESSAGE_NOT_READABLE"))
            .andExpect(jsonPath("$.message").value("請求內容格式錯誤"))
            .andExpect(jsonPath("$.path").value("/test/validate"));
    }

    @Test
    void shouldReturnMethodNotAllowedForUnsupportedHttpMethod() throws Exception {
        mockMvc.perform(put("/test/business-error"))
            .andExpect(status().isMethodNotAllowed())
            .andExpect(jsonPath("$.code").value("METHOD_NOT_ALLOWED"))
            .andExpect(jsonPath("$.message").value("HTTP 方法不支援"))
            .andExpect(jsonPath("$.path").value("/test/business-error"));
    }

    @Test
    void shouldReturnUnsupportedMediaTypeForUnsupportedContentType() throws Exception {
        mockMvc.perform(post("/test/validate")
                .contentType(MediaType.TEXT_PLAIN)
                .content("name=Alice"))
            .andExpect(status().isUnsupportedMediaType())
            .andExpect(jsonPath("$.code").value("UNSUPPORTED_MEDIA_TYPE"))
            .andExpect(jsonPath("$.message").value("Content-Type 不支援"))
            .andExpect(jsonPath("$.path").value("/test/validate"));
    }

    @Test
    void shouldReturnBadRequestForConstraintViolation() throws Exception {
        mockMvc.perform(get("/test/constraint").param("id", "0"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
            .andExpect(jsonPath("$.message").value("輸入資料驗證失敗"))
            .andExpect(jsonPath("$.path").value("/test/constraint"))
            .andExpect(jsonPath("$.errors", hasSize(1)));
    }
}
