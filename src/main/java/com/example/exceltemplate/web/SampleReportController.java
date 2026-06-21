package com.example.exceltemplate.web;

import com.example.exceltemplate.service.SampleReportService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@RestController
@RequestMapping("/api/reports/sample")
public class SampleReportController {

    private static final MediaType XLSX_MEDIA_TYPE = MediaType.parseMediaType(
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

    private final SampleReportService sampleReportService;

    public SampleReportController(SampleReportService sampleReportService) {
        this.sampleReportService = sampleReportService;
    }

    @GetMapping("/download")
    public ResponseEntity<StreamingResponseBody> download() {
        StreamingResponseBody body = outputStream -> sampleReportService.writeSampleReport(outputStream);
        return ResponseEntity.ok()
                .contentType(XLSX_MEDIA_TYPE)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + sampleReportService.getSampleReportFilename() + "\"")
                .body(body);
    }
}