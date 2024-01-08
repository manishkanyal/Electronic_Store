package com.lcwd.Electronic.Store.Eletronic.Store.Services;

import com.lcwd.Electronic.Store.Eletronic.Store.DTOS.PageableResponse;
import com.lcwd.Electronic.Store.Eletronic.Store.DTOS.UserDTO;
import com.lcwd.Electronic.Store.Eletronic.Store.Entities.User;

import java.util.List;

public interface UserService {


    //So we are using DTO to communicate between COntroller layer and bussiness logic layer.
    //Create User
    UserDTO createUser(UserDTO user );

    //Update User
    UserDTO updateUser(UserDTO user,String userId);

    void deleteUser(String userId);

    PageableResponse<UserDTO> getAllUsers(int pageNumber, int pageSize, String sortBy, String sortDir);

    UserDTO getUserById(String userId);

    UserDTO getUserByEmail(String email);

    //search all user that has keyword ex- ajay ,ali . Search for all have a in name
    List<UserDTO> searchUser(String keyword);



}
