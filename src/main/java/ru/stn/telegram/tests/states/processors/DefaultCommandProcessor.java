package ru.stn.telegram.tests.states.processors;

import org.springframework.stereotype.Component;
import ru.stn.telegram.tests.states.entities.sessions.Session;
import ru.stn.telegram.tests.states.protocols.Protocols;
import ru.stn.telegram.tests.states.services.ProtocolService;
import ru.stn.telegram.tests.states.services.SessionService;

import javax.annotation.PostConstruct;
import java.util.function.BiFunction;

@Component
public class DefaultCommandProcessor extends BotCommandProcessor<Void> {
    private final SessionService sessionService;
    private final ProtocolService protocolService;

    public DefaultCommandProcessor(SessionService sessionService, ProtocolService protocolService) {
        super(null);
        this.sessionService = sessionService;
        this.protocolService = protocolService;
    }

    @PostConstruct
    private void init() {
        addHandler("question_full", this::questionFull);
        addHandler("question_brief", this::questionBrief);
        addHandler("keyword", this::keyword);
        addHandler("message", this::message);
        addHandler("timeout", this::timeout);
        addHandler("correct", this::correct);
        addHandler("attempt", this::attempt);
        addHandler("maximum", this::maximum);
        addHandler("show", this::show);

        addHandler("currency", this::currency);

        addHandler("payment", this::payment);
    }

    private <S extends Session> Void common(Args args, Protocols protocol, BiFunction<SessionService, Long, S> getter) {
        S session = getter.apply(sessionService, args.getMessage().getFrom().getId());
        protocolService.start(session, session, protocol, args.getResourceBundle());
        return null;
    }

    private Void question(Args args, Protocols protocol) {
        return common(args, protocol, SessionService::toQuestionSession);
    }

    private Void questionFull(Args args) {
        return question(args, Protocols.FULL);
    }
    private Void questionBrief(Args args) {
        return question(args, Protocols.BRIEF);
    }
    private Void keyword(Args args) {
        return question(args, Protocols.KEYWORD);
    }
    private Void message(Args args) {
        return question(args, Protocols.MESSAGE);
    }
    private Void timeout(Args args) {
        return question(args, Protocols.TIMEOUT);
    }
    private Void correct(Args args) {
        return question(args, Protocols.CORRECT);
    }
    private Void attempt(Args args) {
        return question(args, Protocols.ATTEMPT);
    }
    private Void maximum(Args args) {
        return question(args, Protocols.MAXIMUM);
    }
    private Void show(Args args) {
        return question(args, Protocols.SHOW);
    }

    private Void currency(Args args) {
        return common(args, Protocols.CURRENCY, SessionService::toCurrencySession);
    }

    private Void payment(Args args) {
        return common(args, Protocols.PAYMENT, SessionService::toPaymentSession);
    }
}
