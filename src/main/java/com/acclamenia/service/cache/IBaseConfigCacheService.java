package com.acclamenia.service.cache;

import org.springframework.data.util.Pair;

import java.util.List;

public interface IBaseConfigCacheService<T> {

    void add(Pair<String, T> pair);

    void remove(Pair<String, T> pair);

    void addAll(List<Pair<String, T>> pairs);

    void removeAll(List<Pair<String, T>> pairs);

    T find(String key);

    List<T> findAll(List<String> keys);

}
