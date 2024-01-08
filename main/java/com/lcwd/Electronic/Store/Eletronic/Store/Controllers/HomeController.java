package com.lcwd.Electronic.Store.Eletronic.Store.Controllers;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class HomeController {

    @GetMapping
    public String testing()
    {
        System.out.println("Testing !");
        return "Welcome to testing Site";
    }
}
