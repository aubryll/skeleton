/*
 *   Spring boot skeleton©
 *    Copyright 2020 Acclamenia Ltd
 *     Url: https://github.com/aubryll/skeleton
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */

package com.acclamenia.config.rabbitmq;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Delivery;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.rabbitmq.*;


/**
 * Annotate decedents
 * with
 *
 * @Configuration to activate
 */


public abstract class AbstractRabbitMqConfig {

    abstract public String getQueueName();

    abstract public String getHost();

    abstract public String getUsername();

    abstract public String getPassword();

    abstract public String getConnectionName();

    abstract public int getPort();

    @Lazy
    @Bean("mqQueue")
    Queue getQueue() {
        return new Queue(getQueueName(), false, false, true);
    }

    @Lazy
    @Bean
    Mono<Connection> rabbitMqConnection() {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(getHost());
        connectionFactory.setPort(getPort());
        connectionFactory.setUsername(getUsername());
        connectionFactory.setPassword(getPassword());
        return Mono.fromCallable(() -> connectionFactory.newConnection(getConnectionName())).cache();
    }


    @Lazy
    @Bean("mqConsumer")
    public Flux<Delivery> consumeMessage(Receiver receiver) {
        return receiver.consumeAutoAck(getQueueName());
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