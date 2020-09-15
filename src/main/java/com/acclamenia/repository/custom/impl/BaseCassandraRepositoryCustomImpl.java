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

package com.acclamenia.repository.custom.impl;
import com.acclamenia.model.base.BaseModel;
import com.acclamenia.repository.BaseCassandraRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.cassandra.core.ReactiveCassandraTemplate;
import org.springframework.data.cassandra.core.query.Criteria;
import org.springframework.data.cassandra.core.query.Query;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.lang.reflect.ParameterizedType;

//@Repository
//@Transactional
//@Component
public class BaseCassandraRepositoryCustomImpl<T extends BaseModel> implements BaseCassandraRepositoryCustom<T> {

    @Autowired
    @Lazy
    private ReactiveCassandraTemplate reactiveCassandraTemplate;

    @Override
    public Mono<Long> countAll() {
        Query query = Query.query(Criteria.where("recordStatus").is(BaseModel.Status.ENABLED));
        return reactiveCassandraTemplate.count(query, (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
    }

    @Override
    public Flux<T> findAll(Pageable pageable) {
        Query query = Query.query(Criteria.where("recordStatus").is(BaseModel.Status.ENABLED));
        query.pageRequest(pageable);
        return reactiveCassandraTemplate.select(query, (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
    }
}