package com.hasksy.tba.repository;

import com.hasksy.tba.model.GroupChat;
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
public class GroupChatRepositoryTest {

    @Resource
    private GroupChatRepository groupChatRepository;

    @Test
    public void test() {
        GroupChat groupChat = groupChatRepository
                .save(new GroupChat(1L, "test"));

        GroupChat foundChat = groupChatRepository
                .findById(groupChat.getId()).orElse(null);

        assertNotNull(foundChat);
        assertEquals(groupChat.getName(), foundChat.getName());
    }

    @Test
    public void testChatIdSearch() {
        GroupChat groupChat = groupChatRepository
                .save(new GroupChat(14124L, "test"));
        Optional<GroupChat> foundChat = groupChatRepository.findByTelegramChatId(groupChat.getTelegramChatId());

        assertTrue(foundChat.isPresent());
        assertEquals(groupChat.getName(), foundChat.get().getName());
    }

}
