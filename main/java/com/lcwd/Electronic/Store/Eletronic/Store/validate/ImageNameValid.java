package com.lcwd.Electronic.Store.Eletronic.Store.validate;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;


//Here we are creating a Custom annonotation for validating the bean
@Target({ElementType.FIELD,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = ImageNameValidator.class)
//@interface is used because use it as annonotation in our project
public @interface ImageNameValid {


    //Method declaration is copied from Other Bean validation annonotation  class . Here it is copied from @NotBlank class
    //Default error message
    String message() default "Invalid Image Name!";

    //Represents group of Constraints
    Class<?>[] groups() default {};

    //Additional info about annonotation.
    Class<? extends Payload>[] payload() default {};

}

