package ru.stn.telegram.tests.states.entities.sessions;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.stn.telegram.tests.states.protocols.states.forward.ForwardContext;

import javax.persistence.Entity;

@Data
@Entity
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ForwardSession extends Session implements ForwardContext {
    private long chatId;
    private int postId;

    public ForwardSession(long userId) {
        super(userId);
    }
}
