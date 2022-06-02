package ru.stn.telegram.quiz.telegram;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Getter
@Component
@RequiredArgsConstructor
public class Bot extends TelegramLongPollingBot {
    private final Config config;

    @Override
    public void onUpdateReceived(Update update) {
        System.out.println("Update!");
        if (update.hasMessage()) {
            Message message = update.getMessage();
            System.out.println("  Message!");
            System.out.format("    Text: %s%n", message.getText());
            List<MessageEntity> entities = message.getEntities();
            if (entities == null) {
                System.out.println("  No entities:");
            } else {
                System.out.println("  Has entities:");
                for (MessageEntity entity : entities) {
                    System.out.format("      type = %s; text = %s%n", entity.getType(), entity.getText());
                }
            }
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
