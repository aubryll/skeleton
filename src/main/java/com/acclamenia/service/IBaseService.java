package com.acclamenia.service;

import com.acclamenia.model.base.BaseDto;
import com.acclamenia.model.base.BaseModel;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;
import java.util.Comparator;
import java.util.List;


public interface IBaseService<T extends BaseModel, V extends BaseDto> {

    Mono<T> createModel(V v);

    Mono<T> createUpdateModel(V v);

    Mono<ResponseEntity<?>> create(V v);

    Mono<ResponseEntity<?>> update(V v);

    Mono<ResponseEntity<?>> delete(String id);

    Mono<ResponseEntity<?>> fetch(String id);

    Mono<ResponseEntity<?>> fetchAll(List<String> ids);

    Mono<ResponseEntity<?>> fetch(Pageable pageable);

    Comparator<T> createComparator();


}
