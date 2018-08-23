package com.hoppy.hobbyProject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
@RequestMapping(path = "/")
public class IndexController {

    @GetMapping(path = "/")
    public String index(Principal principal, Model model){
        if(principal != null){
            model.addAttribute("username", principal.getName());
        }else{
            model.addAttribute("anonymous", "Hello whoever u are biacz.");
        }
//nie możnaby tutaj zrobić else? a co w tym elsie byś cchciał
        //jesli user jest null (czyli niezalogowany) to wypluj na froncie user = jakaś nazwa ale żeby było w panelu admina jako nie zalogowany
        //nie będzie widać panelu admina jeżeli jesteś nie zalogowany
        // co do elsa mógł byś ale wtedy na froncie wrzucasz tylko
        //ale to jest brzydko bo 1. nie widzisz w kodzie na froncie czy user jest czy nie
        //najczęsciej gdy czegoś niema a niby powinno być na frocie żeby to wyświetlać srasz błędami i je obsługujesz
        //innaczej mówiąc unikasz sytuacji w których czegoś niema. jeśli masz konkretną sytruacje istnieje /nie istnieje
        //cokolwiek by to nie było robisz do tego ładny przejrzysty kod
        //po twojemu to brzydko i nie clean code
        //chodziło mi o ten nasz tutaj "admin panel" na froncie w indeksie
        return "index";
    }
}
