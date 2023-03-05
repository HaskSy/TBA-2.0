package com.hasksy.tba.model;

import com.hasksy.tba.model.spreadsheet.SpreadsheetType;

import java.util.LinkedHashSet;

public class ReportData extends SpreadsheetData {

    private LinkedHashSet<Object> headers;

    @Override
    public LinkedHashSet<Object> getHeaders() {
        if (this.headers == null) {
            this.headers = new LinkedHashSet<>(
                    SpreadsheetType.REPORT.getHeaders()
            );
        }
        return this.headers;
    }
}
