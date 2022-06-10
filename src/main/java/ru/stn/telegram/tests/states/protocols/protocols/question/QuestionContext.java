package ru.stn.telegram.tests.states.protocols.protocols.question;

import ru.stn.telegram.tests.states.protocols.states.attempt.AttemptContext;
import ru.stn.telegram.tests.states.protocols.states.correct.CorrectContext;
import ru.stn.telegram.tests.states.protocols.states.forward.ForwardContext;
import ru.stn.telegram.tests.states.protocols.states.keyword.KeywordContext;
import ru.stn.telegram.tests.states.protocols.states.maximum.MaximumContext;
import ru.stn.telegram.tests.states.protocols.states.message.MessageContext;
import ru.stn.telegram.tests.states.protocols.states.timeout.TimeoutContext;

public interface QuestionContext extends ForwardContext, KeywordContext, MessageContext, TimeoutContext, CorrectContext, AttemptContext, MaximumContext {
}
