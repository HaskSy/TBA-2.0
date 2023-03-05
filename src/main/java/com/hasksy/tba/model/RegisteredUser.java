package com.hasksy.tba.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Setter
@Table(
        name = "registered_users",
        indexes = {
                @Index(columnList = "telegram_user_id", unique = true),
                @Index(columnList = "name")
        })
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RegisteredUser {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(name = "telegram_user_id", unique = true)
    private Long telegramUserId;
    @Column(name = "name")
    private String name;

    public RegisteredUser(Long telegramUserId, String name) {
        this.telegramUserId = telegramUserId;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        RegisteredUser that = (RegisteredUser) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
