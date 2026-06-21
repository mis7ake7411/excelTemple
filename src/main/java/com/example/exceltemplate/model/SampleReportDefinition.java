package com.example.exceltemplate.model;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class SampleReportDefinition {

    private final String sheetName;
    private final List<String> headers;

    public SampleReportDefinition() {
        this("Sample Report", Arrays.asList("ID", "Name", "Department"));
    }

    public SampleReportDefinition(String sheetName, List<String> headers) {
        this.sheetName = sheetName;
        this.headers = Collections.unmodifiableList(new ArrayList<String>(headers));
    }

    public String getSheetName() {
        return sheetName;
    }

    public List<String> getHeaders() {
        return headers;
    }
}