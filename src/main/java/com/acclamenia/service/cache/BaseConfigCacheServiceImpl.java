package com.acclamenia.service.cache;

import com.hazelcast.core.HazelcastInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    abstract public String getMapName();

    private Map<String, T> cacheMap;


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
