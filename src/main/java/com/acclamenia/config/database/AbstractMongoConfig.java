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
package com.acclamenia.config.database;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.reactivestreams.client.MongoClient;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.ReactiveMongoTransactionManager;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * Annotate decedents
 * with
 *
 * @Configuration
 * @EnableReactiveMongoRepositories(basePackages = {"basepackage"})
 * to activate
 */

public abstract class AbstractMongoConfig extends AbstractReactiveMongoConfiguration {


    abstract public String getConnectionString();


    @Override
    public MongoClient reactiveMongoClient() {
        return super.createReactiveMongoClient(createSettings());
    }


    private MongoClientSettings createSettings() {
        return MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(getConnectionString()))
                .build();
    }


    @Bean
    ReactiveMongoTemplate reactiveMongoTemplate() {
        return new ReactiveMongoTemplate(reactiveMongoClient(), getDatabaseName());
    }

    @Bean
    ReactiveMongoTransactionManager reactiveMongoTransactionManager() {
        return new ReactiveMongoTransactionManager(reactiveMongoDbFactory());
    }

    @Override
    public MongoCustomConversions customConversions() {
        return new MongoCustomConversions(
                Stream.builder()
                        .build()
                        .collect(Collectors.toList())
        );
    }
}
