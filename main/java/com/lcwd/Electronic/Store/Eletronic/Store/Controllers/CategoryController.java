package com.lcwd.Electronic.Store.Eletronic.Store.Controllers;

import com.lcwd.Electronic.Store.Eletronic.Store.DTOS.*;
import com.lcwd.Electronic.Store.Eletronic.Store.Entities.Product;
import com.lcwd.Electronic.Store.Eletronic.Store.Services.CategoryService;
import com.lcwd.Electronic.Store.Eletronic.Store.Services.FileService;
import com.lcwd.Electronic.Store.Eletronic.Store.Services.ProductService;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    Logger logger= LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private FileService fileService;

    @Value("${category.file.image.path}")
    private String imagePath;

    @Autowired
    private ProductService productService;


    //Created
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody CategoryDTO categoryDTO)
    {

        CategoryDTO categoryDTO1= categoryService.create(categoryDTO);
        return new ResponseEntity<>(categoryDTO1, HttpStatus.CREATED);

    }


    //Update
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryDTO> updateCategory(@RequestBody CategoryDTO categoryDTO,@PathVariable String categoryId )
    {
        CategoryDTO categoryDTO1=categoryService.update(categoryDTO,categoryId);
        return new ResponseEntity<>(categoryDTO1, HttpStatus.OK);
    }

    //delete
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<ApiResponseMessage> deleteCategory(@PathVariable String categoryId)
    {
        categoryService.delete(categoryId);
        ApiResponseMessage responseMessage =ApiResponseMessage.builder().message("Category is Deleted Successfully!").success(true).status(HttpStatus.OK).build();
        return new ResponseEntity<>(responseMessage,HttpStatus.OK);
    }

    //GetAlleData
    @GetMapping
    public ResponseEntity<PageableResponse<CategoryDTO>> getAllCategory(@RequestParam(defaultValue = "0",required = false) int pageNumber,
                                                                        @RequestParam(defaultValue = "10",required = false) int pageSize,
                                                                        @RequestParam(defaultValue = "title",required = false) String sortBy,
                                                                        @RequestParam(defaultValue = "asc",required = false) String sortDir)
    {
        PageableResponse<CategoryDTO> pageableResponse=categoryService.getAllCategories(pageNumber,pageSize,sortDir,sortBy);
        return ResponseEntity.ok(pageableResponse);

    }

    //GetSingleData
    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDTO> getSingle(@PathVariable String categoryId)
    {
        CategoryDTO categoryDTO=categoryService.getSingleCategory(categoryId);
        return ResponseEntity.ok(categoryDTO);
    }

    @GetMapping("/search/{keyword}")
    public ResponseEntity<PageableResponse<CategoryDTO>> search(@PathVariable String keyword,@RequestParam(defaultValue = "0",required = false) int pageNumber,
                                                    @RequestParam(defaultValue = "10",required = false) int pageSize,
                                                    @RequestParam(defaultValue = "title",required = false) String sortBy,
                                                    @RequestParam(defaultValue = "asc",required = false) String sortDir)
    {
        return ResponseEntity.ok(categoryService.searchUser(keyword,pageNumber,pageSize,sortDir,sortBy));
    }

    @PostMapping("/image/{categoryId}")
    public ResponseEntity<ImageResponse> uploadCategoryImage(@PathVariable String categoryId,
                                                             @RequestParam("categoryImage") MultipartFile image) throws IOException {
        String imageName=fileService.uploadFile(image,imagePath);

        CategoryDTO categoryDTO=categoryService.getSingleCategory(categoryId);
        categoryDTO.setCoverImage(imageName);
        logger.info("Catgeory cover image name {}",imageName);

        CategoryDTO savedCategory=categoryService.update(categoryDTO,categoryId);

        ImageResponse imageResponse=ImageResponse.builder().imageName(imageName).success(true).status(HttpStatus.CREATED).build();
        return new ResponseEntity<>(imageResponse,HttpStatus.CREATED);
    }

    @GetMapping("/image/{categoryId}")
    public void getServerCatgoryImage(@PathVariable String categoryId, HttpServletResponse response) throws IOException {
        CategoryDTO  categoryDTO=categoryService.getSingleCategory(categoryId);
        logger.info("Cover image name :{}",categoryDTO.getCoverImage());

        InputStream inputStream=fileService.getUploadedImage(imagePath,categoryDTO.getCoverImage());

        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(inputStream,response.getOutputStream());
    }


    @PostMapping("/{categoryId}/products")
    public ResponseEntity<ProductDto> createProductWithCategory(@PathVariable String categoryId,
                                                                @RequestBody ProductDto productDto
    )
    {
        ProductDto productWithCategory =productService.createWithCategory(productDto,categoryId);
        System.out.println("Product With Category O/P: "+productWithCategory);
        return new ResponseEntity<>(productWithCategory,HttpStatus.CREATED);
    }

    @PutMapping("/{categoryId}/products/{productId}")
    public ResponseEntity<ProductDto> setCategoryOfProducts(@PathVariable String categoryId, @PathVariable String productId)
    {
        ProductDto productDto=productService.updateCategoryOfProduct(productId,categoryId);
        return ResponseEntity.ok(productDto);
    }

    @GetMapping("/{categoryId}/products")
    public ResponseEntity<PageableResponse<ProductDto>> getProductOfCategory(@PathVariable String categoryId,
                                                                             @RequestParam(defaultValue = "0",required = false) int pageNumber,
                                                                             @RequestParam(defaultValue = "10",required = false) int pageSize,
                                                                             @RequestParam(defaultValue = "title",required = false) String sortBy,
                                                                             @RequestParam(defaultValue = "asc",required = false) String sortDir)
    {
        PageableResponse<ProductDto> response=productService.getAllProductsOfCategory(categoryId,pageNumber,pageSize,sortDir,sortBy);
        return ResponseEntity.ok(response);
    }




}
