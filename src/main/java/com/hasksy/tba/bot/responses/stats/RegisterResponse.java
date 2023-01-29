package com.hasksy.tba.bot.responses.stats;

import com.hasksy.tba.bot.responses.ResponseAbstractFactory;
import com.hasksy.tba.bot.BotCommand;
import com.hasksy.tba.model.RegisteredUser;
import com.hasksy.tba.model.UserMessage;
import com.hasksy.tba.repository.RegisteredUserRepository;
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

        String name = textMessage.replaceFirst(getFactoryName().getPrefix() + " ", "");

        Optional<RegisteredUser> optUser = userRepository.findByTelegramUserId(message.getUserId());
        optUser.ifPresentOrElse(
                (user) -> {
                    user.setName(name);
                    userRepository.save(user);
                },
                () -> userRepository.save(new RegisteredUser(message.getUserId(), name))
        );

        return new SendMessage(Long.toString(message.getChatId()), "Ваше ФИО записано ".concat(EmojiParser.parseToUnicode(":white_check_mark:")));
    }

    @Override
    public BotCommand getFactoryName() {
        return BotCommand.REGISTER;
    }

}
