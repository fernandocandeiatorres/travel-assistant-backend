package com.fernandodev.suggestionservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
//
    // É crucial que estes nomes sejam EXATAMENTE os mesmos que você
    // definiu no trip-service, para que eles se conectem à mesma estrutura.
    public static final String EXCHANGE_NAME = "trips_exchange";
    public static final String QUEUE_NAME = "suggestions_queue";
    public static final String ROUTING_KEY = "trip.created";

    // O Spring irá procurar por um Bean deste tipo e, se não existir, irá criá-lo.
    // Isso garante que a infraestrutura no RabbitMQ exista antes da aplicação tentar usá-la.
    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public Queue queue() {
        // O segundo parâmetro 'true' torna a fila durável, ou seja,
        // ela sobrevive a reinicializações do servidor RabbitMQ.
        return new Queue(QUEUE_NAME, true);
    }

    @Bean
    public Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter());
        return factory;
    }
}
