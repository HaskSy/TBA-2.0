package com.hasksy.tba.app.services.google;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Map;
import java.util.Set;

import static org.springframework.test.util.AssertionErrors.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ContextConfiguration(classes = QueryBuilder.class)
public class QueryBuilderTest {

    @Test
    public void testWithPropertyParent() {
        String actual = new QueryBuilder().setParent("testParent").build();
        String expected = "'testParent' in parents";
        assertEquals("Generated Query", expected, actual);
    }

    @Test
    public void testWithPropertyTrashed() {
        String actual = new QueryBuilder().setTrashed(false).build();
        String expected = "trashed = false";
        assertEquals("Generated Query", expected, actual);
    }

    @Test
    public void testWithPropertyName() {
        String actual = new QueryBuilder().setName("testName").build();
        String expected = "name = 'testName'";
        assertEquals("Generated Query", expected, actual);
    }

    @Test
    public void testWithPropertyMimeType() {
        String actual = new QueryBuilder().setMimeType(MimeType.GDRIVE_FOLDER).build();
        String expected = "mimeType = 'application/vnd.google-apps.folder'";
        assertEquals("Generated Query", expected, actual);
    }

    @Test
    public void testWithAppProperty() {
        String actual = new QueryBuilder().setAppProperty("testAppProperty", "It's Value").build();
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

        String actual = new QueryBuilder().setAppProperties(appProperties).build();
        String expected = "appProperties has { key='key3' and value='value3' } and appProperties has { key='key2' and value='value2' } and appProperties has { key='key1' and value='value1' }";
        assertEquals("Generated Query", expected, actual);
    }

    @Test
    public void testNullAndEmptyCheckWithPropertyParent() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new QueryBuilder().setParent(null).build(), "Passed in QueryBuilder arguments cannot be null or empty strings");
        Assertions.assertThrows(IllegalArgumentException.class, () -> new QueryBuilder().setParent("").build(), "Passed in QueryBuilder arguments cannot be null or empty strings");
    }

    @Test
    public void testNullAndEmptyCheckWithPropertyName() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new QueryBuilder().setName(null).build(), "Passed in QueryBuilder arguments cannot be null or empty strings");
        Assertions.assertThrows(IllegalArgumentException.class, () -> new QueryBuilder().setName("").build(), "Passed in QueryBuilder arguments cannot be null or empty strings");
    }

    @Test
    public void testNullAndEmptyCheckWithPropertyMimeType() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new QueryBuilder().setMimeType(null).build(), "Passed in QueryBuilder arguments cannot be null or empty strings");
    }

    @Test
    public void testNullAndEmptyCheckWithAppProperty() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new QueryBuilder().setAppProperty(null, "String").build(), "Passed in QueryBuilder arguments cannot be null or empty strings");
        Assertions.assertThrows(IllegalArgumentException.class, () -> new QueryBuilder().setAppProperty("", "String").build(), "Passed in QueryBuilder arguments cannot be null or empty strings");
        Assertions.assertThrows(IllegalArgumentException.class, () -> new QueryBuilder().setAppProperty("String", null).build(), "Passed in QueryBuilder arguments cannot be null or empty strings");
        Assertions.assertThrows(IllegalArgumentException.class, () -> new QueryBuilder().setAppProperty("String", "").build(), "Passed in QueryBuilder arguments cannot be null or empty strings");
    }

    @Test
    public void testWithMultipleProperties() {
        String[] actual = new QueryBuilder().setName("testName").setTrashed(false).setMimeType(MimeType.GDRIVE_FOLDER).build().split(" and ");
        String[] expected = new String[]{"trashed = false", "name = 'testName'", "mimeType = 'application/vnd.google-apps.folder'"};
        assertEquals("Generated Query", Set.of(expected), Set.of(actual));
    }
}
