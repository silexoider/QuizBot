package ru.stn.telegram.tests.states.protocols;

import org.telegram.telegrambots.meta.api.objects.Message;
import ru.stn.telegram.tests.states.entities.sessions.Session;
import ru.stn.telegram.tests.states.services.ProtocolService;

import java.util.ResourceBundle;

@FunctionalInterface
public interface StateConsumer {
    void accept(ProtocolService protocolService, Session session, Message message, ResourceBundle resourceBundle);
}
