package com.hoppy.hobbyProject.controller;


import com.hoppy.hobbyProject.Repo.HobbyRepository;
import com.hoppy.hobbyProject.domain.Hobby;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

@Controller
@RequestMapping(path = "/hobby")
public class HobbyController {

    @Autowired
    HobbyRepository hobbyRepository;
    Logger log = LoggerFactory.getLogger(HobbyController.class);



    @GetMapping(path = "/list")
    public String listHobby(Model model){
        model.addAttribute("hobbies", hobbyRepository.findAll());
        return "hobby/list"; //viewresolver wyszukuje pliku widoku o podanej ścieżce
    }                         //defaultowo viewresolver szuka widoków w resources/templates/....

    @PostMapping(path = "/add")
    public String addHobby(@Valid @ModelAttribute("hobby") Hobby hobby,BindingResult results){

        if(results.hasErrors()){
            System.out.println("an Ilusion? What are you hiding?");

            return ("hobby/hobbyForm");
        }
            hobbyRepository.saveAndFlush(hobby);

        return ("redirect:list");
    }

    @GetMapping(path = "/hobbyForm")
    public String listForm(Model model){
        model.addAttribute("hobby", new Hobby());
        return "hobby/hobbyForm";
    }

    @GetMapping(path = "/editHobby")
    /*
        tutaj przygotowujemy tylko obiekt do którego będzie pakowany form i wyświetlamy ten form.
        hobbyId przesyłamy by wiedzieć któy objekt edytujemy
     */
    public String editHobby(Model model, @RequestParam Long hobbyId){
        model.addAttribute("hobby", hobbyRepository.getOne(hobbyId)); //dodajemy sobie do modelu na front nowy objekcik hobby
        model.addAttribute("hobbyId", hobbyId); //w parametrach wysyłamy hobbyId żeby wiedzieć który obiekt edytujemy
        //to wyżeh można by wywalić i w hidden field wykorzystać hobby.getId()
        //i dodajemy go do modelu ---- można też zrobić przez @PathVariable
        return "hobby/editHobby";
    }

    @PostMapping(path = "/save") //to nasz edit
    /*
        @Valid @ModelAttribute - validuje ci twoje entity Hobby na podstawie adnotacji przy fieldach @Size
        @NotNull itd itd, @ModelAttribute pomaga zmapować forma z frontu do obiektu hobby
        name= "hobby" musi się zgadzać z th:object="hobby" na froncie
        hobby to nasz obiekt z frontu hobbyInDb z bazy
        przypisujemy dane z hobby do hobbyInDb i zapisuje w bazie
        saveAndFlush - zapisz i odświez stosujemy by zmiany były automatycznie zapisane i widoczne
        bo niekiedy może to chwilę potrwać. gdy musimy mieć zmiany natychmiast a najczęściej tak jest to robimy
        saveAndFlush a nie samo save (JpaRepository w HobbyRepository ma automatycznie tę metodę
     */
    public String editHobby(@Valid @ModelAttribute(name = "hobby") Hobby hobby,
                            @RequestParam Long hobbyId ){
        Hobby hobbyInDb = hobbyRepository.getOne(hobbyId);
        //hobbyInDb.setFile(hobby.getFile());
        hobbyInDb.setCurrentImageID(hobby.getCurrentImageID());
        hobbyInDb.setDescription(hobby.getDescription());
        hobbyInDb.setName(hobby.getName());
        hobbyRepository.saveAndFlush(hobbyInDb);
        return "success";
    }
}
