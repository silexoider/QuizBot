package ru.stn.telegram.tests.states.protocols.basics;

import org.springframework.beans.factory.annotation.Autowired;
import ru.stn.telegram.tests.states.entities.sessions.Session;
import ru.stn.telegram.tests.states.localization.Entry;
import ru.stn.telegram.tests.states.localization.Localizer;
import ru.stn.telegram.tests.states.services.BotService;
import ru.stn.telegram.tests.states.services.SessionService;

import javax.naming.Context;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public abstract class BaseProtocol<C> implements Protocol<C> {
    @Autowired
    protected Localizer localizer;
    @Autowired
    protected BotService botService;
    @Autowired
    protected SessionService sessionService;

    private final List<Navigator<? super C, C>> navigators;

    public BaseProtocol(Navigator<? super C, C> ... navigators) {
        this.navigators = Arrays.asList(navigators);
    }

    @Override
    public List<Navigator<? super C, C>> getNavigators() {
        return navigators;
    }

    @Override
    public void commit(Session session, C context, ResourceBundle resourceBundle) {
        botService.sendMessage(session.getUserId(), localizer.localize(Entry.COMPLETED, resourceBundle));
        sessionService.save(session);
    }
}
