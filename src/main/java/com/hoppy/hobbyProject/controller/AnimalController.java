package com.hoppy.hobbyProject.controller;


import com.hoppy.hobbyProject.AnimalRepository;
import com.hoppy.hobbyProject.domain.Animal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
@RequestMapping(path = "/animal")
public class AnimalController {

    @Autowired
    AnimalRepository animalRepository;

    @GetMapping(path = "/list")
    public String listAnimals(Model model){
        model.addAttribute("animals", animalRepository.findAll());
        return "animal/list";
    }

    @PostMapping(path = "/add")
    public String addAnimal(@Valid @ModelAttribute("animal")Animal animal, BindingResult results){

        if(results.hasErrors()){
            System.out.println("an Ilusion? What are you hiding?");
            return ("redirect:/animal/animalForm");
        }
        animalRepository.save(animal);
        return ("redirect:/animal/list");
    }

    @GetMapping(path = "/animalForm")
    public String listForm(Model model){
        model.addAttribute("animal", new Animal());
        return "animal/animalForm";
    }
}
