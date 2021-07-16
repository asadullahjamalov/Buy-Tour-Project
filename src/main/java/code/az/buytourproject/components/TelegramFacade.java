package code.az.buytourproject.components;

import code.az.buytourproject.components.handlers.MessageHandler;
import code.az.buytourproject.enums.CommandType;
import code.az.buytourproject.models.TelegramSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;


@Component
@Slf4j
public class TelegramFacade {

    MessageHandler messageHandler;

    public TelegramFacade(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    public BotApiMethod<?> handleUpdate(Update update, TelegramSession telegramSession) {
        log.info("New message from User:{}, chatId: {},  with text: {}",
                update.getMessage().getFrom().getFirstName(),
                update.getMessage().getChatId(), update.getMessage().getText());
        String inputMessage = update.getMessage().getText();
        if (inputMessage.equals(CommandType.START.getValue())) {
            return messageHandler.handleStartCommand(update, telegramSession);
        } else if (inputMessage.equals(CommandType.STOP.getValue())) {
            return messageHandler.handleStopCommand(update, telegramSession);
        } else {
            return messageHandler.handleMessage(update, telegramSession);
        }
    }


}
