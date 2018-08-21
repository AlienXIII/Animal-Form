package com.hoppy.hobbyProject.controller;


import com.hoppy.hobbyProject.Repo.HobbyRepository;
import com.hoppy.hobbyProject.domain.Hobby;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping(path = "/hobby") //spring musi wiedzieć do jakiego kontrolera i metody ma się odwołać
public class HobbyController {

    @Autowired
    HobbyRepository hobbyRepository;

    @GetMapping(path = "/list")
    //ten kontroler -> localhost:8080/hobby/list - hobby to kontroler a list to metoda w tym kontrolerze
    public String listAnimals(Model model){
        model.addAttribute("hobbies", hobbyRepository.findAll());
        return "hobby/list"; //viewresolver wyszukuje pliku widoku o podanej ścieżce
    }                         //defaultowo viewresolver szuka widoków w resources/templates/....

    @PostMapping(path = "/add")
    public String addAnimal(@Valid @ModelAttribute("hobby") Hobby hobby, BindingResult results){

        if(results.hasErrors()){
            System.out.println("an Ilusion? What are you hiding?");
            return ("redirect:/hobby/hobbyForm");
        }
        hobbyRepository.save(hobby);
        return ("redirect:/hobby/list");
    }

    @GetMapping(path = "/hobbyForm")
    public String listForm(Model model){
        model.addAttribute("hobby", new Hobby());
        return "hobby/hobbyForm";
    }
}
