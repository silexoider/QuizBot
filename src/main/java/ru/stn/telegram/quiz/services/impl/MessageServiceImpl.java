package ru.stn.telegram.quiz.services.impl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.stn.telegram.quiz.entities.Session;
import ru.stn.telegram.quiz.exceptions.OperationException;
import ru.stn.telegram.quiz.services.*;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.BiFunction;
import java.util.function.Function;

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

    private static final String LOCALIZATION_RESOURCE_FILE_NAME = "messages";

    private final ActionService actionService;
    private final SessionService sessionService;
    private final CommandService commandService;
    private final LocalizationService localizationService;

    private final Map<Session.State, Function<Args, BotApiMethod<?>>> handlers = new HashMap<>() {{
        put(Session.State.DEFAULT, MessageServiceImpl.this::defaultHandler);
        put(Session.State.SET_QUESTION_EXPECTING_FORWARD, MessageServiceImpl.this::setQuestionExpectingForwardHandler);
    }};

    private ResourceBundle getResourceBundleByCode(String code) {
        Locale locale = code == null ? null : Locale.forLanguageTag(code);
        return locale == null ? ResourceBundle.getBundle(LOCALIZATION_RESOURCE_FILE_NAME) : ResourceBundle.getBundle(LOCALIZATION_RESOURCE_FILE_NAME, locale);
    }

    private BotApiMethod<?> defaultHandler(Args args) {
        return commandService.process(args.getSession(), args.getMessage(), args.getResourceBundle());
    }

    private BotApiMethod<?> setQuestionExpectingForwardHandler(Args args) {
        String replyMessage = localizationService.getSetQuestionSuccessReply(args.getResourceBundle());
        try {
            if (args.getMessage() == null || args.getMessage().getForwardFromChat() == null || !args.getMessage().getForwardFromChat().getType().equals("channel")) {
                throw new OperationException(localizationService.getSetQuestionFailureReplyInvalidMessage(args.getResourceBundle()));
            }
            Long chatId = args.getMessage().getForwardFromChat().getId();
            Integer postId = args.getMessage().getForwardFromMessageId();
            if (chatId == null || postId == null) {
                throw new OperationException(localizationService.getSetQuestionFailureReplyInvalidMessage(args.getResourceBundle()));
            }
            if (!actionService.setQuestion(chatId, postId, args.getMessage().getFrom().getId(), args.getSession().getText(), args.getResourceBundle())) {
                throw new OperationException(localizationService.getSetQuestionFailureReplyInsufficientPrivilege(args.getResourceBundle()));
            }
        } catch (OperationException e) {
            replyMessage = String.format(localizationService.getSetQuestionFailureReply(args.getResourceBundle()), e.getMessage());
        }
        BotApiMethod<?> result = actionService.sendPrivateMessage(args.getMessage().getFrom().getId(), replyMessage);
        sessionService.toDefault(args.getSession());
        return result;
    }

    @Override
    public BotApiMethod<?> process(Message message) {
        ResourceBundle resourceBundle = getResourceBundleByCode(message.getFrom().getLanguageCode());
        Session session = sessionService.getSession(message.getFrom().getId());
        Function<Args, BotApiMethod<?>> handler = handlers.get(session.getState());
        if (handler != null) {
            return handler.apply(new Args(session, message, resourceBundle));
        } else {
            return null;
        }
    }
}
