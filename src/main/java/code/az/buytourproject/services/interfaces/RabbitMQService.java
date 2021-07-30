package code.az.buytourproject.services.interfaces;

import code.az.buytourproject.dtos.OfferQueueDTO;
import code.az.buytourproject.dtos.RequestQueueDTO;
import code.az.buytourproject.models.Offer;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;

public interface RabbitMQService {

    void send(RequestQueueDTO requestQueueDTO);

    void offerListener(OfferQueueDTO offer) throws IOException, TelegramApiException;
}
