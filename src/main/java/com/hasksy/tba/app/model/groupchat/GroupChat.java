package com.hasksy.tba.app.model.groupchat;

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
        name = "group_chats",
        indexes = {
                @Index(columnList = "telegram_chat_id", unique = true),
                @Index(columnList = "name")
        })
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GroupChat {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(name = "telegram_chat_id", unique = true, nullable = false)
    private Long telegramChatId;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "folder_id", unique = true)
    private String folderId;
    @Column(name = "tsv_chat_id", unique = true)
    private Long tsvChatId;
    @Column(name = "respond_stats", columnDefinition = "boolean default false", nullable = false)
    private boolean respondStats;

    public GroupChat(Long telegramChatId, String name) {
        this.telegramChatId = telegramChatId;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        GroupChat that = (GroupChat) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
