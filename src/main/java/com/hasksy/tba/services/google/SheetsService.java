package com.hasksy.tba.services.google;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ClearValuesRequest;
import com.google.api.services.sheets.v4.model.ValueRange;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Map;

/**
 * Service to work with Google Sheets API also containing some utility functions
 */
@Service
public class SheetsService {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(AuthService.class);

    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    private final DriveService driveService;

    SheetsService(DriveService driveService) {
        this.driveService = driveService;
    }

    private @NotNull Sheets getSheetsService() throws IOException, GeneralSecurityException {
        final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        return new Sheets.Builder(httpTransport, JSON_FACTORY, AuthService.getCredentials(httpTransport))
                .build();
    }

    public String createSpreadsheet(String title, String parent, Map<String, String> appProperties) throws GeneralSecurityException, IOException {
        return driveService.createFile(title, parent, MimeType.GOOGLE_SPREADSHEETS, appProperties);
    }

    public List<List<Object>> getSpreadsheet(String spreadsheetId, String range) throws GeneralSecurityException, IOException {
        ValueRange values = getSheetsService().spreadsheets().values().get(spreadsheetId, range).execute();
        return values.getValues();
    }

    public void updateSpreadsheet(String spreadsheetId, List<List<Object>> data, String range) throws GeneralSecurityException, IOException {
        ValueRange appendBody = new ValueRange().setValues(data);
        getSheetsService().spreadsheets().values()
                .update(spreadsheetId, range, appendBody)
                .setValueInputOption("RAW")
                .execute();
    }

    public void updateSpreadsheet(String spreadsheetId, ValueRange valueRange) throws GeneralSecurityException, IOException {
        getSheetsService().spreadsheets().values()
                .update(spreadsheetId, valueRange.getRange(), valueRange)
                .setValueInputOption("RAW")
                .execute();
    }

    public void clearSpreadsheet(String spreadsheetId, String range) throws GeneralSecurityException, IOException {
        getSheetsService().spreadsheets().values().clear(spreadsheetId, range, new ClearValuesRequest());
    }

}
