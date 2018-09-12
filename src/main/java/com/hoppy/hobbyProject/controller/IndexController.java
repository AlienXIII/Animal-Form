package com.hoppy.hobbyProject.controller;

import com.hoppy.hobbyProject.Repo.HobbyRepository;
import com.hoppy.hobbyProject.domain.Category;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@Slf4j
@RequestMapping(path = "/")
public class IndexController {



    @Autowired
    HobbyRepository hobbyRepository;



    @GetMapping(path = "/")
    public String index(Principal principal, Model model){
        if(principal != null){
            model.addAttribute("username", principal.getName());
        }else{
            model.addAttribute("anonymous", "Hello whoever u are!");
        }

        model.addAttribute("hobbies",hobbyRepository.findAll());




        return "index";
    }

}


