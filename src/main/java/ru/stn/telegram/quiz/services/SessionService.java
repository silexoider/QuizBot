package ru.stn.telegram.quiz.services;

import ru.stn.telegram.quiz.entities.Session;

public interface SessionService {
    Session getSession(long userId);

    void toDefault(Session session);
    void setState(Session session, Session.State state);
    void setProtocol(Session session, Session.Protocol protocol);
    void setForward(Session session, long chatId, int postId);
    void setKeyword(Session session, String keyword);
    void setMessage(Session session, String message);
    void setTimeout(Session session, int timeout);
    void setCorrect(Session session, int timeout);
    void setAttempt(Session session, int timeout);
    void setMaximum(Session session, int timeout);
}
