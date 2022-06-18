package ru.stn.telegram.tests.states.protocols.states.forward;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;

@Component
public class PublicForwardState extends ForwardState {
    @Override
    protected boolean checkChatMember(ChatMember chatMember) {
        return true;
    }
}
