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
import code.az.buytourproject.services.interfaces.RabbitMQService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashMap;
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
    RabbitMQService rabbitMQService;

    Map<Long, TelegramSession> telegramSessionMap = new HashMap<>();

    public TelegramFacade(RedisCache redisCache, MessageHandler messageHandler, OperationDAO operationDAO,
                          QuestionDAO questionDAO, MessageService messageService, RabbitMQService rabbitMQService) {
        this.redisCache = redisCache;
        this.messageHandler = messageHandler;
        this.operationDAO = operationDAO;
        this.questionDAO = questionDAO;
        this.messageService = messageService;
        this.rabbitMQService = rabbitMQService;
    }

    public BotApiMethod<?> handleUpdate(Update update) {
        log.info("New message from User:{}, chatId: {},  with text: {}",
                update.getMessage().getFrom().getFirstName(), update.getMessage().getChatId(), update.getMessage().getText());
        if (!telegramSessionMap.containsKey(update.getMessage().getChatId())) {
            telegramSessionMap.put(update.getMessage().getChatId(), new TelegramSession());
        }
        TelegramSession telegramSession = telegramSessionMap.get(update.getMessage().getChatId());
        String inputMessage = update.getMessage().getText();
        if (inputMessage.equals(CommandType.START.getValue())) {
            telegramSession.setQuestion(questionDAO.findFirstQuestion());
            telegramSession.setOperationList(operationDAO.findOperationsByQuestion(telegramSession.getQuestion()));
            return messageHandler.handleStartCommand(update, telegramSession);
        } else if (inputMessage.equals(CommandType.STOP.getValue())) {
            return messageHandler.handleStopCommand(update, telegramSession);
        } else {
            if (!telegramSession.isActive())
                return new SendMessage(update.getMessage().getChatId(), "Zəhmət olmasa /start edin və dili seçin." + "\n" +
                        "Please /start and select a language." + "\n" + "Пожалуйста, /start и выберите язык.");

            if (telegramSession.getLocale() == null) {
                List<Operation> operationList = telegramSession.getOperationList();
                telegramSession.setQuestion(operationList.get(0).getNextQuestion());
                return setLocale(update, telegramSession);
            }
            if (telegramSession.getLocale() != null && telegramSession.getQuestion() != null) {
                if (operationDAO.findOperationsByQuestion(telegramSession.getQuestion()).get(0).getNextQuestion() != null) {
                    telegramSession.getQuestion_answer_map().put(
                            operationDAO.findOperationsByQuestion(telegramSession.getQuestion()).get(0).getQuestion().getKey(),
                            update.getMessage().getText());
                }


                List<Operation> operationList = operationDAO.findOperationsByQuestion(telegramSession.getQuestion());
                if (operationList.get(0).getType().equals(OperationType.BUTTON)) {
                    boolean counter = false;
                    for (Operation operation : operationList) {
                        if (telegramSession.getLocale().equals(operationDAO.findFirstOperation().getText_az())
                                && update.getMessage().getText().equals(operation.getText_az())) {
                            counter = true;
                            telegramSession.setQuestion(operation.getNextQuestion());
                            break;
                        } else if (telegramSession.getLocale().equals(operationDAO.findFirstOperation().getText_en())
                                && update.getMessage().getText().equals(operation.getText_en())) {
                            counter = true;
                            telegramSession.setQuestion(operation.getNextQuestion());
                            break;
                        } else if (telegramSession.getLocale().equals(operationDAO.findFirstOperation().getText_ru())
                                && update.getMessage().getText().equals(operation.getText_ru())) {
                            counter = true;
                            telegramSession.setQuestion(operation.getNextQuestion());
                            break;
                        }
                    }
                    if (!counter) {
                        return messageService.sendWrongAnswerMessage(telegramSession.getChatId(), telegramSession.getLocale());
                    } else {
                        return messageHandler.handleMessage(update, telegramSession);
                    }
                } else if (operationList.get(0).getType().equals(OperationType.FREETEXT)) {
                    if (telegramSession.getQuestion().getRegex() != null
                            && !(update.getMessage().getText().matches(telegramSession.getQuestion().getRegex()))) {
                        return messageService.sendWrongAnswerMessage(telegramSession.getChatId(), telegramSession.getLocale());
                    }
                    if (operationDAO.findOperationsByQuestion(telegramSession.getQuestion()).get(0).getNextQuestion() != null) {
                        telegramSession.setQuestion(operationList.get(0).getNextQuestion());
                    } else {

                        rabbitMQService.send(telegramSession);
                        //TODO
                        redisCache.save(update.getMessage().getChatId(), telegramSession);
                        System.out.println("redis " + redisCache.findById(update.getMessage().getChatId()).getQuestion_answer_map().toString());
                        return messageService.sendSurveyFinishedMessage(update.getMessage().getChatId(), telegramSession.getLocale(),
                                telegramSession.getQuestion_answer_map().toString());
                    }

                }
                return messageHandler.handleMessage(update, telegramSession);
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
