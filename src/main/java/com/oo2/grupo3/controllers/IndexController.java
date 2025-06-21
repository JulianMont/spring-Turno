package com.oo2.grupo3.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.ui.Model;


@Controller
@RequestMapping("/home")
public class IndexController {

    
    @GetMapping("/")
    public String home() {
        return "redirect:/home/index";
    }

    @GetMapping("/index")
    public String index(Model model) {
        return "home/index";
    }

    @GetMapping("/auth/login")
    public String login() {
        return "auth/login";
    }
}


