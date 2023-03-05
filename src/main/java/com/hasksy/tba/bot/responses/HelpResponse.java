package com.hasksy.tba.bot.responses;

import com.hasksy.tba.bot.BotCommand;
import com.hasksy.tba.model.UserMessage;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
public class HelpResponse implements ResponseAbstractFactory {

    private final String helpMessage = "Данный бот записывает вашу фамилию и статистику продаж по количеству: " + System.lineSeparator() +
            "кредитных карт (КК)" + System.lineSeparator() +
            "дебетовых карт (ДК)" + System.lineSeparator() +
            "инвестиций (ТИ)" + System.lineSeparator() +
            "сим-карт (СИМ)" + System.lineSeparator() +
            "перенесенных номеров (МНП)" + System.lineSeparator() +
            "количеству встреч (ВС)" + System.lineSeparator() + System.lineSeparator() +
            "Как пользоваться ботом:" + System.lineSeparator() +
            "   /reg - Ввести фамилию, чтобы бот вносил вашу стату в таблицу." + System.lineSeparator() +
            "   /send - Отправить статистику " + System.lineSeparator() +
            "Пример записей которые бот распознает:" + System.lineSeparator() + System.lineSeparator() +
            "   /reg Иванов Петр Сидорович" + System.lineSeparator() + System.lineSeparator() +
            "   /send кк 10 сим 4 дк 2 " + System.lineSeparator() + System.lineSeparator() +
            "   /send " + System.lineSeparator() +
            "   кк 10" + System.lineSeparator() +
            "   дк 4" + System.lineSeparator() +
            "   мнп 2 " + System.lineSeparator() + System.lineSeparator() +
            "Перед тем, как пользоваться командой /send вы должны ввести свою фамилию с помощью команды /reg (одинаковые фамилии не предусмотрены), если допустили опечатку в фамилии можно воспользоваться /reg повторно." + System.lineSeparator() + System.lineSeparator() +
            "Если получилось так, что данные введены неверно (записаны неверные числа), то можно дописать следующим /send можно дописать недостающее или с помощью отрицательных чисел вычесть лишнее." + System.lineSeparator() + System.lineSeparator() +
            "Бот может отвечать не сразу, если будет по каким-то причинам выключен. Не надо в этом случае отправлять лишние сообщения, при включении Бот обработает все сообщения, которые были ему адресованы." + System.lineSeparator() + System.lineSeparator() +
            "Так как бот вносит данные ссылаясь на дату и время, то в конце Рассчетного Периода (РП) не стоит задерживать с отправкой сообщений. В последний день РП нужно отправить все данные до конца дня.";

    @Override
    public SendMessage handle(UserMessage message) {
        return new SendMessage(Long.toString(message.getChatId()), helpMessage);
    }

    @Override
    public BotCommand getFactoryName() {
        return BotCommand.HELP;
    }

}
