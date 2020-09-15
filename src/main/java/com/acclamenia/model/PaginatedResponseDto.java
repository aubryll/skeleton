package com.acclamenia.model;

import com.acclamenia.model.base.BaseDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@Builder (toBuilder = true)
public class PaginatedResponseDto<V extends BaseDto> {

    public static final int FIRST_PAGE = 1;
    public static final int DEFAULT_PAGE_SIZE = 20;

    private long pageNumber;
    private long pageSize;
    private long totalElements;
    private List<V> elements;

    @JsonProperty
    public long totalPages() {
        return pageSize > 0 ? (totalElements - 1) / pageSize + 1 : 0;
    }

    @JsonProperty
    public boolean firstPage() {
        return pageNumber == FIRST_PAGE;
    }

    @JsonProperty
    public boolean lastPage() {
        return (pageNumber + 1) * pageSize >= totalElements;
    }

}
