package com.example.imdbg.web.controller.users.admin;

import com.example.imdbg.service.movies.TitleService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/admin/fetchIMDB")
public class FetchController {

    private final TitleService titleService;

    private Thread fetchThread;

    public FetchController(TitleService titleService) {
        this.titleService = titleService;
    }

    @GetMapping("/lists")
    public ModelAndView getFetch(ModelAndView modelAndView) {
        modelAndView.setViewName("fetchIMDBLists");
        modelAndView.addObject("isThreadRunning", isThreadRunning());
        modelAndView.addObject("fetchStatus", this.getStatusString());
        return modelAndView;
    }

    @GetMapping("/single")
    public ModelAndView getFetchSingle(ModelAndView modelAndView) {
        modelAndView.setViewName("fetchIMDBSingle");
        modelAndView.addObject("isThreadRunning", isThreadRunning());
        modelAndView.addObject("fetchStatus", this.getStatusString());
        return modelAndView;
    }

    @GetMapping("/pages")
    public ModelAndView getFetchPages(ModelAndView modelAndView) {
        modelAndView.setViewName("fetchIMDBPages");
        modelAndView.addObject("isThreadRunning", isThreadRunning());
        modelAndView.addObject("fetchStatus", this.getStatusString());
        return modelAndView;
    }

    @GetMapping("/status")
    public ResponseEntity<Boolean> getFetchStatus() {
        return ResponseEntity.ok(isThreadRunning());
    }

    @PostMapping("/start/{method}")
    public ModelAndView postStartFetch(@PathVariable String method, ModelAndView modelAndView) {
        if (isThreadNull() || !isThreadRunning()){
            switch (method) {
                case "top250" -> {
                    fetchThread = new Thread(titleService::fetchTop250ImdbTitles);
                    fetchThread.start();
                    titleService.clearThreadLog();
                    titleService.getThreadLog().add("Top250 Fetch: ");
                }
                case "100MostPopular" -> {
                    fetchThread = new Thread(titleService::fetch100MostPopularImdbTitles);
                    fetchThread.start();
                    titleService.clearThreadLog();
                    titleService.getThreadLog().add("100 Most Popular Fetch: ");
                }
                case "upcoming" -> {
                    fetchThread = new Thread(titleService::fetchUpcomingImdbTitles);
                    fetchThread.start();
                    titleService.clearThreadLog();
                    titleService.getThreadLog().add("Upcoming titles Fetch: ");
                }
            }
        }
        modelAndView.setViewName("redirect:/admin/fetchIMDB/lists");
        return modelAndView;
    }

    @PostMapping("/start/single/{id}")
    public ModelAndView postStartSingleFetch(@PathVariable String id, ModelAndView modelAndView) {
        if (isThreadNull() || !isThreadRunning()){
            fetchThread = new Thread(() -> titleService.fetchSingleImdbTitle(id));
            fetchThread.start();
            titleService.clearThreadLog();
            titleService.getThreadLog().add("Single Title Fetch with id " + id + ": ");
        }
        modelAndView.setViewName("redirect:/admin/fetchIMDB/single");
        return modelAndView;
    }

    @PostMapping("/start/pages/{pageNumber}")
    public ModelAndView postStartPageFetch(@PathVariable Integer pageNumber, ModelAndView modelAndView) {
        if (isThreadNull() || !isThreadRunning()){
            fetchThread = new Thread(() -> titleService.fetch250Titles(pageNumber));
            fetchThread.start();
            titleService.clearThreadLog();
            titleService.getThreadLog().add("Page " + pageNumber + " Out of the Search Results: ");
        }
        modelAndView.setViewName("redirect:/admin/fetchIMDB/pages");
        return modelAndView;
    }

    @PostMapping("/lists/stop")
    public ModelAndView postStopListsFetch(ModelAndView modelAndView) {
        if (isThreadRunning()) {
            fetchThread.interrupt();
        }
        modelAndView.setViewName("redirect:/admin/fetchIMDB/lists");
        return modelAndView;
    }

    @PostMapping("/single/stop")
    public ModelAndView postStopSingleFetch(ModelAndView modelAndView) {
        if (isThreadRunning()) {
            fetchThread.interrupt();
        }
        modelAndView.setViewName("redirect:/admin/fetchIMDB/single");
        return modelAndView;
    }

    @PostMapping("/pages/stop")
    public ModelAndView postStopPageFetch(ModelAndView modelAndView) {
        if (isThreadRunning()) {
            fetchThread.interrupt();
        }
        modelAndView.setViewName("redirect:/admin/fetchIMDB/pages");
        return modelAndView;
    }


    private String getStatusString(){
        if (isThreadNull()){
            return "Idle";
        }
        else if (isThreadRunning()){
            return "Fetching. Check back-end for comprehensive info";
        }
        return String.join("<br><br>", titleService.getThreadLog());
    }

    private boolean isThreadRunning() {
        return fetchThread != null && fetchThread.isAlive();
    }

    private boolean isThreadNull() {
        return fetchThread == null;
    }
}
