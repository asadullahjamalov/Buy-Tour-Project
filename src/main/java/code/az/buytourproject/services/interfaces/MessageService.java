package code.az.buytourproject.services.interfaces;

import code.az.buytourproject.models.TelegramSession;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface MessageService {

    SendMessage sendWrongAnswerMessage(long chatId, String language);

    SendMessage sendSelectLanguageMessage(long chatId);

    SendMessage sendStartAndSelectLanguageMessage(long chatId);

    SendMessage sendStartBeforeStopMessage(long chatId);

    SendMessage sendStopSessionMessage(long chatId, String language, TelegramSession telegramSession);

    SendMessage sendStartAgainAndSelectLanguageMessage(long chatId);

    SendMessage sendSessionAlreadyStartedMessage(long chatId, String language);

}
