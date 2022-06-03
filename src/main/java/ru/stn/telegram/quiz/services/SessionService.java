package ru.stn.telegram.quiz.services;

import ru.stn.telegram.quiz.entities.Session;

public interface SessionService {
    Session getSession(long userId);

    void toDefault(Session session);
    void toSetQuestionExpectingForward(Session session, String text);
}
