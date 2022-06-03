package ru.stn.telegram.quiz.services.impl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import ru.stn.telegram.quiz.entities.Question;
import ru.stn.telegram.quiz.entities.Session;
import ru.stn.telegram.quiz.exceptions.OperationException;
import ru.stn.telegram.quiz.services.ActionService;
import ru.stn.telegram.quiz.services.CommandService;
import ru.stn.telegram.quiz.services.LocalizationService;
import ru.stn.telegram.quiz.services.SessionService;
import ru.stn.telegram.quiz.telegram.Config;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

@Service
public class CommandServiceImpl implements CommandService {
    @Getter
    @RequiredArgsConstructor
    private static class Args {
        private final Session session;
        private final Message message;
        private final String cleanText;
        private final ResourceBundle resourceBundle;
    }

    private static final String COMMAND_TYPE_VALUE = "bot_command";

    private final Config config;
    private final ActionService actionService;
    private final SessionService sessionService;
    private final LocalizationService localizationService;

    private final Map<String, Function<Args, BotApiMethod<?>>> commands = new HashMap<>();

    public CommandServiceImpl(Config config, ActionService actionService, SessionService sessionService, LocalizationService localizationService) {
        this.config = config;
        this.actionService = actionService;
        this.sessionService = sessionService;
        this.localizationService = localizationService;
        registerCommand("get_question", this::getQuestion);
        registerCommand("set_question", this::setQuestion);
        registerCommand("get_answer", this::getAnswer);
        registerCommand("set_answer", this::setAnswer);
    }

    private void registerCommand(String command, Function<Args, BotApiMethod<?>> handler) {
        commands.put(String.format("/%s", command), handler);
        commands.put(String.format("/%s@%s", command, config.getBotName()), handler);
    }

    private BotApiMethod<?> getQuestion(Args args) {
        String replyMessage;
        try {
            if (args.getMessage().getReplyToMessage() == null || args.getMessage().getReplyToMessage().getForwardFromChat() == null || !args.getMessage().getReplyToMessage().getForwardFromChat().getType().equals("channel")) {
                throw new OperationException(localizationService.getGetQuestionFailureReplyInvalidMessage(args.getResourceBundle()));
            }
            Long chatId = args.getMessage().getReplyToMessage().getForwardFromChat().getId();
            Integer postId = args.getMessage().getReplyToMessage().getForwardFromMessageId();
            if (chatId == null || postId == null) {
                throw new OperationException(localizationService.getGetQuestionFailureReplyInvalidMessage(args.getResourceBundle()));
            }
            Question question = actionService.getQuestion(chatId, postId, args.getMessage().getFrom().getId());
            if (question == null) {
                throw new OperationException(localizationService.getGetQuestionFailureReplyNoQuestion(args.getResourceBundle()));
            } else {
                replyMessage = String.format(localizationService.getGetQuestionSuccessReply(args.getResourceBundle()), question.getText());
            }
        } catch (OperationException e) {
            replyMessage = String.format(localizationService.getGetQuestionFailureReply(args.getResourceBundle()), e.getMessage());
        }
        return actionService.sendPrivateMessage(args.getMessage().getFrom().getId(), replyMessage);
    }

    private BotApiMethod<?> setQuestion(Args args) {
        if (args.getMessage().getChat().isUserChat() && args.getMessage().getReplyToMessage() == null) {
            sessionService.toSetQuestionExpectingForward(args.getSession(), args.getCleanText());
        }
        return null;
    }

    private BotApiMethod<?> getAnswer(Args args) {
        return null;
    }

    private BotApiMethod<?> setAnswer(Args args) {
        return null;
    }

    private String getCleanMessageText(String text, MessageEntity entity) {
        return text.substring(entity.getOffset() + entity.getLength()).replaceAll("^\\s+", "");
    }

    @Override
    public BotApiMethod<?> process(Session session, Message message, ResourceBundle resourceBundle) {
        Optional<MessageEntity> entity = message.getEntities() == null ? Optional.empty() : message.getEntities().stream().filter(e -> e.getType().equals(COMMAND_TYPE_VALUE)).findFirst();
        if (entity.isPresent()) {
            Function<Args, BotApiMethod<?>> handler = commands.get(entity.get().getText());
            if (handler != null) {
                return handler.apply(new Args(session, message, getCleanMessageText(message.getText(), entity.get()), resourceBundle));
            }
        }
        return null;
    }
}
