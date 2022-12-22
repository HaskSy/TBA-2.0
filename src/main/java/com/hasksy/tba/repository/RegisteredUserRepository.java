package com.hasksy.tba.repository;

import com.hasksy.tba.model.RegisteredUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RegisteredUserRepository extends JpaRepository<RegisteredUser, Long> {

    @Query("select u from RegisteredUser u where u.telegramUserId = :telegram_user_id")
    RegisteredUser getRegisteredUserByTelegramId(@Param("telegram_user_id") long telegramUserId);

}
