package ru.stn.telegram.tests.states.processors;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class CancelCommandProcessor extends BotCommandProcessor<Boolean> {
    public CancelCommandProcessor() {
        super(false);
    }

    @PostConstruct
    private void init() {
        addHandler("cancel", this::cancel);
    }

    private boolean cancel(Args args) {
        return true;
    }
}
