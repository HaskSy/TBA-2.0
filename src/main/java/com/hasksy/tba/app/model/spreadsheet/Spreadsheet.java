package com.hasksy.tba.app.model.spreadsheet;

import com.hasksy.tba.app.model.groupchat.GroupChat;
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
        name = "spreadsheets",
        indexes = {
                @Index(columnList = "group_chat_id, type, period_marker", unique = true)
        })
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Spreadsheet {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "group_chat_id", referencedColumnName = "id", nullable = false)
    private GroupChat groupChat;
    @Column(name = "google_spreadsheet_id", unique = true, nullable = false)
    private String googleSpreadsheetId;
    @Column(name = "period_marker", nullable = false)
    private String periodMarker;
    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private SpreadsheetType type;


    public Spreadsheet(GroupChat groupChat, String googleSpreadsheetId, String periodMarker, SpreadsheetType type) {
        this.groupChat = groupChat;
        this.googleSpreadsheetId = googleSpreadsheetId;
        this.periodMarker = periodMarker;
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Spreadsheet that = (Spreadsheet) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
