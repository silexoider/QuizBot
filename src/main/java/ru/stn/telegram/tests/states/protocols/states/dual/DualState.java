package ru.stn.telegram.tests.states.protocols.states.dual;

import org.springframework.stereotype.Component;
import ru.stn.telegram.tests.states.localization.Entry;
import ru.stn.telegram.tests.states.protocols.states.BaseStringState;

@Component
public class DualState extends BaseStringState<DualContext> {
    public DualState() {
        super(Entry.DUAL_PROMPT, DualContext::setDual);
    }
}
