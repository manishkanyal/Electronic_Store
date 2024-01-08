package com.lcwd.Electronic.Store.Eletronic.Store.DTOS;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString


public class ProductDto {


    private String productId;

    private String title;

    private String description;

    private double price;

    private double discounted_price;

    private int available_quantity;

    private Date addedDate;

    private boolean live;

    private boolean stock;

    private String productImageName;

    private CategoryDTO category;


}
