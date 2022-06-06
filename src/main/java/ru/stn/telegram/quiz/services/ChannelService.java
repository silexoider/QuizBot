package ru.stn.telegram.quiz.services;

import ru.stn.telegram.quiz.entities.Channel;

public interface ChannelService {
    Channel find(long id);
    Channel get(long id);
    void setCurrency(Channel channel, String singular, String dual, String plural);
    String getCurrency(Channel channel, int value);
    String getValueInCurrency(Channel channel, int value);
}
