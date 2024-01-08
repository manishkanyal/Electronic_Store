package com.lcwd.Electronic.Store.Eletronic.Store.DTOS;

import com.lcwd.Electronic.Store.Eletronic.Store.Entities.Order;
import com.lcwd.Electronic.Store.Eletronic.Store.Entities.OrderItem;
import com.lcwd.Electronic.Store.Eletronic.Store.Entities.Product;
import com.lcwd.Electronic.Store.Eletronic.Store.Entities.User;
import jakarta.persistence.*;
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

public class OrderItemDTO {

    private int orderItemId;
    private int quantity;
    private int totalPrice;
    private ProductDto product;

}
