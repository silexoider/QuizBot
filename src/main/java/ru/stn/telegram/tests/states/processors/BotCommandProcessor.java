package ru.stn.telegram.tests.states.processors;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import ru.stn.telegram.tests.states.telegram.Config;

import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class BotCommandProcessor<R> extends CommandProcessor<BotCommandProcessor.Args, R> {
    @Getter
    @RequiredArgsConstructor
    public static class Args {
        private final Message message;
        private final ResourceBundle resourceBundle;
    }

    private final String BOT_COMMAND_TYPE_VALUE = "bot_command";

    @Autowired
    private Config config;

    public BotCommandProcessor(R defaultResult) {
        super(defaultResult);
    }

    @Override
    protected List<String> getCommandVariants(String command) {
        return Arrays.asList(
                String.format("/%s", command),
                String.format("/%s@%s", command, config.getBotName())
        );
    }

    @Override
    protected String getCommandText(Args args) {
        if (args.getMessage().getEntities() == null) {
            return null;
        }
        return args.getMessage().getEntities().stream().filter(entity -> entity.getType().equals(BOT_COMMAND_TYPE_VALUE)).map(MessageEntity::getText).findFirst().orElse(null);
    }
}
