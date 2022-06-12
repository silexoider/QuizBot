package ru.stn.telegram.tests.states.protocols.basics;

import org.telegram.telegrambots.meta.api.objects.Message;
import ru.stn.telegram.tests.states.entities.sessions.Session;

import java.util.ResourceBundle;

public interface State<C> {
    String getDescription(ResourceBundle resourceBundle);
    void prompt(Session session, C context, ResourceBundle resourceBundle);
    boolean handle(Session session, C context, Message message, ResourceBundle resourceBundle);
}
