package com.acclamenia.config.database;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.reactivestreams.client.MongoClient;
import lombok.RequiredArgsConstructor;
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
 * @Configuration
 * @EnableReactiveMongoRepositories(basePackages = {"basepackage"})
 * to activate
 * */

@RequiredArgsConstructor
public abstract class AbstractMongoConfig extends AbstractReactiveMongoConfiguration {

    private final String database;
    private final String connectionString;



    @Override
    protected String getDatabaseName() {
        return database;
    }


    @Override
    public MongoClient reactiveMongoClient() {
        return super.createReactiveMongoClient(createSettings());
    }


    private MongoClientSettings createSettings() {
        return MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(connectionString))
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
