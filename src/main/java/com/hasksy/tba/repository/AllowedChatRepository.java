package com.hasksy.tba.repository;

import com.hasksy.tba.model.AllowedChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AllowedChatRepository extends JpaRepository<AllowedChat, Long> {

    @Query("select c from AllowedChat c where c.telegramChatId = :telegram_chat_id")
    AllowedChat getAllowedChatByTelegramId(@Param("telegram_chat_id") long telegramChatId);

}
