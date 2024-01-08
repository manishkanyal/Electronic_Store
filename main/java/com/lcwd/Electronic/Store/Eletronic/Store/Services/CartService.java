package com.lcwd.Electronic.Store.Eletronic.Store.Services;

import com.lcwd.Electronic.Store.Eletronic.Store.DTOS.AddItemsToCartRequest;
import com.lcwd.Electronic.Store.Eletronic.Store.DTOS.CartDTO;

public interface CartService {

    //Item add in cart
    // Case 1- cart for user is not available : we will create the cart and then add the item to cart
    //Case 2- cart is available for user and then add item to cart.

    CartDTO addItemToCart(String userId, AddItemsToCartRequest cartRequest);

    //Remove item from cart
    void removeItemFromCart(String userId,int cartItem);

    //To completely delete all items from cart
    void clearCart(String userId);

    CartDTO getCartByUser(String userId);


}
