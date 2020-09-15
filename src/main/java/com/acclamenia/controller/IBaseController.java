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

package com.acclamenia.controller;
import com.acclamenia.model.base.BaseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.List;



public interface IBaseController<T extends BaseDto> {

    Mono<ResponseEntity<?>> create(@RequestBody @Valid T t);

    Mono<ResponseEntity<?>> update(@RequestBody @Valid T t);

    Mono<ResponseEntity<?>> fetch(@PathVariable @Valid String id);

    Mono<ResponseEntity<?>> fetch(@PathVariable @Valid int page, @PathVariable @Valid int size);

    Mono<ResponseEntity<?>> fetchAll(@PathVariable @Valid List<String> ids);

    Mono<ResponseEntity<?>> delete(@PathVariable @Valid String id);

}
