package com.hasksy.tba.bot.responses.reports;

import com.hasksy.tba.bot.BotState;
import com.hasksy.tba.bot.responses.ResponseAbstractFactory;
import com.hasksy.tba.model.UserMessage;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
public class ReportResponse implements ResponseAbstractFactory {

    @Override
    public SendMessage handle(@NotNull UserMessage message) {
        return new SendMessage(Long.toString(message.getChatId()), "ReportHandler");
    }

    @Override
    public BotState getFactoryName() {
        return BotState.REPORT_COLLECTING;
    }

}
