package ru.stn.telegram.tests.states.protocols.states.singular;

import org.springframework.stereotype.Component;
import ru.stn.telegram.tests.states.localization.Entry;
import ru.stn.telegram.tests.states.protocols.states.BaseStringState;

@Component
public class SingularState extends BaseStringState<SingularContext> {
    public SingularState() {
        super(Entry.SINGULAR_PROMPT, SingularContext::setSingular);
    }
}
