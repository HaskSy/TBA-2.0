package com.hasksy.tba.bot.responses.stats;

import com.hasksy.tba.bot.responses.ResponseAbstractFactory;
import com.hasksy.tba.bot.BotState;
import com.hasksy.tba.model.RegisteredUser;
import com.hasksy.tba.model.UserMessage;
import com.hasksy.tba.repository.RegisteredUserRepository;
import com.hasksy.tba.services.GoogleService;
import com.vdurmont.emoji.EmojiParser;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Optional;

@Component
public class RegisterResponse implements ResponseAbstractFactory {

    private final RegisteredUserRepository userRepository;
    public RegisterResponse(RegisteredUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public SendMessage handle(UserMessage message) {

        String textMessage = message.getText();

        if (textMessage.startsWith("/reg ") || textMessage.startsWith("/reg\n")) {

            String name = textMessage.replaceFirst("/reg ", "");

            Optional<RegisteredUser> optUser = userRepository.findByTelegramUserId(message.getUserId());
            optUser.ifPresentOrElse(
                    (user) -> {
                        user.setName(name);
                        GoogleService.updateUserName(message.getUserId(), name, message.getTimestamp(), message.getChatId());
                        userRepository.save(user);
                    },
                    () -> userRepository.save(new RegisteredUser(message.getUserId(), name))
            );

            return new SendMessage(Long.toString(message.getChatId()), "Ваше ФИО записано ".concat(EmojiParser.parseToUnicode(":white_check_mark:")));
        }
        return null;
    }

    @Override
    public BotState getFactoryName() {
        return BotState.REGISTER;
    }

}
