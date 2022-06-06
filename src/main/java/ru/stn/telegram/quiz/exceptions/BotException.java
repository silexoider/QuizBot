package ru.stn.telegram.quiz.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.stn.telegram.quiz.services.LocalizationService;

@RequiredArgsConstructor
public class BotException extends RuntimeException {
    @Getter
    private final LocalizationService.Message localizationMessage;
}
