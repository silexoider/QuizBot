package ru.stn.telegram.tests.states.protocols.states.message;

import org.springframework.stereotype.Component;
import ru.stn.telegram.tests.states.localization.Entry;
import ru.stn.telegram.tests.states.protocols.states.BaseStringState;

@Component
public class MessageState extends BaseStringState<MessageContext> {
    public MessageState() {
        super(Entry.MESSAGE_PROMPT, MessageContext::setMessage);
    }
}
