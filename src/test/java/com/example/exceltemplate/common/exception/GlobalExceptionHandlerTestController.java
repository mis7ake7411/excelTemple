package com.example.exceltemplate.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Validated
@RestController
@RequestMapping("/test")
class GlobalExceptionHandlerTestController {

    @GetMapping("/business-error")
    public String businessError() {
        throw new ExcelTemplateException(
                HttpStatus.BAD_REQUEST,
                "REPORT_001",
                "報表參數錯誤");
    }

    @PostMapping("/validate")
    public String validate(@Valid @RequestBody SampleRequest request) {
        return "ok";
    }

    @GetMapping("/runtime-error")
    public String runtimeError() {
        throw new IllegalStateException("unexpected");
    }

    @GetMapping("/required-param")
    public String requiredParam(@RequestParam String name) {
        return name;
    }

    @GetMapping("/type-mismatch")
    public String typeMismatch(@RequestParam Integer id) {
        return String.valueOf(id);
    }

    @GetMapping("/constraint")
    public String constraint(@RequestParam @Min(value = 1, message = "id must be greater than zero") Integer id) {
        return String.valueOf(id);
    }

    public static class SampleRequest {

        @NotBlank(message = "name is required")
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
