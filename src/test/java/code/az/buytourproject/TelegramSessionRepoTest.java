package code.az.buytourproject;

import code.az.buytourproject.models.TelegramSession;
import code.az.buytourproject.repositories.TelegramSessionRepo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class TelegramSessionRepoTest {

    @Autowired
    private TelegramSessionRepo underTest;


    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void findTelegramSessionsByActive() {

        TelegramSession telegramSession1 = TelegramSession.builder().isActive(true).build();
        underTest.save(telegramSession1);

        TelegramSession telegramSession2 = TelegramSession.builder().isActive(true).build();
        underTest.save(telegramSession2);

        TelegramSession telegramSession3 = TelegramSession.builder().isActive(false).build();
        underTest.save(telegramSession3);

        List<TelegramSession> telegramSessionList = underTest.findTelegramSessionsByActive();

        assertThat(telegramSessionList).isEqualTo(Arrays.asList(telegramSession1, telegramSession2));
    }

    @Test
    void deactivateTelegramSession() {
        TelegramSession telegramSession1 = TelegramSession.builder().chatId(1l).build();
        underTest.save(telegramSession1);

        underTest.deactivateTelegramSession(1l);

        assertThat(telegramSession1.isActive()).isFalse();


    }

    @Test
    void findChatIdByUuid() {
        TelegramSession telegramSession1 = TelegramSession.builder().chatId(1l).uuid("uuid").build();
        underTest.save(telegramSession1);

        Long chatId = underTest.findChatIdByUuid("uuid");

        assertThat(chatId).isEqualTo(1l);

    }
}