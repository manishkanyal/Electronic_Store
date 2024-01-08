package com.lcwd.Electronic.Store.Eletronic.Store.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString

@Entity
@Table(name="cart")
public class Cart {

    @Id
    private String cartId;

    private Date createdAt;

    //@JsonBackReference(value="user_id")
    @OneToOne
    @JoinColumn(name="user_id")
    private User user;


    //Here orphanRemoval is set to true because when we delete the data that is in a relation should also be reflected in database
    @OneToMany(mappedBy = "cart",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<CartItem> items=new ArrayList<>();

}
