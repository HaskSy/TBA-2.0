package com.hasksy.tba.app.model.groupchat;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceException;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@TestPropertySource("/database-test.properties")
public class GroupChatTest {

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
    public void whenSameTelegramChatId_thenUniqueConstraintViolation() {
        GroupChat groupChat1 = new GroupChat(12345L, "Chat1");
        GroupChat groupChat2 = new GroupChat(12345L, "Chat2");

        entityManager.getTransaction().begin();
        entityManager.persist(groupChat1);
        entityManager.flush();
        entityManager.clear();
        entityManager.persist(groupChat2);
        assertThrows(PersistenceException.class, () -> entityManager.flush());
        entityManager.getTransaction().rollback();
    }

    @Test
    public void whenTelegramChatIdIsNull_thenNotNullConstraintViolation() {
        GroupChat groupChat = new GroupChat(null, "Chat1");
        entityManager.getTransaction().begin();
        assertThrows(PersistenceException.class, () -> entityManager.persist(groupChat));
        entityManager.getTransaction().rollback();
    }

    @Test
    public void whenNameIsNull_thenNotNullConstraintViolation() {
        GroupChat groupChat = new GroupChat(123L, null);
        entityManager.getTransaction().begin();
        assertThrows(PersistenceException.class, () -> entityManager.persist(groupChat));
        entityManager.getTransaction().rollback();
    }

    @Test
    public void whenSameFolderId_thenUniqueConstraintViolation() {
        GroupChat groupChat1 = new GroupChat(12345L, "Chat1");
        groupChat1.setFolderId("Folder");
        GroupChat groupChat2 = new GroupChat(6789L, "Chat2");
        groupChat2.setFolderId("Folder");
        entityManager.getTransaction().begin();
        entityManager.persist(groupChat1);
        entityManager.flush();
        entityManager.clear();
        entityManager.persist(groupChat2);
        assertThrows(PersistenceException.class, () -> entityManager.flush());
        entityManager.getTransaction().rollback();
    }

    @Test
    public void whenSamTsvChatId_thenUniqueConstraintViolation() {
        GroupChat groupChat1 = new GroupChat(12345L, "Chat1");
        groupChat1.setTsvChatId(11111L);
        GroupChat groupChat2 = new GroupChat(6789L, "Chat2");
        groupChat2.setTsvChatId(11111L);
        entityManager.getTransaction().begin();
        entityManager.persist(groupChat1);
        entityManager.flush();
        entityManager.clear();
        entityManager.persist(groupChat2);
        assertThrows(PersistenceException.class, () -> entityManager.flush());
        entityManager.getTransaction().rollback();
    }

    @Test
    public void shouldCreateGroupChat() {
        GroupChat groupChat = new GroupChat(12345L, "Chat1");
        groupChat.setId(1L);
        assertEquals(1L, groupChat.getId());
        assertEquals(12345L, groupChat.getTelegramChatId());
        assertEquals("Chat1", groupChat.getName());
    }

    @Test
    public void testEqualsAndHashCode() {
        GroupChat chat1 = new GroupChat(12345L, "Chat1");
        chat1.setId(1L);
        GroupChat chat2 = new GroupChat(12345L, "Chat1");
        chat2.setId(1L);
        GroupChat chat3 = new GroupChat(67890L, "Chat2");
        chat3.setId(2L);

        assertEquals(chat1, chat2);
        assertNotEquals(chat1, chat3);

        assertEquals(chat1.hashCode(), chat2.hashCode());
    }
}
