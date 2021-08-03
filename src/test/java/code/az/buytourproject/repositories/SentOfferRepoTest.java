package code.az.buytourproject.repositories;

import code.az.buytourproject.models.SentOffer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class SentOfferRepoTest {

    @Autowired
    private SentOfferRepo underTest;


    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void findSentOfferByMessageIdAndChatId() {
        SentOffer sentOffer1 = SentOffer.builder().messageId(1).chatId(1l).build();

        underTest.save(sentOffer1);

        SentOffer sentOffer = underTest.findSentOfferByMessageIdAndChatId(1, 1l);

        assertThat(sentOffer).isEqualTo(sentOffer1);
    }
}