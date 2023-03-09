package com.hasksy.tba.app.repository;

import com.hasksy.tba.app.model.registereduser.RegisteredUser;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import javax.annotation.Resource;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@TestPropertySource("/database-test.properties")
public class RegisteredUserRepositoryTest {

    @Resource
    private RegisteredUserRepository registeredUserRepository;

    @Test
    public void test() {
        RegisteredUser registeredUser = registeredUserRepository
                .save(new RegisteredUser(1L, "test"));

        RegisteredUser foundUser = registeredUserRepository
                .findById(registeredUser.getId()).orElse(null);

        assertNotNull(foundUser);
        assertEquals(registeredUser.getName(), foundUser.getName());
    }

    @Test
    public void whenFindByTelegramUserId_thenReturnRegisteredUser() {
        RegisteredUser registeredUser = registeredUserRepository
                .save(new RegisteredUser(14124L, "test"));
        Optional<RegisteredUser> foundUser = registeredUserRepository.findByTelegramUserId(registeredUser.getTelegramUserId());

        assertTrue(foundUser.isPresent());
        assertEquals(registeredUser.getName(), foundUser.get().getName());
    }
}
