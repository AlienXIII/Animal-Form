package com.hoppy.hobbyProject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
@RequestMapping(path = "/")
public class IndexController {

    @GetMapping(path = "/")
    public String index(Principal principal, Model model){
        if(principal != null){
            model.addAttribute("username", principal.getName());
        }else{
            model.addAttribute("anonymous", "Hello whoever u are!");
        }
        return "index";
    }
}
