package code.az.buytourproject.configs;

import code.az.buytourproject.models.TelegramSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class RedisConfig {

//    @Bean
//    JedisConnectionFactory jedisConnectionFactory() {
//        return new JedisConnectionFactory();
//    }
//
//    @Bean
//    public RedisTemplate<Long, TelegramSession> redisTemplate() {
//        RedisTemplate<Long, TelegramSession> redisTemplate = new RedisTemplate<>();
//        redisTemplate.setConnectionFactory(jedisConnectionFactory());
//        redisTemplate.setEnableTransactionSupport(true);
//        redisTemplate.afterPropertiesSet();
//        return redisTemplate;
//    }

}
