package com.lcwd.Electronic.Store.Eletronic.Store.Repositories;

import com.lcwd.Electronic.Store.Eletronic.Store.DTOS.CategoryDTO;
import com.lcwd.Electronic.Store.Eletronic.Store.Entities.Category;
import com.lcwd.Electronic.Store.Eletronic.Store.Entities.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category,String> {

    List<Category> findByTitleContaining(String keywords,Pageable pageable);

}
