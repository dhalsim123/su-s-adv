package com.example.imdbg.aop;

import com.example.imdbg.web.controller.admin.FetchController;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ModelAndView;

@Aspect
@Configuration
public class FetchControllerAspect {



    @Pointcut("execution(* com.example.imdbg.web.controller.admin.FetchController.*(..))")
    void fetchControllerMethods(){
    };

    @Pointcut("execution(* com.example.imdbg.web.controller.admin.FetchController.setFetchEnabled()) || " +
            "execution(* com.example.imdbg.web.controller.admin.FetchController.handleInitTitlesStarted()) || " +
            "execution(* com.example.imdbg.web.controller.admin.FetchController.handleInitTitlesFinished())")
    void fetchControllerMethods_Without_Around(){
    };


    @Around("fetchControllerMethods() && !fetchControllerMethods_Without_Around()")
    Object aroundFetchController (ProceedingJoinPoint joinPoint) throws Throwable {
        if (FetchController.isFetchEnabled()){
            return joinPoint.proceed();
        }
        return new ModelAndView("initTitlesActiveFetch");
    }
}
