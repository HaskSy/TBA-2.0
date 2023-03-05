package com.hasksy.tba.bot.controller;

import org.slf4j.Logger;
import com.hasksy.tba.bot.TelegramBot;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import javax.annotation.Resource;

@RestController
@RequestMapping("update")
@Tag(name="WebHookController", description="Controller which detects update on Bot settled WebHook URL")
public class WebHookController {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(WebHookController.class);

    @Resource(name = "telegramBot")
    private final TelegramBot telegramBot;

    public WebHookController(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    @Operation(
            summary = "Catches new messages & Starts handling process",
            description = "After new message received to a Bot, HTTP callback getting triggered & and bot sends processing result " +
                    "via POST method to configured URL"
    )
    @PostMapping
    public BotApiMethod<?> onUpdateReceived(@RequestBody Update update) {
        log.info("Controller caught new WebHook Update! ID: {}",
                update.getUpdateId());
        return telegramBot.onWebhookUpdateReceived(update);
    }

}
