package ru.stn.telegram.tests.states.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.stn.telegram.tests.states.entities.sessions.*;
import ru.stn.telegram.tests.states.protocols.Protocols;
import ru.stn.telegram.tests.states.repositories.SessionRepository;
import ru.stn.telegram.tests.states.services.SessionService;

import java.util.function.Consumer;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {
    private final SessionRepository sessionRepository;

    private <S extends Session, T> void setAttribute(S session, Consumer<T> setter, T value) {
        setter.accept(value);
        sessionRepository.save(session);
    }

    private Session findCommon(long userId) {
        Session session = sessionRepository.findById(userId).orElse(null);
        if (session == null) {
            return null;
        } else {
            return session;
        }
    }

    private <S extends Session> S getSpecificSession(long userId, Function<Long, S> instantiator) {
        S session = find(userId);
        if (session == null) {
            session = instantiator.apply(userId);
            sessionRepository.save(session);
        }
        return session;
    }
    private <S extends Session> S toSpecificSession(long userId, Function<Long, S> instantiator) {
        Session oldSession = findCommon(userId);
        if (oldSession != null) {
            sessionRepository.delete(oldSession);
        }
        return getSpecificSession(userId, instantiator);
    }

    @Override
    public <S extends Session> S find(long userId) {
        return (S) findCommon(userId);
    }

    @Override
    public void setState(Session session, int state) {
        setAttribute(session, session::setState, state);
    }

    @Override
    public void setProtocol(Session session, Protocols protocol) {
        setAttribute(session, session::setProtocol, protocol);
    }

    @Override
    public void save(Session session) {
        sessionRepository.save(session);
    }

    @Override
    public QuestionSession getQuestionSession(long userId) {
        return getSpecificSession(userId, QuestionSession::new);
    }
    @Override
    public CurrencySession getCurrencySession(long userId) {
        return getSpecificSession(userId, CurrencySession::new);
    }
    @Override
    public PaymentSession getPaymentSession(long userId) {
        return getSpecificSession(userId, PaymentSession::new);
    }

    @Override
    public QuestionSession toQuestionSession(long userId) {
        return toSpecificSession(userId, QuestionSession::new);
    }
    @Override
    public CurrencySession toCurrencySession(long userId) {
        return toSpecificSession(userId, CurrencySession::new);
    }
    @Override
    public PaymentSession toPaymentSession(long userId) {
        return toSpecificSession(userId, PaymentSession::new);
    }
    @Override
    public BalanceSession toBalanceSession(long userId) {
        return toSpecificSession(userId, BalanceSession::new);
    }
}
