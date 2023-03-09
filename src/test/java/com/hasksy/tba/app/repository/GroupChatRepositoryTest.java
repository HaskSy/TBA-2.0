package com.hasksy.tba.app.repository;

import com.hasksy.tba.app.model.groupchat.GroupChat;
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
public class GroupChatRepositoryTest {

    @Resource
    private GroupChatRepository groupChatRepository;

    @Test
    public void test() {
        // Given
        GroupChat groupChat = groupChatRepository
                .save(new GroupChat(1L, "test"));
        // When
        GroupChat foundChat = groupChatRepository
                .findById(groupChat.getId()).orElse(null);
        // Then
        assertNotNull(foundChat);
        assertEquals(groupChat.getName(), foundChat.getName());
    }

    @Test
    public void whenFindByTelegramChatId_thenReturnGroupChat() {
        final long TELEGRAM_CHAT_ID = 1234L;
        final String NAME = "Test Chat";
        // Given
        GroupChat groupChat = new GroupChat(TELEGRAM_CHAT_ID, NAME);
        groupChatRepository.save(groupChat);
        // When
        Optional<GroupChat> found = groupChatRepository.findByTelegramChatId(TELEGRAM_CHAT_ID);
        // Then
        assertTrue(found.isPresent());
        assertEquals(TELEGRAM_CHAT_ID, found.get().getTelegramChatId());
        assertEquals(NAME, found.get().getName());
    }

    @Test
    public void whenFindByTelegramChatIdOrTsvChatId_thenReturnGroupChat() {
        final long ID = 1L;
        final long TELEGRAM_CHAT_ID = 1234L;
        final String NAME = "Test Chat";
        final long TSV_CHAT_ID = 5678L;
        final String FOLDER_IDENTIFIER = "FOLDER_IDENTIFIER";

        // Given
        GroupChat groupChat = new GroupChat(TELEGRAM_CHAT_ID, NAME);
        groupChat.setId(ID);
        groupChat.setTsvChatId(TSV_CHAT_ID);
        groupChat.setFolderId(FOLDER_IDENTIFIER);
        groupChat.setRespondStats(true);
        groupChatRepository.save(groupChat);

        // When
        Optional<GroupChat> foundByTelegramChatId = groupChatRepository.findByTelegramChatIdOrTsvChatId(TELEGRAM_CHAT_ID, 9999L);
        Optional<GroupChat> foundByTsvChatId = groupChatRepository.findByTelegramChatIdOrTsvChatId(9999L, TSV_CHAT_ID);

        // Then
        assertTrue(foundByTelegramChatId.isPresent());
        assertEquals(groupChat, foundByTelegramChatId.get());
        assertEquals(TELEGRAM_CHAT_ID, foundByTelegramChatId.get().getTelegramChatId());
        assertEquals(NAME, foundByTelegramChatId.get().getName());
        assertEquals(FOLDER_IDENTIFIER, foundByTelegramChatId.get().getFolderId());
        assertTrue(foundByTelegramChatId.get().isRespondStats());

        assertTrue(foundByTsvChatId.isPresent());
        assertEquals(groupChat, foundByTsvChatId.get());
        assertEquals(TSV_CHAT_ID, foundByTsvChatId.get().getTsvChatId());
        assertEquals(NAME, foundByTsvChatId.get().getName());
        assertEquals(FOLDER_IDENTIFIER, foundByTsvChatId.get().getFolderId());
        assertTrue(foundByTsvChatId.get().isRespondStats());
    }
}
