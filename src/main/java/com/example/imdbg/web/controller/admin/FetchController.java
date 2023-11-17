package com.example.imdbg.web.controller.admin;

import com.example.imdbg.service.admin.FetchService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/admin/fetchIMDB")
public class FetchController {

    private final FetchService fetchService;
    private Thread fetchThread;

    public FetchController(FetchService fetchService) {
        this.fetchService = fetchService;
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

    @GetMapping("/updates")
    public ModelAndView getUpdateLists(ModelAndView modelAndView) {
        modelAndView.setViewName("updateIMDBLists");
        modelAndView.addObject("isThreadRunning", isThreadRunning());
        modelAndView.addObject("fetchStatus", this.getStatusString());
        return modelAndView;
    }

    @GetMapping("/updates/single")
    public ModelAndView getUpdateSignle(ModelAndView modelAndView) {
        modelAndView.setViewName("updateIMDBSingle");
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
                    fetchThread = new Thread(fetchService::fetchTop250ImdbTitles);
                    fetchThread.start();
                }
                case "100MostPopular" -> {
                    fetchThread = new Thread(fetchService::fetch100MostPopularImdbTitles);
                    fetchThread.start();
                }
                case "upcoming" -> {
                    fetchThread = new Thread(fetchService::fetchUpcomingImdbTitles);
                    fetchThread.start();
                }
            }
        }
        modelAndView.setViewName("redirect:/admin/fetchIMDB/lists");
        return modelAndView;
    }

    @PostMapping("/start/single/{id}")
    public ModelAndView postStartSingleFetch(@PathVariable String id, ModelAndView modelAndView) {
        if (isThreadNull() || !isThreadRunning()){
            fetchThread = new Thread(() -> fetchService.fetchSingleImdbTitle(id));
            fetchThread.start();
        }
        modelAndView.setViewName("redirect:/admin/fetchIMDB/single");
        return modelAndView;
    }

    @PostMapping("/start/pages/{pageNumber}")
    public ModelAndView postStartPageFetch(@PathVariable Integer pageNumber, ModelAndView modelAndView) {
        if (isThreadNull() || !isThreadRunning()){
            fetchThread = new Thread(() -> fetchService.fetch250Titles(pageNumber));
            fetchThread.start();
        }
        modelAndView.setViewName("redirect:/admin/fetchIMDB/pages");
        return modelAndView;
    }

    @PostMapping("/start/updates/{method}")
    public ModelAndView postStartPageFetch(@PathVariable String method, ModelAndView modelAndView) {
        if (isThreadNull() || !isThreadRunning()){
            switch (method) {
                case "100MostPopular" -> {
                    fetchThread = new Thread(fetchService::updateImdbPopularityRanks100MostPopular);
                    fetchThread.start();
                }
                case "top250" -> {
                    fetchThread = new Thread(fetchService::updateImdbTop250);
                    fetchThread.start();
                }
            }
        }
        modelAndView.setViewName("redirect:/admin/fetchIMDB/updates");
        return modelAndView;
    }

    @PostMapping("/start/updates/single/{id}")
    public ModelAndView postStartUpdateSingleFetch(@PathVariable String id, ModelAndView modelAndView) {
        if (isThreadNull() || !isThreadRunning()){
            fetchThread = new Thread(() -> fetchService.updateSingleTitle(id));
            fetchThread.start();
        }
        modelAndView.setViewName("redirect:/admin/fetchIMDB/updates/single");
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
            return "Fetching. Check back-end for more comprehensive info";
        }
        return String.join("<br><br>", fetchService.getFetchThreadLog());
    }

    private boolean isThreadRunning() {
        return fetchThread != null && fetchThread.isAlive();
    }

    private boolean isThreadNull() {
        return fetchThread == null;
    }
}
