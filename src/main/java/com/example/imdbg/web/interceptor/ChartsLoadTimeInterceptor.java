package com.example.imdbg.web.interceptor;

import com.example.imdbg.model.exceptions.BadRequestException;
import com.example.imdbg.service.movies.PageViewService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.LocalDate;
import java.util.Locale;

@Component
public class ChartsLoadTimeInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler){
        String requestURI = request.getRequestURI();
        if (requestURI.startsWith("/chart/100MostPopular") || requestURI.startsWith("/chart/top250") || requestURI.startsWith("/chart/sitePopularityRankings")){
            long starTime = System.currentTimeMillis();
            request.setAttribute("startTime", starTime);
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex){
        String requestURI = request.getRequestURI();

        if ((requestURI.startsWith("/chart/100MostPopular") || requestURI.startsWith("/chart/top250") || requestURI.startsWith("/chart/sitePopularityRankings")) && request.getAttribute("startTime") != null){

            long startTime = (long) request.getAttribute("startTime");
            long endTime = System.currentTimeMillis();

            long timeTaken = endTime - startTime;

            System.out.println("Chart loaded in " + timeTaken + " ms.");
        }
    }
}
