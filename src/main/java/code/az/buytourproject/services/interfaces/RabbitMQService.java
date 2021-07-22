package code.az.buytourproject.services.interfaces;

import code.az.buytourproject.models.TelegramSession;

public interface RabbitMQService {

    void send(TelegramSession telegramSession);

}
