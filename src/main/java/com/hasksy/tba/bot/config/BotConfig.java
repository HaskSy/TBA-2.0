package com.hasksy.tba.bot.config;

import com.hasksy.tba.bot.TelegramBot;
import com.hasksy.tba.bot.MessageHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.bots.DefaultBotOptions;

@Configuration
public class BotConfig {

    private final String webHookPath = System.getenv("WEBHOOK_PATH");
    private final String botUsername = System.getenv("BOT_USERNAME");
    private final String botToken = System.getenv("BOT_TOKEN");

    /**
     * @param messageHandler Input processing object
     * @return configured Bot
     */
    @Bean(name = "telegramBot")
    public TelegramBot telegramBot(MessageHandler messageHandler) {
        DefaultBotOptions options = new DefaultBotOptions();

        TelegramBot telegramBot = new TelegramBot(options, messageHandler);
        telegramBot.setBotUsername(botUsername);
        telegramBot.setBotToken(botToken);
        telegramBot.setBotPath(webHookPath);

        return telegramBot;
    }

}
