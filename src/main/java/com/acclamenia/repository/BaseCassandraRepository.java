package com.acclamenia.repository;

import com.acclamenia.model.base.BaseModel;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@NoRepositoryBean
public interface BaseCassandraRepository<T extends BaseModel> extends ReactiveCassandraRepository<T, String> {


    Flux<T> findAll(Pageable pageable);

    Mono<Long> countByStatusNot(BaseModel.Status status);

}
