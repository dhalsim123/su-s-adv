package com.example.imdbg.config;

import com.example.imdbg.web.interceptor.PageViewInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final PageViewInterceptor pageViewInterceptor;

    public WebConfig(PageViewInterceptor pageViewInterceptor) {
        this.pageViewInterceptor = pageViewInterceptor;
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(pageViewInterceptor);
    }



}
