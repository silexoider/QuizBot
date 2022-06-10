package ru.stn.telegram.tests.states.entities.sessions;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.stn.telegram.tests.states.entities.sessions.ForwardSession;
import ru.stn.telegram.tests.states.protocols.protocols.question.QuestionContext;

import javax.persistence.Entity;

@Data
@Entity
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class QuestionSession extends ForwardSession implements QuestionContext {
    private String keyword;
    private String message;
    private int timeout;
    private int correct;
    private int attempt;
    private int maximum;

    public QuestionSession(long userId) {
        super(userId);
    }
}
