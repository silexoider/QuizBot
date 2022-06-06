package ru.stn.telegram.quiz.services.impl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import ru.stn.telegram.quiz.entities.Session;
import ru.stn.telegram.quiz.services.*;
import ru.stn.telegram.quiz.telegram.Config;

import java.util.*;
import java.util.function.Function;

@Service("Private")
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
    private final ProtocolManagerService protocolManagerService;

    private final Map<String, Function<Args, BotApiMethod<?>>> commands = new HashMap<>();

    public CommandServiceImpl(Config config, ActionService actionService, SessionService sessionService, LocalizationService localizationService, ProtocolManagerService protocolManagerService) {
        this.config = config;
        this.actionService = actionService;
        this.sessionService = sessionService;
        this.localizationService = localizationService;
        this.protocolManagerService = protocolManagerService;
        registerCommand("question_brief", this::questionBrief);
        registerCommand("question_full", this::questionFull);
        registerCommand("keyword", this::keyword);
        registerCommand("message", this::message);
        registerCommand("timeout", this::timeout);
        registerCommand("show", this::show);
    }

    private void registerCommand(String command, Function<Args, BotApiMethod<?>> handler) {
        commands.put(String.format("/%s", command), handler);
        commands.put(String.format("/%s@%s", command, config.getBotName()), handler);
    }

    private String getCleanMessageText(String text, MessageEntity entity) {
        return text.substring(entity.getOffset() + entity.getLength()).replaceAll("^\\s+", "");
    }

    private BotApiMethod<?> commonStateCommandHandler(Session.Protocol protocol, Args args) {
        if (!actionService.checkPrivatePost(args.getMessage())) {
            return null;
        }
        long userId = args.getMessage().getFrom().getId();
        Session session = sessionService.getSession(userId);
        sessionService.setProtocol(session, protocol);
        ProtocolService protocolService = protocolManagerService.getProtocolService(protocol);
        Session.State state = protocolService.getInitialState();
        sessionService.setState(session, state);
        return actionService.sendPrivateMessage(userId, localizationService.getMessage(state.getPrompt(), args.getResourceBundle()));
    }

    private BotApiMethod<?> questionBrief(Args args) {
        return commonStateCommandHandler(Session.Protocol.BRIEF, args);
    }
    private BotApiMethod<?> questionFull(Args args) {
        return commonStateCommandHandler(Session.Protocol.FULL, args);
    }
    private BotApiMethod<?> keyword(Args args) {
        return commonStateCommandHandler(Session.Protocol.KEYWORD, args);
    }
    private BotApiMethod<?> message(Args args) {
        return commonStateCommandHandler(Session.Protocol.MESSAGE, args);
    }
    private BotApiMethod<?> timeout(Args args) {
        return commonStateCommandHandler(Session.Protocol.TIMEOUT, args);
    }

    private BotApiMethod<?> show(Args args) {
        return commonStateCommandHandler(Session.Protocol.SHOW, args);
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
