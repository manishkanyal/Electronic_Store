package com.lcwd.Electronic.Store.Eletronic.Store.Services.Implementation;

import com.lcwd.Electronic.Store.Eletronic.Store.Controllers.CategoryController;
import com.lcwd.Electronic.Store.Eletronic.Store.DTOS.CategoryDTO;
import com.lcwd.Electronic.Store.Eletronic.Store.DTOS.PageableResponse;
import com.lcwd.Electronic.Store.Eletronic.Store.DTOS.UserDTO;
import com.lcwd.Electronic.Store.Eletronic.Store.Entities.Category;
import com.lcwd.Electronic.Store.Eletronic.Store.Entities.User;
import com.lcwd.Electronic.Store.Eletronic.Store.Exceptions.ResourceNotFoundException;
import com.lcwd.Electronic.Store.Eletronic.Store.Helpers.Helper;
import com.lcwd.Electronic.Store.Eletronic.Store.Repositories.CategoryRepository;
import com.lcwd.Electronic.Store.Eletronic.Store.Services.CategoryService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImplementation implements CategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper mapper;

    @Value("${category.file.image.path}")
    private String imagePath;

    Logger logger= LoggerFactory.getLogger(CategoryServiceImplementation.class);

    @Override
    public CategoryDTO create(CategoryDTO categoryDTO) {

        String userId= UUID.randomUUID().toString();
        categoryDTO.setId(userId);

        Category category= mapper.map(categoryDTO,Category.class);
        Category savedCategory=categoryRepository.save(category);
        return mapper.map(savedCategory,CategoryDTO.class);
    }

    @Override
    public CategoryDTO update(CategoryDTO categoryDTO, String id) {

        Category category=categoryRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Category Not Found Exception!!"));
        category.setTitle(categoryDTO.getTitle());
        category.setDescription(categoryDTO.getDescription());
        category.setCoverImage(categoryDTO.getCoverImage());

        Category updatedCategory=categoryRepository.save(category);
        return mapper.map(updatedCategory,CategoryDTO.class);
    }

    @Override
    public void delete(String id) {

        Category category=categoryRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Category Not Found Exception!!"));
        String imageName=category.getCoverImage();
        String imageFullPathName=imagePath+ File.separator+imageName;

        try{
            Path path= Paths.get(imageFullPathName);
            Files.delete(path);
        }
        catch(NoSuchFileException ex)
        {
            logger.info("User Image not Found in folder: {}",imageFullPathName);
            ex.printStackTrace();
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
        }
        categoryRepository.delete(category);
    }

    @Override
    public PageableResponse<CategoryDTO> getAllCategories(int pageNumber,int pageSize,String sortDir,String sortBy) {

        Sort sort= (sortDir.equalsIgnoreCase("asc")) ? (Sort.by(sortBy).ascending()) :(Sort.by(sortBy).descending());
        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);    //Applying paginatiaon and sorting.

        Page<Category> page = categoryRepository.findAll(pageable);     //It returns page type value
        PageableResponse<CategoryDTO> response= Helper.getPageableResponse(page,CategoryDTO.class);
        return response;
    }

    @Override
    public CategoryDTO getSingleCategory(String id) {
        Category category=categoryRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Category Not Found Exception!!"));
        return mapper.map(category,CategoryDTO.class);
    }

    @Override
    public PageableResponse<CategoryDTO> searchUser(String keyword,int pageNumber,int pageSize,String sortDir,String sortBy) {

        Sort sort= (sortDir.equalsIgnoreCase("asc")) ? (Sort.by(sortBy).ascending()) :(Sort.by(sortBy).descending());
        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);    //Applying paginatiaon and sorting.

        Page<Category> page = (Page<Category>) categoryRepository.findByTitleContaining(keyword,pageable);
        PageableResponse<CategoryDTO> response= Helper.getPageableResponse(page,CategoryDTO.class);
        return response;
    }
}
