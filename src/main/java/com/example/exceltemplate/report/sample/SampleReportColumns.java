package com.example.exceltemplate.report.sample;

import com.example.exceltemplate.common.ExcelColumn;
import com.example.exceltemplate.model.SampleReportRow;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class SampleReportColumns {

    public List<ExcelColumn<SampleReportRow>> getColumns() {
        return Arrays.asList(
                new ExcelColumn<>("ID", row -> row.getId()),
                new ExcelColumn<>("姓名", row -> row.getName()),
                new ExcelColumn<>("部門", row -> row.getDepartment())
        );
    }
}
