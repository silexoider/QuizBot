package ru.stn.telegram.tests.states.telegram;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.stn.telegram.tests.states.processors.BotCommandProcessor;
import ru.stn.telegram.tests.states.processors.DefaultCommandProcessor;
import ru.stn.telegram.tests.states.processors.MessageProcessor;

import java.util.Locale;
import java.util.ResourceBundle;

@Getter
@Component
@RequiredArgsConstructor
public class Bot extends TelegramLongPollingBot {
    @Autowired
    private Config config;
    @Autowired
    private MessageProcessor messageProcessor;

    private ResourceBundle getResourceBundle(Message message) {
        String languageCode = message.getFrom().getLanguageCode();
        Locale locale = languageCode == null ? null : Locale.forLanguageTag(languageCode);
        ResourceBundle resourceBundle = ResourceBundle.getBundle("messages", locale);
        return resourceBundle;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (!update.hasMessage()) {
            return;
        }
        Message message = update.getMessage();
        ResourceBundle resourceBundle = getResourceBundle(message);
        messageProcessor.process(message, resourceBundle);
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
