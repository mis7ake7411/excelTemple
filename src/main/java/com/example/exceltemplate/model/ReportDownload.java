package com.example.exceltemplate.model;

public class ReportDownload {

    private final String filename;
    private final byte[] content;

    public ReportDownload(String filename, byte[] content) {
        this.filename = filename;
        this.content = content;
    }

    public String getFilename() {
        return filename;
    }

    public byte[] getContent() {
        return content;
    }
}
