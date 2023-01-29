package com.hasksy.tba.repository;

import com.hasksy.tba.model.AllowedChat;
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
public class AllowedChatRepositoryTest {

    @Resource
    private AllowedChatRepository allowedChatRepository;

    @Test
    public void test() {
        AllowedChat allowedChat = allowedChatRepository
                .save(new AllowedChat(1L, "test"));

        AllowedChat foundChat = allowedChatRepository
                .findById(allowedChat.getId()).orElse(null);

        assertNotNull(foundChat);
        assertEquals(allowedChat.getName(), foundChat.getName());
    }

    @Test
    public void testChatIdSearch() {
        AllowedChat allowedChat = allowedChatRepository
                .save(new AllowedChat(14124L, "test"));
        Optional<AllowedChat> foundChat = allowedChatRepository.findByTelegramChatId(allowedChat.getTelegramChatId());

        assertTrue(foundChat.isPresent());
        assertEquals(allowedChat.getName(), foundChat.get().getName());
    }

}
