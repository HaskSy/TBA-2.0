package com.hasksy.tba.bot;

import com.hasksy.tba.bot.responses.ResponseAbstractFactory;
import com.hasksy.tba.model.UserMessage;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.HashMap;
import java.util.List;

/**
 * Controller which maps factories on bot states (dispatches message handling on specific handlers)
 */
@Component
public class BotModelController {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(MessageHandler.class);

    private final HashMap<BotState, ResponseAbstractFactory> responseFactoryMap = new HashMap<>();

    public BotModelController(@NotNull List<ResponseAbstractFactory> responseFactories) {
        responseFactories.forEach(responseFactory ->
                this.responseFactoryMap.put(
                        responseFactory.getFactoryName(),
                        responseFactory
                )
        );
    }

    public SendMessage execute(BotState currentState, @NotNull UserMessage message) {
        ResponseAbstractFactory responseFactory = selectResponseFactory(currentState);
        log.info("Starting handling AbstractResponseFactory: {}, Message: {}",
                responseFactory.getFactoryName(),
                message.getUserId());
        return responseFactory.handle(message);
    }

    private ResponseAbstractFactory selectResponseFactory(BotState currentState) {
        log.info("Trying to find BotState in responseFactoryMap: {}", currentState);
        return responseFactoryMap.get(currentState);
    }

}
