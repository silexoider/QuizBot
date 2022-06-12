package ru.stn.telegram.tests.states.protocols.basics;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.stn.telegram.tests.states.entities.sessions.Session;
import ru.stn.telegram.tests.states.exceptions.CancelledException;
import ru.stn.telegram.tests.states.exceptions.InsufficientPrivilegesException;
import ru.stn.telegram.tests.states.exceptions.InvalidInputException;
import ru.stn.telegram.tests.states.localization.Entry;
import ru.stn.telegram.tests.states.localization.Localizer;
import ru.stn.telegram.tests.states.services.BotService;

import java.util.ResourceBundle;

@RequiredArgsConstructor
public class BaseNavigator<S, C extends S> implements Navigator<S, C> {
    @Getter
    private final State<S> state;
    protected final BotService botService;
    protected final Localizer localizer;

    @Override
    public Transition<S, C> navigate(Session session, C context, Message message, ResourceBundle resourceBundle) {
        Entry feedbackEntry = null;
        String feedbackMessage = null;
        boolean result = true;
        Transition<S, C> transition;
        try {
            result = state.handle(session, context, message, resourceBundle);
            transition = Transition.next();
        } catch (CancelledException e) {
            feedbackEntry = Entry.CANCELLED;
            transition = Transition.cancel();
        } catch (InvalidInputException e) {
            feedbackEntry = Entry.INVALID_INPUT;
            transition = Transition.self();
        } catch (InsufficientPrivilegesException e) {
            feedbackEntry = Entry.INSUFFICIENT_PRIVILEGES;
            transition = Transition.self();
        } catch (Exception e) {
            feedbackMessage = e.getMessage();
            transition = Transition.cancel();
        }
        if (feedbackEntry != null) {
            feedbackMessage = localizer.localize(feedbackEntry, resourceBundle);
        } else {
            if (feedbackMessage != null) {
                feedbackMessage = String.format(
                        localizer.localize(Entry.UNEXPECTED_ERROR_FORMAT, resourceBundle),
                        feedbackMessage
                );
            }
        }
        if (feedbackMessage == null) {
            if (result) {
                transition = check(transition, session, context, message, resourceBundle);
            } else {
                transition = Transition.self();
            }
        } else {
            botService.sendMessage(message.getFrom().getId(), feedbackMessage);
        }
        return transition;
    }

    protected Transition<S, C> check(Transition<S, C> transition, Session session, C context, Message message, ResourceBundle resourceBundle) {
        return transition;
    }
}
