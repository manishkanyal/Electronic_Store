package com.lcwd.Electronic.Store.Eletronic.Store.Services.Implementation;

import com.lcwd.Electronic.Store.Eletronic.Store.DTOS.AddItemsToCartRequest;
import com.lcwd.Electronic.Store.Eletronic.Store.DTOS.CartDTO;
import com.lcwd.Electronic.Store.Eletronic.Store.Entities.Cart;
import com.lcwd.Electronic.Store.Eletronic.Store.Entities.CartItem;
import com.lcwd.Electronic.Store.Eletronic.Store.Entities.Product;
import com.lcwd.Electronic.Store.Eletronic.Store.Entities.User;
import com.lcwd.Electronic.Store.Eletronic.Store.Exceptions.BadApiRequest;
import com.lcwd.Electronic.Store.Eletronic.Store.Exceptions.ResourceNotFoundException;
import com.lcwd.Electronic.Store.Eletronic.Store.Repositories.CartItemRepository;
import com.lcwd.Electronic.Store.Eletronic.Store.Repositories.CartRepository;
import com.lcwd.Electronic.Store.Eletronic.Store.Repositories.ProductRepository;
import com.lcwd.Electronic.Store.Eletronic.Store.Repositories.UserRepository;
import com.lcwd.Electronic.Store.Eletronic.Store.Services.CartService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Service
public class CartServiceImplementation implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CartItemRepository cartItemRepository;


    @Override
    public CartDTO addItemToCart(String userId, AddItemsToCartRequest cartRequest) {

        int quantity=cartRequest.getQuantity();

        if(quantity<=0)
        {
            throw new BadApiRequest("Requested Quantity not Valid!");
        }

        String productId= cartRequest.getProductId();

        //Fetch the product
        Product product=productRepository.findById(productId).orElseThrow(()->new ResourceNotFoundException("Unable to find the Product with given Id"));

        //Fetch the user
        User user=userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("Unable to find the User with given Id"));

        Cart cart=null;

        try{
            cart=cartRepository.findByUser(user).get();
        }
        catch (NoSuchElementException e)
        {
            cart=new Cart();
            cart.setCartId(UUID.randomUUID().toString());
            cart.setCreatedAt(new Date());
        }

        //Cart Operation
        //Boolean updated= new Boolean(false);
        //We are using new Updated because inside lambda method we use final variables so using AtomicBoolean
        AtomicBoolean updated= new AtomicBoolean(false);

        //If cart already present , updating the cart
        List<CartItem> items=cart.getItems();
        items=items.stream().map(item->{
            if(item.getProduct().getProductId().equals(productId))
            {
                item.setQuantity(quantity);
                item.setTotalPrice((int) (quantity * product.getDiscounted_price()));
                updated.set(true);
            }
            return item;
        }).collect(Collectors.toList());

        cart.setItems(items);

        //created items
        if(!updated.get())
        {
            CartItem cartItem= CartItem.builder().quantity(quantity).totalPrice((int) (quantity * product.getDiscounted_price())).cart(cart).product(product).build();
            cart.getItems().add(cartItem);
        }

        cart.setUser(user);
        Cart updatedCart=cartRepository.save(cart);
        System.out.println("CArt Service updatedCart: "+updatedCart);

        return modelMapper.map(updatedCart,CartDTO.class);
    }

    @Override
    public void removeItemFromCart(String userId, int cartItemId) {

        CartItem cartItem=cartItemRepository.findById(cartItemId).orElseThrow(()->new ResourceNotFoundException("Unable to find the Product with given Id"));
        cartItemRepository.delete(cartItem);

    }

    @Override
    public void clearCart(String userId) {

        User user=userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("Unable to find the User with given Id"));
        Cart cart=cartRepository.findByUser(user).orElseThrow(()-> new ResourceNotFoundException("Cart for given User not Found!"));
        cart.getItems().clear();

        cartRepository.save(cart);

    }

    @Override
    public CartDTO getCartByUser(String userId) {

        User user=userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("Unable to find the User with given Id"));
        Cart cart=cartRepository.findByUser(user).orElseThrow(()-> new ResourceNotFoundException("Cart for given User not Found!"));

        return modelMapper.map(cart,CartDTO.class);

    }
}
