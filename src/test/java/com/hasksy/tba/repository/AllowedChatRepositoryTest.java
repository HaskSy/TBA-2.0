package com.hasksy.tba.repository;

import com.hasksy.tba.model.AllowedChat;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@Transactional
@ActiveProfiles("test")
@SpringBootTest
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
        AllowedChat foundChat = allowedChatRepository
                .getAllowedChatByTelegramId(allowedChat.getTelegramChatId());

        assertNotNull(foundChat);
        assertEquals(allowedChat.getName(), foundChat.getName());
    }

}
