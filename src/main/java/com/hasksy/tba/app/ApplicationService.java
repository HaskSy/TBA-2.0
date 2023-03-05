package com.hasksy.tba.app;

import com.google.api.services.drive.model.File;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.hasksy.tba.app.exceptions.MessageHandlingException;
import com.hasksy.tba.app.exceptions.UserNotRegisteredException;
import com.hasksy.tba.bot.ChatType;
import com.hasksy.tba.model.GroupChat;
import com.hasksy.tba.model.RegisteredUser;
import com.hasksy.tba.model.SpreadsheetData;
import com.hasksy.tba.model.spreadsheet.Spreadsheet;
import com.hasksy.tba.model.spreadsheet.SpreadsheetType;
import com.hasksy.tba.repository.GroupChatRepository;
import com.hasksy.tba.repository.RegisteredUserRepository;
import com.hasksy.tba.repository.SpreadsheetRepository;
import com.hasksy.tba.services.DateTimeService;
import com.hasksy.tba.services.google.*;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.*;

@Service
public class ApplicationService {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(ApplicationService.class);

    private final RegisteredUserRepository registeredUserRepository;
    private final GroupChatRepository groupChatRepository;
    private final DriveService driveService;
    private final SheetsService sheetsService;
    private final SpreadsheetRepository spreadsheetRepository;

    // Root folder ID
    private String applicationRootFolder;

    // Property for identifying Application's root folder despite it's location in drive
    private static final String APP_ROOT_PROPERTY = "appRootFolder";

    public ApplicationService(
            RegisteredUserRepository registeredUserRepository,
            GroupChatRepository groupChatRepository,
            DriveService driveService,
            SheetsService sheetsService,
            SpreadsheetRepository spreadsheetRepository
    ) {
        this.registeredUserRepository = registeredUserRepository;
        this.groupChatRepository = groupChatRepository;
        this.driveService = driveService;
        this.sheetsService = sheetsService;
        this.spreadsheetRepository = spreadsheetRepository;
    }

    public String getName(Long telegramUserId) throws UserNotRegisteredException {
        RegisteredUser user = this.registeredUserRepository.findByTelegramUserId(telegramUserId).orElseThrow(
                () -> {
                    log.error("User with id {} is not registered", telegramUserId);
                    return new UserNotRegisteredException("User with id " + telegramUserId + " is not registered");
                }
        );
        return user.getName();
    }

    /**
     * Defines chat type and marks for sanitizing
     * @param telegramChatId .
     * @return chat type
     */
    public ChatType getChatType(Long telegramChatId) {
        Optional<GroupChat> optGroupChat = groupChatRepository.findByTelegramChatIdOrTsvChatId(telegramChatId, telegramChatId);
        if (optGroupChat.isEmpty()) {
            return ChatType.FORBIDDEN;
        }
        if (optGroupChat.get().getTelegramChatId().equals(telegramChatId)) {
            return ChatType.STATS;
        }
        return ChatType.REPORT;
    }

    public void registerUser(Long telegramUserId, String name) {
        registeredUserRepository.findByTelegramUserId(telegramUserId)
                .ifPresentOrElse((user) -> {
                    user.setName(name);
                    registeredUserRepository.save(user);
                }, () -> registeredUserRepository.save(new RegisteredUser(telegramUserId, name)));
    }

    public List<List<Object>> processMessage(SpreadsheetData userData, Long timestampMillis, Long telegramChatId, SpreadsheetType type) throws MessageHandlingException {
        GroupChat groupChat = this.groupChatRepository.findByTelegramChatId(telegramChatId).orElseThrow(() -> {
            log.error("Passed chat ID is not present in database: {}", telegramChatId);
            throw new IllegalArgumentException("Passed chat ID is not present in database: " + telegramChatId);
        });
        String spreadsheetId = this.getCurrentGroupSpreadsheet(groupChat.getId(), timestampMillis, type);
        try {
            List<List<Object>> sheetData = this.sheetsService.getSpreadsheet(spreadsheetId, "Лист1");
            List<Object> userDataList = userData.getData();
            ValueRange valueRange = type.execute(sheetData, userDataList);
            this.sheetsService.updateSpreadsheet(spreadsheetId, valueRange);
            return valueRange.getValues();
        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getApplicationRootFolder() {
        if (this.applicationRootFolder == null) {
            this.applicationRootFolder = this.findOrCreateApplicationRootFolder();
        }
        return this.applicationRootFolder;
    }

    private String findOrCreateApplicationRootFolder() {
        // Find
        QueryBuilder query = new QueryBuilder()
                .setTrashed(false)
                .setMimeType(MimeType.GDRIVE_FOLDER)
                .setAppProperty(APP_ROOT_PROPERTY, APP_ROOT_PROPERTY);
        try {
            List<File> foundFiles = driveService.findFile(query);
            if (foundFiles.size() == 1) {
                return foundFiles.get(0).getId();
            }
            if (foundFiles.size() > 1) {
                log.error("Application cannot have multiple root folders");
                throw new IllegalStateException("Application cannot have multiple root folders");
            }
        } catch (GeneralSecurityException | IOException e) {
            log.error("Error while trying to find application root folder using following query: {}", query.build());
            throw new RuntimeException(e);
        }
        // Else create
        try {
            return driveService.createFile(
                    "tba_spreadsheets",
                    "root",
                    MimeType.GDRIVE_FOLDER,
                    Map.of(APP_ROOT_PROPERTY, APP_ROOT_PROPERTY));
        } catch (GeneralSecurityException | IOException e) {
            log.error("Error while trying to create root folder");
            throw new RuntimeException(e);
        }
    }

    private String getGroupFolder(GroupChat groupChat) {

        String folderId = groupChat.getFolderId();
        if (folderId != null && !folderId.isBlank()) {
            return folderId;
        }

        QueryBuilder query = new QueryBuilder()
                .setTrashed(false)
                .setMimeType(MimeType.GDRIVE_FOLDER)
                .setAppProperty("telegramChatId", groupChat.getTelegramChatId().toString());
        try {
            List<File> foundFiles = driveService.findFile(query);
            if (foundFiles.size() == 1) {
                folderId = foundFiles.get(0).getId();
                groupChat.setFolderId(folderId);
                this.groupChatRepository.save(groupChat);
                return folderId;
            }
            if (foundFiles.size() == 0) {
                return createGroupFolder(groupChat);
            }
            log.error("Group chat cannot have multiple folders: {}", groupChat.getTelegramChatId().toString());
            throw new IllegalStateException("Group chat cannot have multiple folders");
        } catch (GeneralSecurityException | IOException e) {
            log.error("Error while trying to find group folder using following query: {}", query.build());
            throw new RuntimeException(e);
        }
    }

    private String createGroupFolder(GroupChat groupChat) {
        try {
            String folderId =  driveService.createFile(
                    groupChat.getName(),
                    getApplicationRootFolder(),
                    MimeType.GDRIVE_FOLDER,
                    Map.of("telegramChatId", groupChat.getTelegramChatId().toString())
            );
            groupChat.setFolderId(folderId);
            this.groupChatRepository.save(groupChat);
            return folderId;
        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getCurrentGroupSpreadsheet(Long groupChatId, Long timestampMillis, SpreadsheetType type) {
        String periodMarker = DateTimeService.getPeriodMarker(timestampMillis);
        Optional<Spreadsheet> optSpreadsheet = this.spreadsheetRepository.findByGroupChatIdAndTypeAndPeriodMarker(
                groupChatId,
                type,
                periodMarker
        );
        if (optSpreadsheet.isPresent()) {
            return optSpreadsheet.get().getGoogleSpreadsheetId();
        }
        return findGroupMonthSpreadsheet(groupChatId, type, periodMarker);
    }

    private String findGroupMonthSpreadsheet(Long groupChatId, SpreadsheetType type, String periodMarker) {
        GroupChat groupChat = this.groupChatRepository.getReferenceById(groupChatId);

        QueryBuilder query = new QueryBuilder()
                .setTrashed(false)
                .setParent(getGroupFolder(groupChat))
                .setMimeType(MimeType.GOOGLE_SPREADSHEETS)
                .setAppProperty("groupChatId", groupChatId.toString())
                .setAppProperty("periodMarker", periodMarker)
                .setAppProperty("spreadsheetType", type.toString());
        try {
            List<File> foundFiles = driveService.findFile(query);
            String spreadsheetId;
            switch (foundFiles.size()) {
                case 1:
                    spreadsheetId = foundFiles.get(0).getId();
                    break;
                case 0:
                    spreadsheetId = createGroupMonthSpreadsheet(groupChat, periodMarker, type);
                    break;
                default:
                    log.error("Group chat cannot have multiple valid spreadsheets: {}\n" +
                            "FoundFiles: {}", groupChatId, foundFiles);
                    throw new IllegalStateException("Group chat cannot have multiple folders");
            }
            this.spreadsheetRepository.save(new Spreadsheet(groupChat, spreadsheetId, periodMarker, type));
            return spreadsheetId;
        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String createGroupMonthSpreadsheet(GroupChat groupChat, String periodMarker, SpreadsheetType type) {
        try {
            String spreadsheetId = sheetsService.createSpreadsheet(
                    groupChat.getName() + "-" + periodMarker,
                    groupChat.getFolderId(),
                    Map.of("groupChatId", groupChat.getTelegramChatId().toString(),
                            "periodMarker", periodMarker,
                            "spreadsheetType", type.toString())
            );
            sheetsService.updateSpreadsheet(spreadsheetId, new ValueRange().setValues(
                    Collections.singletonList(type.getHeaders())
            ).setRange("Лист1"));
            return spreadsheetId;
        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException(e);
        }
    }

}
