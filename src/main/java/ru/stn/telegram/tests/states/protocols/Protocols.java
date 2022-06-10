package ru.stn.telegram.tests.states.protocols;

import lombok.Getter;
import ru.stn.telegram.tests.states.protocols.protocols.currency.CurrencyContext;
import ru.stn.telegram.tests.states.protocols.protocols.payment.PaymentContext;
import ru.stn.telegram.tests.states.protocols.protocols.question.QuestionContext;

public enum Protocols {
    FULL("FullQuestionProtocol", (ps, s, m, rb) -> ps.process(s, (QuestionContext) s, m, rb)),
    BRIEF("BriefQuestionProtocol", (ps, s, m, rb) -> ps.process(s, (QuestionContext) s, m, rb)),
    KEYWORD("KeywordQuestionProtocol", (ps, s, m, rb) -> ps.process(s, (QuestionContext) s, m, rb)),
    MESSAGE("MessageQuestionProtocol", (ps, s, m, rb) -> ps.process(s, (QuestionContext) s, m, rb)),
    TIMEOUT("TimeoutQuestionProtocol", (ps, s, m, rb) -> ps.process(s, (QuestionContext) s, m, rb)),
    CORRECT("CorrectQuestionProtocol", (ps, s, m, rb) -> ps.process(s, (QuestionContext) s, m, rb)),
    ATTEMPT("AttemptQuestionProtocol", (ps, s, m, rb) -> ps.process(s, (QuestionContext) s, m, rb)),
    MAXIMUM("MaximumQuestionProtocol", (ps, s, m, rb) -> ps.process(s, (QuestionContext) s, m, rb)),
    SHOW("ShowQuestionProtocol", (ps, s, m, rb) -> ps.process(s, (QuestionContext) s, m, rb)),

    CURRENCY("CurrencyProtocol", (ps, s, m, rb) -> ps.process(s, (CurrencyContext) s, m, rb)),

    PAYMENT("PaymentProtocol", (ps, s, m, rb) -> ps.process(s, (PaymentContext) s, m, rb));

    @Getter
    private final String name;
    @Getter
    private final StateConsumer consumer;

    Protocols(String name, StateConsumer consumer) {
        this.name = name;
        this.consumer = consumer;
    }
}
