package com.hasksy.tba.app.services.google;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Service to work with Google Drive API also containing some utility functions
 */
@Service
public class DriveService {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(AuthService.class);

    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    protected @NotNull Drive getDriveService() throws GeneralSecurityException, IOException {
        final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        return new Drive.Builder(httpTransport, JSON_FACTORY, AuthService.getCredentials(httpTransport))
                .build();
    }

    public List<File> findFile(QueryBuilder queryBuilder) throws GeneralSecurityException, IOException {
        String query = queryBuilder.build();
        log.info("Looking for files matching this query: {}", query);

        FileList result = getDriveService().files().list()
                .setQ(query)
                .setSpaces("drive")
                .setFields("files(id, name, appProperties)")
                .execute();

        return result.getFiles();
    }

    public String createFile(String name, String parent, MimeType mimeType, Map<String, String> appProperties) throws GeneralSecurityException, IOException {
        File fileMetadata = new File();
        fileMetadata.setName(name)
                .setParents(Collections.singletonList(parent))
                .setMimeType(mimeType.getValue())
                .setAppProperties(appProperties);

        return getDriveService().files().create(fileMetadata)
                .setFields("id")
                .execute().getId();
    }

    public void deleteFile(String fileId) throws GeneralSecurityException, IOException {
        getDriveService().files().delete(fileId).execute();
    }

    public String updateFile(String fileId, File fileContent) throws GeneralSecurityException, IOException {
        return getDriveService().files().update(fileId, fileContent).execute().getId();
    }

}
