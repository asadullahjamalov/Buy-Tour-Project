package code.az.buytourproject.cache;

import code.az.buytourproject.models.TelegramSession;

public interface RedisCache {

    void save(Long chatId, TelegramSession telegramSession);

    TelegramSession findById(Long chatId);

    void delete(Long chatId);
}
