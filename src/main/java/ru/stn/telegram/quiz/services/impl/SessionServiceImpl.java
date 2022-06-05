package ru.stn.telegram.quiz.services.impl;

import lombok.RequiredArgsConstructor;
import org.checkerframework.common.util.report.qual.ReportOverride;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Service;
import ru.stn.telegram.quiz.entities.Session;
import ru.stn.telegram.quiz.repositories.SessionRepository;
import ru.stn.telegram.quiz.services.ProtocolService;
import ru.stn.telegram.quiz.services.SessionService;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {
    private final SessionRepository sessionRepository;

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
    public void setState(Session session, Session.State state) {
        session.setState(state);
        sessionRepository.save(session);
    }

    @Override
    public void setProtocol(Session session, Session.Protocol protocol) {
        setAttribute(session, session::setProtocol, protocol);
    }

    @Override
    public void setForward(Session session, long chatId, int postId) {
        session.setChatId(chatId);
        session.setPostId(postId);
        sessionRepository.save(session);
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
