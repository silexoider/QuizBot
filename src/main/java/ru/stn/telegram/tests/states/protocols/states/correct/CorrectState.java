package ru.stn.telegram.tests.states.protocols.states.correct;

import org.springframework.stereotype.Component;
import ru.stn.telegram.tests.states.localization.Entry;
import ru.stn.telegram.tests.states.protocols.states.BaseIntegerState;

@Component
public class CorrectState extends BaseIntegerState<CorrectContext> {
    public CorrectState() {
        super(Entry.CORRECT_PROMPT, CorrectContext::setCorrect);
    }
}
