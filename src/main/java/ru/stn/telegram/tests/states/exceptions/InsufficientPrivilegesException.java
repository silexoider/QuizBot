package ru.stn.telegram.tests.states.exceptions;

import ru.stn.telegram.tests.states.localization.Entry;

public class InsufficientPrivilegesException extends BotException {
    public InsufficientPrivilegesException() {
        super(Entry.INSUFFICIENT_PRIVILEGES);
    }
}
