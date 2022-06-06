package ru.stn.telegram.quiz.exceptions;

import ru.stn.telegram.quiz.services.LocalizationService;

public class OperationCancelledException extends BotException {
    public OperationCancelledException() {
        super(LocalizationService.Message.CANCELLED);
    }
}
