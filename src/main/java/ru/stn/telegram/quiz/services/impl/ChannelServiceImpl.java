package ru.stn.telegram.quiz.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.stn.telegram.quiz.entities.Channel;
import ru.stn.telegram.quiz.repositories.ChannelRepository;
import ru.stn.telegram.quiz.services.ChannelService;

@Service
@RequiredArgsConstructor
public class ChannelServiceImpl implements ChannelService {
    private final ChannelRepository channelRepository;

    @Override
    public Channel find(long id) {
        return channelRepository.findById(id).orElse(null);
    }

    @Override
    public Channel get(long id) {
        Channel channel = find(id);
        if (channel == null) {
            channel = new Channel(id);
            channelRepository.save(channel);
        }
        return channel;
    }

    @Override
    public void setCurrency(Channel channel, String singular, String dual, String plural) {
        channel.setCurrencySingular(singular);
        channel.setCurrencyDual(dual);
        channel.setCurrencyPlural(plural);
        channelRepository.save(channel);
    }

    @Override
    public String getCurrency(Channel channel, int value) {
        if (value % 100 > 10 && value % 100 < 20) {
            return channel.getCurrencyPlural();
        }
        if (value % 10 == 1) {
            return channel.getCurrencySingular();
        }
        if (value % 10 >= 2 && value % 10 <= 4) {
            return channel.getCurrencyDual();
        }
        return channel.getCurrencyPlural();
    }

    @Override
    public String getValueInCurrency(Channel channel, int value) {
        String currency = getCurrency(channel, value);
        String format = currency == null || currency.length() == 0 ? "%d" : "%d %s";
        return String.format(format, value, currency);
    }
}
