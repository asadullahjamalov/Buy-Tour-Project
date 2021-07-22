package code.az.buytourproject.services;

import code.az.buytourproject.models.TelegramSession;
import code.az.buytourproject.services.interfaces.RabbitMQService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQServiceImpl implements RabbitMQService {
    RabbitTemplate rabbitTemplate;

    public RabbitMQServiceImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void send(TelegramSession telegramSession) {
        rabbitTemplate.convertAndSend("telegram_bot_exchange", "telegram_bot_routing_key", telegramSession);
    }

}
