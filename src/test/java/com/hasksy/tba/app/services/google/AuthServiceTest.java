package com.hasksy.tba.app.services.google;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String REFRESH_TOKEN = "fake_refresh_token";
    private static final String CLIENT_ID = "fake_client_id";
    private static final String CLIENT_SECRET = "fake_client_secret";
    private static final String ACCESS_TOKEN = "fake_access_token";
    private static final String CREDENTIALS_JSON = "{ \"installed\": { \"client_id\": \"" + CLIENT_ID + "\", \"client_secret\": \"" + CLIENT_SECRET + "\" } }";

    @Mock
    private ClassLoader authServiceClassLoader;
    @Mock
    private TokenRefresher tokenRefresher;

    private AuthService authService;
    private AutoCloseable autoCloseableMocks;

    @BeforeEach
    void setUp() {
        autoCloseableMocks = MockitoAnnotations.openMocks(this);
        authService = new AuthService(tokenRefresher);
    }

    @AfterEach
    void turnDown() throws Exception {
        autoCloseableMocks.close();
        authService = null;
    }

    @Test
    void testGetCredentials() throws IOException {
        // Setting up the response
        // Setting up the refresh request mock
        when(tokenRefresher.refreshAccessToken(anyString(), anyString(), anyString())).thenReturn(ACCESS_TOKEN);
        // set up application property
        System.setProperty("GOOGLE_REFRESH_TOKEN", REFRESH_TOKEN);

        // set up credentials.json file
        InputStream credentialsStream = new ByteArrayInputStream(CREDENTIALS_JSON.getBytes(StandardCharsets.UTF_8));
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(credentialsStream));
        InputStream in = new ByteArrayInputStream(CREDENTIALS_JSON.getBytes(StandardCharsets.UTF_8));
        when(authServiceClassLoader.getResourceAsStream(CREDENTIALS_FILE_PATH)).thenReturn(in);
        Thread.currentThread().setContextClassLoader(authServiceClassLoader);
        // invoke method
        Credential credential = authService.getCredentials(new NetHttpTransport());
        verify(tokenRefresher).refreshAccessToken(anyString(), anyString(), anyString());
        // verify credential object
        assertNotNull(credential);
        assertEquals(CLIENT_ID, clientSecrets.getDetails().getClientId());
        assertEquals(CLIENT_SECRET, clientSecrets.getDetails().getClientSecret());
        assertEquals(REFRESH_TOKEN, credential.getRefreshToken());
        assertEquals(ACCESS_TOKEN, credential.getAccessToken());
    }

    @Test
    void testGetCredentialsWithInvalidJsonResource() {
        when(authServiceClassLoader.getResourceAsStream(CREDENTIALS_FILE_PATH)).thenReturn(null);
        Thread.currentThread().setContextClassLoader(authServiceClassLoader);
        assertThrows(FileNotFoundException.class, () -> authService.getCredentials(new NetHttpTransport()));
    }

}
