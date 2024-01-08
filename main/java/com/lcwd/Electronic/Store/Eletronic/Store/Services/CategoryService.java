package com.lcwd.Electronic.Store.Eletronic.Store.Services;

import com.lcwd.Electronic.Store.Eletronic.Store.DTOS.CategoryDTO;
import com.lcwd.Electronic.Store.Eletronic.Store.DTOS.PageableResponse;

import java.util.List;

public interface CategoryService {

    CategoryDTO create(CategoryDTO categoryDTO);

    CategoryDTO update(CategoryDTO categoryDTO,String id);

    void delete(String id);

    PageableResponse<CategoryDTO> getAllCategories(int pageNumber,int pageSize,String sortDir,String sortBy);

    CategoryDTO getSingleCategory(String id);

    PageableResponse<CategoryDTO> searchUser(String keyword,int pageNumber,int pageSize,String sortDir,String sortBy);

}
