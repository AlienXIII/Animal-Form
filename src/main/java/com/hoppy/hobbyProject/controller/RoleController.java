package com.hoppy.hobbyProject.controller;

import com.hoppy.hobbyProject.Repo.RoleRepository;
import com.hoppy.hobbyProject.domain.Role;
import com.hoppy.hobbyProject.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping(path = "/role")
public class RoleController {

    @Autowired
    RoleRepository roleRepository;

    @GetMapping(path = "/create")
    public String create(Model model){
        model.addAttribute("role", new Role());
        return("role/create");
    }

    @PostMapping(path = "/save")
    public String save(@Valid @ModelAttribute("role") Role role, BindingResult result){
        if(result.hasErrors()){
            return("role/create");
        }
        roleRepository.save(role);

        return "success";
    }
}
