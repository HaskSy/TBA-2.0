package com.hasksy.tba.app.repository;

import com.hasksy.tba.app.model.groupchat.GroupChat;
import com.hasksy.tba.app.model.spreadsheet.Spreadsheet;
import com.hasksy.tba.app.model.spreadsheet.SpreadsheetType;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import javax.annotation.Resource;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@TestPropertySource("/database-test.properties")
public class SpreadsheetRepositoryTest {

    @Resource
    private SpreadsheetRepository spreadsheetRepository;

    @Test
    public void test() {
        GroupChat groupChat = new GroupChat(12345L, "Test Group");
        Spreadsheet spreadsheet = spreadsheetRepository
                .save(new Spreadsheet(groupChat, "test", "202203", SpreadsheetType.STATS));

        Spreadsheet foundSpreadsheet = spreadsheetRepository
                .findById(spreadsheet.getId()).orElse(null);

        assertNotNull(foundSpreadsheet);
        assertEquals(spreadsheet.getGoogleSpreadsheetId(), foundSpreadsheet.getGoogleSpreadsheetId());
        assertEquals(spreadsheet.getPeriodMarker(), foundSpreadsheet.getPeriodMarker());
        assertEquals(spreadsheet.getType(), foundSpreadsheet.getType());
        assertEquals(spreadsheet.getGroupChat(), foundSpreadsheet.getGroupChat());
    }

    @Test
    public void whenFindByGroupChatIdAndTypeAndPeriodMarker_thenReturnSpreadsheet() {
        GroupChat groupChat1 = new GroupChat(12345L, "Test Group");
        GroupChat groupChat2 = new GroupChat(12346L, "Test Group");

        Spreadsheet spreadsheet1 = spreadsheetRepository
                .save(new Spreadsheet(groupChat1, "test1", "test", SpreadsheetType.STATS));
        Spreadsheet spreadsheet2 = spreadsheetRepository
                .save(new Spreadsheet(groupChat2, "test2", "202203", SpreadsheetType.REPORT));
        Spreadsheet spreadsheet3 = spreadsheetRepository
                .save(new Spreadsheet(groupChat1, "test3", "202203", SpreadsheetType.REPORT));

        System.out.println(spreadsheet1.getGroupChat().getId());

        Optional<Spreadsheet> foundSpreadsheet = spreadsheetRepository.findByGroupChatIdAndTypeAndPeriodMarker(spreadsheet1.getGroupChat().getId(), SpreadsheetType.REPORT, "202203");

        assertTrue(foundSpreadsheet.isPresent());
        assertEquals(spreadsheet3, foundSpreadsheet.get());
        assertEquals(spreadsheet3.getType(), foundSpreadsheet.get().getType());
        assertEquals(spreadsheet3.getGoogleSpreadsheetId(), foundSpreadsheet.get().getGoogleSpreadsheetId());
        assertEquals(spreadsheet3.getPeriodMarker(), foundSpreadsheet.get().getPeriodMarker());
        assertEquals(spreadsheet3.getGroupChat().getId(), foundSpreadsheet.get().getGroupChat().getId());
    }
}
