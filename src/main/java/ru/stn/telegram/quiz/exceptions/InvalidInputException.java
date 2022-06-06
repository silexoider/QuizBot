package ru.stn.telegram.quiz.exceptions;

import ru.stn.telegram.quiz.services.LocalizationService;

public class InvalidInputException extends BotException {
    public InvalidInputException() {
        super(LocalizationService.Message.INVALID_INPUT);
    }
}
