package com.lcwd.Electronic.Store.Eletronic.Store.Services.Implementation;

import com.lcwd.Electronic.Store.Eletronic.Store.Controllers.CategoryController;
import com.lcwd.Electronic.Store.Eletronic.Store.DTOS.CategoryDTO;
import com.lcwd.Electronic.Store.Eletronic.Store.DTOS.PageableResponse;
import com.lcwd.Electronic.Store.Eletronic.Store.Entities.Category;
import com.lcwd.Electronic.Store.Eletronic.Store.Entities.Product;
import com.lcwd.Electronic.Store.Eletronic.Store.DTOS.ProductDto;
import com.lcwd.Electronic.Store.Eletronic.Store.Exceptions.ResourceNotFoundException;
import com.lcwd.Electronic.Store.Eletronic.Store.Helpers.Helper;
import com.lcwd.Electronic.Store.Eletronic.Store.Repositories.CategoryRepository;
import com.lcwd.Electronic.Store.Eletronic.Store.Repositories.ProductRepository;
import com.lcwd.Electronic.Store.Eletronic.Store.Services.ProductService;
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
import org.springframework.web.bind.annotation.PostMapping;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductServiceImplementation implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ModelMapper mapper;

    @Value("${product.image.path}")
    private String imagePath;

    @Autowired
    private CategoryRepository categoryRepository;

    Logger logger= LoggerFactory.getLogger(ProductServiceImplementation.class);

    @Override
    public ProductDto create(ProductDto productDto) {


        Product product=mapper.map(productDto,Product.class);

        String productId= UUID.randomUUID().toString();
        product.setProductId(productId);

        product.setAddedDate(new Date());

        Product savedProduct= productRepository.save(product);
        System.out.println("Saved Product: "+savedProduct);
        return mapper.map(savedProduct,ProductDto.class);
    }

    @Override
    public ProductDto update(ProductDto productDto,String product_id) {

        Product product=productRepository.findById(product_id).orElseThrow(()->new ResourceNotFoundException("Unable to find product with gievn Id"));

        product.setTitle(productDto.getTitle());
        product.setLive(product.isLive());
        product.setDescription(product.getDescription());
        product.setPrice(product.getPrice());
        product.setAvailable_quantity(productDto.getAvailable_quantity());
        product.setStock(productDto.isStock());
        product.setDiscounted_price(productDto.getDiscounted_price());
        product.setProductImageName(productDto.getProductImageName());

        Product savedProduct= productRepository.save(product);
        return mapper.map(savedProduct,ProductDto.class);
    }

    @Override
    public void delete(String productId) {
        Product product=productRepository.findById(productId).orElseThrow(()->new ResourceNotFoundException("Unable to find product with gievn Id"));
        String imageName=product.getProductImageName();
        String fullImagePath=imagePath+ File.separator+imageName;

        try {
            Path path = Paths.get(fullImagePath);
            Files.delete(path);
        }
        catch(NoSuchFileException ex)
        {
            logger.info("User Image not Found in folder: {}", fullImagePath);
            ex.printStackTrace();
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
        }

        productRepository.delete(product);
    }

    @Override
    public ProductDto getProduct(String product_id) {
        Product product=productRepository.findById(product_id).orElseThrow(()->new ResourceNotFoundException("Unable to find product with gievn Id"));
        return mapper.map(product,ProductDto.class);
    }

    @Override
    public PageableResponse<ProductDto> getAll(int pageNumber,int pageSize,String sortDir,String sortBy) {

        Sort sort= (sortDir.equalsIgnoreCase("asc")) ? (Sort.by(sortBy).ascending()) :(Sort.by(sortBy).descending());
        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);    //Applying paginatiaon and sorting.
        Page<Product> page = productRepository.findAll(pageable);     //It returns page type value
        PageableResponse<ProductDto> response= Helper.getPageableResponse(page,ProductDto.class);
        return response;
    }

    @Override
    public PageableResponse<ProductDto> getAllLive(int pageNumber,int pageSize,String sortDir,String sortBy) {
        Sort sort= (sortDir.equalsIgnoreCase("asc")) ? (Sort.by(sortBy).ascending()) :(Sort.by(sortBy).descending());
        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);    //Applying paginatiaon and sorting.
        Page<Product> page = productRepository.findByLiveTrue(pageable);     //It returns page type value
        PageableResponse<ProductDto> response= Helper.getPageableResponse(page,ProductDto.class);
        return response;
    }


    @Override
    public PageableResponse<ProductDto> searchByTitle(String subTitle,int pageNumber,int pageSize,String sortDir,String sortBy) {
        Sort sort= (sortDir.equalsIgnoreCase("asc")) ? (Sort.by(sortBy).ascending()) :(Sort.by(sortBy).descending());
        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);    //Applying paginatiaon and sorting.
        Page<Product> page = productRepository.findByTitleContaining(subTitle,pageable);     //It returns page type value
        PageableResponse<ProductDto> response= Helper.getPageableResponse(page,ProductDto.class);
        return response;
    }

    @Override
    public ProductDto createWithCategory(ProductDto productDto, String categoryId) {

        Category category=categoryRepository.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException("Category not found!"));
        Product product=mapper.map(productDto,Product.class);

        String productId= UUID.randomUUID().toString();
        product.setProductId(productId);

        product.setAddedDate(new Date());

        product.setCategory(category);

        Product savedProduct= productRepository.save(product);

        System.out.println("Saved Product O/P"+ savedProduct);

        return mapper.map(savedProduct,ProductDto.class);    //Here in map method it is not able to map Category with CategoryDTO that is inside Product and ProductDTO class
    }

    @Override
    public ProductDto updateCategoryOfProduct(String productId, String categoryId) {

        Product product=productRepository.findById(productId).orElseThrow(()-> new ResourceNotFoundException("Unable to find the Product with given Id"));
        Category category=categoryRepository.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException("Unable to find the Category with given Id"));

        product.setCategory(category);

        Product savedProduct =productRepository.save(product);

        return mapper.map(savedProduct,ProductDto.class);
    }

    @Override
    public PageableResponse<ProductDto> getAllProductsOfCategory(String categoryId,int pageNumber,int pageSize,String sortDir,String sortBy) {

        Category category=categoryRepository.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException("Unable to find the Category with given Id"));
        Sort sort= (sortDir.equalsIgnoreCase("asc")) ? (Sort.by(sortBy).ascending()) :(Sort.by(sortBy).descending());
        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);    //Applying paginatiaon and sorting.
        Page<Product> page=productRepository.findByCategory(category,pageable);

        return Helper.getPageableResponse(page,ProductDto.class);

    }
}
