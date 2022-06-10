package ru.stn.telegram.tests.states.protocols.basics;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.stn.telegram.tests.states.processors.BotCommandProcessor;
import ru.stn.telegram.tests.states.processors.CancelCommandProcessor;
import ru.stn.telegram.tests.states.entities.sessions.Session;
import ru.stn.telegram.tests.states.exceptions.CancelledException;
import ru.stn.telegram.tests.states.localization.Entry;
import ru.stn.telegram.tests.states.localization.Localizer;
import ru.stn.telegram.tests.states.services.BotService;
import ru.stn.telegram.tests.states.services.SessionService;

import java.util.ResourceBundle;

@RequiredArgsConstructor
public abstract class BaseState<C> implements State<C> {
    @Autowired
    protected SessionService sessionService;
    @Autowired
    protected BotService botService;
    @Autowired
    protected Localizer localizer;
    @Autowired
    private CancelCommandProcessor commandProcessor;

    private final Entry promptEntry;

    @Override
    public void prompt(Session session, C context, ResourceBundle resourceBundle) {
        botService.sendMessage(session.getUserId(), localizer.localize(promptEntry, resourceBundle));
    }

    @Override
    public void handle(Session session, C context, Message message, ResourceBundle resourceBundle) {
        boolean cancelled = commandProcessor.process(new BotCommandProcessor.Args(message, resourceBundle));
        if (cancelled) {
            botService.sendMessage(message.getFrom().getId(), localizer.localize(Entry.CANCELLED, resourceBundle));
            throw new CancelledException();
        } else {
            process(session, context, message, resourceBundle);
        }
    }

    protected abstract void process(Session session, C context, Message message, ResourceBundle resourceBundle);
}
