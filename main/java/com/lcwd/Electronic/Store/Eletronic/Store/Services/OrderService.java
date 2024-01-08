package com.lcwd.Electronic.Store.Eletronic.Store.Services;

import com.lcwd.Electronic.Store.Eletronic.Store.DTOS.CreateOrderRequest;
import com.lcwd.Electronic.Store.Eletronic.Store.DTOS.OrderDTO;
import com.lcwd.Electronic.Store.Eletronic.Store.DTOS.PageableResponse;
import com.lcwd.Electronic.Store.Eletronic.Store.Entities.Order;
import org.aspectj.weaver.ast.Or;

import java.util.List;

public interface OrderService {

    //create order
    OrderDTO createOrder(CreateOrderRequest order);


    //remove order
    void removeOrder(String orderId);

    //get order of users
    List<OrderDTO> getOrdersOfUsers(String userId);

    //get order if you are admin
    PageableResponse<OrderDTO> getAllOrders(int pageNumber,int pageSize,String sortDir,String sortBy);

    OrderDTO updateOrder(String orderId,CreateOrderRequest request);

}
