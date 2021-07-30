package code.az.buytourproject.services;

import code.az.buytourproject.TelegramWebHook;
import code.az.buytourproject.dtos.OfferQueueDTO;
import code.az.buytourproject.dtos.RequestQueueDTO;
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

    public RabbitMQServiceImpl(RabbitTemplate rabbitTemplate, TelegramWebHook telegramWebHook,
                               TelegramSessionRepo telegramSessionRepo) {
        this.rabbitTemplate = rabbitTemplate;
        this.telegramWebHook = telegramWebHook;
        this.telegramSessionRepo = telegramSessionRepo;
    }

    @Override
    public void send(RequestQueueDTO requestQueueDTO) {
        rabbitTemplate.convertAndSend("telegram_bot_exchange",
                "telegram_bot_routing_key", requestQueueDTO);
    }

    @Override
    @RabbitListener(queues = "buy_tour_web_queue")
    public void offerListener(OfferQueueDTO offerQueueDTO) throws IOException, TelegramApiException {

        BufferedImage bufferedImage = toBufferedImage(offerQueueDTO.getImage());

        File image = new File("image.png");
        ImageIO.write(bufferedImage, "png", image);


        System.out.println(telegramSessionRepo.findChatIdByUuid(offerQueueDTO.getUuid()));
        System.out.println("Image was sent");

        telegramWebHook.execute(new SendPhoto().setPhoto(image).setChatId(telegramSessionRepo.findChatIdByUuid(offerQueueDTO.getUuid())));

//        return sendPhoto;

    }

    public static BufferedImage toBufferedImage(byte[] bytes) throws IOException {
        InputStream is = new ByteArrayInputStream(bytes);
        BufferedImage bi = ImageIO.read(is);
        return bi;

    }

}
