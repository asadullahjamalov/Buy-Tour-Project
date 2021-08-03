package code.az.buytourproject;

import code.az.buytourproject.dtos.AcceptQueueDTO;
import code.az.buytourproject.models.SentOffer;
import code.az.buytourproject.processors.TelegramFacade;
import code.az.buytourproject.repositories.SentOfferRepo;
import code.az.buytourproject.services.interfaces.RabbitMQService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;


@Slf4j
public class TelegramWebHook extends TelegramWebhookBot {
    private String webHookPath;
    private String botUserName;
    private String botToken;

    @Autowired
    TelegramFacade telegramFacade;

    @Autowired
    RabbitMQService rabbitMQService;

    @Autowired
    SentOfferRepo sentOfferRepo;


    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {

        if (update.getMessage().isReply()) {
            System.out.println(update.getMessage().getReplyToMessage().getMessageId());
            System.out.println("here");
            SentOffer sentOffer = sentOfferRepo.findSentOfferByMessageIdAndChatId(
                    update.getMessage().getReplyToMessage().getMessageId(), update.getMessage().getChatId());
            rabbitMQService.sendAcceptEvent(AcceptQueueDTO.builder()
                    .agentId(sentOffer.getAgentId())
                    .uuid(sentOffer.getUuid())
                    .info(update.getMessage().getText())
                    .build());
            return new SendMessage(update.getMessage().getChatId(), "Tur agent sizinlə əlaqə saxlayacaq." + "\n" +
                    "The tour agent will contact you." + "\n" + "Тур агент свяжется с вами.");

        }

        if (update.getMessage() != null && update.getMessage().hasText()) {
            log.info("New message from User:{}, chatId: {},  with text: {}",
                    update.getMessage().getFrom().getFirstName(), update.getMessage().getChatId(), update.getMessage().getText());
            return telegramFacade.handleUpdate(update);
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
