package code.az.buytourproject.controllers;

import code.az.buytourproject.TelegramWebHook;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@RestController
public class WebHookController {
    private final TelegramWebHook telegramBot;
    public WebHookController(TelegramWebHook telegramBot) {
        this.telegramBot = telegramBot;
    }

    private boolean reset = false;

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public BotApiMethod<?> onUpdateReceived(@RequestBody Update update) {
        if (reset) {
            return new SendMessage();
        }
        return telegramBot.onWebhookUpdateReceived(update);
    }


}