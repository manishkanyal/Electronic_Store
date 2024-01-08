package com.lcwd.Electronic.Store.Eletronic.Store.Helpers;

import com.lcwd.Electronic.Store.Eletronic.Store.DTOS.PageableResponse;
import com.lcwd.Electronic.Store.Eletronic.Store.DTOS.UserDTO;
import com.lcwd.Electronic.Store.Eletronic.Store.Entities.User;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class Helper {

    public static<U,V> PageableResponse<V> getPageableResponse(Page<U> page,Class<V> type)
    {
        List<U> users= page.getContent();
        List<V> userDtos=users.stream().map(user-> new ModelMapper().map(user,type)).collect(Collectors.toList());

        PageableResponse<V> response=new PageableResponse<>();
        response.setContent(userDtos);
        response.setPageNumber(page.getNumber());
        response.setPageSize(page.getSize());
        response.setTotalElements(page.getTotalElements());
        response.setTotalPages(page.getTotalPages());
        response.setLastPage(page.isLast());
        return response;
    }

}
