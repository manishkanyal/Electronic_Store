package com.lcwd.Electronic.Store.Eletronic.Store.DTOS;

import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder

public class OrderDTO {

    private String orderId;

    private String orderStatus="Pending";

    private String paymentStatus="NotPaid";

    private int orderAmount;

    private String billingAddress;

    private String billingPhoneNumber;

    private String billingName;

    private Date orderedDate=new Date();

    private Date deliveredDate;

    //private UserDTO user;  //We don't want to send User data when sending orderItem info

    private List<OrderItemDTO> orderItems  = new ArrayList<>();

}
