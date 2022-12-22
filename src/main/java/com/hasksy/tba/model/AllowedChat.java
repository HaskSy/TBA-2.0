package com.hasksy.tba.model;

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
        name = "allowed_chats",
        indexes = {
                @Index(columnList = "telegram_chat_id", unique = true),
                @Index(columnList = "name")
        })
@NoArgsConstructor
public class AllowedChat {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(name = "telegram_chat_id", unique = true)
    private Long telegramChatId;
    @Column(name = "name")
    private String name;

    public AllowedChat(Long telegramChatId, String name) {
        this.telegramChatId = telegramChatId;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        AllowedChat that = (AllowedChat) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
