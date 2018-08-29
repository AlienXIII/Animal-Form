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
import java.io.InputStream;
import java.nio.file.*;

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
    public String editHobby(Model model, @RequestParam Long hobbyId){
        model.addAttribute("hobby", hobbyRepository.getOne(hobbyId));
        model.addAttribute("hobbyId", hobbyId);
        return "hobby/editHobby";
    }

    @PostMapping(path = "/save")

    public String editHobby(@Valid @ModelAttribute(name = "hobby") Hobby hobby,
                            @RequestParam Long hobbyId ){
        Hobby hobbyInDb = hobbyRepository.getOne(hobbyId);
        hobbyInDb.setCurrentImageID(hobby.getCurrentImageID());
        hobbyInDb.setDescription(hobby.getDescription());
        hobbyInDb.setName(hobby.getName());
        hobbyRepository.saveAndFlush(hobbyInDb);
        return "success";
    }

    @GetMapping(path = "/uploadFile")
    public String upload(@RequestParam Long hobbyId, Model model){
        model.addAttribute("hobbyId", hobbyId);

        return "hobby/uploadForm";
    }

    @PostMapping(path = "/uploadFile")
    //wysyłam formem plik i hobbyId jako hidden
    public String upload(@RequestParam Long hobbyId, @RequestParam MultipartFile file){
        Hobby hobby = hobbyRepository.getOne(hobbyId); //wyciągamy hobby z bazy jesli istnieje

        if(hobby != null && !file.isEmpty()){
            Path rootLocation = Paths.get("upload");
            //metoda która zwraca path względem pliku który damy hobbyProject/upload/xyz.jpg
            int indexOfDot = file.getOriginalFilename().indexOf('.');
            String fileExt = file.getOriginalFilename().substring(indexOfDot);
            System.out.println("EXTENSION: " + fileExt);

            String vari = "janek";
            System.out.println(vari);
            vari = "nie janek";
            System.out.println(vari);

            try(InputStream inputStream = file.getInputStream()){ //inputStream to poprostu strumień danych
                String fileName = "upload_" + hobbyId + fileExt;
                Files.copy(inputStream, rootLocation.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);

                hobby.setFileName(fileName);
                hobbyRepository.saveAndFlush(hobby); //wyciągam inputStream z pliku i za pomocą Files zapisuje/kopiuję
            }catch (IOException e){
                System.out.println("Obrassek spsuty!");
            }
            return "success";
        }
        return "error";
    }

}
