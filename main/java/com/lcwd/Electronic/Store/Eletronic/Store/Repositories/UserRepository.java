package com.lcwd.Electronic.Store.Eletronic.Store.Repositories;

import com.lcwd.Electronic.Store.Eletronic.Store.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,String> {

    Optional<User> findByEmail(String email);
    Optional<User> findByEmailAndPassword(String email,String password);
    List<User> findByNameContaining(String keywords);

}
