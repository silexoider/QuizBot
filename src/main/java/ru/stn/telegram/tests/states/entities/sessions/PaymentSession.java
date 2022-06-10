package ru.stn.telegram.tests.states.entities.sessions;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.stn.telegram.tests.states.entities.sessions.ForwardSession;
import ru.stn.telegram.tests.states.protocols.protocols.payment.PaymentContext;

import javax.persistence.Entity;

@Data
@Entity
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PaymentSession extends ForwardSession implements PaymentContext {
    private int payAmount;

    public PaymentSession(long userId) {
        super(userId);
    }
}
