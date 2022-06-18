package ru.stn.telegram.tests.states.protocols.states.forward;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;

import java.util.Arrays;
import java.util.List;

@Component
public class PrivateForwardState extends ForwardState {
    private static final List<String> SUPER_USER_ROLE_NAMES = Arrays.asList("creator", "administrator");

    private boolean isSuperUser(ChatMember chatMember) {
        return SUPER_USER_ROLE_NAMES.contains(chatMember.getStatus());
    }

    @Override
    protected boolean checkChatMember(ChatMember chatMember) {
        return isSuperUser(chatMember);
    }
}
