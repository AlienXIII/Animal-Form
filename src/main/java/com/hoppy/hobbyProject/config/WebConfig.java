package com.hoppy.hobbyProject.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
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

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
//        registry.addViewController("/").setViewName("index");
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/thematrix").setViewName("thematrix");
        registry.addViewController("/error").setViewName("error");
    }

    @Override //handlery dla css i js na stronke
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/css/**").addResourceLocations("classpath:/static/css/");
        registry.addResourceHandler("/js/**").addResourceLocations("classpath:/static/js/");
        //Mapujemy sobie /images/upload_14.jpg na przykład który wyciąga img z dokładnego miejsca na dysku
        //więc na stronce żeby ukryć ścieżkę absulutną (c:/asd/asd/...) wpisujemy /images/**
        registry.addResourceHandler("/images/**").addResourceLocations("file:///C:/Projekty/hobbyProject/upload/");
    }
}
