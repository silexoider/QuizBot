package ru.stn.telegram.quiz.services.impl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.stn.telegram.quiz.entities.Session;
import ru.stn.telegram.quiz.exceptions.InvalidFormatException;
import ru.stn.telegram.quiz.exceptions.OperationCancelledException;
import ru.stn.telegram.quiz.services.*;
import ru.stn.telegram.quiz.telegram.Config;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.function.Supplier;

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

    private final SessionService sessionService;
    private final CommandService commandService;
    private final ProtocolManagerService protocolManagerService;

    private final Map<Session.State, Function<Args, BotApiMethod<?>>> stateHandlers = new HashMap<>() {{
        put(Session.State.DEFAULT, StateServiceImpl.this::defaultHandler);
        put(Session.State.FORWARD, StateServiceImpl.this::forwardHandler);
        put(Session.State.KEYWORD, StateServiceImpl.this::keywordHandler);
        put(Session.State.MESSAGE, StateServiceImpl.this::messageHandler);
        put(Session.State.TIMEOUT, StateServiceImpl.this::timeoutHandler);
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

    @Override
    public BotApiMethod<?> process(Session session, Message message, ResourceBundle resourceBundle) {
        Function<Args, BotApiMethod<?>> handler = stateHandlers.get(session.getState());
        if (handler == null) {
            return null;
        }
        return handler.apply(new Args(session, message, resourceBundle));
    }
}