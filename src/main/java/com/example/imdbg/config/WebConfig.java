package com.example.imdbg.config;

import com.example.imdbg.web.interceptor.ChartsLoadTimeInterceptor;
import com.example.imdbg.web.interceptor.PageViewInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final PageViewInterceptor pageViewInterceptor;
    private final ChartsLoadTimeInterceptor chartsLoadTimeInterceptor;

    public WebConfig(PageViewInterceptor pageViewInterceptor, ChartsLoadTimeInterceptor chartsLoadTimeInterceptor) {
        this.pageViewInterceptor = pageViewInterceptor;
        this.chartsLoadTimeInterceptor = chartsLoadTimeInterceptor;
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(pageViewInterceptor);
        registry.addInterceptor(chartsLoadTimeInterceptor);
    }



}
