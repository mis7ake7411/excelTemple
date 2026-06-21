package com.example.exceltemplate.web;

import com.example.exceltemplate.service.SampleReportService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.OutputStream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SampleReportController.class)
class SampleReportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SampleReportService sampleReportService;

    @Test
    void shouldStreamXlsxDownloadResponse() throws Exception {
        byte[] content = new byte[] { 1, 2, 3 };
        when(sampleReportService.getSampleReportFilename()).thenReturn("sample-report.xlsx");
        doAnswer(invocation -> {
            OutputStream outputStream = invocation.getArgument(0);
            outputStream.write(content);
            return null;
        }).when(sampleReportService).writeSampleReport(any(OutputStream.class));

        MvcResult mvcResult = mockMvc.perform(get("/api/reports/sample/download"))
                .andExpect(request().asyncStarted())
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type",
                        MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet").toString()))
                .andExpect(header().string("Content-Disposition", "attachment; filename=\"sample-report.xlsx\""))
                .andExpect(content().bytes(content));

        verify(sampleReportService).writeSampleReport(any(OutputStream.class));
    }
}