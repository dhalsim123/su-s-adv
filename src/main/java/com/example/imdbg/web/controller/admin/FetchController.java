package com.example.imdbg.web.controller.admin;

import com.example.imdbg.event.InitTitlesFinishedEvent;
import com.example.imdbg.event.InitTitlesStartedEvent;
import com.example.imdbg.service.admin.FetchService;
import lombok.Getter;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/fetchIMDB")
public class FetchController {

    private final FetchService fetchService;
    private static Thread fetchThread;

    @Getter
    private static boolean fetchEnabled = true;

    public FetchController(FetchService fetchService) {
        this.fetchService = fetchService;
    }

    @GetMapping("/lists")
    public ModelAndView getFetchLists(ModelAndView modelAndView) {
        return getFetchMV(modelAndView, "fetchIMDBLists");
    }

    @GetMapping("/single")
    public ModelAndView getFetchSingle(ModelAndView modelAndView) {
        return getFetchMV(modelAndView, "fetchIMDBSingle");
    }

    @GetMapping("/pages")
    public ModelAndView getFetchPages(ModelAndView modelAndView) {
        return getFetchMV(modelAndView, "fetchIMDBPages");
    }

    @GetMapping("/updates")
    public ModelAndView getUpdateLists(ModelAndView modelAndView) {
        return getFetchMV(modelAndView, "updateIMDBLists");
    }

    @GetMapping("/updates/single")
    public ModelAndView getUpdateSingle(ModelAndView modelAndView) {
        return getFetchMV(modelAndView, "updateIMDBSingle");
    }



    @GetMapping("/status")
    public Object getFetchStatus(@RequestHeader("Accept") String acceptHeader) {
        if (acceptHeader.contains("text/html")){
            return new ModelAndView("redirect:/admin/panel");
        }
        return ResponseEntity.ok(isThreadRunning());
    }

    @PostMapping("/start/{method}")
    public ModelAndView postStartListFetch(@PathVariable String method, ModelAndView modelAndView) {
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

    @PostMapping("/start/pages/{stringPageNumber}")
    public ModelAndView postStartPageFetch(@PathVariable String stringPageNumber, RedirectAttributes redirectAttributes, ModelAndView modelAndView) {
        try {
            int pageNumber = Integer.parseInt(stringPageNumber);
            if (isThreadNull() || !isThreadRunning()){
                fetchThread = new Thread(() -> fetchService.fetch250Titles(pageNumber));
                fetchThread.start();
            }
        }
        catch (NumberFormatException e){
            redirectAttributes.addFlashAttribute("badRequestError", "Bad request: Page number must be a number");
        }
        modelAndView.setViewName("redirect:/admin/fetchIMDB/pages");
        return modelAndView;
    }

    @PostMapping("/start/updates/{method}")
    public ModelAndView postStartUpdate(@PathVariable String method, ModelAndView modelAndView) {
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

    @PostMapping("/start/updates/single/{stringId}")
    public ModelAndView postStartUpdateSingleFetch(@PathVariable String stringId, RedirectAttributes redirectAttributes, ModelAndView modelAndView) {
        try {
            Long id = Long.parseLong(stringId);
            if (isThreadNull() || !isThreadRunning()){
                fetchThread = new Thread(() -> fetchService.updateSingleTitle(id));
                fetchThread.start();
            }
        } catch (NumberFormatException e){
            redirectAttributes.addFlashAttribute("badRequestError", "Bad request: Id must be a number");
        }
        modelAndView.setViewName("redirect:/admin/fetchIMDB/updates/single");
        return modelAndView;
    }

    @PostMapping("/lists/stop")
    public ModelAndView postStopListsFetch(ModelAndView modelAndView) {
        getStopFetchMV(modelAndView, "redirect:/admin/fetchIMDB/lists");
        return modelAndView;
    }



    @PostMapping("/single/stop")
    public ModelAndView postStopSingleFetch(ModelAndView modelAndView) {
        getStopFetchMV(modelAndView, "redirect:/admin/fetchIMDB/single");
        return modelAndView;
    }

    @PostMapping("/pages/stop")
    public ModelAndView postStopPageFetch(ModelAndView modelAndView) {
        getStopFetchMV(modelAndView, "redirect:/admin/fetchIMDB/pages");
        return modelAndView;
    }

    private ModelAndView getFetchMV(ModelAndView modelAndView, String fetchIMDBLists) {
        modelAndView.setViewName(fetchIMDBLists);
        modelAndView.addObject("isThreadRunning", isThreadRunning());
        modelAndView.addObject("fetchStatus", this.getStatusString());
        return modelAndView;
    }

    private void getStopFetchMV(ModelAndView modelAndView, String viewName) {
        if (isThreadRunning()) {
            fetchThread.interrupt();
        }
        modelAndView.setViewName(viewName);
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

    public boolean isThreadRunning() {
        return fetchThread != null && fetchThread.isAlive();
    }

    public boolean isThreadNull() {
        return fetchThread == null;
    }

    public static void setFetchEnabled(boolean flag){
        FetchController.fetchEnabled = flag;
    }

    @EventListener(InitTitlesStartedEvent.class)
    public void handleInitTitlesStarted (){
        FetchController.setFetchEnabled(false);
    }

    @EventListener(InitTitlesFinishedEvent.class)
    public void handleInitTitlesFinished (){
        FetchController.setFetchEnabled(true);
    }
}
