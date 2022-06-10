package ru.stn.telegram.tests.states.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.stn.telegram.tests.states.localization.Entry;

@RequiredArgsConstructor
public class BotException extends RuntimeException {
    @Getter
    private final Entry entry;
}
