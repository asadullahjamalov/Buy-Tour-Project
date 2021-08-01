package code.az.buytourproject.configs;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    private String request_queue = "request_queue";
    private String request_routingKey = "request_routing_key";

    private String stop_queue = "stop_queue";
    private String stop_routingKey = "stop_routing_key";

    private String accept_queue ="accept_queue";
    private String accept_routingKey = "accept_routing_key";

    private String exchange = "telegram_bot_exchange";


    @Bean
    Queue requestQueue() {
        return new Queue(request_queue, true);
    }

    @Bean
    Queue stopQueue() {
        return new Queue(stop_queue, true);
    }

    @Bean
    Queue acceptQueue() {
        return new Queue(accept_queue, true);
    }

    @Bean
    DirectExchange exchange() {
        return new DirectExchange(exchange);
    }

    @Bean
    Binding requestBinding(Queue requestQueue, DirectExchange exchange) {
        return BindingBuilder.bind(requestQueue).to(exchange).with(request_routingKey);
    }

    @Bean
    Binding stopBinding(Queue stopQueue, DirectExchange exchange) {
        return BindingBuilder.bind(stopQueue).to(exchange).with(stop_routingKey);
    }

    @Bean
    Binding acceptBinding(Queue acceptQueue, DirectExchange exchange) {
        return BindingBuilder.bind(acceptQueue).to(exchange).with(accept_routingKey);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }



}
