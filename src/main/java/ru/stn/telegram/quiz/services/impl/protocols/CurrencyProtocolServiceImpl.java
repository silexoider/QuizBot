package ru.stn.telegram.quiz.services.impl.protocols;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import ru.stn.telegram.quiz.entities.Channel;
import ru.stn.telegram.quiz.entities.Session;
import ru.stn.telegram.quiz.services.*;
import ru.stn.telegram.quiz.telegram.Config;

import java.util.ResourceBundle;

@Service("CurrencyProtocol")
public class CurrencyProtocolServiceImpl extends CommonProtocolServiceImpl {
    private final ChannelService channelService;

    public CurrencyProtocolServiceImpl(Config config, ActionService actionService, SessionService sessionService, LocalizationService localizationService, ChannelService channelService) {
        super(Session.State.FORWARD, config, actionService, sessionService, localizationService);
        this.channelService = channelService;
        transitions.put(Session.State.FORWARD, Session.State.CURRENCY_SINGULAR);
        transitions.put(Session.State.CURRENCY_SINGULAR, Session.State.CURRENCY_DUAL);
        transitions.put(Session.State.CURRENCY_DUAL, Session.State.CURRENCY_PLURAL);
        transitions.put(Session.State.CURRENCY_PLURAL, Session.State.DEFAULT);
    }

    @Override
    protected BotApiMethod<?> commit(Session session, ResourceBundle resourceBundle) {
        Channel channel = channelService.get(session.getChatId());
        channelService.setCurrency(channel, session.getCurrencySingular(), session.getCurrencyDual(), session.getCurrencyPlural());
        return actionService.sendPrivateMessage(session.getUserId(), localizationService.getSuccess(resourceBundle));
    }
}
