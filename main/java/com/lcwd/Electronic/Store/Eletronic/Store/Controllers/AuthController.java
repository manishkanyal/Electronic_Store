package com.lcwd.Electronic.Store.Eletronic.Store.Controllers;

import com.lcwd.Electronic.Store.Eletronic.Store.DTOS.JwtRequest;
import com.lcwd.Electronic.Store.Eletronic.Store.DTOS.JwtResponse;
import com.lcwd.Electronic.Store.Eletronic.Store.DTOS.UserDTO;
import com.lcwd.Electronic.Store.Eletronic.Store.Entities.User;
import com.lcwd.Electronic.Store.Eletronic.Store.Exceptions.BadApiRequest;
import com.lcwd.Electronic.Store.Eletronic.Store.Security.JwtHelper;
import com.lcwd.Electronic.Store.Eletronic.Store.Services.UserService;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;

//This is for only testing purpose
@RestController
@RequestMapping("/auth")
@Tag(name="Auth Controller",description = "Apis For Authentication")
public class AuthController {

    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private AuthenticationManager manager;
    @Autowired
    private UserService userService;

    @Autowired
    private JwtHelper helper;

    private Logger logger = LoggerFactory.getLogger(AuthController.class);


    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request) {
        this.doAuthenticate(request.getEmail(), request.getPassword());

        //getting user details after authenticating user
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        //Generating token
        String token = this.helper.generateToken(userDetails);

        UserDTO userDto = modelMapper.map(userDetails, UserDTO.class);
        JwtResponse response = JwtResponse.builder()
                .jwtToken(token)
                .user(userDto).build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private void doAuthenticate(String email, String password) {

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email, password);
        try {
            //AuthenticationManager will be authenticating the user here
            manager.authenticate(authentication);
        } catch (BadCredentialsException e) {
            throw new BadApiRequest(" Invalid Username or Password  !!");
        }

    }


    @GetMapping("/current")
    public ResponseEntity<UserDTO> getCurrentUser(Principal principal) {
        String name = principal.getName();
        return new ResponseEntity<>(modelMapper.map(userDetailsService.loadUserByUsername(name), UserDTO.class), HttpStatus.OK);
    }



}
