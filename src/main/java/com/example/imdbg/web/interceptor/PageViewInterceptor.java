package com.example.imdbg.web.interceptor;

import com.example.imdbg.model.exceptions.BadRequestException;
import com.example.imdbg.service.movies.PageViewService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class PageViewInterceptor implements HandlerInterceptor {

    private final PageViewService pageViewService;

    public PageViewInterceptor(PageViewService pageViewService) {
        this.pageViewService = pageViewService;
    }

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler){
        String requestURI = request.getRequestURI();
        if (requestURI.startsWith("/title/")){
            String sessionId = request.getSession().getId();
            try {
                pageViewService.incrementUniquePageView(requestURI.substring(7), sessionId);
            }
            catch (Exception e){
                throw new BadRequestException("Wrong title id format. " + requestURI);
            }
        }
        return true;
    }

}
