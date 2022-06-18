package ru.stn.telegram.tests.states.services;

import ru.stn.telegram.tests.states.entities.sessions.*;
import ru.stn.telegram.tests.states.protocols.Protocols;

public interface SessionService {
    <S extends Session> S find(long userId);
    void setState(Session session, int state);
    void setProtocol(Session session, Protocols protocol);
    void save(Session session);

    QuestionSession getQuestionSession(long userId);
    CurrencySession getCurrencySession(long userId);
    PaymentSession getPaymentSession(long userId);

    QuestionSession toQuestionSession(long userId);
    CurrencySession toCurrencySession(long userId);
    PaymentSession toPaymentSession(long userId);
    BalanceSession toBalanceSession(long userId);
}
