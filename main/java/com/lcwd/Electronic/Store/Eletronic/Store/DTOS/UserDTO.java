package com.lcwd.Electronic.Store.Eletronic.Store.DTOS;

import com.lcwd.Electronic.Store.Eletronic.Store.Entities.Roles;
import com.lcwd.Electronic.Store.Eletronic.Store.validate.ImageNameValid;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private String userId;

    @Size(min=3,max=30,message = "Invalid User Name")    //This annonotation belong to spring boot validater
    //the @Schema annotation is used in to provide additional information about the schema of a model or parameter in your API.
    @Schema(name="username",accessMode = Schema.AccessMode.READ_ONLY,description = "UserName of New User")
    private String name;

    //@Email(message = "Invalid Email Passed!")//This annonotation belong to spring boot validater. This is commented because we are using PAttern annonotation
    @NotBlank
    @Pattern(regexp ="^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$",message = "Invalid Email Passed!" )
    private String email;

    @NotBlank
    private String password;

    @Size(min=3,max=6,message = "Invalid Gender!")     //This annonotation belong to spring boot validater
    private String gender;

    @NotBlank(message = "Write Something about yourself ") //This annonotation belong to spring boot validater
    private String about;

    //some imp annonotaions
    //@Pattern annonotaion is used to map with custom regular expression
    //We can also make Custom Validator.

    @ImageNameValid          //This is a custom validator
    private String imageName;

    private Set<RolesDTO> roles=new HashSet<>();


}
