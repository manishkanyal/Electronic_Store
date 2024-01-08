package com.lcwd.Electronic.Store.Eletronic.Store.Repositories;

import com.lcwd.Electronic.Store.Eletronic.Store.DTOS.PageableResponse;
import com.lcwd.Electronic.Store.Eletronic.Store.Entities.Category;
import com.lcwd.Electronic.Store.Eletronic.Store.Entities.Product;
import com.lcwd.Electronic.Store.Eletronic.Store.DTOS.ProductDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,String> {

    Page<Product> findByTitleContaining(String subTitle,Pageable page);

    Page<Product> findByLiveTrue(Pageable page);      //Return all products whose isLive value is true

    Page<Product> findByCategory(Category category,Pageable pageable);



}
