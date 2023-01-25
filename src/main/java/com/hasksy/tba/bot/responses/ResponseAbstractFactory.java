package com.hasksy.tba.bot.responses;

import com.hasksy.tba.bot.BotCommand;
import com.hasksy.tba.model.UserMessage;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

/**
 * Abstract factory for bot response messages
 * @implNote For proper work every implementation of it should be annotated with Spring's {@link Component} annotation
 */
public interface ResponseAbstractFactory {

    Logger log = org.slf4j.LoggerFactory.getLogger(ResponseAbstractFactory.class);

    /**
     * Main factory method
     * @param message User Input Message
     * @return Response message on specified input
     */
    SendMessage handle(UserMessage message);

    /**
     * @return BotState object, which shows on which bot state {@link #handle(UserMessage)} function should be triggered
     * @implNote each BotState object should be returned only in one implementation
     */
    BotCommand getFactoryName();

}
