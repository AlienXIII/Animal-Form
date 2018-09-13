package com.hoppy.hobbyProject.controller;


import com.hoppy.hobbyProject.Repo.HobbyRepository;
import com.hoppy.hobbyProject.domain.Category;
import com.hoppy.hobbyProject.domain.Hobby;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping(path = "/")
@Slf4j
public class CategoriesController {


   @Autowired
   HobbyRepository hobbyRepository;

   @GetMapping("/{category}")
    public String listHobbiesByCategory(@PathVariable("category") String category, Model model){
       List<Hobby> hobbies = hobbyRepository.findAllByCategory(Category.valueOf(category));
        //odwolanie do query
       model.addAttribute("hobbies", hobbies);
       return "hobby/hobbyListByCategory";
    }

}
