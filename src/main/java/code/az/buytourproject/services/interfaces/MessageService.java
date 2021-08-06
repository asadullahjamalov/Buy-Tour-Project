package code.az.buytourproject.services.interfaces;

import code.az.buytourproject.models.TelegramSession;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface MessageService {

    SendMessage sendWrongAnswerMessage(long chatId, String locale);

    SendMessage sendSelectLanguageMessage(long chatId);

    SendMessage sendStartAndSelectLanguageMessage(long chatId);

    SendMessage sendStartBeforeStopMessage(long chatId);

    SendMessage sendStopSessionMessage(long chatId, String locale, TelegramSession telegramSession);

    SendMessage sendStartAgainAndSelectLanguageMessage(long chatId);

    SendMessage sendSessionAlreadyStartedMessage(long chatId, String locale);

    SendMessage sendFirstMessage(long chatId);

    SendMessage sendSurveyFinishedMessage(long chatId, String locale, String finishedSurvey);

    String getCaptionByLocale(String locale);

}
