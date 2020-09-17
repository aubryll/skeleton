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

package com.acclamenia.event;

import com.acclamenia.service.rabbitmq.receiver.IRabbitMQReceiver;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Delivery;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.util.stereotypes.Lazy;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import reactor.core.Disposable;
import reactor.core.Disposables;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import javax.annotation.PreDestroy;
import java.util.Objects;

@Slf4j
@Component
public class RabbitMQBootListener {

    @Lazy
    @Autowired
    private Mono<Connection> connectionMono;

    @Lazy
    @Autowired
    private AmqpAdmin amqpAdmin;

    @Lazy
    @Autowired
    private Queue queue;

    @Lazy
    @Autowired
    private Flux<Delivery> messages;

    @Autowired
    private IRabbitMQReceiver rabbitMQReceiver;
    private Disposable.Composite allDisposables;



    @EventListener(ApplicationReadyEvent.class)
    public void init(){
        amqpAdmin.declareQueue(queue);
        allDisposables = Disposables.composite();

        Disposable disposable = messages
                .subscribeOn(Schedulers.elastic())
                .doOnError(Throwable::printStackTrace)
                .subscribe(message -> rabbitMQReceiver.receive(message.getBody()));

        allDisposables.add(disposable);
    }


    @PreDestroy
    @SneakyThrows
    private void close() {
        Objects.requireNonNull(connectionMono.block()).close();
        allDisposables.dispose();
    }

}
