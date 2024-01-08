package com.lcwd.Electronic.Store.Eletronic.Store.Services.Implementation;

import com.lcwd.Electronic.Store.Eletronic.Store.DTOS.CreateOrderRequest;
import com.lcwd.Electronic.Store.Eletronic.Store.DTOS.OrderDTO;
import com.lcwd.Electronic.Store.Eletronic.Store.DTOS.OrderItemDTO;
import com.lcwd.Electronic.Store.Eletronic.Store.DTOS.PageableResponse;
import com.lcwd.Electronic.Store.Eletronic.Store.Entities.*;
import com.lcwd.Electronic.Store.Eletronic.Store.Exceptions.BadApiRequest;
import com.lcwd.Electronic.Store.Eletronic.Store.Exceptions.ResourceNotFoundException;
import com.lcwd.Electronic.Store.Eletronic.Store.Helpers.Helper;
import com.lcwd.Electronic.Store.Eletronic.Store.Repositories.CartRepository;
import com.lcwd.Electronic.Store.Eletronic.Store.Repositories.OrderRepository;
import com.lcwd.Electronic.Store.Eletronic.Store.Repositories.UserRepository;
import com.lcwd.Electronic.Store.Eletronic.Store.Services.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class OrderServiceImplementation implements OrderService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CartRepository cartRepository;

    @Override
    public OrderDTO createOrder(CreateOrderRequest order) {

        String userId = order.getUserId();
        String cartId=order.getCartId();

        User user=userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User not found with given Id!!"));
        Cart cart= cartRepository.findById(cartId).orElseThrow(()->new ResourceNotFoundException("Cart not found with given Id!!"));

        List<CartItem> cartItems=cart.getItems();

        if(cartItems.size()<=0)
        {
            throw new BadApiRequest("Invalid Request! Order cannot be place with zero Items in cart!!");
        }

        //Creating order
        Order createdOrder= Order.builder()
                .billingName(order.getBillingName())
                .billingPhoneNumber(order.getBillingPhoneNumber())
                .billingAddress(order.getBillingAddress())
                .orderedDate(new Date())
                .deliveredDate(null)
                .paymentStatus(order.getPaymentStatus())
                .orderStatus(order.getOrderStatus())
                .orderId(UUID.randomUUID().toString())
                .user(user)
                .build();

        AtomicInteger orderAmount=new AtomicInteger();

        //Preparing orderItems for order
        List<OrderItem> orderItems=cartItems.stream().map(cartItem->{
           OrderItem orderItem= OrderItem.builder()
                   .quantity(cartItem.getQuantity())
                   .product(cartItem.getProduct())
                   .totalPrice((int) (cartItem.getQuantity()*cartItem.getProduct().getDiscounted_price()))
                   .order(createdOrder)
                   .build();
           orderAmount.set(orderAmount.get()+orderItem.getTotalPrice());
           return orderItem;
        }).collect(Collectors.toList());

        createdOrder.setOrderItems(orderItems);
        createdOrder.setOrderAmount(orderAmount.get());

        cart.getItems().clear();
        cartRepository.save(cart);

        Order savedOrder=orderRepository.save(createdOrder);

        return modelMapper.map(savedOrder,OrderDTO.class);
    }

    @Override
    public void removeOrder(String orderId) {

        Order order=orderRepository.findById(orderId).orElseThrow(()->new ResourceNotFoundException("Order not found with given Id!!"));
        orderRepository.delete(order);
    }

    @Override
    public List<OrderDTO> getOrdersOfUsers(String userId) {

        User user=userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User not found with given Id!!"));
        List<Order> orders=orderRepository.findByUser(user);
        List<OrderDTO> ordersDTO=orders.stream().map(order->modelMapper.map(order,OrderDTO.class)).collect(Collectors.toList());
        return ordersDTO;
    }

    @Override
    public PageableResponse<OrderDTO> getAllOrders(int pageNumber, int pageSize, String sortDir, String sortBy) {

        Sort sort= (sortDir.equalsIgnoreCase("asc")) ? (Sort.by(sortBy).ascending()) :(Sort.by(sortBy).descending());
        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);    //Applying paginatiaon and sorting.

        Page<Order> page=orderRepository.findAll(pageable);
        PageableResponse<OrderDTO> response= Helper.getPageableResponse(page,OrderDTO.class);
        return response;
    }

    @Override
    public OrderDTO updateOrder(String orderId,CreateOrderRequest request)
    {
        String orderStatus= request.getOrderStatus();
        String paymentStatus= request.getPaymentStatus();
        Order order=orderRepository.findById(orderId).orElseThrow(()->new ResourceNotFoundException("Order Not Found!!"));

        order.setOrderStatus(orderStatus);
        order.setPaymentStatus(paymentStatus);
        order.setDeliveredDate(new Date());

        Order saved=orderRepository.save(order);
        return modelMapper.map(saved,OrderDTO.class);
    }
}
