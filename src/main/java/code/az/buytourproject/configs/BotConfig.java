package code.az.buytourproject.configs;

import code.az.buytourproject.TelegramWebHook;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "telegrambot")
public class BotConfig {
    private String webHookPath;
    private String botUserName;
    private String botToken;


    @Bean
    public TelegramWebHook tourTelegramBot() {
        TelegramWebHook telegramBot = new TelegramWebHook();
        telegramBot.setBotToken(botToken);
        telegramBot.setWebHookPath(webHookPath);
        telegramBot.setBotUserName(botUserName);
        return telegramBot;
    }


}
