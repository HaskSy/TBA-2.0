package com.hasksy.tba.services.google;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Builder class for search queries used in Google Drive API
 * @see <a href=https://developers.google.com/drive/api/guides/search-files#examples>Search for files and folders</a>
 * @implNote Only required for this application queries are implemented
 */
public class QueryBuilder {

    private final Map<String, String> params;

    QueryBuilder() {
        this.params = new HashMap<>();
    }

    private static void requireNotNullNotEmpty(String str) {
        if (str == null || str.isEmpty()) {
            throw new IllegalArgumentException("Passed in QueryBuilder arguments cannot be null or empty strings");
        }
    }

    public QueryBuilder setName(String name) {
        requireNotNullNotEmpty(name);
        params.put("name", "name = '"+ name + "'");
        return this;
    }

    public QueryBuilder setParent(String parent) {
        requireNotNullNotEmpty(parent);
        params.put("parent", "'" + parent + "' in parents");
        return this;
    }

    public QueryBuilder setTrashed(boolean trashed) {
        params.put("trashed", "trashed = " + trashed);
        return this;
    }

    public QueryBuilder setMimeType(String mimeType) {
        requireNotNullNotEmpty(mimeType);
        params.put("mimeType", "mimeType = '"+ mimeType + "'");
        return this;
    }

    public QueryBuilder setAppProperty(String key, String value) {
        requireNotNullNotEmpty(key);
        requireNotNullNotEmpty(value);
        params.put("AP_"+key, "appProperties has { key='" + key + "' and value='" + value + "' }");
        return this;
    }

    public QueryBuilder setAppProperties(Map<String, String> appProperties) {
        for (Map.Entry<String, String> entry : appProperties.entrySet()) {
            setAppProperty(entry.getKey(), entry.getValue());
        }
        return this;
    }

    public QueryBuilder clear() {
        params.clear();
        return this;
    }

    public String build() {
        StringBuilder query = new StringBuilder();
        Iterator<String> paramsIterator = this.params.values().iterator();

        while (paramsIterator.hasNext()) {
            query.append(paramsIterator.next());
            if (paramsIterator.hasNext()) {
                query.append(" and ");
            }
        }
        return query.toString();
    }

}
