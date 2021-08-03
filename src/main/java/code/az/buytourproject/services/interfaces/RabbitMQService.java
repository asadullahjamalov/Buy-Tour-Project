package code.az.buytourproject.services.interfaces;

import code.az.buytourproject.dtos.AcceptQueueDTO;
import code.az.buytourproject.dtos.OfferQueueDTO;
import code.az.buytourproject.dtos.RequestQueueDTO;
import code.az.buytourproject.dtos.StopQueueDTO;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;

public interface RabbitMQService {

    void sendRequest(RequestQueueDTO requestQueueDTO);

    void sendStopEvent(StopQueueDTO stopQueueDTO);

    void sendAcceptEvent(AcceptQueueDTO acceptQueueDTO);

    void offerListener(OfferQueueDTO offer) throws IOException, TelegramApiException;
}
