package code.az.buytourproject;

import code.az.buytourproject.processors.TelegramFacade;
import code.az.buytourproject.models.TelegramSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashMap;
import java.util.Map;


public class TelegramWebHook extends TelegramWebhookBot {
    private String webHookPath;
    private String botUserName;
    private String botToken;

    @Autowired
    TelegramFacade telegramFacade;

    Map<Long, TelegramSession> telegramSessionMap = new HashMap<>();


    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {

        if (update.getMessage() != null && update.getMessage().hasText()) {
            return telegramFacade.handleUpdate(update, telegramSessionMap);
        }
        return null;

    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public String getBotUsername() {
        return botUserName;
    }

    @Override
    public String getBotPath() {
        return webHookPath;
    }

    public void setWebHookPath(String webHookPath) {
        this.webHookPath = webHookPath;
    }

    public void setBotUserName(String botUserName) {
        this.botUserName = botUserName;
    }

    public void setBotToken(String botToken) {
        this.botToken = botToken;
    }
}
