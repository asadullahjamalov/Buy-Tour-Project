package code.az.buytourproject.processors;

import code.az.buytourproject.processors.handlers.MessageHandler;
import code.az.buytourproject.enums.CommandType;
import code.az.buytourproject.models.TelegramSession;
import code.az.buytourproject.repositories.OperationRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Map;


@Component
@Slf4j
public class TelegramFacade {

    MessageHandler messageHandler;

    OperationRepo operationRepo;

    public TelegramFacade(MessageHandler messageHandler, OperationRepo operationRepo) {
        this.messageHandler = messageHandler;
        this.operationRepo = operationRepo;
    }


    public BotApiMethod<?> handleUpdate(Update update, Map<Long, TelegramSession> telegramSessionMap) {
        log.info("New message from User:{}, chatId: {},  with text: {}",
                update.getMessage().getFrom().getFirstName(),
                update.getMessage().getChatId(), update.getMessage().getText());
        if (!telegramSessionMap.containsKey(update.getMessage().getChatId())){
            System.out.println("new session was created");
            telegramSessionMap.put(update.getMessage().getChatId(), new TelegramSession());
        }

        String inputMessage = update.getMessage().getText();
        if (inputMessage.equals(CommandType.START.getValue())) {
            return messageHandler.handleStartCommand(update, telegramSessionMap.get(update.getMessage().getChatId()));
        } else if (inputMessage.equals(CommandType.STOP.getValue())) {
            return messageHandler.handleStopCommand(update, telegramSessionMap.get(update.getMessage().getChatId()));
        } else {
            if ((telegramSessionMap.get(update.getMessage().getChatId()).isActive()) &&
                    (telegramSessionMap.get(update.getMessage().getChatId()).getChatId() == 0) &&
                    !(inputMessage.equals(operationRepo.findFirstOperation().getText_az()) ||
                    inputMessage.equals(operationRepo.findFirstOperation().getText_en()) ||
                    inputMessage.equals(operationRepo.findFirstOperation().getText_ru()))) {
                return messageHandler.handleStartCommand(update, telegramSessionMap.get(update.getMessage().getChatId()));
            }
            return messageHandler.handleMessage(update, telegramSessionMap.get(update.getMessage().getChatId()));
        }
    }


}
