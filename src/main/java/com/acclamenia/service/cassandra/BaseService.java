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

package com.acclamenia.service.cassandra;
import com.acclamenia.model.PaginatedResponseDto;
import com.acclamenia.model.Response;
import com.acclamenia.model.base.BaseDto;
import com.acclamenia.model.base.BaseModel;
import com.acclamenia.repository.BaseCassandraRepository;
import com.acclamenia.service.IBaseService;
import lombok.Getter;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import static java.util.Comparator.comparing;
import static java.util.Comparator.naturalOrder;


@Transactional
public abstract class BaseService<T extends BaseModel, V extends BaseDto, E extends BaseCassandraRepository<T>> implements IBaseService<T, V> {

    @Getter
    private final ModelMapper modelMapper;

    public BaseService() {
        modelMapper = new ModelMapper();
    }

    abstract public E getRepository();

    public Mono<T> validate(Mono<T> t) {
        return t;
    }

    public Flux<T> validate(Flux<T> t) {
        return t;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Mono<T> createModel(V v) {
        T t = getModelMapper().map(v, (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
        t.setRecordStatus(BaseModel.Status.ENABLED);
        return Mono.just(t);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Mono<T> createUpdateModel(V v) {
        return getRepository().findById(v.getId())
                .flatMap(x -> {
                    T t = getModelMapper().map(v, (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
                    t.setId(x.getId());
                    t.setRecordStatus(BaseModel.Status.ENABLED);
                    return getRepository().save(t);
                });
    }

    @Override
    public Mono<ResponseEntity<?>> create(V v) {
        Mono<T> model = this.createModel(v);
        return validate(model)
                .flatMap(k -> getRepository().save(k))
                .flatMap(__ -> Mono.<ResponseEntity<?>>just(ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body(Response.builder()
                                .payLoad(HttpStatus.CREATED)
                                .build())))
                .switchIfEmpty(Mono.defer(() ->
                        Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(Response.builder()
                                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                        .message("Unknown error occurred, try again")
                                        .build()))));
    }


    @Override
    public Mono<ResponseEntity<?>> update(V v) {
        Mono<T> model = this.createUpdateModel(v);
        return validate(model)
                .filter(x -> x.getRecordStatus() == BaseModel.Status.ENABLED)
                .flatMap(x -> getRepository().save(x))
                .flatMap(__ -> Mono.<ResponseEntity<?>>just(ResponseEntity
                        .status(HttpStatus.OK)
                        .body(Response.builder()
                                .payLoad(HttpStatus.OK)
                                .build())))
                .switchIfEmpty(Mono.defer(() ->
                        Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(Response.builder()
                                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                        .message("Unknown error occurred, try again")
                                        .build()))));
    }


    @Override
    public Mono<ResponseEntity<?>> delete(String id) {
        return getRepository().findById(id)
                .filter(t -> t.getRecordStatus() == BaseModel.Status.ENABLED)
                .flatMap(b -> {
                    b.setRecordStatus(BaseModel.Status.DELETED);
                    return getRepository().save(b);
                })
                .flatMap(__ -> Mono.<ResponseEntity<?>>just(ResponseEntity
                        .status(HttpStatus.OK)
                        .body(Response.builder()
                                .payLoad(HttpStatus.OK)
                                .build())))
                .switchIfEmpty(Mono.defer(() ->
                        Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(Response.builder().status(HttpStatus.INTERNAL_SERVER_ERROR)
                                        .message("Unknown error occurred, try again")
                                        .build()))));
    }


    @Override
    @SuppressWarnings("unchecked")
    public Mono<ResponseEntity<?>> fetch(String id) {
        return getRepository().findById(id)
                .filter(t -> t.getRecordStatus() == BaseModel.Status.ENABLED)
                .flatMap(t -> Mono.<ResponseEntity<?>>just(ResponseEntity
                        .status(HttpStatus.OK)
                        .body(Response.builder()
                                .status(HttpStatus.OK)
                                .payLoad(getModelMapper().map(t, (Class<V>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1]))
                                .build())))
                .switchIfEmpty(Mono.defer(() ->
                        Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body(Response.builder().status(HttpStatus.NOT_FOUND)
                                        .message("Not found")
                                        .build()))));
    }


    @Override
    @SuppressWarnings("unchecked")
    public Mono<ResponseEntity<?>> fetchAll(List<String> ids) {
        return getRepository().findAllById(ids)
                .filter(t -> t.getRecordStatus() == BaseModel.Status.ENABLED)
                .sort(createComparator())
                .collectList()
                .flatMap(t -> Mono.<ResponseEntity<?>>just(ResponseEntity
                        .status(HttpStatus.OK)
                        .body(Response.builder()
                                .payLoad(t.stream().map(x -> getModelMapper().map(x, (Class<V>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1]))
                                        .collect(Collectors.toList()))
                                .build())))
                .switchIfEmpty(Mono.defer(() ->
                        Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body(Response.builder().status(HttpStatus.NOT_FOUND)
                                        .message("Not found")
                                        .build()))));
    }


    @Override
    public Comparator<T> createComparator() {
        return comparing(T::getCreatedAt, naturalOrder());
    }

    @Override
    @SuppressWarnings("unchecked")
    public Mono<ResponseEntity<?>> fetch(Pageable pageable) {
        return Mono.zip(getRepository().findAll(pageable).collectList(),
                getRepository().countAll())
                .flatMap(r -> {
                    List<V> mResult = r.getT1().stream()
                            .map(t -> getModelMapper().map(t, (Class<V>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1]))
                            .collect(Collectors.toList());

                    PaginatedResponseDto<V> paginatedListDto = PaginatedResponseDto.<V>builder()
                            .totalElements(r.getT2())
                            .elements(mResult)
                            .pageNumber(pageable.getPageNumber())
                            .pageSize(pageable.getPageSize())
                            .build();
                    return Mono.<ResponseEntity<?>>just(ResponseEntity.status(HttpStatus.OK)
                            .body(Response.builder()
                                    .status(HttpStatus.OK)
                                    .payLoad(paginatedListDto)
                                    .build()));
                }).switchIfEmpty(Mono.defer(() ->
                        Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body(Response.builder()
                                        .status(HttpStatus.NOT_FOUND)
                                        .message("Not found")
                                        .build()))));

    }


}
