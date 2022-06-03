package ru.stn.telegram.quiz.telegram;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.stn.telegram.quiz.services.CommandService;
import ru.stn.telegram.quiz.services.MessageService;

import java.util.Locale;
import java.util.ResourceBundle;

@Getter
@Component
@RequiredArgsConstructor
public class Bot extends TelegramLongPollingBot {
    private final Config config;
    private final MessageService messageService;

    @Override
    public void onUpdateReceived(Update update) {
        try {
            if (update.hasMessage()) {
                Message message = update.getMessage();
                BotApiMethod<?> response = messageService.process(message);
                if (response != null) {
                    execute(response);
                }
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getBotToken();
    }
}
