package ru.stn.telegram.quiz.services.impl.protocols;

import lombok.Getter;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.stn.telegram.quiz.entities.Session;
import ru.stn.telegram.quiz.exceptions.InvalidFormatException;
import ru.stn.telegram.quiz.exceptions.OperationCancelledException;
import ru.stn.telegram.quiz.services.ActionService;
import ru.stn.telegram.quiz.services.LocalizationService;
import ru.stn.telegram.quiz.services.ProtocolService;
import ru.stn.telegram.quiz.services.SessionService;
import ru.stn.telegram.quiz.telegram.Config;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Supplier;

public abstract class CommonProtocolServiceImpl implements ProtocolService {
    @Getter
    private final Session.State initialState;

    protected final Config config;
    protected final ActionService actionService;
    protected final SessionService sessionService;
    protected final LocalizationService localizationService;

    protected final Map<Session.State, Session.State> transitions = new HashMap<>() {{
        put(Session.State.FORWARD, Session.State.KEYWORD);
        put(Session.State.KEYWORD, Session.State.MESSAGE);
        put(Session.State.MESSAGE, Session.State.TIMEOUT);
        put(Session.State.TIMEOUT, Session.State.DEFAULT);
    }};

    public CommonProtocolServiceImpl(Session.State initialState, Config config, ActionService actionService, SessionService sessionService, LocalizationService localizationService) {
        this.initialState = initialState;
        this.config = config;
        this.actionService = actionService;
        this.sessionService = sessionService;
        this.localizationService = localizationService;
    }

    private boolean checkCommand(String text, String command) {
        return String.format("/%s", command).equals(text) || String.format("/%s@%s", command, config.getBotName()).equals(text);
    }
    private boolean checkCancelled(Message message) {
        if (message.getEntities() == null) {
            return false;
        }
        return message.getEntities().stream().anyMatch(entity -> entity.getType().equals("bot_command") && checkCommand(entity.getText(), "cancel"));
    }
    private BotApiMethod<?> commonStateHandler(Supplier<Session.State> action, Session session, Message message, ResourceBundle resourceBundle) {
        try {
            Session.State next = null;
            if (checkCancelled(message)) {
                throw new OperationCancelledException();
            }
            try { next = action.get(); }
            catch (RuntimeException e) {
                throw new InvalidFormatException();
            }
            sessionService.setState(session, next);
            if (next == Session.State.DEFAULT) {
                return commit(session, resourceBundle);
            } else {
                return actionService.sendPrivateMessage(message.getFrom().getId(), localizationService.getMessage(next.getPrompt(), resourceBundle));
            }
        } catch (OperationCancelledException e) {
            sessionService.toDefault(session);
            return actionService.sendPrivateMessage(message.getFrom().getId(), localizationService.getCancelled(resourceBundle));
        } catch (InvalidFormatException e) {
            sessionService.toDefault(session);
            return actionService.sendPrivateMessage(message.getFrom().getId(), localizationService.getInvalidInput(resourceBundle));
        }
    }

    @Override
    public BotApiMethod<?> processForward(Session session, Message message, ResourceBundle resourceBundle) {
        return commonStateHandler(() -> processForwardInternal(session, message, resourceBundle), session, message, resourceBundle);
    }

    @Override
    public BotApiMethod<?> processKeyword(Session session, Message message, ResourceBundle resourceBundle) {
        return commonStateHandler(() -> processKeywordInternal(session, message, resourceBundle), session, message, resourceBundle);
    }

    @Override
    public BotApiMethod<?> processMessage(Session session, Message message, ResourceBundle resourceBundle) {
        return commonStateHandler(() -> processMessageInternal(session, message, resourceBundle), session, message, resourceBundle);
    }

    @Override
    public BotApiMethod<?> processTimeout(Session session, Message message, ResourceBundle resourceBundle) {
        return commonStateHandler(() -> processTimeoutInternal(session, message, resourceBundle), session, message, resourceBundle);
    }

    protected Session.State processForwardInternal(Session session, Message message, ResourceBundle resourceBundle) {
        ActionService.Post post = actionService.getPrivatePost(message);
        if (post == null) {
            throw new InvalidFormatException();
        }
        sessionService.setForward(session, post.getChatId(), post.getPostId());
        return transitions.get(Session.State.FORWARD);
    }

    protected Session.State processKeywordInternal(Session session, Message message, ResourceBundle resourceBundle) {
        sessionService.setKeyword(session, message.getText());
        return transitions.get(Session.State.KEYWORD);
    }

    protected Session.State processMessageInternal(Session session, Message message, ResourceBundle resourceBundle) {
        sessionService.setMessage(session, message.getText());
        return transitions.get(Session.State.MESSAGE);
    }

    protected Session.State processTimeoutInternal(Session session, Message message, ResourceBundle resourceBundle) {
        int timeout = Integer.parseInt(message.getText()) * 60 * 60;
        sessionService.setTimeout(session, timeout);
        return transitions.get(Session.State.TIMEOUT);
    }

    protected abstract BotApiMethod<?> commit(Session session, ResourceBundle resourceBundle);
}
