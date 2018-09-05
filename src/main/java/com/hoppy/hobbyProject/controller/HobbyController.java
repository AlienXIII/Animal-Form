package com.hoppy.hobbyProject.controller;


import com.hoppy.hobbyProject.Repo.HobbyRepository;
import com.hoppy.hobbyProject.domain.Hobby;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
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

    @Autowired
    Environment env;

    //    ta metoda sszykuje model atrybótów do wykorzystania na hobby/list.html
    //    za pomocą thymeleaf (czyli tagi th:coś) możesz wyciągać różne dane a atrrybutów modelu
    //    np z objektów który przesylasz na list.html
    //    dodajesz do modelu listę hobbiesów więc do nich masz dostęp
    //    chcesz coś innego musisz dodać do modelu
    //    co chciales zrobić na tej liście? miniaturki z imydży dla każdego hobbysa?aye
    //    więc pomyśl, wiesz że potrzebujesz listę hobbysow i ją dodales do modelu.
    //    każdy hobby ma fileNames i możesz ją wyciągnąć getterem getFileNames()
    //    więc iterujesz dwa razy
    //    po hobbysach i wewnątrz po ich zdjęciach

    @GetMapping(path = "/list")
    public String listHobby(Model model){
        model.addAttribute("hobbies", hobbyRepository.findAll());
        return "hobby/list"; //viewresolver wyszukuje pliku widoku o podanej ścieżce
    }                         //defaultowo viewresolver szuka widoków w resources/templates/....

    @PostMapping(path = "/add")
    public String addHobby(@Valid @ModelAttribute("hobby") Hobby hobby, BindingResult results){

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

    @GetMapping(path = "/deleteImage")
    public String deleteImage(@RequestParam long hobbyId, @RequestParam String imageName, Model model){
        Hobby hobby = hobbyRepository.getOne(hobbyId);

        if(hobby == null){
            log.warn("Co to robisz ciulu!?");
            return "hobby/list";
        }else  if (!hobby.getFileNames().contains(imageName)){
            log.info("Ten hobby nie ma takiego imydża");
            return "hobby/list"; //DRY
        }

        File file = new File(env.getProperty("upload.path")+imageName);
        if(!file.delete()){
            log.warn("Upsie daisy");
        }//that should do.
        hobby.getFileNames().remove(imageName);

        hobbyRepository.saveAndFlush(hobby);

        model.addAttribute("hobbyId", hobbyId);
        model.addAttribute("hobby", hobby);
        return "success";

    }

    @GetMapping(path = "/changeImage")
    public String changeImageForm(@RequestParam long hobbyId, @RequestParam String imageName, Model model){
        model.addAttribute("hobbyId", hobbyId);
        model.addAttribute("imageName", imageName);
        return "hobby/changeImage";
    }

    @PostMapping(path = "/changeImage")
    public String changeImage(@RequestParam MultipartFile file, @RequestParam String imageName, @RequestParam long hobbyId){

        Hobby hobby = hobbyRepository.getOne(hobbyId);
        if(hobby == null){
            log.warn("Upsie");
            return "hobby/list";
        }else if(!hobby.getFileNames().contains(imageName)){
            log.warn("Upsie 2");
            return "hobby/list";
        }else if (file == null ||file.isEmpty()){
            log.warn("Upsie 3");
            return "hobby/list";
        }

        try(InputStream inputStream = file.getInputStream()){ // strumień danych
            Path rootLocation = Paths.get("upload");
            Files.copy(inputStream, rootLocation.resolve(imageName), StandardCopyOption.REPLACE_EXISTING);
        }catch (IOException e){
            System.out.println("Obrassek spsuty!");
        }
        return "redirect:list";
    }

}
