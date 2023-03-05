package com.hasksy.tba.app.services.google;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;

import static org.springframework.test.util.AssertionErrors.assertEquals;

public class QueryBuilderTest {

    private QueryBuilder queryBuilder;

    @BeforeEach
    void setUp() {
        this.queryBuilder = new QueryBuilder();
    }

    @Test
    public void testWithPropertyParent() {
        String actual = this.queryBuilder.setParent("testParent").build();
        String expected = "'testParent' in parents";
        assertEquals("Generated Query", expected, actual);
    }

    @Test
    public void testWithPropertyTrashed() {
        String actual = this.queryBuilder.setTrashed(false).build();
        String expected = "trashed = false";
        assertEquals("Generated Query", expected, actual);
    }

    @Test
    public void testWithPropertyName() {
        String actual = this.queryBuilder.setName("testName").build();
        String expected = "name = 'testName'";
        assertEquals("Generated Query", expected, actual);
    }

    @Test
    public void testWithPropertyMimeType() {
        String actual = this.queryBuilder.setMimeType(MimeType.GDRIVE_FOLDER).build();
        String expected = "mimeType = 'application/vnd.google-apps.folder'";
        assertEquals("Generated Query", expected, actual);
    }

    @Test
    public void testWithAppProperty() {
        String actual = this.queryBuilder.setAppProperty("testAppProperty", "It's Value").build();
        String expected = "appProperties has { key='testAppProperty' and value='It's Value' }";
        assertEquals("Generated Query", expected, actual);
    }

    @Test
    public void testWithAppPropertiesMap() {
        Map<String, String> appProperties = Map.of(
                "key1", "value1",
                "key2", "value2",
                "key3", "value3"
        );

        String actual = this.queryBuilder.setAppProperties(appProperties).build();
        String expected = "appProperties has { key='key3' and value='value3' } and appProperties has { key='key2' and value='value2' } and appProperties has { key='key1' and value='value1' }";
        assertEquals("Generated Query", expected, actual);
    }

    @Test
    public void testNullAndEmptyCheckWithPropertyParent() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> this.queryBuilder.setParent(null).build(), "Passed in QueryBuilder arguments cannot be null or empty strings");
        Assertions.assertThrows(IllegalArgumentException.class, () -> this.queryBuilder.setParent("").build(), "Passed in QueryBuilder arguments cannot be null or empty strings");
    }

    @Test
    public void testNullAndEmptyCheckWithPropertyName() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> this.queryBuilder.setName(null).build(), "Passed in QueryBuilder arguments cannot be null or empty strings");
        Assertions.assertThrows(IllegalArgumentException.class, () -> this.queryBuilder.setName("").build(), "Passed in QueryBuilder arguments cannot be null or empty strings");
    }

    @Test
    public void testNullAndEmptyCheckWithPropertyMimeType() {
        Assertions.assertThrows(NullPointerException.class, () -> this.queryBuilder.setMimeType(null).build());
    }

    @Test
    public void testNullAndEmptyCheckWithAppProperty() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> this.queryBuilder.setAppProperty(null, "String").build(), "Passed in QueryBuilder arguments cannot be null or empty strings");
        Assertions.assertThrows(IllegalArgumentException.class, () -> this.queryBuilder.setAppProperty("", "String").build(), "Passed in QueryBuilder arguments cannot be null or empty strings");
        Assertions.assertThrows(IllegalArgumentException.class, () -> this.queryBuilder.setAppProperty("String", null).build(), "Passed in QueryBuilder arguments cannot be null or empty strings");
        Assertions.assertThrows(IllegalArgumentException.class, () -> this.queryBuilder.setAppProperty("String", "").build(), "Passed in QueryBuilder arguments cannot be null or empty strings");
    }

    @Test
    public void testWithMultipleProperties() {
        String[] actual = this.queryBuilder.setName("testName").setTrashed(false).setMimeType(MimeType.GDRIVE_FOLDER).build().split(" and ");
        String[] expected = new String[]{"trashed = false", "name = 'testName'", "mimeType = 'application/vnd.google-apps.folder'"};
        assertEquals("Generated Query", Set.of(expected), Set.of(actual));
    }
}
