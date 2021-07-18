package code.az.buytourproject.services.interfaces;

import code.az.buytourproject.models.Question;
import code.az.buytourproject.models.TelegramSession;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

public interface KeyboardService {
    ReplyKeyboardMarkup getKeyboardButtons(Question question, TelegramSession telegramSession);

    ReplyKeyboardMarkup getLocaleKeyboard();
}
