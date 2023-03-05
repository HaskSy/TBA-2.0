package com.hasksy.tba.app.repository;

import com.hasksy.tba.app.model.registereduser.RegisteredUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RegisteredUserRepository extends JpaRepository<RegisteredUser, Long> {

    Optional<RegisteredUser> findByTelegramUserId(long telegramUserId);

}
