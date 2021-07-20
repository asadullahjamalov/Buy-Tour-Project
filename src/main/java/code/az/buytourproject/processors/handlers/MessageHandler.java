package code.az.buytourproject.processors.handlers;

import code.az.buytourproject.daos.interfaces.OperationDAO;
import code.az.buytourproject.daos.interfaces.QuestionDAO;
import code.az.buytourproject.enums.OperationType;
import code.az.buytourproject.models.TelegramSession;
import code.az.buytourproject.services.interfaces.KeyboardService;
import code.az.buytourproject.services.interfaces.MessageService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class MessageHandler {
    OperationDAO operationDAO;
    QuestionDAO questionDAO;
    KeyboardService keyboardService;
    MessageService messageService;

    public MessageHandler(OperationDAO operationDAO, QuestionDAO questionDAO,
                          KeyboardService keyboardService, MessageService messageService) {
        this.operationDAO = operationDAO;
        this.questionDAO = questionDAO;
        this.keyboardService = keyboardService;
        this.messageService = messageService;
    }

    public SendMessage handleStartCommand(Update update, TelegramSession telegramSession) {
        telegramSession.setActive(true);
        if (telegramSession.getChatId() == update.getMessage().getFrom().getId()) {
            return messageService.sendSessionAlreadyStartedMessage(update.getMessage().getChatId(), telegramSession.getLocale());
        }
        return messageService.sendFirstMessage(update.getMessage().getChatId());
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

    public SendMessage handleMessage(Update update, TelegramSession telegramSession) {
        SendMessage sendMessage = new SendMessage();
        if (!telegramSession.isActive()) {
            return messageService.sendStartAndSelectLanguageMessage(update.getMessage().getChatId());
        }

        if (operationDAO.getOperationsByQuestion(telegramSession.getQuestion()).get(0).getQuestion() != null) {
            System.out.println(telegramSession.getQuestion_answer_map().toString());
            if (telegramSession.getLocale().equals(operationDAO.findFirstOperation().getText_az())) {
                if (operationDAO.getOperationsByQuestion(telegramSession.getQuestion()).get(0).getType().equals(OperationType.BUTTON)) {
                    sendMessage.setReplyMarkup(keyboardService.getKeyboardButtons(telegramSession.getQuestion(), telegramSession));
                }
                return sendMessage.setChatId(update.getMessage().getChatId().toString())
                        .setText(telegramSession.getQuestion().getQuestion_az());
            } else if (telegramSession.getLocale().equals(operationDAO.findFirstOperation().getText_en())) {
                if (operationDAO.getOperationsByQuestion(telegramSession.getQuestion()).get(0).getType().equals(OperationType.BUTTON)) {
                    sendMessage.setReplyMarkup(keyboardService.getKeyboardButtons(telegramSession.getQuestion(), telegramSession));
                }
                return sendMessage.setChatId(update.getMessage().getChatId().toString())
                        .setText(telegramSession.getQuestion().getQuestion_en());
            } else if (telegramSession.getLocale().equals(operationDAO.findFirstOperation().getText_ru())) {
                if (operationDAO.getOperationsByQuestion(telegramSession.getQuestion()).get(0).getType().equals(OperationType.BUTTON)) {
                    sendMessage.setReplyMarkup(keyboardService.getKeyboardButtons(telegramSession.getQuestion(), telegramSession));
                }
                return sendMessage.setChatId(update.getMessage().getChatId().toString())
                        .setText(telegramSession.getQuestion().getQuestion_ru());
            }
        }
        return null;
    }

}
