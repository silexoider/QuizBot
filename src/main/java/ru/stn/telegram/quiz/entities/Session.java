package ru.stn.telegram.quiz.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
@NoArgsConstructor
public class Session {
    public enum State {
        DEFAULT,
        SET_QUESTION_EXPECTING_FORWARD
    }

    @Id
    private long userId;
    private State state;
    private String text;

    public Session(long userId) {
        this.userId = userId;
        state = State.DEFAULT;
    }
}
