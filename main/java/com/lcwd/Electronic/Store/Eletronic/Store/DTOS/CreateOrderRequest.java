package com.lcwd.Electronic.Store.Eletronic.Store.DTOS;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class CreateOrderRequest {

    @NotBlank(message = "Cart Id is required")
    private String cartId;

    @NotBlank(message = "User Id is required")
    private String  userId;

    private String orderStatus="Pending";

    private String paymentStatus="NotPaid";

    @NotBlank(message = "Address is required")
    private String billingAddress;

    @NotBlank(message = "Phone number is required")
    private String billingPhoneNumber;

    @NotBlank(message = "Billing name is required")
    private String billingName;


}
