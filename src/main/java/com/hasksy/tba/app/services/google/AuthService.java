package com.hasksy.tba.app.services.google;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleRefreshTokenRequest;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.sheets.v4.SheetsScopes;
import org.slf4j.Logger;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

/**
 * Infrastructure service to get authorized to google and get future access to Google APIs
 */
class AuthService {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(AuthService.class);

    private static final List<String> SCOPES = Arrays.asList(SheetsScopes.SPREADSHEETS, DriveScopes.DRIVE_FILE); // App's working scopes
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json"; //OAuth 2.0 client credentials json

    private static String refreshAccessToken(String refreshToken, String clientId, String clientSecret) throws IOException {
        log.info("Trying to refresh Google OAuth 2.0 access token...");
        TokenResponse tokenResponse = new GoogleRefreshTokenRequest(new NetHttpTransport(), new GsonFactory(),
                refreshToken, clientId, clientSecret).setScopes(SCOPES).setGrantType("refresh_token").execute();
        return tokenResponse.getAccessToken();
    }

    protected static Credential getCredentials(NetHttpTransport httpTransport) throws IOException {
        log.info("Obtaining Google OAuth 2.0 credentials...");
        InputStream in = AuthService.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        String clientId = clientSecrets.getDetails().getClientId();
        String clientSecret = clientSecrets.getDetails().getClientSecret();

        String refreshToken = System.getenv("GOOGLE_REFRESH_TOKEN");
        GoogleCredential credential = new GoogleCredential.Builder()
                .setTransport(httpTransport)
                .setJsonFactory(JSON_FACTORY)
                .setClientSecrets(clientId, clientSecret)
                .build();

        credential.setRefreshToken(refreshToken);
        credential.setAccessToken(refreshAccessToken(refreshToken, clientId, clientSecret));

        return credential;
    }

}
