package com.lcwd.Electronic.Store.Eletronic.Store.Entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder


@Entity
@Table(name="categories")
public class Category {

    @Id
    private String id;

    @Column(name="category_title",length=60,nullable = false)
    private String title;
    @Column(name="category_description",length = 50)
    private String description;
    private String coverImage;

    //FetchType.LAZY =  When we fetch categories ,so we don't need to fetch all product it is associated with
    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "category")
    private List<Product> products=new ArrayList<>();

}
