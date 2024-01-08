package com.lcwd.Electronic.Store.Eletronic.Store.Services.Implementation;

import com.lcwd.Electronic.Store.Eletronic.Store.Controllers.UserControllers;
import com.lcwd.Electronic.Store.Eletronic.Store.DTOS.PageableResponse;
import com.lcwd.Electronic.Store.Eletronic.Store.DTOS.UserDTO;
import com.lcwd.Electronic.Store.Eletronic.Store.Entities.Roles;
import com.lcwd.Electronic.Store.Eletronic.Store.Exceptions.ResourceNotFoundException;
import com.lcwd.Electronic.Store.Eletronic.Store.Helpers.Helper;
import com.lcwd.Electronic.Store.Eletronic.Store.Repositories.RoleRepository;
import com.lcwd.Electronic.Store.Eletronic.Store.Repositories.UserRepository;
import com.lcwd.Electronic.Store.Eletronic.Store.Services.UserService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.lcwd.Electronic.Store.Eletronic.Store.Entities.User;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImplementation implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper mapper;

    @Value("${user.file.image.path}")
    private String imagePath;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${normal.role.id}")
    private String normalRoleId;

    @Autowired
   private RoleRepository roleRepository;

    Logger logger = LoggerFactory.getLogger(UserControllers.class);

    @Override
    public UserDTO createUser(UserDTO userDTO) {

        //Generate unique id
        String userId= UUID.randomUUID().toString();
        userDTO.setUserId(userId);

        //Encoding password before string it in database
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        //DTO -> Entity
        User user=dtoToEntity(userDTO);        //Here we are converting DTO object to User object .


        //fetch Role like admin , normal user
        Roles role=roleRepository.findById(normalRoleId).get();
        user.getRoles().add(role);
        User savedUser=userRepository.save(user);

        //Entity -> DTO
        UserDTO newDTO=entityToDTO(savedUser);
        return newDTO;
    }



    @Override
    public UserDTO updateUser(UserDTO userDto, String userId) {

        User user=userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User Not Found!") );

        user.setName(userDto.getName());
        user.setAbout(userDto.getAbout());
        user.setGender(userDto.getGender());
        user.setImageName(userDto.getImageName());
        user.setPassword(userDto.getPassword());

        userRepository.save(user);
        UserDTO updatedUserDto=entityToDTO(user);
        return updatedUserDto;

    }

    @Override
    public void deleteUser(String userId) {

        User user=userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User Not Found!") );

        //Delete user profile pic

        String userImageName=user.getImageName();
        String imageFullPathName=imagePath+ File.separator+userImageName;
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
        userRepository.delete(user);

    }

 //   @Override
//    public List<UserDTO> getAllUsers(int pageNumber,int pageSize,String sortBy,String sortDir) {    //Applying pagination
//
//
//        Sort sort= (sortDir.equalsIgnoreCase("asc")) ? (Sort.by(sortBy).ascending()) :(Sort.by(sortBy).descending());
//
//        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);    //Applying paginatiaon and sorting.
//
//        //List<User> users=userRepository.findAll();
//
//        Page<User> page =userRepository.findAll(pageable);     //It returns page type value
//        List<User> users=page.getContent();
//        List<UserDTO> userDtos=users.stream().map(user-> entityToDTO(user)).collect(Collectors.toList());
//        return userDtos;
//    }

    @Override
    public PageableResponse<UserDTO> getAllUsers(int pageNumber, int pageSize, String sortBy, String sortDir) {    //Applying pagination


        Sort sort= (sortDir.equalsIgnoreCase("asc")) ? (Sort.by(sortBy).ascending()) :(Sort.by(sortBy).descending());

        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);    //Applying paginatiaon and sorting.

        //List<User> users=userRepository.findAll();

        Page<User> page =userRepository.findAll(pageable);     //It returns page type value

//        List<User> users=page.getContent();     //This stuff is transferred to Helper class
//        List<UserDTO> userDtos=users.stream().map(user-> entityToDTO(user)).collect(Collectors.toList());
//
//        PageableResponse<UserDTO> response=new PageableResponse<>();
//        response.setContent(userDtos);
//        response.setPageNumber(page.getNumber());
//        response.setPageSize(page.getSize());
//        response.setTotalElements(page.getTotalElements());
//        response.setTotalPages(page.getTotalPages());
//        response.setLastPage(page.isLast());

        PageableResponse<UserDTO> response= Helper.getPageableResponse(page,UserDTO.class);
        return response;
    }

    @Override
    public UserDTO getUserById(String userId) {
        User user=userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User Not Found!") );
        UserDTO userDto=entityToDTO(user);
        return userDto;
    }

    @Override
    public UserDTO getUserByEmail(String email) {
        User user=userRepository.findByEmail(email).orElseThrow(()-> new ResourceNotFoundException("User not found!"));
        return entityToDTO(user);

    }

    @Override
    public List<UserDTO> searchUser(String keyword) {
        List<User> users=userRepository.findByNameContaining(keyword);
        return users.stream().map(user-> entityToDTO(user)).collect(Collectors.toList());
    }

    private UserDTO entityToDTO(User savedUser) {

//        UserDTO userDto=UserDTO.builder()
//                .userId(savedUser.getUserId())
//                .name(savedUser.getName())
//                .email(savedUser.getEmail())
//                .password(savedUser.getPassword())
//                .gender(savedUser.getGender())
//                .about(savedUser.getAbout())
//                .imageName(savedUser.getImageName()).build();

 //       return userDto;
        return mapper.map(savedUser,UserDTO.class);  //Here we have mapped UserDTO class with User class using ModelMapper
        //savedUser is a object that need to be mapped and UserDTO.class is the class to which savedUser need to be mapped to

    }

    private User dtoToEntity(UserDTO userDTO) {

//        User user=User.builder()                //User.builder() creates a object has gives us new way to handle it .
//                .userId(userDTO.getUserId())
//                .name(userDTO.getName())
//                .gender(userDTO.getGender())
//                .email(userDTO.getEmail())
//                .password(userDTO.getPassword())
//                .about(userDTO.getAbout())
//                .imageName(userDTO.getImageName()).build();

 //       return user;
        return mapper.map(userDTO,User.class);


    }

}
