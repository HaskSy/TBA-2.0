package com.hasksy.tba.repository;

import com.hasksy.tba.model.AllowedChat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AllowedChatRepository extends JpaRepository<AllowedChat, Long> {

    Optional<AllowedChat> findByTelegramChatId(long telegramChatId);

}
