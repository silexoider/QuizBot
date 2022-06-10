package ru.stn.telegram.tests.states.protocols.protocols.payment;

import ru.stn.telegram.tests.states.protocols.states.amount.PayAmountContext;
import ru.stn.telegram.tests.states.protocols.states.comment.CommentContext;
import ru.stn.telegram.tests.states.protocols.states.forward.ForwardContext;

public interface PaymentContext extends ForwardContext, CommentContext, PayAmountContext {
}
