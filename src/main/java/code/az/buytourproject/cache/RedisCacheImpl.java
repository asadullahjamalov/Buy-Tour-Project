package code.az.buytourproject.cache;

import code.az.buytourproject.models.TelegramSession;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisCacheImpl implements RedisCache {

    HashOperations<String, Long, TelegramSession> hashOps;

    private final String KEY = "telegram_redis_key";

    public RedisCacheImpl(RedisTemplate<String, Object> redisTemplate) {
        this.hashOps = redisTemplate.opsForHash();
    }

    @Override
    public void save(Long chatId, TelegramSession telegramSession) {
        hashOps.put(KEY, chatId, telegramSession);
    }

    @Override
    public TelegramSession findById(Long chatId) {
        return hashOps.get(KEY, chatId);
    }

    @Override
    public void delete(Long chatId) {
        hashOps.delete(KEY, chatId);
    }

}
