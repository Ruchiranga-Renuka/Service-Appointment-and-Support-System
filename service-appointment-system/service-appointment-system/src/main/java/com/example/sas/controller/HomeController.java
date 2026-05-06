package com.example.sas.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @GetMapping("/error/403")
    public String error403() { return "error/403"; }

    @GetMapping("/error/404")
    public String error404() { return "error/404"; }
}
