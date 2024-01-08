package com.lcwd.Electronic.Store.Eletronic.Store.Repositories;

import com.lcwd.Electronic.Store.Eletronic.Store.Entities.Order;
import com.lcwd.Electronic.Store.Eletronic.Store.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,String> {

    List<Order> findByUser(User user);


}
