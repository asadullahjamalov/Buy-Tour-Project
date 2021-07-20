package code.az.buytourproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class BuyTourProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(BuyTourProjectApplication.class, args);
    }

}
