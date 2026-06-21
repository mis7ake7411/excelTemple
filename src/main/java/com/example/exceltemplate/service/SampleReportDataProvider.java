package com.example.exceltemplate.service;

import com.example.exceltemplate.model.SampleReportRow;

public interface SampleReportDataProvider {

    Iterable<SampleReportRow> findRows();
}