package ru.stn.telegram.tests.states.protocols.states.keyword;

import org.springframework.stereotype.Component;
import ru.stn.telegram.tests.states.localization.Entry;
import ru.stn.telegram.tests.states.protocols.states.BaseStringState;

@Component
public class KeywordState extends BaseStringState<KeywordContext> {
    public KeywordState() {
        super(Entry.KEYWORD_PROMPT, KeywordContext::setKeyword);
    }
}
