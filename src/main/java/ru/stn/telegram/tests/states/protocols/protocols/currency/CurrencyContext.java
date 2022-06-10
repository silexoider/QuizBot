package ru.stn.telegram.tests.states.protocols.protocols.currency;

import ru.stn.telegram.tests.states.protocols.states.dual.DualContext;
import ru.stn.telegram.tests.states.protocols.states.forward.ForwardContext;
import ru.stn.telegram.tests.states.protocols.states.plural.PluralContext;
import ru.stn.telegram.tests.states.protocols.states.singular.SingularContext;

public interface CurrencyContext extends ForwardContext, SingularContext, DualContext, PluralContext {
}
