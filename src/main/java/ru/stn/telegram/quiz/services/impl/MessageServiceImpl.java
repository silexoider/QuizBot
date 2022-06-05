package ru.stn.telegram.quiz.services.impl;

import com.sun.xml.bind.v2.model.runtime.RuntimeNonElementRef;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.glassfish.jersey.internal.util.Producer;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.stn.telegram.quiz.entities.Question;
import ru.stn.telegram.quiz.entities.Session;
import ru.stn.telegram.quiz.exceptions.InvalidFormatException;
import ru.stn.telegram.quiz.exceptions.OperationCancelledException;
import ru.stn.telegram.quiz.exceptions.OperationException;
import ru.stn.telegram.quiz.services.*;
import ru.stn.telegram.quiz.telegram.Config;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    @Getter
    @RequiredArgsConstructor
    private static class Args {
        private final Session session;
        private final Message message;
        private final ResourceBundle resourceBundle;
    }

    @FunctionalInterface
    interface SetQuestionAttributeFunction<T> {
        boolean apply(long chatId, int postId, T value);
    }

    private static final String LOCALIZATION_RESOURCE_FILE_NAME = "messages";

    private final Config config;
    private final ActionService actionService;
    private final SessionService sessionService;
    private final CommandService commandService;
    private final QuestionService questionService;
    private final LocalizationService localizationService;

    private final List<AbstractMap.Entry<Function<Message, Boolean>, BiFunction<Message, ResourceBundle, BotApiMethod<?>>>> globalHandlers = Arrays.asList(
            new AbstractMap.SimpleEntry<Function<Message, Boolean>, BiFunction<Message, ResourceBundle, BotApiMethod<?>>>(MessageServiceImpl.this::checkPrivate, MessageServiceImpl.this::processPrivate),
            new AbstractMap.SimpleEntry<Function<Message, Boolean>, BiFunction<Message, ResourceBundle, BotApiMethod<?>>>(MessageServiceImpl.this::checkPublic, MessageServiceImpl.this::processPublic)
    );

    private final Map<Session.State, Function<Args, BotApiMethod<?>>> stateHandlers = new HashMap<>() {{
        put(Session.State.DEFAULT, MessageServiceImpl.this::defaultHandler);
        put(Session.State.EXPECTING_KEYWORD, MessageServiceImpl.this::keywordStateHandler);
        put(Session.State.EXPECTING_MESSAGE, MessageServiceImpl.this::messageStateHandler);
        put(Session.State.EXPECTING_TIMEOUT, MessageServiceImpl.this::timeoutStateHandler);
        put(Session.State.EXPECTING_FORWARD, MessageServiceImpl.this::forwardStateHandler);
    }};
    private final Map<Session.Protocol, BiFunction<ActionService.Post, Args, BotApiMethod<?>>> protocolHandlers = new HashMap<>() {{
        put(Session.Protocol.FULL, MessageServiceImpl.this::fullProtocolHandler);
        put(Session.Protocol.KEYWORD, MessageServiceImpl.this::keywordProtocolHandler);
        put(Session.Protocol.MESSAGE, MessageServiceImpl.this::messageProtocolHandler);
        put(Session.Protocol.TIMEOUT, MessageServiceImpl.this::timeoutProtocolHandler);
        put(Session.Protocol.SHOW, MessageServiceImpl.this::showProtocolHandler);
    }};

    private ResourceBundle getResourceBundleByCode(String code) {
        Locale locale = code == null ? null : Locale.forLanguageTag(code);
        return locale == null ? ResourceBundle.getBundle(LOCALIZATION_RESOURCE_FILE_NAME) : ResourceBundle.getBundle(LOCALIZATION_RESOURCE_FILE_NAME, locale);
    }

    private BotApiMethod<?> defaultHandler(Args args) {
        return commandService.process(args.getSession(), args.getMessage(), args.getResourceBundle());
    }

    private boolean checkCommand(String text, String command) {
        return String.format("/%s", command).equals(text) || String.format("/%s@%s", command, config.getBotName()).equals(text);
    }
    private boolean checkCancelled(Message message) {
        if (message.getEntities() == null) {
            return false;
        }
        return message.getEntities().stream().filter(entity -> entity.getType().equals("bot_command") && checkCommand(entity.getText(), "cancel")).findFirst().isPresent();
    }
    private BotApiMethod<?> commonPartHandler(Runnable action, Args args) {
        try {
            if (checkCancelled(args.getMessage())) {
                throw new OperationCancelledException();
            }
            try { action.run(); }
            catch (RuntimeException e) {
                throw new InvalidFormatException();
            }
            Session.State next = sessionService.toNextState(args.getSession());
            return actionService.sendPrivateMessage(args.getMessage().getFrom().getId(), localizationService.getMessage(next.getPrompt(), args.getResourceBundle()));
        } catch (OperationCancelledException e) {
            sessionService.toDefault(args.getSession());
            return actionService.sendPrivateMessage(args.getMessage().getFrom().getId(), localizationService.getCancelled(args.getResourceBundle()));
        } catch (InvalidFormatException e) {
            sessionService.toDefault(args.getSession());
            return actionService.sendPrivateMessage(args.getMessage().getFrom().getId(), localizationService.getInvalidInput(args.getResourceBundle()));
        }
    }
    private BotApiMethod<?> keywordStateHandler(Args args) {
        return commonPartHandler(
                () -> sessionService.setKeyword(args.getSession(), args.getMessage().getText()),
                args
        );
    }
    private BotApiMethod<?> messageStateHandler(Args args) {
        return commonPartHandler(
                () -> sessionService.setMessage(args.getSession(), args.getMessage().getText()),
                args
        );
    }
    private BotApiMethod<?> timeoutStateHandler(Args args) {
        return commonPartHandler(
                () -> {
                    int hours = Integer.parseInt(args.getMessage().getText());
                    sessionService.setTimeout(args.getSession(), hours * 60 * 60);
                },
                args
        );
    }
    private BotApiMethod<?> forwardStateHandler(Args args) {
        try {
            if (checkCancelled(args.getMessage())) {
                throw new OperationCancelledException();
            }
            ActionService.Post post = actionService.getPrivatePost(args.getMessage());
            if (post == null) {
                throw new InvalidFormatException();
            }
            Session.Protocol protocol = args.getSession().getProtocol();
            return protocolHandlers.get(protocol).apply(post, args);
        } catch (OperationCancelledException e) {
            sessionService.toDefault(args.getSession());
            return actionService.sendPrivateMessage(args.getMessage().getFrom().getId(), localizationService.getCancelled(args.getResourceBundle()));
        } catch (InvalidFormatException e) {
            sessionService.toDefault(args.getSession());
            return actionService.sendPrivateMessage(args.getMessage().getFrom().getId(), localizationService.getInvalidInput(args.getResourceBundle()));
        }
    }

    private BotApiMethod<?> commonProtocolHandler(ActionService.Post post, Args args, Supplier<BotApiMethod<?>> action) {
        sessionService.toDefault(args.getSession());
        long userId = args.getMessage().getFrom().getId();
        if (!actionService.isSuperUser(post.getChatId(), userId)) {
            return actionService.sendPrivateMessage(userId, localizationService.getInsufficientPrivilege(args.getResourceBundle()));
        }
        BotApiMethod<?> result = null;
        try { result = action.get(); }
        catch (Exception e) {
            return actionService.sendPrivateMessage(userId, e.getMessage());
        }
        if (result == null) {
            return actionService.sendPrivateMessage(userId, localizationService.getSuccess(args.getResourceBundle()));
        } else {
            return result;
        }
    }
    private <T> BotApiMethod<?> commonPartProtocolHandler(ActionService.Post post, Args args, SetQuestionAttributeFunction<T> func, T value) {
        return commonProtocolHandler(
                post,
                args,
                () -> {
                    if (!func.apply(post.getChatId(), post.getPostId(), value))
                        throw new OperationException(localizationService.getPartFailure(args.getResourceBundle()));
                    return null;
                }
        );
    }
    private BotApiMethod<?> fullProtocolHandler(ActionService.Post post, Args args) {
        return commonProtocolHandler(
                post,
                args,
                () -> {
                    questionService.setQuestion(post.getChatId(), post.getPostId(), args.getSession().getKeyword(), args.getSession().getMessage(), args.getSession().getTimeout());
                    return null;
                }
        );
    }
    private BotApiMethod<?> keywordProtocolHandler(ActionService.Post post, Args args) {
        return commonPartProtocolHandler(post, args, questionService::setKeyword, args.getSession().getKeyword());
    }
    private BotApiMethod<?> messageProtocolHandler(ActionService.Post post, Args args) {
        return commonPartProtocolHandler(post, args, questionService::setMessage, args.getSession().getMessage());
    }
    private BotApiMethod<?> timeoutProtocolHandler(ActionService.Post post, Args args) {
        return commonPartProtocolHandler(post, args, questionService::setTimeout, args.getSession().getTimeout());
    }

    private BotApiMethod<?> showProtocolHandler(ActionService.Post post, Args args) {
        return commonProtocolHandler(
                post,
                args,
                () -> {
                    long userId = args.getMessage().getFrom().getId();
                    Question question = questionService.find(post.getChatId(), post.getPostId());
                    if (question == null) {
                        return actionService.sendPrivateMessage(userId, localizationService.getNoQuestion(args.getResourceBundle()));
                    } else {
                        return actionService.sendPrivateMessage(
                                userId,
                                String.format(
                                        localizationService.getQuestionFormat(args.getResourceBundle()),
                                        question.getKeyword(),
                                        question.getMessage(),
                                        question.getTimeout() / (60 * 60)
                                )
                        );
                    }
                }
        );
    }

    private boolean checkPrivate(Message message) {
        return actionService.checkPrivatePost(message);
    }
    private BotApiMethod<?> processPrivate(Message message, ResourceBundle resourceBundle) {
        Session session = sessionService.getSession(message.getFrom().getId());
        Function<Args, BotApiMethod<?>> handler = stateHandlers.get(session.getState());
        if (handler != null) {
            return handler.apply(new Args(session, message, resourceBundle));
        }
        return null;
    }
    private boolean checkPublic(Message message) {
        return actionService.checkPublicPost(message);
    }
    private BotApiMethod<?> processPublic(Message message, ResourceBundle resourceBundle) {
        ActionService.Post post = actionService.getPublicPost(message);
        if (post == null) {
            return null;
        }
        Question question = questionService.find(post.getChatId(), post.getPostId());
        if (question == null) {
            return null;
        }
        if (questionService.checkKeyword(question, message) && questionService.checkTimeout(question, message)) {
            return actionService.sendPrivateMessage(message.getFrom().getId(), question.getMessage());
        }
        return null;
    }

    @Override
    public BotApiMethod<?> process(Message message) {
        ResourceBundle resourceBundle = getResourceBundleByCode(message.getFrom().getLanguageCode());
        Optional<AbstractMap.Entry<Function<Message, Boolean>, BiFunction<Message, ResourceBundle, BotApiMethod<?>>>> found = globalHandlers.stream().filter(entry -> entry.getKey().apply(message)).findFirst();
        if (found.isPresent()) {
            return found.get().getValue().apply(message, resourceBundle);
        }
        return null;
    }
}
