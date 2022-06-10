package ru.stn.telegram.tests.states.protocols.states.amount;

import org.springframework.stereotype.Component;
import ru.stn.telegram.tests.states.localization.Entry;
import ru.stn.telegram.tests.states.protocols.states.BaseIntegerState;

@Component
public class PayAmountState extends BaseIntegerState<PayAmountContext> {
    public PayAmountState() {
        super(Entry.PAYMENT_PROMPT, PayAmountContext::setPayAmount);
    }
}
