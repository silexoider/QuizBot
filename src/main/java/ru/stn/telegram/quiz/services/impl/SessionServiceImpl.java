package ru.stn.telegram.quiz.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.stn.telegram.quiz.entities.Session;
import ru.stn.telegram.quiz.repositories.SessionRepository;
import ru.stn.telegram.quiz.services.SessionService;

import java.util.Arrays;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {
    private final SessionRepository sessionRepository;

    private void setState(Session session, Session.State state) {
        session.setState(state);
        sessionRepository.save(session);
    }

    private <T> void setAttribute(Session session, Consumer<T> action, T value) {
        action.accept(value);
        sessionRepository.save(session);
    }

    @Override
    public Session getSession(long userId) {
        Session session = sessionRepository.findById(userId).orElse(null);
        if (session == null) {
            session = new Session(userId);
            sessionRepository.save(session);
        }
        return session;
    }

    @Override
    public void toDefault(Session session) {
        setState(session, Session.State.DEFAULT);
    }

    @Override
    public void setProtocol(Session session, Session.Protocol protocol) {
        setAttribute(session, session::setProtocol, protocol);
    }

    @Override
    public Session.State getNextState(Session session) {
        Session.State state = session.getState();
        do {
            state = state.getNext();
        } while (state.getValue() != (state.getValue() & session.getProtocol().getMask()) && state != session.getState());
        if (state == session.getState()) {
            return null;
        } else {
            return state;
        }
    }

    @Override
    public Session.State toNextState(Session session) {
        Session.State next = getNextState(session);
        setAttribute(session, session::setState, next);
        return next;
    }

    @Override
    public Session.State toInitialState(Session session) {
        Session.State next = session.getProtocol().getInitialState();
        setAttribute(session, session::setState, next);
        return next;
    }

    @Override
    public void setKeyword(Session session, String keyword) {
        setAttribute(session, session::setKeyword, keyword);
    }

    @Override
    public void setMessage(Session session, String message) {
        setAttribute(session, session::setMessage, message);
    }

    @Override
    public void setTimeout(Session session, int timeout) {
        setAttribute(session, session::setTimeout, timeout);
    }
}
