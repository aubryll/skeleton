package com.acclamenia.model.base;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BaseDto<ID> {

    private ID id;

}
