package code.az.buytourproject.repositories;

import code.az.buytourproject.models.TelegramSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TelegramSessionRepo extends JpaRepository<TelegramSession, Long> {

    @Query("select t from TelegramSession t where t.isActive=true")
    List<TelegramSession> findTelegramSessionsByActive();

    @Modifying
    @Transactional
    @Query("update TelegramSession t set t.isActive=false where t.chatId=:chatId ")
    void deactivateTelegramSession(long chatId);

    @Query("select t.chatId from TelegramSession t where t.uuid=:uuid")
    Long findChatIdByUuid(String uuid);
}
