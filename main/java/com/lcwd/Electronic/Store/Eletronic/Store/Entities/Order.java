package com.lcwd.Electronic.Store.Eletronic.Store.Entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "orders")
public class Order {

    @Id
    private String orderId;

    private String orderStatus;

    private String paymentStatus;

    private int orderAmount;

    @Column(length = 1000)
    private String billingAddress;

    private String billingPhoneNumber;

    private String billingName;

    private Date orderedDate;

    private Date deliveredDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="user_id")
    private User user;

    @OneToMany(mappedBy ="order" ,fetch=FetchType.EAGER,cascade = CascadeType.ALL)
    private List<OrderItem> orderItems  = new ArrayList<>();


}
