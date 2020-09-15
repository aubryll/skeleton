package com.acclamenia.controller;

import com.acclamenia.model.base.BaseDto;
import com.acclamenia.model.base.BaseModel;
import com.acclamenia.service.IBaseService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;


@Slf4j
public abstract class BaseController<T extends BaseModel, V extends BaseDto, K extends IBaseService<T, V>> implements IBaseController<V> {


    public Logger getLogger() {
        return log;
    }

    public abstract K getService();


    @PostMapping(value = "/create")
    @ResponseBody
    @Override
    public Mono<ResponseEntity<?>> create(@Valid V v) {
        return getService().create(v);
    }

    @PutMapping(value = "/update")
    @ResponseBody
    @Override
    public Mono<ResponseEntity<?>> update(@Valid V v) {
        return getService().update(v);
    }

    @GetMapping(value = "/{id}")
    @ResponseBody
    @Override
    public Mono<ResponseEntity<?>> fetch(@PathVariable String id) {
        return getService().fetch(id);
    }

    @GetMapping(value = "/{page}/{size}")
    @Override
    public Mono<ResponseEntity<?>> fetch(@PathVariable int page, @PathVariable int size) {
        return getService().fetch(PageRequest.of(page, size));
    }

    @PostMapping
    @ResponseBody
    @Override
    public Mono<ResponseEntity<?>> fetchAll(@NotEmpty(message = "Items not specified") List<String> id) {
        return getService().fetchAll(id);
    }

    @DeleteMapping(value = "/delete/{id}")
    @ResponseBody
    @Override
    public Mono<ResponseEntity<?>> delete(@PathVariable String id) {
        return getService().delete(id);
    }

}
