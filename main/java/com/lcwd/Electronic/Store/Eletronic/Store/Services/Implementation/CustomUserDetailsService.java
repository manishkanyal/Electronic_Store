package com.lcwd.Electronic.Store.Eletronic.Store.Services.Implementation;

import com.lcwd.Electronic.Store.Eletronic.Store.Entities.User;
import com.lcwd.Electronic.Store.Eletronic.Store.Exceptions.ResourceNotFoundException;
import com.lcwd.Electronic.Store.Eletronic.Store.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


//This is for authentication thorugh database
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

       User user= userRepository.findByEmail(username).orElseThrow(()->new UsernameNotFoundException("User with given Email Not found!!"));
       return user;
    }
}
