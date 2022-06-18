package ru.stn.telegram.tests.states.entities.sessions;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.stn.telegram.tests.states.protocols.protocols.payment.OwnBalanceContext;
import ru.stn.telegram.tests.states.protocols.protocols.payment.UserBalanceContext;

import javax.persistence.Entity;

@Data
@Entity
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class BalanceSession extends ForwardSession implements OwnBalanceContext, UserBalanceContext {
    private Long otherUserId;

    public BalanceSession(long userId) {
        super(userId);
    }
}
