package com.tinkoff.prototypetinkoffbotassistant.bot.responses.reports;

import com.tinkoff.prototypetinkoffbotassistant.bot.BotState;
import com.tinkoff.prototypetinkoffbotassistant.bot.responses.ResponseAbstractFactory;
import com.tinkoff.prototypetinkoffbotassistant.model.UserMessage;
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
