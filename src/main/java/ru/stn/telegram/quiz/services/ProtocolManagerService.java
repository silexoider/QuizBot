package ru.stn.telegram.quiz.services;

import ru.stn.telegram.quiz.entities.Session;

public interface ProtocolManagerService {
    ProtocolService getProtocolService(Session.Protocol protocol);
}
