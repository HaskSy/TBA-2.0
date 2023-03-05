package com.hasksy.tba.app.model.spreadsheet;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

public class UserData extends SpreadsheetData {

    private LinkedHashSet<Object> headers;

    @Override
    public LinkedHashSet<Object> getHeaders() {
        if (this.headers == null) {
            this.headers = new LinkedHashSet<>(
                    SpreadsheetType.STATS.getHeaders()
            );
        }
        return this.headers;
    }

    @Override
    public List<Object> getData() {
        return super.getData().stream().map((Object value) -> value == null ? "0" : value).collect(Collectors.toList());
    }
}
