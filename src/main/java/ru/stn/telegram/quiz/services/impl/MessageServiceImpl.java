package ru.stn.telegram.quiz.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.stn.telegram.quiz.entities.Answer;
import ru.stn.telegram.quiz.entities.Channel;
import ru.stn.telegram.quiz.entities.Question;
import ru.stn.telegram.quiz.entities.Session;
import ru.stn.telegram.quiz.services.*;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private static final String LOCALIZATION_RESOURCE_FILE_NAME = "messages";

    private final StateService stateService;
    private final AnswerService answerService;
    private final ActionService actionService;
    private final SessionService sessionService;
    private final ChannelService channelService;
    private final QuestionService questionService;
    private final LocalizationService localizationService;

    private final List<AbstractMap.Entry<Function<Message, Boolean>, BiFunction<Message, ResourceBundle, BotApiMethod<?>>>> globalHandlers = Arrays.asList(
            new AbstractMap.SimpleEntry<Function<Message, Boolean>, BiFunction<Message, ResourceBundle, BotApiMethod<?>>>(MessageServiceImpl.this::checkPrivate, MessageServiceImpl.this::processPrivate),
            new AbstractMap.SimpleEntry<Function<Message, Boolean>, BiFunction<Message, ResourceBundle, BotApiMethod<?>>>(MessageServiceImpl.this::checkPublic, MessageServiceImpl.this::processPublic)
    );

    private ResourceBundle getResourceBundleByCode(String code) {
        Locale locale = code == null ? null : Locale.forLanguageTag(code);
        return locale == null ? ResourceBundle.getBundle(LOCALIZATION_RESOURCE_FILE_NAME) : ResourceBundle.getBundle(LOCALIZATION_RESOURCE_FILE_NAME, locale);
    }

    private boolean checkPrivate(Message message) {
        return actionService.checkPrivatePost(message);
    }
    private BotApiMethod<?> processPrivate(Message message, ResourceBundle resourceBundle) {
        Session session = sessionService.getSession(message.getFrom().getId());
        return stateService.process(session, message, resourceBundle);
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
        if (questionService.checkTimeout(question, message)) {
            Answer answer = answerService.get(post.getChatId(), message.getFrom().getId(), post.getPostId());
            if (questionService.checkKeyword(question, message) && answerService.processCorrect(answer, question)) {
                return actionService.sendPrivateMessage(
                        message.getFrom().getId(),
                        getSuccessMessageText(question, answer, channelService.get(post.getChatId()), resourceBundle)
                );
            } else {
                answerService.processAttempt(answer, question);
            }
        }
        return null;
    }

    private String getSuccessMessageText(Question question, Answer answer, Channel channel, ResourceBundle resourceBundle) {
        return String.format(
                localizationService.getSuccessNotificationFormat(resourceBundle),
                channelService.getValueInCurrency(channel, question.getCorrect()),
                channelService.getValueInCurrency(channel, answerService.getChatBalance(answer)),
                question.getMessage()
        );
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
