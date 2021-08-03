package code.az.buytourproject.services;

import code.az.buytourproject.TelegramWebHook;
import code.az.buytourproject.dtos.AcceptQueueDTO;
import code.az.buytourproject.dtos.OfferQueueDTO;
import code.az.buytourproject.dtos.RequestQueueDTO;
import code.az.buytourproject.dtos.StopQueueDTO;
import code.az.buytourproject.models.SentOffer;
import code.az.buytourproject.repositories.SentOfferRepo;
import code.az.buytourproject.repositories.TelegramSessionRepo;
import code.az.buytourproject.services.interfaces.RabbitMQService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@Service
public class RabbitMQServiceImpl implements RabbitMQService {
    private final RabbitTemplate rabbitTemplate;
    private final TelegramWebHook telegramWebHook;
    private final TelegramSessionRepo telegramSessionRepo;
    private final SentOfferRepo sentOfferRepo;

    public RabbitMQServiceImpl(RabbitTemplate rabbitTemplate, TelegramWebHook telegramWebHook,
                               TelegramSessionRepo telegramSessionRepo, SentOfferRepo sentOfferRepo) {
        this.rabbitTemplate = rabbitTemplate;
        this.telegramWebHook = telegramWebHook;
        this.telegramSessionRepo = telegramSessionRepo;
        this.sentOfferRepo = sentOfferRepo;
    }

    @Override
    public void sendRequest(RequestQueueDTO requestQueueDTO) {
        rabbitTemplate.convertAndSend("telegram_bot_exchange",
                "request_routing_key", requestQueueDTO);
    }

    @Override
    public void sendStopEvent(StopQueueDTO stopQueueDTO) {
        rabbitTemplate.convertAndSend("telegram_bot_exchange",
                "stop_routing_key", stopQueueDTO);
    }

    @Override
    public void sendAcceptEvent(AcceptQueueDTO acceptQueueDTO) {
        rabbitTemplate.convertAndSend("telegram_bot_exchange",
                "accept_routing_key", acceptQueueDTO);
    }

    @Override
    @RabbitListener(queues = "buy_tour_web_queue")
    public void offerListener(OfferQueueDTO offerQueueDTO) throws IOException, TelegramApiException {

        File image = new File("image.png");
        ImageIO.write(toBufferedImage(offerQueueDTO.getImage()), "png", image);

        Integer messageId = telegramWebHook.execute(new SendPhoto().setPhoto(image)
                .setChatId(telegramSessionRepo.findChatIdByUuid(offerQueueDTO.getUuid())))
                .getMessageId();

        sentOfferRepo.save(SentOffer.builder().agentId(offerQueueDTO.getAgentId())
                .uuid(offerQueueDTO.getUuid())
                .chatId(telegramSessionRepo.findChatIdByUuid(offerQueueDTO.getUuid()))
                .messageId(messageId)
                .build());
    }

    public static BufferedImage toBufferedImage(byte[] bytes) throws IOException {
        InputStream is = new ByteArrayInputStream(bytes);
        return ImageIO.read(is);

    }

}
