package com.example.exceltemplate.service;

import com.example.exceltemplate.common.exception.ErrorCode;
import com.example.exceltemplate.common.exception.ExcelTemplateException;
import com.example.exceltemplate.model.ReportDownload;
import com.example.exceltemplate.model.SampleReportDefinition;
import com.example.exceltemplate.report.excel.PoiReportWorkbookBuilder;
import com.example.exceltemplate.report.sample.SampleReportColumns;
import com.example.exceltemplate.report.sample.SampleReportWorkbookBuilderImpl;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SampleReportServiceTest {

    private final SampleReportService service = new SampleReportService(
            new SampleReportWorkbookBuilderImpl(new SampleReportDefinition(), new SampleReportColumns(), new PoiReportWorkbookBuilder()),
            new SampleReportDataProviderImpl());

    @Test
    void shouldGenerateXlsxDownloadPayload() throws Exception {
        ReportDownload download = service.generateSampleReport();

        assertEquals("sample-report.xlsx", download.getFilename());
        assertWorkbookCanBeOpened(download.getContent());
    }

    @Test
    void shouldWriteSampleReportToOutputStream() throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        service.writeSampleReport(outputStream);

        assertWorkbookCanBeOpened(outputStream.toByteArray());
    }

    @Test
    void shouldWrapWriteIOExceptionWithReportGenerationError() throws IOException {
        IOException writeFailure = new IOException("write failed");
        ExcelTemplateException exception;
        try (OutputStream outputStream = new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                throw writeFailure;
            }

            @Override
            public void write(byte[] b, int off, int len) throws IOException {
                throw writeFailure;
            }
        }) {

            exception = assertThrows(
                    ExcelTemplateException.class,
                    () -> service.writeSampleReport(outputStream));
        }

        assertEquals(ErrorCode.REPORT_GENERATION_FAILED.getCode(), exception.getCode());
        assertSame(writeFailure, exception.getCause());
    }

    @Test
    void shouldExposeSampleReportFilename() {
        assertEquals("sample-report.xlsx", service.getSampleReportFilename());
    }

    private void assertWorkbookCanBeOpened(byte[] content) throws Exception {
        assertTrue(content.length > 0);
        try (Workbook workbook = WorkbookFactory.create(new ByteArrayInputStream(content))) {
            assertEquals("Sample Report", workbook.getSheetAt(0).getSheetName());
        }
    }
}
