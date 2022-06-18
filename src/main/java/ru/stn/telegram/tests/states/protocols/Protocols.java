package ru.stn.telegram.tests.states.protocols;

import lombok.Getter;
import ru.stn.telegram.tests.states.protocols.protocols.currency.CurrencyContext;
import ru.stn.telegram.tests.states.protocols.protocols.payment.OwnBalanceContext;
import ru.stn.telegram.tests.states.protocols.protocols.payment.PaymentContext;
import ru.stn.telegram.tests.states.protocols.protocols.payment.UserBalanceContext;
import ru.stn.telegram.tests.states.protocols.protocols.question.QuestionContext;

public enum Protocols {
    FULL("FullQuestionProtocol", "/question_full", (ps, s, m, rb) -> ps.process(s, (QuestionContext) s, m, rb)),
    BRIEF("BriefQuestionProtocol", "/question_brief", (ps, s, m, rb) -> ps.process(s, (QuestionContext) s, m, rb)),
    KEYWORD("KeywordQuestionProtocol", "/question", (ps, s, m, rb) -> ps.process(s, (QuestionContext) s, m, rb)),
    MESSAGE("MessageQuestionProtocol", "/message", (ps, s, m, rb) -> ps.process(s, (QuestionContext) s, m, rb)),
    TIMEOUT("TimeoutQuestionProtocol", "/timeout", (ps, s, m, rb) -> ps.process(s, (QuestionContext) s, m, rb)),
    CORRECT("CorrectQuestionProtocol", "/correct", (ps, s, m, rb) -> ps.process(s, (QuestionContext) s, m, rb)),
    ATTEMPT("AttemptQuestionProtocol", "/attempt", (ps, s, m, rb) -> ps.process(s, (QuestionContext) s, m, rb)),
    MAXIMUM("MaximumQuestionProtocol", "/maximum", (ps, s, m, rb) -> ps.process(s, (QuestionContext) s, m, rb)),
    SHOW("ShowQuestionProtocol", "/show", (ps, s, m, rb) -> ps.process(s, (QuestionContext) s, m, rb)),

    CURRENCY("CurrencyProtocol", "/currency", (ps, s, m, rb) -> ps.process(s, (CurrencyContext) s, m, rb)),

    PAYMENT("PaymentProtocol", "/payment", (ps, s, m, rb) -> ps.process(s, (PaymentContext) s, m, rb)),
    OWN_BALANCE("OwnBalanceProtocol", "/own_balance", (ps, s, m, rb) -> ps.process(s, (OwnBalanceContext) s, m, rb)),
    USER_BALANCE("UserBalanceProtocol", "/user_balance", (ps, s, m, rb) -> ps.process(s, (UserBalanceContext) s, m, rb));

    @Getter
    private final String name;
    @Getter
    private final String command;
    @Getter
    private final StateConsumer consumer;

    Protocols(String name, String command, StateConsumer consumer) {
        this.name = name;
        this.command = command;
        this.consumer = consumer;
    }
}
