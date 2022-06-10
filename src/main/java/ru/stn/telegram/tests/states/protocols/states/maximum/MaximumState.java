package ru.stn.telegram.tests.states.protocols.states.maximum;

import org.springframework.stereotype.Component;
import ru.stn.telegram.tests.states.localization.Entry;
import ru.stn.telegram.tests.states.protocols.states.BaseIntegerState;

@Component
public class MaximumState extends BaseIntegerState<MaximumContext> {
    public MaximumState() {
        super(Entry.MAXIMUM_PROMPT, MaximumContext::setMaximum);
    }
}
