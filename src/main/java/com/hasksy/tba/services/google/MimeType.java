package com.hasksy.tba.services.google;

/**
 * Enum of Google Workspace and Google Drive specific MIME types
 * @implNote Only sufficient to this application are implemented
 * @see <a href=https://developers.google.com/drive/api/guides/mime-types>Full list of MIME types</a>
 */
public enum MimeType {
    GOOGLE_SPREADSHEETS("application/vnd.google-apps.spreadsheet"),
    GDRIVE_FOLDER("application/vnd.google-apps.folder");

    private final String value;

    MimeType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
