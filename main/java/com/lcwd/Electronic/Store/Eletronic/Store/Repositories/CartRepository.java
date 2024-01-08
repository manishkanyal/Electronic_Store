package com.lcwd.Electronic.Store.Eletronic.Store.Repositories;

import com.lcwd.Electronic.Store.Eletronic.Store.Entities.Cart;
import com.lcwd.Electronic.Store.Eletronic.Store.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart,String> {

    Optional<Cart> findByUser(User user);

}
