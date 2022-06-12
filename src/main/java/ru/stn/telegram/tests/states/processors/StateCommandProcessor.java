package ru.stn.telegram.tests.states.processors;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.stn.telegram.tests.states.entities.sessions.Session;
import ru.stn.telegram.tests.states.localization.Entry;
import ru.stn.telegram.tests.states.localization.Localizer;
import ru.stn.telegram.tests.states.services.BotService;
import ru.stn.telegram.tests.states.services.ProtocolService;

import javax.annotation.PostConstruct;
import java.util.ResourceBundle;

@Component
public class StateCommandProcessor extends BotCommandProcessor<StateCommandProcessor.Args, StateCommandProcessor.Result> {
    public enum Result {
        IGNORED,
        CANCELLED,
        PROCESSED,
    }

    @Getter
    @RequiredArgsConstructor
    public static class Args implements BotCommandProcessor.Args {
        private final Session session;
        private final Message message;
        private final ResourceBundle resourceBundle;
    }

    @Autowired
    private Localizer localizer;
    @Autowired
    private BotService botService;
    @Autowired
    private ProtocolService protocolService;

    public StateCommandProcessor() {
        super(Result.IGNORED);
    }

    @PostConstruct
    private void init() {
        addHandler("cancel", this::cancel);
        addHandler("whereami", this::whereami);
    }

    private Result whereami(Args args) {
        botService.sendMessage(
                args.getMessage().getFrom().getId(),
                String.format(
                        localizer.localize(
                                Entry.WHEREAMI_FORMAT,
                                args.getResourceBundle()
                        ),
                        args.getSession().getProtocol().getCommand(),
                        protocolService.getProtocol(args.getSession().getProtocol().getName()).getNavigators().get(args.getSession().getState()).getState().getDescription(args.getResourceBundle())
                )
        );
        return Result.PROCESSED;
    }

    private Result cancel(Args args) {
        return Result.CANCELLED;
    }
}
