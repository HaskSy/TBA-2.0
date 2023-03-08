package com.hasksy.tba.app.services.google;

import com.google.api.client.googleapis.auth.oauth2.GoogleRefreshTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.sheets.v4.SheetsScopes;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
public class TokenRefresher {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(AuthService.class);

    private static final List<String> SCOPES = Arrays.asList(SheetsScopes.SPREADSHEETS, DriveScopes.DRIVE_FILE); // App's working scopes

    protected String refreshAccessToken(String refreshToken, String clientId, String clientSecret) throws IOException {
        log.info("Trying to refresh Google OAuth 2.0 access token...");
        GoogleRefreshTokenRequest tokenRequest = new GoogleRefreshTokenRequest(new NetHttpTransport(), new GsonFactory(),
                refreshToken, clientId, clientSecret).setScopes(SCOPES).setGrantType("refresh_token");
        GoogleTokenResponse tokenResponse = tokenRequest.execute();
        return tokenResponse.getAccessToken();
    }
}
