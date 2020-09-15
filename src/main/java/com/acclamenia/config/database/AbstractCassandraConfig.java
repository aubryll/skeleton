package com.acclamenia.config.database;

import lombok.RequiredArgsConstructor;
import org.springframework.data.cassandra.config.AbstractReactiveCassandraConfiguration;


/**
 * Annotate decedents
 * with
 * @Configuration
 * @EnableReactiveCassandraRepositories(basePackages = {"basepackage"})
 * activate
 * */
@RequiredArgsConstructor
public abstract class AbstractCassandraConfig extends AbstractReactiveCassandraConfiguration {

    private final String keySpace;
    private final String contactPoints;


    @Override
    protected String getKeyspaceName() {
        return keySpace;
    }

    @Override
    protected String getContactPoints() {
        return contactPoints;
    }
}
