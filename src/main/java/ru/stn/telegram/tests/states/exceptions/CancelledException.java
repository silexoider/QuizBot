package ru.stn.telegram.tests.states.exceptions;

import ru.stn.telegram.tests.states.localization.Entry;

public class CancelledException extends BotException {
    public CancelledException() {
        super(Entry.CANCELLED);
    }
}
