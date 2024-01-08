package com.lcwd.Electronic.Store.Eletronic.Store.Controllers;

import com.lcwd.Electronic.Store.Eletronic.Store.DTOS.AddItemsToCartRequest;
import com.lcwd.Electronic.Store.Eletronic.Store.DTOS.ApiResponseMessage;
import com.lcwd.Electronic.Store.Eletronic.Store.DTOS.CartDTO;
import com.lcwd.Electronic.Store.Eletronic.Store.Services.CartService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carts")
@Tag(name="Cart Controller",description = "This is a API for cart Operation ")
public class CartController {

    @Autowired
    private CartService cartService ;


    @PostMapping("/{userId}")
    public ResponseEntity<CartDTO> addItemToCart(@PathVariable String userId, @RequestBody AddItemsToCartRequest request)
    {
        CartDTO cartDto =cartService.addItemToCart(userId,request);
        System.out.println("In Cart Controller : "+cartDto);
        return new ResponseEntity<>(cartDto, HttpStatus.OK);
    }

    @DeleteMapping("/{userId}/items/{itemId}")
    public ResponseEntity<ApiResponseMessage> removeItemFromCart(@PathVariable String userId,@PathVariable int itemId)
    {
        cartService.removeItemFromCart(userId,itemId);

       ApiResponseMessage response= ApiResponseMessage.builder().message("Item is Removed Successfully!").success(true).status(HttpStatus.OK).build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponseMessage> clearCartWithUserId(@PathVariable String userId)
    {
        cartService.clearCart(userId);

        ApiResponseMessage response= ApiResponseMessage.builder().message("Cart is Cleared Successfully!").success(true).status(HttpStatus.OK).build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<CartDTO> getCart(@PathVariable String userId)
    {
        CartDTO cartDTO= cartService.getCartByUser(userId);
        return ResponseEntity.ok(cartDTO);
    }

}
