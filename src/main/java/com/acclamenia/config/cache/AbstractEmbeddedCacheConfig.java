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

package com.acclamenia.config.cache;
import com.hazelcast.config.Config;
import com.hazelcast.config.EvictionPolicy;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.MaxSizeConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;



/**
 * Annotate decedents
 * with
 *
 * @Configuration
 * @EnableCachinge"}) activate
 */



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
