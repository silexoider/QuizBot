package ru.stn.telegram.tests.states.protocols.states.timeout;

import org.springframework.stereotype.Component;
import ru.stn.telegram.tests.states.localization.Entry;
import ru.stn.telegram.tests.states.protocols.states.BaseIntegerState;

@Component
public class TimeoutState extends BaseIntegerState<TimeoutContext> {
    public TimeoutState() {
        super(Entry.TIMEOUT_PROMPT, (c, t) -> c.setTimeout(t * 60 * 60));
    }
}
