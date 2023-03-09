package com.hasksy.tba.app.model.registereduser;

import org.junit.jupiter.api.*;
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
public class RegisteredUserTest {

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
    public void whenSameTelegramUserId_thenUniqueConstraintViolation() {
        RegisteredUser user1 = new RegisteredUser(12345L, "Alice");
        RegisteredUser user2 = new RegisteredUser(12345L, "Bob");
        entityManager.getTransaction().begin();
        entityManager.persist(user1);
        entityManager.flush();
        entityManager.clear();
        entityManager.persist(user2);
        assertThrows(PersistenceException.class, () -> entityManager.flush());
        entityManager.getTransaction().rollback();
    }

    @Test
    public void whenTelegramUserIdIsNull_thenNotNullConstraintViolation() {
        RegisteredUser user = new RegisteredUser(null, "Alice");
        entityManager.getTransaction().begin();
        assertThrows(PersistenceException.class, () -> entityManager.persist(user));
        entityManager.getTransaction().rollback();
    }

    @Test
    public void shouldCreateRegisteredUser() {
        RegisteredUser registeredUser = new RegisteredUser(12345L, "Alice");
        registeredUser.setId(1L);
        assertEquals(1L, registeredUser.getId());
        assertEquals(12345L, registeredUser.getTelegramUserId());
        assertEquals("Alice", registeredUser.getName());
    }

    @Test
    public void testEqualsAndHashCode() {
        RegisteredUser user1 = new RegisteredUser(12345L, "Alice");
        user1.setId(1L);
        RegisteredUser user2 = new RegisteredUser(12345L, "Alice");
        user2.setId(1L);
        RegisteredUser user3 = new RegisteredUser(67890L, "Bob");
        user3.setId(2L);

        assertEquals(user1, user2);
        assertNotEquals(user1, user3);

        assertEquals(user1.hashCode(), user2.hashCode());
    }
}