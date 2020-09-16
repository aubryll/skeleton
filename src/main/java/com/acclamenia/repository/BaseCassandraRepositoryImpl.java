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

package com.acclamenia.repository;
import com.acclamenia.model.base.BaseModel;
import org.springframework.data.cassandra.core.ReactiveCassandraOperations;
import org.springframework.data.cassandra.core.query.Criteria;
import org.springframework.data.cassandra.core.query.Query;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.data.cassandra.repository.query.CassandraEntityInformation;
import org.springframework.data.cassandra.repository.support.SimpleReactiveCassandraRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;


public class BaseCassandraRepositoryImpl<T extends BaseModel<ID>, ID> extends SimpleReactiveCassandraRepository<T, ID> implements BaseCassandraRepository<T, ID>  {


    private final ReactiveCassandraOperations operations;
    /**
     * Create a new {@link SimpleReactiveCassandraRepository} for the given {@link CassandraEntityInformation} and
     * {@link ReactiveCassandraOperations}.
     *
     * @param metadata   must not be {@literal null}.
     * @param operations must not be {@literal null}.
     */
    public BaseCassandraRepositoryImpl(CassandraEntityInformation<T, ID> metadata, ReactiveCassandraOperations operations) {
        super(metadata, operations);
        this.operations = operations;
    }


    @Override
    public Mono<Long> countAll() {
        Query query = Query.query(Criteria.where("recordStatus").is(BaseModel.Status.ENABLED));
        return operations.count(query, (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
    }



    @Nonnull
    @Override
    public Mono<T> findById(@Nonnull ID id) {
        Query query = Query.query(Criteria.where("recordStatus").is(BaseModel.Status.ENABLED));
        query.and(Criteria.where("id").is(id));
        return operations.selectOne(query, (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
    }

    @Nonnull
    @Override
    public Flux<T> findAllById(@Nonnull Iterable<ID> ids) {
        Query query = Query.query(Criteria.where("recordStatus").is(BaseModel.Status.ENABLED));
        return operations.select(query, (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
    }

    @Nonnull
    @Override
    public Mono<Void> deleteById(@Nonnull ID id) {
        Query query = Query.query(Criteria.where("recordStatus").is(BaseModel.Status.ENABLED));
        query.and(Criteria.where("id").is(id));
        Mono<T> entity = operations.selectOne(query, (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
        return entity.flatMap(t -> {
            t.setRecordStatus(BaseModel.Status.DELETED);
            return operations.update(entity)
                    .then();
        });
    }


    @Nonnull
    @Override
    public Mono<Void> delete(T entity) {
        entity.setRecordStatus(BaseModel.Status.DELETED);
        return operations.update(entity)
                .then();
    }

    @Override
    public Flux<T> findAll(Pageable pageable) {
        Query query = Query.query(Criteria.where("recordStatus").is(BaseModel.Status.ENABLED));
        query.pageRequest(pageable);
        return operations.select(query, (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
    }
}
