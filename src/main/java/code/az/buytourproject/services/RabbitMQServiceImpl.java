package code.az.buytourproject.services;

import code.az.buytourproject.dtos.RequestQueueDTO;
import code.az.buytourproject.models.Offer;
import code.az.buytourproject.models.TelegramSession;
import code.az.buytourproject.services.interfaces.RabbitMQService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Service
public class RabbitMQServiceImpl implements RabbitMQService {
    RabbitTemplate rabbitTemplate;

    public RabbitMQServiceImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void send(RequestQueueDTO requestQueueDTO) {
        rabbitTemplate.convertAndSend("telegram_bot_exchange",
                "telegram_bot_routing_key", requestQueueDTO);
    }

    @Override
    @RabbitListener(queues = "buy_tour_web_queue")
    public Offer offerListener(Offer offer){
        System.out.println("Message received");
        return offer;
    }

}
