package code.az.buytourproject.processors.handlers;

import code.az.buytourproject.enums.OperationType;
import code.az.buytourproject.models.Operation;
import code.az.buytourproject.models.Question;
import code.az.buytourproject.models.TelegramSession;
import code.az.buytourproject.repositories.OperationRepo;
import code.az.buytourproject.repositories.QuestionRepo;
import code.az.buytourproject.services.KeyboardServiceImpl;
import code.az.buytourproject.services.interfaces.MessageService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.util.List;

@Component
public class MessageHandler {
    OperationRepo operationRepo;
    QuestionRepo questionRepo;
    KeyboardServiceImpl keyboardService;
    MessageService messageService;

    public MessageHandler(OperationRepo operationRepo, QuestionRepo questionRepo,
                          KeyboardServiceImpl keyboardService, MessageService messageService) {
        this.operationRepo = operationRepo;
        this.questionRepo = questionRepo;
        this.keyboardService = keyboardService;
        this.messageService = messageService;
    }

    public SendMessage handleStartCommand(Update update, TelegramSession telegramSession) {
        Message message = update.getMessage();
        long chatId = message.getChatId();
        telegramSession.setActive(true);
        Question first_question = questionRepo.findFirstQuestion();
        String first_question_str = first_question.getQuestion_az() + "\n" + first_question.getQuestion_en() +
                "\n" + first_question.getQuestion_ru();
        SendMessage sendMessage = new SendMessage(chatId, first_question_str);
        if (telegramSession.getChatId() == update.getMessage().getFrom().getId()) {
            if (telegramSession.getLocale().equals(operationRepo.findFirstOperation().getText_az())) {
                sendMessage.setText("Siz artıq sessiyanı başlatmısız. Ləğv etmək üçün /stop əmrini yerinə yetirin");
            }
            if (telegramSession.getLocale().equals(operationRepo.findFirstOperation().getText_en())) {
                sendMessage.setText("You have already started the session. Execute the /stop command to cancel.");
            }
            if (telegramSession.getLocale().equals(operationRepo.findFirstOperation().getText_ru())) {
                sendMessage.setText("Вы уже начали сеанс. Для отмены выполните команду /stop .");
            }
            return sendMessage;
        }
        ReplyKeyboardMarkup languageButtons = keyboardService.getLocaleKeyboard();
        sendMessage.setReplyMarkup(languageButtons);
        return sendMessage;
    }

    public SendMessage handleStopCommand(Update update, TelegramSession telegramSession) {
        if (telegramSession.getChatId() == update.getMessage().getFrom().getId()) {
            telegramSession.setChatId(0);
            telegramSession.setActive(false);
            telegramSession.setQuestion(null);
            telegramSession.setOperationList(null);
            telegramSession.getQuestion_answer_map().clear();
            return messageService.sendStopSessionMessage(update.getMessage().getChatId(), telegramSession.getLocale(), telegramSession);
        } else if (telegramSession.getChatId() == 0 && !telegramSession.isActive()) {
            return messageService.sendStartBeforeStopMessage(update.getMessage().getChatId());

        } else if (telegramSession.getChatId() == 0 && telegramSession.isActive()) {
            telegramSession.setActive(false);
            return messageService.sendStartAgainAndSelectLanguageMessage(update.getMessage().getChatId());

        }
        return null;
    }

}
