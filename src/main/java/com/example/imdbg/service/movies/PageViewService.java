package com.example.imdbg.service.movies;

import com.example.imdbg.model.entity.movies.TitleEntity;
import com.example.imdbg.service.movies.TitleService;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PageViewService {

    private final TitleService titleService;
    private ConcurrentHashMap<Long, Set<String>> pageViews = new ConcurrentHashMap<>();

    public PageViewService(TitleService titleService) {
        this.titleService = titleService;
    }

    public void incrementUniquePageView(String uri, String sessionId) {
        pageViews.compute(Long.valueOf(uri), (key, value) -> {
            if (value == null){
                value = new HashSet<>();
            }
            value.add(sessionId);
            return value;
        });
    }

    @Scheduled(fixedRate = 60000)
    public void updatePageViewsInDb() {
        if (!pageViews.isEmpty()) {
            pageViews.forEach((key, value) -> {
                try {
                    titleService.incrementPageViews(key, value.size());
                } catch (Exception ignored) {
                }
            });
            System.out.println("updated page views in db " + pageViews);
            clearPageViews();
        }
    }


    private void clearPageViews() {
        pageViews = new ConcurrentHashMap<>();
    }


}

// With separate timers for db update, and sessions reset


//
//@Service
//public class PageViewService {
//
//    private final TitleService titleService;
//    private ConcurrentHashMap<Long, Set<String>> sessionMap = new ConcurrentHashMap<>();
//
//    private ConcurrentHashMap<Long, Integer> uniqueClicks = new ConcurrentHashMap<>();
//
//
//    public PageViewService(TitleService titleService) {
//        this.titleService = titleService;
//    }
//
//    public void incrementUniquePageView(String uri, String sessionId) {
//        sessionMap.compute(Long.valueOf(uri), (key, value) -> {
//            if (value == null){
//                value = new HashSet<>();
//            }
//            if (value.add(sessionId)) {
//                uniqueClicks.compute(Long.valueOf(uri), (key1, value1) -> value1 == null ? 1 : value1 + 1);
//            }
//            return value;
//        });
//    }
//
//    @Scheduled(fixedRate = 30000)
//    public void updatePageViewsInDb() {
//        if (!uniqueClicks.isEmpty()) {
//            uniqueClicks.forEach((key, value) -> {
//                try {
//                    titleService.incrementPageViews(key, value);
//                } catch (Exception ignored) {
//                }
//            });
//            uniqueClicks = new ConcurrentHashMap<>();
//        }
//    }
//
//    @Scheduled(fixedRate = 60000)
//    private void clearSessionMap() {
//        sessionMap = new ConcurrentHashMap<>();
//    }
//
//
//}
