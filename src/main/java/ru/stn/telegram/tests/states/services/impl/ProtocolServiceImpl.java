package ru.stn.telegram.tests.states.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.stn.telegram.tests.states.entities.sessions.Session;
import ru.stn.telegram.tests.states.exceptions.CancelledException;
import ru.stn.telegram.tests.states.protocols.Protocols;
import ru.stn.telegram.tests.states.protocols.basics.Navigator;
import ru.stn.telegram.tests.states.protocols.basics.Protocol;
import ru.stn.telegram.tests.states.protocols.basics.State;
import ru.stn.telegram.tests.states.protocols.basics.Transition;
import ru.stn.telegram.tests.states.services.ProtocolService;
import ru.stn.telegram.tests.states.services.SessionService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

@Service
@RequiredArgsConstructor
public class ProtocolServiceImpl implements ProtocolService {
    @FunctionalInterface
    private interface Handler<C> {
        Navigator<? super C, C> apply(
                Session session,
                C context,
                Navigator<? super C, C> navigator,
                Transition<? super C, C> transition,
                Protocol<C> protocol,
                List<Navigator<? super C, C>> navigators,
                ResourceBundle resourceBundle
        );
    }

    private final SessionService sessionService;
    private final BeanFactory beanFactory;

    private <C> Protocol<C> getProtocol(String name) {
        return (Protocol<C>) beanFactory.getBean(name, Protocol.class);
    }

    private <C> Map<Transition.Kind, Handler<C>> getHandlers() {
        return new HashMap<>() {{
            put(Transition.Kind.NEXT, (s, c, n, t, p, ns, rb) -> next(s, c, n, t, p, ns, rb));
            put(Transition.Kind.SELF, (s, c, n, t, p, ns, rb) -> self(s, c, n, t, p, ns, rb));
            put(Transition.Kind.CANCEL, (s, c, n, t, p, ns, rb) -> cancel(s, c, n, t, p, ns, rb));
            put(Transition.Kind.STANDBY, (s, c, n, t, p, ns, rb) -> standby(s, c, n, t, p, ns, rb));
            put(Transition.Kind.SPECIFIC, (s, c, n, t, p, ns, rb) -> specific(s, c, n, t, p, ns, rb));
        }};
    }

    private <C> Navigator<? super C, C> next(
            Session session,
            C context,
            Navigator<? super C, C> navigator,
            Transition<? super C, C> transition,
            Protocol<C> protocol,
            List<Navigator<? super C, C>> navigators,
            ResourceBundle resourceBundle
    ) {
        int index = session.getState() + 1;
        if (index == navigators.size()) {
            index = -1;
            navigator = null;
            protocol.commit(session, context, resourceBundle);
        } else {
            navigator = navigators.get(index);
        }
        sessionService.setState(session, index);
        return navigator;
    }
    private <C> Navigator<? super C, C> self(
            Session session,
            C context,
            Navigator<? super C, C> navigator,
            Transition<? super C, C> transition,
            Protocol<C> protocol,
            List<Navigator<? super C, C>> navigators,
            ResourceBundle resourceBundle
    ) {
        return navigator;
    }
    private <C> Navigator<? super C, C> cancel(
            Session session,
            C context,
            Navigator<? super C, C> navigator,
            Transition<? super C, C> transition,
            Protocol<C> protocol,
            List<Navigator<? super C, C>> navigators,
            ResourceBundle resourceBundle
    ) {
        sessionService.setState(session, -1);
        return null;
    }
    private <C> Navigator<? super C, C> standby(
            Session session,
            C context,
            Navigator<? super C, C> navigator,
            Transition<? super C, C> transition,
            Protocol<C> protocol,
            List<Navigator<? super C, C>> navigators,
            ResourceBundle resourceBundle
    ) {
        sessionService.setState(session, -1);
        protocol.commit(session, context, resourceBundle);
        return null;
    }
    private <C> Navigator<? super C, C> specific(
            Session session,
            C context,
            Navigator<? super C, C> navigator,
            Transition<? super C, C> transition,
            Protocol<C> protocol,
            List<Navigator<? super C, C>> navigators,
            ResourceBundle resourceBundle
    ) {
        int index = navigators.indexOf(transition.getNavigator());
        sessionService.setState(session, index);
        return transition.getNavigator();
    }

    @Override
    public <C> void start(Session session, C context, Protocols protocol, ResourceBundle resourceBundle) {
        sessionService.setProtocol(session, protocol);
        Protocol<C> foundProtocol = getProtocol(protocol.getName());
        Navigator<? super C, C> navigator = foundProtocol.getNavigators().get(0);
        State<? super C> state = navigator.getState();
        sessionService.setState(session, 0);
        state.prompt(session, context, resourceBundle);
    }

    @Override
    public <C> void process(Session session, C context, Message message, ResourceBundle resourceBundle) {
        Protocol<C> protocol = getProtocol(session.getProtocol().getName());
        List<Navigator<? super C, C>> navigators = protocol.getNavigators();
        Navigator<? super C, C> navigator = navigators.get(session.getState());
        Transition<? super C, C> transition;
        try {
            transition = navigator.navigate(session, context, message, resourceBundle);
        } catch (CancelledException e) {
            transition = Transition.cancel();
        }
        Map<Transition.Kind, Handler<C>> handlers = getHandlers();
        navigator = handlers.get(transition.getKind()).apply(session, context, navigator, transition, protocol, navigators, resourceBundle);
        if (navigator != null) {
            navigator.getState().prompt(session, context, resourceBundle);
        }
    }
}
