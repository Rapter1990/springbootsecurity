package com.springboot.springbootsecurity.common.model.dto.response;

import com.springboot.springbootsecurity.common.model.CustomPage;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CustomPagingResponse<T> {

    private List<T> content;

    private Integer pageNumber;

    private Integer pageSize;

    private Long totalElementCount;

    private Integer totalPageCount;

    public static class CustomPagingResponseBuilder<T> {

        public <C> CustomPagingResponseBuilder<T> of(final CustomPage<C> customPage) {
            return CustomPagingResponse.<T>builder()
                    .pageNumber(customPage.getPageNumber())
                    .pageSize(customPage.getPageSize())
                    .totalElementCount(customPage.getTotalElementCount())
                    .totalPageCount(customPage.getTotalPageCount());
        }

    }

}
