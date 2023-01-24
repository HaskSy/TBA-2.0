package com.hasksy.tba.repository;

import com.hasksy.tba.model.RegisteredUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RegisteredUserRepository extends JpaRepository<RegisteredUser, Long> {

    Optional<RegisteredUser> findByTelegramUserId(long telegramUserId);

}
