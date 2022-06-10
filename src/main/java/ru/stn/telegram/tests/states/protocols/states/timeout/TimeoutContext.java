package ru.stn.telegram.tests.states.protocols.states.timeout;

public interface TimeoutContext {
    int getTimeout();
    void setTimeout(int timeout);
}
