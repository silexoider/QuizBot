package ru.stn.telegram.tests.states.entities.sessions;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.stn.telegram.tests.states.protocols.protocols.currency.CurrencyContext;

import javax.persistence.Entity;

@Data
@Entity
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CurrencySession extends ForwardSession implements CurrencyContext {
    private String singular;
    private String dual;
    private String plural;

    public CurrencySession(long userId) {
        super(userId);
    }
}
