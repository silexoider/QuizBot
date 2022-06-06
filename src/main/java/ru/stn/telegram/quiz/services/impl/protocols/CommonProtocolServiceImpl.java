package ru.stn.telegram.quiz.services.impl.protocols;

import lombok.Getter;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.stn.telegram.quiz.entities.Session;
import ru.stn.telegram.quiz.exceptions.BotException;
import ru.stn.telegram.quiz.exceptions.InsufficientPrivilegeException;
import ru.stn.telegram.quiz.exceptions.InvalidInputException;
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
        put(Session.State.TIMEOUT, Session.State.CORRECT);
        put(Session.State.CORRECT, Session.State.ATTEMPT);
        put(Session.State.ATTEMPT, Session.State.MAXIMUM);
        put(Session.State.MAXIMUM, Session.State.DEFAULT);
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
            if (checkCancelled(message)) {
                throw new OperationCancelledException();
            }
            Session.State next = null;
            try { next = action.get(); }
            catch (InsufficientPrivilegeException e) {
                throw e;
            } catch (BotException e) {
                throw e;
            } catch (RuntimeException e) {
                throw new InvalidInputException();
            }
            sessionService.setState(session, next);
            if (next == Session.State.DEFAULT) {
                return commit(session, resourceBundle);
            } else {
                return actionService.sendPrivateMessage(message.getFrom().getId(), localizationService.getMessage(next.getPrompt(), resourceBundle));
            }
        } catch (BotException e) {
            sessionService.toDefault(session);
            return actionService.sendPrivateMessage(message.getFrom().getId(), localizationService.getMessage(e.getLocalizationMessage(), resourceBundle));
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

    @Override
    public BotApiMethod<?> processCorrect(Session session, Message message, ResourceBundle resourceBundle) {
        return commonStateHandler(() -> processCorrectInternal(session, message, resourceBundle), session, message, resourceBundle);
    }

    @Override
    public BotApiMethod<?> processAttempt(Session session, Message message, ResourceBundle resourceBundle) {
        return commonStateHandler(() -> processAttemptInternal(session, message, resourceBundle), session, message, resourceBundle);
    }

    @Override
    public BotApiMethod<?> processMaximum(Session session, Message message, ResourceBundle resourceBundle) {
        return commonStateHandler(() -> processMaximumInternal(session, message, resourceBundle), session, message, resourceBundle);
    }

    @Override
    public BotApiMethod<?> processCurrencySingular(Session session, Message message, ResourceBundle resourceBundle) {
        return commonStateHandler(() -> processCurrencySingularInternal(session, message, resourceBundle), session, message, resourceBundle);
    }

    @Override
    public BotApiMethod<?> processCurrencyDual(Session session, Message message, ResourceBundle resourceBundle) {
        return commonStateHandler(() -> processCurrencyDualInternal(session, message, resourceBundle), session, message, resourceBundle);
    }

    @Override
    public BotApiMethod<?> processCurrencyPlural(Session session, Message message, ResourceBundle resourceBundle) {
        return commonStateHandler(() -> processCurrencyPluralInternal(session, message, resourceBundle), session, message, resourceBundle);
    }

    @Override
    public BotApiMethod<?> processComment(Session session, Message message, ResourceBundle resourceBundle) {
        return commonStateHandler(() -> processCommentInternal(session, message, resourceBundle), session, message, resourceBundle);
    }

    @Override
    public BotApiMethod<?> processPay(Session session, Message message, ResourceBundle resourceBundle) {
        return commonStateHandler(() -> processPayInternal(session, message, resourceBundle), session, message, resourceBundle);
    }

    protected ActionService.Post checkForward(Session session, Message message, ResourceBundle resourceBundle) {
        ActionService.Post post = actionService.getPrivatePost(message);
        if (post == null) {
            throw new InvalidInputException();
        }
        if (!actionService.isSuperUser(post.getChatId(), message.getFrom().getId())) {
            throw new InsufficientPrivilegeException();
        }
        return post;
    }

    protected Session.State processForwardInternal(Session session, Message message, ResourceBundle resourceBundle) {
        ActionService.Post post = checkForward(session, message, resourceBundle);
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

    protected Session.State processCorrectInternal(Session session, Message message, ResourceBundle resourceBundle) {
        int timeout = Integer.parseInt(message.getText());
        sessionService.setCorrect(session, timeout);
        return transitions.get(Session.State.CORRECT);
    }

    protected Session.State processAttemptInternal(Session session, Message message, ResourceBundle resourceBundle) {
        int timeout = Integer.parseInt(message.getText());
        sessionService.setAttempt(session, timeout);
        return transitions.get(Session.State.ATTEMPT);
    }

    protected Session.State processMaximumInternal(Session session, Message message, ResourceBundle resourceBundle) {
        int timeout = Integer.parseInt(message.getText());
        sessionService.setMaximum(session, timeout);
        return transitions.get(Session.State.MAXIMUM);
    }

    protected Session.State processCurrencySingularInternal(Session session, Message message, ResourceBundle resourceBundle) {
        sessionService.setCurrencySingular(session, message.getText());
        return transitions.get(Session.State.CURRENCY_SINGULAR);
    }

    protected Session.State processCurrencyDualInternal(Session session, Message message, ResourceBundle resourceBundle) {
        sessionService.setCurrencyDual(session, message.getText());
        return transitions.get(Session.State.CURRENCY_DUAL);
    }

    protected Session.State processCurrencyPluralInternal(Session session, Message message, ResourceBundle resourceBundle) {
        sessionService.setCurrencyPlural(session, message.getText());
        return transitions.get(Session.State.CURRENCY_PLURAL);
    }

    protected Session.State processCommentInternal(Session session, Message message, ResourceBundle resourceBundle) {
        sessionService.setCommentUserId(session, message.getFrom().getId());
        return transitions.get(Session.State.COMMENT);
    }

    protected Session.State processPayInternal(Session session, Message message, ResourceBundle resourceBundle) {
        int amount = Integer.parseInt(message.getText());
        sessionService.setPayAmount(session, amount);
        return transitions.get(Session.State.PAY);
    }

    protected abstract BotApiMethod<?> commit(Session session, ResourceBundle resourceBundle);
}
