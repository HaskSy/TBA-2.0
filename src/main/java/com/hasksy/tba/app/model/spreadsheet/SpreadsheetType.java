package com.hasksy.tba.app.model.spreadsheet;

import com.google.api.services.sheets.v4.model.ValueRange;
import com.hasksy.tba.app.exceptions.InvalidNumberDataException;
import com.hasksy.tba.app.exceptions.MessageHandlingException;
import com.hasksy.tba.app.exceptions.SameReportIdException;

import java.util.*;
import java.util.stream.Collectors;

public enum SpreadsheetType {

    STATS {
        @Override
        public List<Object> getHeaders() {
            return Arrays.asList("ФИО", "КК", "ДК", "ТИ", "СИМ", "МНП", "ВС", "ПД", "МБ", "ID");
        }
        @Override
        public ValueRange execute(List<List<Object>> sheetData, List<Object> userDataList) throws InvalidNumberDataException {
            int i = 1;
            for (; i < sheetData.size(); i++) {
                List<Object> row = sheetData.get(i).stream()
                        .map((Object v) -> v.toString().isBlank() ? "0" : v)
                        .collect(Collectors.toList());
                if (String.valueOf(row.get(userDataList.size()-1)).equals(String.valueOf(userDataList.get(userDataList.size()-1)))) {
                    List<Object> appendBody = new ArrayList<>(userDataList.size());
                    appendBody.add(userDataList.get(0));
                    try {
                        for (int j = 1; j < userDataList.size() - 1; j++) {
                            Object userDataItem = userDataList.get(j);
                            appendBody.add(
                                    userDataItem == null ?
                                            row.get(j) :
                                            Integer.parseInt(String.valueOf(row.get(j))) + Integer.parseInt(String.valueOf(userDataItem))
                            );
                        }
                    } catch (NumberFormatException e) {
                        throw new InvalidNumberDataException(e.getMessage());
                    }

                    appendBody.add(null);

                    return new ValueRange()
                            .setValues(Collections.singletonList(appendBody))
                            .setRange("Лист1!A"+(i+1))
                            .setMajorDimension("ROWS");
                }
            }
            return new ValueRange()
                    .setValues(Collections.singletonList(userDataList))
                    .setRange("Лист1!A"+(i+1))
                    .setMajorDimension("ROWS");
        }
    },

    REPORT {
        @Override
        public List<Object> getHeaders() {
            return Arrays.asList("Логин", "Id активности", "Вопрос");
        }
        @Override
        public ValueRange execute(List<List<Object>> sheetData, List<Object> userDataList) throws SameReportIdException {
            int i = 1;
            for (; i < sheetData.size(); i++) {
                List<Object> row = sheetData.get(i);
                if (String.valueOf(row.get(1)).equals(String.valueOf(userDataList.get(1)))) {
                    throw new SameReportIdException("");
                }
            }

            return new ValueRange()
                    .setValues(Collections.singletonList(userDataList))
                    .setRange("Лист1")
                    .setMajorDimension("ROWS");
        }
    };

    public abstract List<Object> getHeaders();
    public abstract ValueRange execute(List<List<Object>> sheetData, List<Object> userDataList) throws MessageHandlingException;

}
