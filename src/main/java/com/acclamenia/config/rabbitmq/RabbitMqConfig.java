package com.acclamenia.config.rabbitmq;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Delivery;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.rabbitmq.*;



/**
 * Annotate decedents
 * with
 * @Configuration to activate
 * */
@RequiredArgsConstructor
public abstract class RabbitMqConfig {


    private final String emailQueue;
    private final int port;
    private final String host;
    private final String username;
    private final String password;
    private final String connectionName;



    @Lazy
    @Bean("emailQueue")
    Queue getEmailQueue() {
        return new Queue(emailQueue, false, false, true);
    }

    @Lazy
    @Bean
    Mono<Connection> rabbitMqConnection() {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(host);
        connectionFactory.setPort(port);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        return Mono.fromCallable(() -> connectionFactory.newConnection(connectionName)).cache();
    }


    @Lazy
    @Bean("mqConsumer")
    public Flux<Delivery> consumeMessage(Receiver receiver) {
        return receiver.consumeAutoAck(emailQueue);
    }


    @Lazy
    @Bean("mqReceiver")
    public Receiver receiver(Mono<Connection> connectionMono) {
        return RabbitFlux.createReceiver(new ReceiverOptions().connectionMono(connectionMono));
    }


    @Lazy
    @Bean("mqSender")
    public Sender sender(Mono<Connection> connectionMono) {
        return RabbitFlux.createSender(new SenderOptions().connectionMono(connectionMono));
    }


}