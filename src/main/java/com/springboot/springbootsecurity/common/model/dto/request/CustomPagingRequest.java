package com.springboot.springbootsecurity.common.model.dto.request;

import com.springboot.springbootsecurity.common.model.CustomPaging;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class CustomPagingRequest {

    private CustomPaging pagination;

    public Pageable toPageable() {
        return PageRequest.of(
                Math.toIntExact(pagination.getPageNumber()),
                Math.toIntExact(pagination.getPageSize())
        );
    }

}
