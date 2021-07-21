package code.az.buytourproject.processors;

import code.az.buytourproject.cache.RedisCache;
import code.az.buytourproject.daos.interfaces.OperationDAO;
import code.az.buytourproject.daos.interfaces.QuestionDAO;
import code.az.buytourproject.enums.OperationType;
import code.az.buytourproject.models.Operation;
import code.az.buytourproject.processors.handlers.MessageHandler;
import code.az.buytourproject.enums.CommandType;
import code.az.buytourproject.models.TelegramSession;
import code.az.buytourproject.services.interfaces.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.Map;


@Component
@Slf4j
public class TelegramFacade {

    RedisCache redisCache;
    MessageHandler messageHandler;
    OperationDAO operationDAO;
    QuestionDAO questionDAO;
    MessageService messageService;

    public TelegramFacade(RedisCache redisCache, MessageHandler messageHandler, OperationDAO operationDAO,
                          QuestionDAO questionDAO, MessageService messageService) {
        this.redisCache = redisCache;
        this.messageHandler = messageHandler;
        this.operationDAO = operationDAO;
        this.questionDAO = questionDAO;
        this.messageService = messageService;
    }

    public BotApiMethod<?> handleUpdate(Update update, Map<Long, TelegramSession> telegramSessionMap) {
        log.info("New message from User:{}, chatId: {},  with text: {}",
                update.getMessage().getFrom().getFirstName(), update.getMessage().getChatId(), update.getMessage().getText());
        if (!telegramSessionMap.containsKey(update.getMessage().getChatId())) {
            telegramSessionMap.put(update.getMessage().getChatId(), new TelegramSession());
        }

        String inputMessage = update.getMessage().getText();
        if (inputMessage.equals(CommandType.START.getValue())) {
            telegramSessionMap.get(update.getMessage().getChatId()).setQuestion(questionDAO.findFirstQuestion());
            telegramSessionMap.get(update.getMessage().getChatId())
                    .setOperationList(operationDAO.getOperationsByQuestion(telegramSessionMap.get(update.getMessage().getChatId()).getQuestion()));
            return messageHandler.handleStartCommand(update, telegramSessionMap.get(update.getMessage().getChatId()));
        } else if (inputMessage.equals(CommandType.STOP.getValue())) {
            return messageHandler.handleStopCommand(update, telegramSessionMap.get(update.getMessage().getChatId()));
        } else {
            if (!telegramSessionMap.get(update.getMessage().getChatId()).isActive())
                return new SendMessage(update.getMessage().getChatId(), "Zəhmət olmasa /start edin və dili seçin." + "\n" +
                        "Please /start and select a language." + "\n" + "Пожалуйста, /start и выберите язык.");

            if (telegramSessionMap.get(update.getMessage().getChatId()).getLocale() == null) {
                List<Operation> operationList = telegramSessionMap.get(update.getMessage().getChatId()).getOperationList();
                telegramSessionMap.get(update.getMessage().getChatId()).setQuestion(operationList.get(0).getNextQuestion());
                return setLocale(update, telegramSessionMap.get(update.getMessage().getChatId()));
            }
            if (telegramSessionMap.get(update.getMessage().getChatId()).getLocale() != null
                    && telegramSessionMap.get(update.getMessage().getChatId()).getQuestion() != null) {
                if (operationDAO.getOperationsByQuestion(telegramSessionMap.get(update.getMessage().getChatId()).getQuestion()).get(0).getNextQuestion() != null) {
                    telegramSessionMap.get(update.getMessage().getChatId()).getQuestion_answer_map().put(
                            operationDAO.getOperationsByQuestion(telegramSessionMap.get(update.getMessage().getChatId()).getQuestion()).get(0).getQuestion().getKey(),
                            update.getMessage().getText());
                }


                List<Operation> operationList = operationDAO.getOperationsByQuestion(telegramSessionMap.get(update.getMessage().getChatId()).getQuestion());
                if (operationList.get(0).getType().equals(OperationType.BUTTON)) {
                    boolean counter = false;
                    for (Operation operation : operationList) {
                        if (telegramSessionMap.get(update.getMessage().getChatId()).getLocale().equals(operationDAO.findFirstOperation().getText_az())
                                && update.getMessage().getText().equals(operation.getText_az())) {
                            counter = true;
                            telegramSessionMap.get(update.getMessage().getChatId()).setQuestion(operation.getNextQuestion());
                            break;
                        } else if (telegramSessionMap.get(update.getMessage().getChatId()).getLocale().equals(operationDAO.findFirstOperation().getText_en())
                                && update.getMessage().getText().equals(operation.getText_en())) {
                            counter = true;
                            telegramSessionMap.get(update.getMessage().getChatId()).setQuestion(operation.getNextQuestion());
                            break;
                        } else if (telegramSessionMap.get(update.getMessage().getChatId()).getLocale().equals(operationDAO.findFirstOperation().getText_ru())
                                && update.getMessage().getText().equals(operation.getText_ru())) {
                            counter = true;
                            telegramSessionMap.get(update.getMessage().getChatId()).setQuestion(operation.getNextQuestion());
                            break;
                        }
                    }
                    if (!counter) {
                        return messageService.sendWrongAnswerMessage(telegramSessionMap.get(update.getMessage().getChatId()).getChatId(),
                                telegramSessionMap.get(update.getMessage().getChatId()).getLocale());
                    } else {
                        return messageHandler.handleMessage(update, telegramSessionMap.get(update.getMessage().getChatId()));
                    }
                } else if (operationList.get(0).getType().equals(OperationType.FREETEXT)) {
                    if (telegramSessionMap.get(update.getMessage().getChatId()).getQuestion().getRegex() != null
                            && !(update.getMessage().getText().matches(telegramSessionMap.get(update.getMessage().getChatId()).getQuestion().getRegex()))) {
                        return messageService.sendWrongAnswerMessage(telegramSessionMap.get(update.getMessage().getChatId()).getChatId(),
                                telegramSessionMap.get(update.getMessage().getChatId()).getLocale());
                    }
                    if (operationDAO.getOperationsByQuestion(telegramSessionMap.get(update.getMessage().getChatId()).getQuestion()).get(0).getNextQuestion() != null) {
                        telegramSessionMap.get(update.getMessage().getChatId()).setQuestion(operationList.get(0).getNextQuestion());
                    } else {

                        //TODO
                        redisCache.save(update.getMessage().getChatId(), telegramSessionMap.get(update.getMessage().getChatId()));
                        System.out.println("redis " + redisCache.findById(update.getMessage().getChatId()).getQuestion_answer_map().toString());
                        return messageService.sendSurveyFinishedMessage(update.getMessage().getChatId(), telegramSessionMap.get(update.getMessage().getChatId()).getLocale(),
                                telegramSessionMap.get(update.getMessage().getChatId()).getQuestion_answer_map().toString());
                    }

                }
                return messageHandler.handleMessage(update, telegramSessionMap.get(update.getMessage().getChatId()));
            }
            return new SendMessage(update.getMessage().getChatId(), "Try again , lang");
        }
    }


    public SendMessage setLocale(Update update, TelegramSession telegramSession) {
        if (update.getMessage().getText().equals(operationDAO.findFirstOperation().getText_az())) {
            telegramSession.setChatId(update.getMessage().getFrom().getId());
            telegramSession.setLocale(update.getMessage().getText());
            telegramSession.getQuestion_answer_map().put(operationDAO.findFirstOperation().getQuestion().getKey(),
                    operationDAO.findFirstOperation().getText_az());
            return messageHandler.handleMessage(update, telegramSession);
        } else if (update.getMessage().getText().equals(operationDAO.findFirstOperation().getText_en())) {
            telegramSession.setChatId(update.getMessage().getFrom().getId());
            telegramSession.setLocale(update.getMessage().getText());
            telegramSession.getQuestion_answer_map().put(operationDAO.findFirstOperation().getQuestion().getKey(),
                    operationDAO.findFirstOperation().getText_en());
            return messageHandler.handleMessage(update, telegramSession);
        } else if (update.getMessage().getText().equals(operationDAO.findFirstOperation().getText_ru())) {
            telegramSession.setChatId(update.getMessage().getFrom().getId());
            telegramSession.setLocale(update.getMessage().getText());
            telegramSession.getQuestion_answer_map().put(operationDAO.findFirstOperation().getQuestion().getKey(),
                    operationDAO.findFirstOperation().getText_ru());
            return messageHandler.handleMessage(update, telegramSession);
        }
        return messageService.sendSelectLanguageMessage(update.getMessage().getChatId());
    }

}
