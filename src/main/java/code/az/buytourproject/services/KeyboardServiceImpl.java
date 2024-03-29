package code.az.buytourproject.services;

import code.az.buytourproject.daos.interfaces.OperationDAO;
import code.az.buytourproject.models.Operation;
import code.az.buytourproject.models.Question;
import code.az.buytourproject.models.TelegramSession;
import code.az.buytourproject.services.interfaces.KeyboardService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Service
public class KeyboardServiceImpl implements KeyboardService {

    OperationDAO operationDAO;

    public KeyboardServiceImpl(OperationDAO operationDAO) {
        this.operationDAO = operationDAO;
    }

    @Override
    public ReplyKeyboardMarkup getKeyboardButtons(Question question, TelegramSession telegramSession) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(false);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);

        List<Operation> operations = operationDAO.findOperationsByQuestion(question);
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        for (Operation operation : operations) {
            if (telegramSession.getLocale().equals(operationDAO.findFirstOperation().getText_az())) {
                row.add(new KeyboardButton(operation.getText_az()));
            } else if (telegramSession.getLocale().equals(operationDAO.findFirstOperation().getText_en())) {
                row.add(new KeyboardButton(operation.getText_en()));
            } else if (telegramSession.getLocale().equals(operationDAO.findFirstOperation().getText_ru())) {
                row.add(new KeyboardButton(operation.getText_ru()));
            }
        }
        keyboard.add(row);
        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }

    @Override
    public ReplyKeyboardMarkup getLocaleKeyboard() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(false);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        Operation first_operation = operationDAO.findFirstOperation();
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add(first_operation.getText_az());
        row.add(first_operation.getText_en());
        row.add(first_operation.getText_ru());
        keyboard.add(row);
        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }
}
