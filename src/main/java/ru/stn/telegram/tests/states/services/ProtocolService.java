package ru.stn.telegram.tests.states.services;

import org.telegram.telegrambots.meta.api.objects.Message;
import ru.stn.telegram.tests.states.entities.sessions.Session;
import ru.stn.telegram.tests.states.protocols.Protocols;

import java.util.ResourceBundle;

public interface ProtocolService {
    <C> void start(Session session, C context, Protocols protocol, ResourceBundle resourceBundle);
    <C> void process(Session session, C context, Message message, ResourceBundle resourceBundle);
}
