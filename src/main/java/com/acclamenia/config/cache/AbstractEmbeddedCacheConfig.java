package com.acclamenia.config.cache;

import com.hazelcast.config.Config;
import com.hazelcast.config.EvictionPolicy;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.MaxSizeConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;


/**
 * Annotate decedents
 * with
 *@Configuration
 *@EnableCachinge"})
 * activate
 * */
public abstract class AbstractEmbeddedCacheConfig {

    abstract public String getCacheName();
    abstract public String getTimeToLiveSeconds();
    abstract public String getMapName();

    @Lazy
    @Bean
    Config config() {
        return new Config()
                .setInstanceName(getCacheName())
                .addMapConfig(
                        new MapConfig()
                                .setName(getMapName())
                                .setMaxSizeConfig(new MaxSizeConfig(10, MaxSizeConfig.MaxSizePolicy.FREE_HEAP_SIZE))
                                .setEvictionPolicy(EvictionPolicy.LRU)
                                .setTimeToLiveSeconds(Integer.parseInt(getTimeToLiveSeconds())));
    }
}
