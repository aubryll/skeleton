package com.acclamenia.repository;

import com.acclamenia.model.base.BaseModel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.NoRepositoryBean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@NoRepositoryBean
public interface BaseMongoRepository<T extends BaseModel> extends ReactiveMongoRepository<T, String> {

    Flux<T> findAll(Pageable pageable);

    Mono<Long> countByStatusNot(BaseModel.Status status);

}