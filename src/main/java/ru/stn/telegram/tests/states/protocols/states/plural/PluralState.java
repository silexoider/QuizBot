package ru.stn.telegram.tests.states.protocols.states.plural;

import org.springframework.stereotype.Component;
import ru.stn.telegram.tests.states.localization.Entry;
import ru.stn.telegram.tests.states.protocols.states.BaseStringState;

@Component
public class PluralState extends BaseStringState<PluralContext> {
    public PluralState() {
        super(Entry.PLURAL_PROMPT, PluralContext::setPlural);
    }
}
