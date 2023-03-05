package com.hasksy.tba.bot.responses;

import com.hasksy.tba.app.ApplicationService;
import com.hasksy.tba.app.exceptions.MessageHandlingException;
import com.hasksy.tba.app.exceptions.SameReportIdException;
import com.hasksy.tba.bot.BotCommand;
import com.hasksy.tba.model.ReportData;
import com.hasksy.tba.model.UserMessage;
import com.hasksy.tba.model.spreadsheet.SpreadsheetType;
import com.vdurmont.emoji.EmojiParser;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ReportResponse implements ResponseAbstractFactory {

    private final ApplicationService applicationService;

    ReportResponse(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }
    @Override
    public SendMessage handle(@NotNull UserMessage message) {

        String regex = "^Привет, я (.+)\\n" +
                "Логин - (.+)\\n" +
                "Клиент - (.+)\\n" +
                "Id активности - (.+)\\n" +
                "Регион - (.+)\\n" +
                "Адрес встречи - (.+)\\n" +
                "\\n" +
                "Мой вопрос:([\\S\\s]+)$";
        if (!message.getText().matches(regex)) {
            return new SendMessage(Long.toString(message.getChatId()), "Данные введены неверно " + EmojiParser.parseToUnicode(":x:"));
        }
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(message.getText());
        ReportData reportData = new ReportData();
        reportData.setColumnData("Логин", matcher.group(2));
        reportData.setColumnData("Id активности", matcher.group(4));
        reportData.setColumnData("Вопрос", matcher.group(7));

        try {
            applicationService.processMessage(reportData, message.getTimestampMillis(), message.getChatId(), SpreadsheetType.REPORT);
            return new SendMessage(Long.toString(message.getChatId()), "Данные были успешно записаны " + EmojiParser.parseToUnicode(":white_check_mark:"));
        } catch (MessageHandlingException e) {
            if (e instanceof SameReportIdException) {
                return new SendMessage(Long.toString(message.getChatId()), "Данные с данным ID активности: " + reportData.getColumnData("Id активности") + " уже были внесены " + EmojiParser.parseToUnicode(":x:"));
            }
            throw new RuntimeException(e);
        }
    }

    @Override
    public BotCommand getFactoryName() {
        return BotCommand.REPORT_COLLECTING;
    }

}
