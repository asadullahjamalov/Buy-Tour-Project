package code.az.buytourproject.repositories;

import code.az.buytourproject.models.SentOffer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SentOfferRepo  extends JpaRepository<SentOffer, Long> {

    SentOffer findSentOfferByMessageIdAndChatId(Integer messageId, Long chatId);
}
