package com.example.imdbg.event;

import org.springframework.context.ApplicationEvent;

public class InitTitlesFinishedEvent extends ApplicationEvent {
    public InitTitlesFinishedEvent(Object source) {
        super(source);
    }
}
