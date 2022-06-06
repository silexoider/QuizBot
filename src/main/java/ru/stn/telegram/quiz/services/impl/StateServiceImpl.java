package ru.stn.telegram.quiz.services.impl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.stn.telegram.quiz.entities.Session;
import ru.stn.telegram.quiz.services.*;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class StateServiceImpl implements StateService {
    @Getter
    @RequiredArgsConstructor
    private static class Args {
        private final Session session;
        private final Message message;
        private final ResourceBundle resourceBundle;
    }

    @FunctionalInterface
    interface HandlerFunction {
        BotApiMethod<?> apply(ProtocolService protocolService, Session session, Message message, ResourceBundle resourceBundle);
    }

    @Qualifier("Private")
    private final CommandService commandService;
    private final ProtocolManagerService protocolManagerService;

    private final Map<Session.State, Function<Args, BotApiMethod<?>>> stateHandlers = new HashMap<>() {{
        put(Session.State.DEFAULT, StateServiceImpl.this::defaultHandler);
        put(Session.State.FORWARD, StateServiceImpl.this::forwardHandler);
        put(Session.State.KEYWORD, StateServiceImpl.this::keywordHandler);
        put(Session.State.MESSAGE, StateServiceImpl.this::messageHandler);
        put(Session.State.TIMEOUT, StateServiceImpl.this::timeoutHandler);
        put(Session.State.CORRECT, StateServiceImpl.this::correctHandler);
        put(Session.State.ATTEMPT, StateServiceImpl.this::attemptHandler);
        put(Session.State.MAXIMUM, StateServiceImpl.this::maximumHandler);
        put(Session.State.CURRENCY_SINGULAR, StateServiceImpl.this::currencySingularHandler);
        put(Session.State.CURRENCY_DUAL, StateServiceImpl.this::currencyDualHandler);
        put(Session.State.CURRENCY_PLURAL, StateServiceImpl.this::currencyPluralHandler);
        put(Session.State.COMMENT, StateServiceImpl.this::commentHandler);
        put(Session.State.PAY, StateServiceImpl.this::payHandler);
    }};

    private ProtocolService getProtocolService(Args args) {
        return protocolManagerService.getProtocolService(args.getSession().getProtocol());
    }
    private BotApiMethod<?> commonHandler(Args args, HandlerFunction handler) {
        return handler.apply(getProtocolService(args), args.getSession(), args.getMessage(), args.getResourceBundle());
    }

    private BotApiMethod<?> defaultHandler(Args args) {
        return commandService.process(args.getSession(), args.getMessage(), args.getResourceBundle());
    }

    private BotApiMethod<?> forwardHandler(Args args) {
        return commonHandler(args, ProtocolService::processForward);
    }

    private BotApiMethod<?> keywordHandler(Args args) {
        return commonHandler(args, ProtocolService::processKeyword);
    }

    private BotApiMethod<?> messageHandler(Args args) {
        return commonHandler(args, ProtocolService::processMessage);
    }

    private BotApiMethod<?> timeoutHandler(Args args) {
        return commonHandler(args, ProtocolService::processTimeout);
    }

    private BotApiMethod<?> correctHandler(Args args) {
        return commonHandler(args, ProtocolService::processCorrect);
    }

    private BotApiMethod<?> attemptHandler(Args args) {
        return commonHandler(args, ProtocolService::processAttempt);
    }

    private BotApiMethod<?> maximumHandler(Args args) {
        return commonHandler(args, ProtocolService::processMaximum);
    }

    private BotApiMethod<?> currencySingularHandler(Args args) {
        return commonHandler(args, ProtocolService::processCurrencySingular);
    }

    private BotApiMethod<?> currencyDualHandler(Args args) {
        return commonHandler(args, ProtocolService::processCurrencyDual);
    }

    private BotApiMethod<?> currencyPluralHandler(Args args) {
        return commonHandler(args, ProtocolService::processCurrencyPlural);
    }

    private BotApiMethod<?> commentHandler(Args args) {
        return commonHandler(args, ProtocolService::processComment);
    }

    private BotApiMethod<?> payHandler(Args args) {
        return commonHandler(args, ProtocolService::processPay);
    }

    @Override
    public BotApiMethod<?> process(Session session, Message message, ResourceBundle resourceBundle) {
        Function<Args, BotApiMethod<?>> handler = stateHandlers.get(session.getState());
        if (handler == null) {
            return null;
        }
        return handler.apply(new Args(session, message, resourceBundle));
    }
}
