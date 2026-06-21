package com.example.exceltemplate.service;

import com.example.exceltemplate.model.SampleReportRow;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class SampleReportDataProviderImpl implements SampleReportDataProvider {

    @Override
    public Iterable<SampleReportRow> findRows() {
        return Arrays.asList(
                new SampleReportRow(1L, "Alice", "Sales"),
                new SampleReportRow(2L, "Bob", "Engineering"));
    }
}