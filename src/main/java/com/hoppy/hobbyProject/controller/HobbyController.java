package com.hoppy.hobbyProject.controller;


import com.hoppy.hobbyProject.Repo.HobbyRepository;
import com.hoppy.hobbyProject.domain.Hobby;
import lombok.extern.slf4j.Slf4j;
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
import java.util.ArrayList;

@Controller
@RequestMapping(path = "/hobby")
@Slf4j
public class HobbyController {

    @Autowired
    HobbyRepository hobbyRepository;

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

            long filesPerHobbyCounter = hobbyRepository.countFilesByHobbyId(hobby.getId()) + 1;
            try(InputStream inputStream = file.getInputStream()){ // strumień danych
                String fileName = "upload_" + hobbyId + "_" + filesPerHobbyCounter + fileExt;

                Files.copy(inputStream, rootLocation.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
                if(hobby.getFileNames() == null){
                    ArrayList<String> newFileNames = new ArrayList<>();
                    newFileNames.add(fileName);  //jeżeli hobby niema jeszcze plików zadnych
                    // to mu tworzymy nową listę
                    //newFileNames i dodajemy nazwę pliku którą tworzymy podczas uploadu czyli fileName u góry
                    // do tej nowej listy
                    //ustawiamy listę FileNames na newFileNames którą stworzyliśmy
                    hobby.setFileNames(newFileNames);
                }else {
                    //jeśli lista istnieje to dodajemy tylko nowa nazwe pliku
                    hobby.getFileNames().add(fileName);
                }

                hobbyRepository.saveAndFlush(hobby); //wyciągam inputStream z pliku i za pomocą Files zapisuje/kopiuję
            }catch (IOException e){
                System.out.println("Obrassek spsuty!");
            }
            return "success";
        }
        return "error";
    }

}
