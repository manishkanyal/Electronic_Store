package com.lcwd.Electronic.Store.Eletronic.Store.DTOS;

import com.lcwd.Electronic.Store.Eletronic.Store.Entities.CartItem;
import com.lcwd.Electronic.Store.Eletronic.Store.Entities.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString

public class CartDTO {

    private String cartId;

    private Date createdAt;

    private UserDTO user;

    private List<CartItemDTO> items=new ArrayList<>();

}
