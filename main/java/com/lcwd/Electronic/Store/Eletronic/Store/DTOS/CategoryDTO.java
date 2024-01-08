package com.lcwd.Electronic.Store.Eletronic.Store.DTOS;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder


public class CategoryDTO {

    private String id;

    @NotBlank(message = "Title is required!!")
    @Size(min=4,message = "Title must be of atleast 4 character")
    private String title;

    @NotBlank(message = "Description Required!!")
    private String description;

    private String coverImage;

}
