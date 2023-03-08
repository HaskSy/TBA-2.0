package com.hasksy.tba.app.services.google;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Infrastructure service to get authorized to google and get future access to Google APIs
 */
@Service
@RequiredArgsConstructor
class AuthService {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(AuthService.class);
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json"; //OAuth 2.0 client credentials json

    private final TokenRefresher tokenRefresher;

    protected Credential getCredentials(NetHttpTransport httpTransport) throws IOException {
        log.info("Obtaining Google OAuth 2.0 credentials...");
        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        String clientId = clientSecrets.getDetails().getClientId();
        String clientSecret = clientSecrets.getDetails().getClientSecret();

        String refreshToken = System.getProperty("GOOGLE_REFRESH_TOKEN");
        GoogleCredential credential = new GoogleCredential.Builder()
                .setTransport(httpTransport)
                .setJsonFactory(JSON_FACTORY)
                .setClientSecrets(clientId, clientSecret)
                .build();

        credential.setRefreshToken(refreshToken);
        credential.setAccessToken(tokenRefresher.refreshAccessToken(refreshToken, clientId, clientSecret));

        return credential;
    }

}
