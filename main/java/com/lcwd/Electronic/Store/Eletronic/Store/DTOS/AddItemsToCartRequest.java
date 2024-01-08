package com.lcwd.Electronic.Store.Eletronic.Store.DTOS;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class AddItemsToCartRequest {

    private  String productId;
    private int quantity;

}
