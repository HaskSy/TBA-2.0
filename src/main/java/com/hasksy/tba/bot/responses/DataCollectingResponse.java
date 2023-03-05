package com.hasksy.tba.bot.responses;

import com.hasksy.tba.app.ApplicationService;
import com.hasksy.tba.app.exceptions.InvalidNumberDataException;
import com.hasksy.tba.app.exceptions.MessageHandlingException;
import com.hasksy.tba.app.exceptions.UserNotRegisteredException;
import com.hasksy.tba.bot.BotCommand;
import com.hasksy.tba.app.model.spreadsheet.UserData;
import com.hasksy.tba.bot.model.UserMessage;
import com.hasksy.tba.app.model.spreadsheet.SpreadsheetType;
import com.vdurmont.emoji.EmojiParser;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;

@Component
public class DataCollectingResponse implements ResponseAbstractFactory {

    private final ApplicationService applicationService;

    DataCollectingResponse(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @Override
    public SendMessage handle(@NotNull UserMessage message) {
        String name;
        try {
            name = applicationService.getName(message.getUserId());
        }
        catch (UserNotRegisteredException e) {
            return new SendMessage(Long.toString(message.getChatId()), "[" + message.getUsername() + "]: Вы должны сначала записать свое ФИО через /reg");
        }

        String[] dataList = message.getText()
                .replaceFirst(getFactoryName().getPrefix() + " ", "")
                .toUpperCase().split("\\s|\\n");

        UserData userData = new UserData();
        userData.setColumnData("ФИО", name);
        userData.setColumnData("ID", message.getUserId().toString());

        LinkedHashSet<Object> headers = userData.getHeaders();

        boolean correctFlag = true;
        for (int i = 0; i < dataList.length / 2; i++) {
            int index = i * 2;
            if (headers.contains(dataList[index]) && isInteger(dataList[index + 1])) {
                userData.setColumnData(dataList[index], dataList[index + 1]);
            }
            else {
                correctFlag = false;
                break;
            }
        }

        if (correctFlag) {
            List<List<Object>> result;
            try {
                result = applicationService.processMessage(userData, message.getTimestampMillis(), message.getChatId(), SpreadsheetType.STATS);
            } catch (MessageHandlingException e) {
                if (e instanceof InvalidNumberDataException) {
                    return new SendMessage(Long.toString(message.getChatId()), "Переданные числа слишком большие, либо в таблице ошибка" + EmojiParser.parseToUnicode(":x:"));
                }
                throw new RuntimeException(e);
            }

            return new SendMessage(Long.toString(message.getChatId()), Arrays.toString(result.get(0).toArray()));
        }
        return new SendMessage(Long.toString(message.getChatId()), "[" + name + "]: Данные введены неверно " + EmojiParser.parseToUnicode(":x:"));

    }

    @Override
    public BotCommand getFactoryName() {
        return BotCommand.DATA_COLLECTING;
    }

    private static boolean isInteger(String s) {
        if (s == null || s.isBlank()) return false;
        int i = 0;
        if (s.charAt(i) == '-') {
            if (s.length() == 1) {
                return false;
            }
            i = 1;
        }
        for (; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }
}
