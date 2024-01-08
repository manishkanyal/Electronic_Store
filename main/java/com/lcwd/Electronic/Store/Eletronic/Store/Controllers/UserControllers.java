package com.lcwd.Electronic.Store.Eletronic.Store.Controllers;

import com.lcwd.Electronic.Store.Eletronic.Store.DTOS.ApiResponseMessage;
import com.lcwd.Electronic.Store.Eletronic.Store.DTOS.ImageResponse;
import com.lcwd.Electronic.Store.Eletronic.Store.DTOS.PageableResponse;
import com.lcwd.Electronic.Store.Eletronic.Store.DTOS.UserDTO;
import com.lcwd.Electronic.Store.Eletronic.Store.Entities.User;
import com.lcwd.Electronic.Store.Eletronic.Store.Services.FileService;
import com.lcwd.Electronic.Store.Eletronic.Store.Services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/users")
//Tag is used to set basic information for the controller in documentation
@Tag(name="User Controller",description = "Rest API related to perform User related Operation")
public class UserControllers {

    @Autowired
    private UserService userService;

    @Autowired
    private FileService fileService;

    @Value("${user.file.image.path}")
    private String imagePath;

    Logger logger = LoggerFactory.getLogger(UserControllers.class);

    //create
    //@Valid annonotation is used to validate the constraints used in particular field of class .
    @PostMapping
    @Operation(summary = "Create new User!!",description = "This is a User API")
    //@ApiResponses annotation can be added to an API operation method to provide a list of possible responses for that operation.
    // Each response is specified using an @ApiResponse annotation.
    @ApiResponses(value={
            @ApiResponse(responseCode = "200",description="Success| Ok"),
            @ApiResponse(responseCode = "401",description="Not Authorised!!"),
            @ApiResponse(responseCode = "201",description="New User Created!!")
    })
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserDTO userDto)
    {
        UserDTO user=userService.createUser(userDto);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }


    //update
    @PutMapping("/{userId}")
    public ResponseEntity<UserDTO> updateUser(@Valid @RequestBody UserDTO userDto,@PathVariable("userId") String id)
    {
        UserDTO user=userService.updateUser(userDto,id);
        return new ResponseEntity<>(user,HttpStatus.OK);
    }


    //delete
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponseMessage> deleteUser(@PathVariable String userId)
    {
        userService.deleteUser(userId);
        ApiResponseMessage message=ApiResponseMessage.builder().message("User with given userId deleted successfully! ")
                .success(true).status(HttpStatus.OK).build();
        return new ResponseEntity<>(message,HttpStatus.OK);
    }

    //getall
    @GetMapping
    //@Operation annotation is used to provide metadata for a single API operation.
    @Operation(summary = "Get ALL Users")
    public ResponseEntity<PageableResponse<UserDTO>>  getAllUsers(@RequestParam(value="pageNumber",defaultValue = "0",required = false) int pageNumber,
                                                                  @RequestParam(value="pageSize",defaultValue = "10",required = false) int pageSize,
                                                                  @RequestParam(value="sortBy",defaultValue = "name",required = false) String sortBy,
                                                                  @RequestParam(value="sortDir",defaultValue = "asc",required = false) String sortDir){

        PageableResponse<UserDTO> usersPageResponse=userService.getAllUsers(pageNumber,pageSize,sortBy,sortDir);
        return new ResponseEntity<>(usersPageResponse,HttpStatus.OK);
    }

    //get user by ID
    @GetMapping("/{userId}")
    @Operation(summary = "Get single User By userId!!")
    public ResponseEntity<UserDTO> getUserById(@PathVariable String userId)
    {
        return new ResponseEntity<>(userService.getUserById(userId),HttpStatus.OK);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserDTO> getUserByEmail(@PathVariable String email)
    {
        return new ResponseEntity<>(userService.getUserByEmail(email),HttpStatus.OK);
    }

    @GetMapping("/search/{keywords}")
    public ResponseEntity<List<UserDTO>>  getAllUsersByKeywords(@PathVariable String keywords)
    {
        return new ResponseEntity<>(userService.searchUser(keywords),HttpStatus.OK);
    }

    @PostMapping("/image/{userId}")
    public ResponseEntity<ImageResponse> uploadUserImage(@RequestParam("userimage")MultipartFile image,
                                                         @PathVariable String userId) throws IOException {
        String imageName=fileService.uploadFile(image,imagePath);

        UserDTO user=userService.getUserById(userId);
        user.setImageName(imageName);

        UserDTO savedUser=userService.updateUser(user,userId);

        ImageResponse imageResponse=ImageResponse.builder().imageName(imageName).success(true).status(HttpStatus.CREATED).build();
        return new ResponseEntity<>(imageResponse,HttpStatus.CREATED);
    }


    //sending image from backend to client

    @GetMapping("/image/{userId}")
    public void serveUserImage(@PathVariable String userId, HttpServletResponse response) throws IOException {
        UserDTO user=userService.getUserById(userId);
        logger.info("User image name: {}",user.getImageName());
        InputStream resource= fileService.getUploadedImage(imagePath,user.getImageName());

        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource,response.getOutputStream());


    }



}
