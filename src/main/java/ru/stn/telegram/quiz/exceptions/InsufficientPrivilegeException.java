package ru.stn.telegram.quiz.exceptions;

import ru.stn.telegram.quiz.services.LocalizationService;

public class InsufficientPrivilegeException extends BotException {
    public InsufficientPrivilegeException() {
        super(LocalizationService.Message.INSUFFICIENT_PRIVILEGE);
    }
}
