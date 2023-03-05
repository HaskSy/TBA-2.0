package com.hasksy.tba.app.repository;

import com.hasksy.tba.app.model.groupchat.GroupChat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GroupChatRepository extends JpaRepository<GroupChat, Long> {

    Optional<GroupChat> findByTelegramChatId(long telegramChatId);

    Optional<GroupChat> findByTelegramChatIdOrTsvChatId(long telegramChatId, long tsvChatId);


}
