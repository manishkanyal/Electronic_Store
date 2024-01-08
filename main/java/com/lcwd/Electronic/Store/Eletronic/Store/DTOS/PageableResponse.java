package com.lcwd.Electronic.Store.Eletronic.Store.DTOS;

import lombok.*;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageableResponse<T> {

    private List<T> content;
    private int pageNumber;
    private int pageSize;
    private long totalElements;
    private  int totalPages;
    private boolean lastPage;

}
