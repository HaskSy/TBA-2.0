package com.hasksy.tba.services.google;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.sheets.v4.Sheets;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * Service to work with Google Sheets API also containing some utility functions
 */
@Service
public class SheetsService {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(AuthService.class);

    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    private @NotNull Sheets getSheetsService() throws IOException, GeneralSecurityException {
        final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        return new Sheets.Builder(httpTransport, JSON_FACTORY, AuthService.getCredentials(httpTransport))
                .build();
    }
}
