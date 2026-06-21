package com.example.exceltemplate.common;

import java.util.function.Function;

public class ExcelColumn<T> {

    private final String header;
    private final Function<T, Object> valueGetter;

    public ExcelColumn(String header, Function<T, Object> valueGetter) {
        this.header = header;
        this.valueGetter = valueGetter;
    }

    public String getHeader() {
        return header;
    }

    public Object getValue(T row) {
        return valueGetter.apply(row);
    }
}
