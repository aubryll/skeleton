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

package com.acclamenia.service.rabbitmq.sender;

import lombok.SneakyThrows;
import org.hibernate.validator.internal.util.stereotypes.Lazy;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.Disposable;
import reactor.core.Disposables;
import reactor.core.publisher.Flux;
import reactor.rabbitmq.OutboundMessage;
import reactor.rabbitmq.Sender;
import javax.annotation.PreDestroy;


public abstract class BaseRabbitMQSenderImpl implements IBaseRabbitMQSender {

    @Lazy
    @Autowired
    private Sender sender;

    private Disposable.Composite allDisposables;

    public abstract String getQueueName();


    @Override
    public void send(byte[] message) {
        allDisposables = Disposables.composite();
        Disposable disposable = sender.send(Flux.just(new OutboundMessage("", getQueueName(), message))).subscribe();
        allDisposables.add(disposable);
    }

    @PreDestroy
    @SneakyThrows
    private void close() {
        sender.close();
        allDisposables.dispose();
    }
}
