package com.lcwd.Electronic.Store.Eletronic.Store.Services;

import com.lcwd.Electronic.Store.Eletronic.Store.DTOS.PageableResponse;
import com.lcwd.Electronic.Store.Eletronic.Store.DTOS.ProductDto;

import java.util.List;

public interface ProductService {

    ProductDto create(ProductDto productDto);

    ProductDto update(ProductDto productDto,String category_id);

    void delete(String ProductId);

    ProductDto getProduct(String product_id);

    PageableResponse<ProductDto> getAll(int pageNumber,int pageSize,String sortDir,String sortBy);

    PageableResponse<ProductDto> getAllLive(int pageNumber,int pageSize,String sortDir,String sortBy);

    PageableResponse<ProductDto> searchByTitle(String subTitle,int pageNumber,int pageSize,String sortDir,String sortBy);


    //create Product with category
    ProductDto createWithCategory(ProductDto productDto,String categoryId);


    //UPdate category of Product

    ProductDto updateCategoryOfProduct(String productId,String categoryId);

    PageableResponse<ProductDto> getAllProductsOfCategory(String categoryId,int pageNumber,int pageSize,String sortDir,String sortBy);



}
