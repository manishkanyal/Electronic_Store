package com.lcwd.Electronic.Store.Eletronic.Store.Entities;

import com.lcwd.Electronic.Store.Eletronic.Store.DTOS.CategoryDTO;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString

@Entity
@Table(name="products")
public class Product {

    @Id
    private String productId;

    private String title;

    @Column(length = 1000)
    private String description;

    private double price;

    private double discounted_price;

    private int available_quantity;

    private Date addedDate;

    private boolean live;

    private boolean stock;

    private String productImageName;

    //FetchType.EAGER=  When we fetch product ,so we want to fetch  categories it is associated with

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="id_category")
    private Category category;


}
