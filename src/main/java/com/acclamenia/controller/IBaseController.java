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
