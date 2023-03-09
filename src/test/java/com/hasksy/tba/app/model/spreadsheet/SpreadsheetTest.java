package com.hasksy.tba.app.model.spreadsheet;

import com.hasksy.tba.app.model.groupchat.GroupChat;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@ActiveProfiles("test")
@TestPropertySource("/database-test.properties")
public class SpreadsheetTest {

    @Resource
    private EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    @BeforeEach
    public void setUp() {
        entityManager = entityManagerFactory.createEntityManager();
    }

    @AfterEach
    public void turnDown() {
        entityManager.close();
    }

    @Test
    public void whenSameGoogleSpreadsheetId_thenUniqueConstraintViolation() {
        GroupChat groupChat = new GroupChat(12345L, "Test Group");
        Spreadsheet spreadsheet1 = new Spreadsheet(groupChat, "test", "202203", SpreadsheetType.STATS);
        Spreadsheet spreadsheet2 = new Spreadsheet(groupChat, "test", "203", SpreadsheetType.REPORT);
        entityManager.getTransaction().begin();
        entityManager.persist(spreadsheet1);
        entityManager.flush();
        entityManager.persist(spreadsheet2);
        assertThrows(PersistenceException.class, () -> entityManager.flush());
        entityManager.getTransaction().rollback();
    }

    @Test
    public void whenGoogleSpreadsheetIdIsNull_thenNotNullConstraintViolation() {
        GroupChat groupChat = new GroupChat(12345L, "Test Group");
        Spreadsheet spreadsheet = new Spreadsheet(groupChat, null, "202203", SpreadsheetType.STATS);
        entityManager.getTransaction().begin();
        assertThrows(PersistenceException.class, () -> entityManager.persist(spreadsheet));
        entityManager.getTransaction().rollback();
    }

    @Test
    public void whenPeriodMarkerIsNull_thenNotNullConstraintViolation() {
        GroupChat groupChat = new GroupChat(12345L, "Test Group");
        Spreadsheet spreadsheet = new Spreadsheet(groupChat, "test", null, SpreadsheetType.STATS);
        entityManager.getTransaction().begin();
        assertThrows(PersistenceException.class, () -> entityManager.persist(spreadsheet));
        entityManager.getTransaction().rollback();
    }

    @Test
    public void whenTypeIsNull_thenNotNullConstraintViolation() {
        GroupChat groupChat = new GroupChat(12345L, "Test Group");
        Spreadsheet spreadsheet = new Spreadsheet(groupChat, "test", "marked", null);
        entityManager.getTransaction().begin();
        assertThrows(PersistenceException.class, () -> entityManager.persist(spreadsheet));
        entityManager.getTransaction().rollback();
    }

    @Test
    public void whenGroupChatIsNull_thenNotNullConstraintViolation() {
        Spreadsheet spreadsheet = new Spreadsheet(null, "test", "marked", null);
        entityManager.getTransaction().begin();
        assertThrows(PersistenceException.class, () -> entityManager.persist(spreadsheet));
        entityManager.getTransaction().rollback();
    }

    @Test
    public void shouldCreateSpreadsheet() {
        GroupChat groupChat = new GroupChat(12345L, "Test Group");
        Spreadsheet spreadsheet = new Spreadsheet(groupChat, "abc123", "202203", SpreadsheetType.STATS);
        spreadsheet.setId(1L);
        assertEquals(groupChat, spreadsheet.getGroupChat());
        assertEquals("abc123", spreadsheet.getGoogleSpreadsheetId());
        assertEquals("202203", spreadsheet.getPeriodMarker());
        assertEquals(SpreadsheetType.STATS, spreadsheet.getType());
    }

    @Test
    public void testEqualsAndHashCode() {
        GroupChat groupChat1 = new GroupChat(12345L, "Test Group 1");
        Spreadsheet spreadsheet1 = new Spreadsheet(groupChat1, "abc123", "202203", SpreadsheetType.STATS);
        spreadsheet1.setId(1L);
        Spreadsheet spreadsheet2 = new Spreadsheet(groupChat1, "def456", "202204", SpreadsheetType.REPORT);
        spreadsheet2.setId(2L);
        Spreadsheet spreadsheet3 = new Spreadsheet(groupChat1, "abc123", "202203", SpreadsheetType.STATS);
        spreadsheet3.setId(1L);

        // Test equality based on ID
        assertEquals(spreadsheet1, spreadsheet3);
        Assertions.assertNotEquals(spreadsheet1, spreadsheet2);

        // Test hash code
        assertEquals(spreadsheet1.hashCode(), spreadsheet3.hashCode());
    }

}