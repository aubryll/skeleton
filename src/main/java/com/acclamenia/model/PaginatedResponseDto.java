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
package com.acclamenia.model;
import com.acclamenia.model.base.BaseDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@Builder(toBuilder = true)
public class PaginatedResponseDto<V extends BaseDto<ID>, ID> {

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
