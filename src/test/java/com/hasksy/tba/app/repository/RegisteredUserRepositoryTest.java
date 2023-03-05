package com.hasksy.tba.app.repository;

import com.hasksy.tba.app.model.registereduser.RegisteredUser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@Transactional
@ActiveProfiles("test")
@SpringBootTest
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
    public void testUserIdSearch() {
        RegisteredUser registeredUser = registeredUserRepository
                .save(new RegisteredUser(14124L, "test"));
        Optional<RegisteredUser> foundUser = registeredUserRepository.findByTelegramUserId(registeredUser.getTelegramUserId());

        assertTrue(foundUser.isPresent());
        assertEquals(registeredUser.getName(), foundUser.get().getName());
    }

}
