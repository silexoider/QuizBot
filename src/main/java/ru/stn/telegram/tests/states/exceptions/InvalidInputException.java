package ru.stn.telegram.tests.states.exceptions;

import ru.stn.telegram.tests.states.localization.Entry;

public class InvalidInputException extends BotException {
    public InvalidInputException() {
        super(Entry.INVALID_INPUT);
    }
}
