package com.lcwd.Electronic.Store.Eletronic.Store.DTOS;

import com.lcwd.Electronic.Store.Eletronic.Store.Entities.Cart;
import com.lcwd.Electronic.Store.Eletronic.Store.Entities.Product;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class CartItemDTO {

    private int cartItemId;

    private ProductDto product;

    private int quantity;

    private int totalPrice;

}
