package ru.stn.telegram.quiz.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.stn.telegram.quiz.entities.Session;
import ru.stn.telegram.quiz.repositories.SessionRepository;
import ru.stn.telegram.quiz.services.SessionService;

@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {
    private final SessionRepository sessionRepository;

    private void setState(Session session, Session.State state) {
        session.setState(state);
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
    public void toSetQuestionExpectingForward(Session session, String text) {
        session.setState(Session.State.SET_QUESTION_EXPECTING_FORWARD);
        session.setText(text);
        sessionRepository.save(session);
    }
}
