package code.az.buytourproject.configs;

import code.az.buytourproject.TourTelegramBot;
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
    public TourTelegramBot tourTelegramBot() {
        TourTelegramBot telegramBot = new TourTelegramBot();
        telegramBot.setBotToken(botToken);
        telegramBot.setWebHookPath(webHookPath);
        telegramBot.setBotUserName(botUserName);
        return telegramBot;
    }
}
