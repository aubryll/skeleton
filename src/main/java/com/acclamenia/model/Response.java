package com.acclamenia.model;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;


@SuperBuilder(toBuilder = true)
@Getter
public class Response {

    private final HttpStatus status;
    private final Object payLoad;
    private final String message;
    private final Object errors;
    private final Object metadata;

}