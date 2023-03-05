package com.hasksy.tba.model;

import java.util.*;

public abstract class SpreadsheetData {

    private final LinkedHashMap<String, String> data = new LinkedHashMap<>();

    {
        LinkedHashSet<Object> headers = getHeaders();
        for (Object header: headers) {
            this.data.putIfAbsent((String) header, null);
        }
    }

    abstract LinkedHashSet<Object> getHeaders();

    public String getColumnData(String columnName) {
        return this.data.get(columnName);
    }

    public List<Object> getData() {
        return new ArrayList<>(this.data.values());
    }

    public void setColumnData(String columnName, String data) {
        this.data.replace(columnName, data);
    }

}
