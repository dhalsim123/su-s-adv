package com.example.imdbg.event;

import org.springframework.context.ApplicationEvent;

public class InitTitlesStartedEvent extends ApplicationEvent {
    public InitTitlesStartedEvent(Object source) {
        super(source);
    }
}
