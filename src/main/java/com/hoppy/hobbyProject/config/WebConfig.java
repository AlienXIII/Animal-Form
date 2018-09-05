package com.hoppy.hobbyProject.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ComponentScan
public class WebConfig implements WebMvcConfigurer {

    @Bean
    BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Autowired
    Environment env;

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
//        registry.addViewController("/").setViewName("index");
        registry.addViewController("/login").setViewName("login");

        registry.addViewController("/error").setViewName("error");
    }

    @Override //handlery dla css i js na stronke
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //Mapujemy sobie /images/upload_14.jpg na przykład który wyciąga img z dokładnego miejsca na dysku
        //więc na stronce żeby ukryć ścieżkę absulutną (c:/asd/asd/...) wpisujemy /images/**
        registry.addResourceHandler("/images/**").addResourceLocations("file:///" + env.getProperty("upload.path"));
        //zmienna środowiskowa brana z app.properties
    }
}
