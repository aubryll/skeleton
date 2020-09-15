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
