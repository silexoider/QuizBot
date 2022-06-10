package ru.stn.telegram.tests.states.protocols.basics;

import ru.stn.telegram.tests.states.entities.sessions.Session;

import java.util.List;
import java.util.ResourceBundle;

public interface Protocol<C> {
    List<Navigator<? super C, C>> getNavigators();
    void commit(Session session, C context, ResourceBundle resourceBundle);
}
