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

package com.acclamenia.service.cache;

import com.hazelcast.core.HazelcastInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.util.Pair;
import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class BaseConfigCacheServiceImpl<T> implements IBaseConfigCacheService<T> {

    @Autowired
    @Lazy
    private HazelcastInstance hazelcastInstance;
    private Map<String, T> cacheMap;

    abstract public String getMapName();

    @PostConstruct
    public void init() {
        cacheMap = hazelcastInstance.getMap(getMapName());
    }


    @Override
    public void add(Pair<String, T> pair) {
        cacheMap.put(pair.getFirst(), pair.getSecond());
    }

    @Override
    public void remove(Pair<String, T> pair) {
        cacheMap.remove(pair.getFirst());
    }

    @Override
    public void addAll(List<Pair<String, T>> pairs) {
        pairs.forEach(pair -> cacheMap.putIfAbsent(pair.getFirst(), pair.getSecond()));
    }

    @Override
    public void removeAll(List<Pair<String, T>> pairs) {
        pairs.forEach(pair -> cacheMap.remove(pair.getFirst()));
    }

    @Override
    public T find(String key) {
        return cacheMap.get(key);
    }

    @Override
    public List<T> findAll(List<String> keys) {
        return keys.stream().map(key -> cacheMap.get(key)).collect(Collectors.toList());
    }
}
