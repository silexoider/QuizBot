package ru.stn.telegram.tests.states.protocols.states.attempt;

import org.springframework.stereotype.Component;
import ru.stn.telegram.tests.states.localization.Entry;
import ru.stn.telegram.tests.states.protocols.states.BaseIntegerState;

@Component
public class AttemptState extends BaseIntegerState<AttemptContext> {
    public AttemptState() {
        super(Entry.ATTEMPT_PROMPT, AttemptContext::setAttempt);
    }
}
