package ru.stn.telegram.quiz.telegram;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class Config {
    @Value("${bot.name}")
    private String botName;
    @Value("${bot.token}")
    private String botToken;

    @Value("${bot.question.correct}")
    private int correct;
    @Value("${bot.question.attempt}")
    private int attempt;
    @Value("${bot.question.maximum}")
    private int maximum;
}
