package ru.stn.telegram.tests.states.entities.sessions;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.stn.telegram.tests.states.protocols.Protocols;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
@NoArgsConstructor
public class Session {
    @Id
    private long userId;
    private int state;
    private Protocols protocol;

    public Session(long userId) {
        this.userId = userId;
    }
}
