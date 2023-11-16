//package com.example.imdbg.config;
//
//import com.example.imdbg.web.interceptor.PageViewInterceptor;
//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//import org.modelmapper.ModelMapper;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.reactive.function.client.WebClient;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//public class AppConfig{
//
//    @Bean
//    public ModelMapper modelMapper() {
//        return new ModelMapper();
//    }
//
//    @Bean
//    public Gson gson(){
//        return new GsonBuilder()
//                .setPrettyPrinting()
//                .create();
//    }
//
//}
