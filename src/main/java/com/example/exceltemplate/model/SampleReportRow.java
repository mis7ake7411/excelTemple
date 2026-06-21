package com.example.exceltemplate.model;

public class SampleReportRow {

    private final Long id;
    private final String name;
    private final String department;

    public SampleReportRow(Long id, String name, String department) {
        this.id = id;
        this.name = name;
        this.department = department;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDepartment() {
        return department;
    }
}
