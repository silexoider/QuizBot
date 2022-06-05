package ru.stn.telegram.quiz.services;

import ru.stn.telegram.quiz.entities.Session;

public interface SessionService {
    Session getSession(long userId);

    void setProtocol(Session session, Session.Protocol protocol);
    void toDefault(Session session);
    Session.State getNextState(Session session);
    Session.State toNextState(Session session);
    Session.State toInitialState(Session session);
    void setKeyword(Session session, String keyword);
    void setMessage(Session session, String message);
    void setTimeout(Session session, int timeout);
}
