package com.hoppy.hobbyProject.controller;

import com.hoppy.hobbyProject.Repo.RoleRepository;
import com.hoppy.hobbyProject.Repo.UserRepository;
import com.hoppy.hobbyProject.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping(path = "/user")
public class UserController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    BCryptPasswordEncoder passwordEncoder;


    @GetMapping(path = "/create")
    public String create(Model model){
        model.addAttribute("user", new User());
        model.addAttribute("roleList", roleRepository.findAll());

        return("user/create");
    }

    @PostMapping(path = "/save")
    public String save(@Valid @ModelAttribute("user") User user, BindingResult result){
        if(result.hasErrors()){
            return("user/create");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        return "success";
    }
}
