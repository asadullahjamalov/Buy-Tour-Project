package code.az.buytourproject.services.interfaces;

import code.az.buytourproject.dtos.RequestQueueDTO;
import code.az.buytourproject.models.Offer;
import code.az.buytourproject.models.TelegramSession;

import java.util.HashMap;
import java.util.Map;

public interface RabbitMQService {

    void send(RequestQueueDTO requestQueueDTO);

    Offer offerListener(Offer offer);
}
