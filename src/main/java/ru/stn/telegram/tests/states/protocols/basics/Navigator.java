package ru.stn.telegram.tests.states.protocols.basics;

import org.telegram.telegrambots.meta.api.objects.Message;
import ru.stn.telegram.tests.states.entities.sessions.Session;

import java.util.ResourceBundle;

public interface Navigator<S, C extends S> {
    State<S> getState();
    Transition<S, C> navigate(Session session, C context, Message message, ResourceBundle resourceBundle);
}
