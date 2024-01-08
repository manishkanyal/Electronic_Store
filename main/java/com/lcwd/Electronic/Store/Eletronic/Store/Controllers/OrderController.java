package com.lcwd.Electronic.Store.Eletronic.Store.Controllers;

import com.lcwd.Electronic.Store.Eletronic.Store.DTOS.ApiResponseMessage;
import com.lcwd.Electronic.Store.Eletronic.Store.DTOS.CreateOrderRequest;
import com.lcwd.Electronic.Store.Eletronic.Store.DTOS.OrderDTO;
import com.lcwd.Electronic.Store.Eletronic.Store.DTOS.PageableResponse;
import com.lcwd.Electronic.Store.Eletronic.Store.Services.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(@Valid @RequestBody CreateOrderRequest request)
    {
        OrderDTO orderDTO= orderService.createOrder(request);
        return new ResponseEntity<>(orderDTO, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{orderId}")
    public ResponseEntity<ApiResponseMessage> remove(@PathVariable String orderId)
    {
        orderService.removeOrder(orderId);
        ApiResponseMessage response= ApiResponseMessage.builder().message("Order with given Id is Deleted!")
                .status(HttpStatus.OK)
                .success(true)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderDTO>> getOrderOfUsers(String userId)
    {
        List<OrderDTO> orderDTOS=orderService.getOrdersOfUsers(userId);
        return ResponseEntity.ok(orderDTOS);
    }

    @GetMapping
    public ResponseEntity<PageableResponse<OrderDTO>> getAllOrders(@RequestParam(defaultValue = "0",required = false) int pageNumber,
                                                                   @RequestParam(defaultValue = "10",required = false) int pageSize,
                                                                   @RequestParam(defaultValue = "billingName",required = false) String sortBy,
                                                                   @RequestParam(defaultValue = "asc",required = false) String sortDir)
    {
        PageableResponse<OrderDTO> response= orderService.getAllOrders(pageNumber,pageSize,sortDir,sortBy);
        return ResponseEntity.ok(response);
    }


    @PutMapping("/update/{orderId}")
    public ResponseEntity<OrderDTO> updateOrder(@PathVariable("orderId") String orderId,
                                                @RequestBody CreateOrderRequest request)
    {
        OrderDTO orderDTO=orderService.updateOrder(orderId,request);
        return ResponseEntity.ok(orderDTO);
    }


}
