package ru.stn.telegram.quiz.exceptions;

import ru.stn.telegram.quiz.services.LocalizationService;

public class PartFailureException extends BotException {
    public PartFailureException() {
        super(LocalizationService.Message.PART_FAILURE);
    }
}
