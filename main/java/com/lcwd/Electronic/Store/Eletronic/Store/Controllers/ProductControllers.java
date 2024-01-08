package com.lcwd.Electronic.Store.Eletronic.Store.Controllers;

import com.lcwd.Electronic.Store.Eletronic.Store.DTOS.*;
import com.lcwd.Electronic.Store.Eletronic.Store.Entities.Product;
import com.lcwd.Electronic.Store.Eletronic.Store.Services.FileService;
import com.lcwd.Electronic.Store.Eletronic.Store.Services.ProductService;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductControllers {

    @Autowired
    private ProductService productService;

    @Autowired
    private FileService fileService;

    @Value("${product.image.path}")
    private String imagePath;

    Logger logger= LoggerFactory.getLogger(ProductControllers.class);

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto)
    {
        System.out.println("client Val: "+productDto);
        ProductDto createdProdctDto =productService.create(productDto);
        System.out.println("Server Val: "+createdProdctDto);
        return new ResponseEntity<>(createdProdctDto, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{productId}")
    ResponseEntity<ProductDto> updateProduct(@PathVariable String productId,@RequestBody ProductDto productDto)
    {
        ProductDto updatedProdctDto =productService.update(productDto,productId);
        return new ResponseEntity<>(updatedProdctDto, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{productId}")
    ResponseEntity<ApiResponseMessage> deleteProduct(@PathVariable String productId)
    {
        productService.delete(productId);
        ApiResponseMessage responseMessage=ApiResponseMessage.builder().message("Product is deleted Successfully").status(HttpStatus.OK).success(true).build();
        return ResponseEntity.ok(responseMessage);
    }

    @GetMapping
    public ResponseEntity<PageableResponse<ProductDto>> getAllProducts(@RequestParam(defaultValue = "0",required = false) int pageNumber,
                                                                        @RequestParam(defaultValue = "10",required = false) int pageSize,
                                                                        @RequestParam(defaultValue = "title",required = false) String sortBy,
                                                                        @RequestParam(defaultValue = "asc",required = false) String sortDir)
    {
        PageableResponse<ProductDto> pageableResponse=productService.getAll(pageNumber,pageSize,sortDir,sortBy);
        return ResponseEntity.ok(pageableResponse);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductDto> getSingle(@PathVariable String productId)
    {
        ProductDto productDTO=productService.getProduct(productId);
        return ResponseEntity.ok(productDTO);
    }


    // /products/live path
    @GetMapping("/live")
    public ResponseEntity<PageableResponse<ProductDto>> getAllLive(@RequestParam(defaultValue = "0",required = false) int pageNumber,
                                                                       @RequestParam(defaultValue = "10",required = false) int pageSize,
                                                                       @RequestParam(defaultValue = "title",required = false) String sortBy,
                                                                       @RequestParam(defaultValue = "asc",required = false) String sortDir)
    {
        PageableResponse<ProductDto> pageableResponse=productService.getAllLive(pageNumber,pageSize,sortDir,sortBy);
        return ResponseEntity.ok(pageableResponse);
    }

    @GetMapping("/search/{keyword}")
    public ResponseEntity<PageableResponse<ProductDto>> search(@PathVariable String keyword,
                                                    @RequestParam(defaultValue = "0",required = false) int pageNumber,
                                                    @RequestParam(defaultValue = "10",required = false) int pageSize,
                                                    @RequestParam(defaultValue = "title",required = false) String sortBy,
                                                    @RequestParam(defaultValue = "asc",required = false) String sortDir)
    {
        return ResponseEntity.ok(productService.searchByTitle(keyword,pageNumber,pageSize,sortDir,sortBy));
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/image/{productId}")
    public ResponseEntity<ImageResponse> uploadProductImage(@PathVariable String productId,
                                                            @RequestParam("productImage")MultipartFile image) throws IOException {
        String fileName=fileService.uploadFile(image,imagePath);

        ProductDto product=productService.getProduct(productId);
        product.setProductImageName(fileName);

        ProductDto updatedProductDto= productService.update(product,productId);

        ImageResponse imageResponse=ImageResponse.builder().imageName(fileName).success(true).status(HttpStatus.CREATED).build();
        return new ResponseEntity<>(imageResponse,HttpStatus.CREATED);
    }


    @GetMapping("/image/{productId}")
    public void getServerCatgoryImage(@PathVariable String productId, HttpServletResponse response) throws IOException {
        ProductDto  productDTO=productService.getProduct(productId);
        logger.info("Cover image name :{}",productDTO.getProductImageName());

        InputStream inputStream=fileService.getUploadedImage(imagePath,productDTO.getProductImageName());

        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(inputStream,response.getOutputStream());
    }


}
