package ru.stn.telegram.tests.states.services.impl;

import org.springframework.stereotype.Service;
import ru.stn.telegram.tests.states.entities.Channel;
import ru.stn.telegram.tests.states.repositories.ChannelRepository;
import ru.stn.telegram.tests.states.services.ChannelService;

@Service
public class ChannelServiceImpl extends BaseEntityServiceImpl<Channel, Long, ChannelRepository> implements ChannelService {
    public ChannelServiceImpl(ChannelRepository repository) {
        super(repository);
    }

    @Override
    public Channel find(long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public Channel get(long id) {
        Channel channel = find(id);
        if (channel == null) {
            channel = new Channel(id);
            repository.save(channel);
        }
        return channel;
    }

    @Override
    public void setCurrency(Channel channel, String singular, String dual, String plural) {
        channel.setCurrencySingular(singular);
        channel.setCurrencyDual(dual);
        channel.setCurrencyPlural(plural);
        repository.save(channel);
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
