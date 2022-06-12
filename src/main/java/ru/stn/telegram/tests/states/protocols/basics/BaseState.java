package ru.stn.telegram.tests.states.protocols.basics;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.stn.telegram.tests.states.processors.BotCommandProcessor;
import ru.stn.telegram.tests.states.processors.StateCommandProcessor;
import ru.stn.telegram.tests.states.entities.sessions.Session;
import ru.stn.telegram.tests.states.exceptions.CancelledException;
import ru.stn.telegram.tests.states.localization.Entry;
import ru.stn.telegram.tests.states.localization.Localizer;
import ru.stn.telegram.tests.states.services.BotService;
import ru.stn.telegram.tests.states.services.SessionService;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

@RequiredArgsConstructor
public abstract class BaseState<C> implements State<C> {
    @FunctionalInterface
    private interface Handler<C> {
        boolean apply(Session session, C context, Message message, ResourceBundle resourceBundle);
    }

    @Autowired
    protected SessionService sessionService;
    @Autowired
    protected BotService botService;
    @Autowired
    protected Localizer localizer;
    @Autowired
    private StateCommandProcessor commandProcessor;

    private final Entry promptEntry;
    private final Map<StateCommandProcessor.Result, Handler<C>> handlers = new HashMap<>() {{
        put(StateCommandProcessor.Result.IGNORED, BaseState.this::ignored);
        put(StateCommandProcessor.Result.CANCELLED, BaseState.this::cancelled);
        put(StateCommandProcessor.Result.PROCESSED, BaseState.this::processed);
    }};

    @Override
    public String getDescription(ResourceBundle resourceBundle) {
        return localizer.localize(promptEntry, resourceBundle);
    }

    @Override
    public void prompt(Session session, C context, ResourceBundle resourceBundle) {
        botService.sendMessage(session.getUserId(), localizer.localize(promptEntry, resourceBundle));
    }

    private boolean ignored(Session session, C context, Message message, ResourceBundle resourceBundle) {
        process(session, context, message, resourceBundle);
        return true;
    }
    private boolean cancelled(Session session, C context, Message message, ResourceBundle resourceBundle) {
        throw new CancelledException();
    }
    private boolean processed(Session session, C context, Message message, ResourceBundle resourceBundle) {
        return false;
    }

    @Override
    public boolean handle(Session session, C context, Message message, ResourceBundle resourceBundle) {
        StateCommandProcessor.Result result = commandProcessor.process(new StateCommandProcessor.Args(session, message, resourceBundle));
        return handlers.get(result).apply(session, context, message, resourceBundle);
    }

    protected abstract void process(Session session, C context, Message message, ResourceBundle resourceBundle);
}
