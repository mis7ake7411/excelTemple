package com.example.exceltemplate.service;

import com.example.exceltemplate.model.ReportDownload;
import com.example.exceltemplate.model.SampleReportDefinition;
import com.example.exceltemplate.report.excel.SampleReportWorkbookBuilderImpl;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SampleReportServiceTest {

    private final SampleReportService service = new SampleReportService(
            new SampleReportWorkbookBuilderImpl(new SampleReportDefinition()),
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