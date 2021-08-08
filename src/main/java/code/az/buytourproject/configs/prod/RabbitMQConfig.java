package code.az.buytourproject.configs.prod;


import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.net.URI;
import java.net.URISyntaxException;

@Profile("!dev")
@Configuration
public class RabbitMQConfig {

    private String request_queue = "request_queue";
    private String request_routingKey = "request_routing_key";

    private String stop_queue = "stop_queue";
    private String stop_routingKey = "stop_routing_key";

    private String accept_queue ="accept_queue";
    private String accept_routingKey = "accept_routing_key";

    private String exchange = "telegram_bot_exchange";


    private String queueName = "buy_tour_web_queue";
    private String exchangeWeb = "buy_tour_web_exchange";
    private String routingKey = "buy_tour_web_routing_key";

    @Bean
    Queue queue() {
        return new Queue(queueName, true);
    }

    @Bean
    DirectExchange exchangeWeb() {
        return new DirectExchange(exchangeWeb);
    }

    @Bean
    Binding binding(Queue queue, DirectExchange exchangeWeb) {
        return BindingBuilder.bind(queue).to(exchangeWeb).with(routingKey);
    }



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
    public Jackson2JsonMessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public ConnectionFactory connectionFactory() throws URISyntaxException {
        final URI rabbitMqUrl = new URI(System.getenv("CLOUDAMQP_URL"));
        final CachingConnectionFactory factory = new CachingConnectionFactory();
        factory.setUri(rabbitMqUrl);
        return factory;
    }

    @Bean
    public RabbitTemplate template() throws URISyntaxException {
        RabbitTemplate temp = new RabbitTemplate(connectionFactory());
        temp.setMessageConverter(converter());
        return temp;
    }

}
