package ru.stn.telegram.quiz.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.stereotype.Service;
import ru.stn.telegram.quiz.entities.Session;
import ru.stn.telegram.quiz.services.ProtocolManagerService;
import ru.stn.telegram.quiz.services.ProtocolService;

@Service
@RequiredArgsConstructor
public class ProtocolManagerServiceImpl implements ProtocolManagerService {
    private final BeanFactory beanFactory;

    @Override
    public ProtocolService getProtocolService(Session.Protocol protocol) {
        return beanFactory.getBean(String.format("%sProtocol", protocol.getName()), ProtocolService.class);
    }
}
