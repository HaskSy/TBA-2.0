package com.hasksy.tba.bot.responses.stats;

import com.hasksy.tba.bot.BotCommand;
import com.hasksy.tba.bot.responses.ResponseAbstractFactory;
import com.hasksy.tba.model.UserMessage;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
public class HelpResponse implements ResponseAbstractFactory {

    private final String helpMessage = "Данный бот записывает вашу фамилию и статистику продаж по количеству: ".concat(System.lineSeparator())
            .concat("кредитных карт (КК)").concat(System.lineSeparator())
            .concat("дебетовых карт (ДК)").concat(System.lineSeparator())
            .concat("инвестиций (ТИ)").concat(System.lineSeparator())
            .concat("сим-карт (СИМ)").concat(System.lineSeparator())
            .concat("перенесенных номеров (МНП)").concat(System.lineSeparator())
            .concat("количеству встреч (ВС)").concat(System.lineSeparator()).concat(System.lineSeparator())
            .concat("Как пользоваться ботом:").concat(System.lineSeparator())
            .concat("   /reg - Ввести фамилию, чтобы бот вносил вашу стату в таблицу.").concat(System.lineSeparator())
            .concat("   /send - Отправить статистику ").concat(System.lineSeparator())
            .concat("Пример записей которые бот распознает:").concat(System.lineSeparator()).concat(System.lineSeparator())
            .concat("   /reg Иванов Петр Сидорович").concat(System.lineSeparator()).concat(System.lineSeparator())
            .concat("   /send кк 10 сим 4 дк 2 ").concat(System.lineSeparator()).concat(System.lineSeparator())
            .concat("   /send ").concat(System.lineSeparator())
            .concat("   кк 10").concat(System.lineSeparator())
            .concat("   дк 4").concat(System.lineSeparator())
            .concat("   мнп 2 ").concat(System.lineSeparator()).concat(System.lineSeparator())
            .concat("Перед тем, как пользоваться командой /send вы должны ввести свою фамилию с помощью команды /reg (одинаковые фамилии не предусмотрены), если допустили опечатку в фамилии можно воспользоваться /reg повторно.").concat(System.lineSeparator()).concat(System.lineSeparator())
            .concat("Если получилось так, что данные введены неверно (записаны неверные числа), то можно дописать следующим /send можно дописать недостающее или с помощью отрицательных чисел вычесть лишнее.").concat(System.lineSeparator()).concat(System.lineSeparator())
            .concat("Бот может отвечать не сразу, если будет по каким-то причинам выключен. Не надо в этом случае отправлять лишние сообщения, при включении Бот обработает все сообщения, которые были ему адресованы.").concat(System.lineSeparator()).concat(System.lineSeparator())
            .concat("Так как бот вносит данные ссылаясь на дату и время, то в конце Рассчетного Периода (РП) не стоит задерживать с отправкой сообщений. В последний день РП нужно отправить все данные до конца дня.");

    @Override
    public SendMessage handle(UserMessage message) {
        return new SendMessage(Long.toString(message.getChatId()), helpMessage);
    }

    @Override
    public BotCommand getFactoryName() {
        return BotCommand.HELP;
    }

}
