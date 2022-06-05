package ru.stn.telegram.quiz.entities;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.stn.telegram.quiz.services.LocalizationService;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Arrays;

@Data
@Entity
@NoArgsConstructor
public class Session {
    public enum State {
        DEFAULT(1, null) {
            @Override
            public State getNext() {
                return null;
            }
        },
        EXPECTING_KEYWORD(2, LocalizationService.Message.KEYWORD_PROMPT) {
            @Override
            public State getNext() {
                return EXPECTING_MESSAGE;
            }
        },
        EXPECTING_MESSAGE(4, LocalizationService.Message.MESSAGE_PROMPT) {
            @Override
            public State getNext() {
                return EXPECTING_TIMEOUT;
            }
        },
        EXPECTING_TIMEOUT(8, LocalizationService.Message.TIMEOUT_PROMPT) {
            @Override
            public State getNext() {
                return EXPECTING_FORWARD;
            }
        },
        EXPECTING_FORWARD(16, LocalizationService.Message.FORWARD_PROMPT) {
            @Override
            public State getNext() {
                return DEFAULT;
            }
        };

        @Getter
        private int value;
        @Getter
        private LocalizationService.Message prompt;

        State(int value, LocalizationService.Message prompt) {
            this.value = value;
            this.prompt = prompt;
        }

        public abstract State getNext();
    }

    public enum Protocol {
        FULL(State.EXPECTING_KEYWORD, State.EXPECTING_KEYWORD, State.EXPECTING_MESSAGE, State.EXPECTING_TIMEOUT),
        KEYWORD(State.EXPECTING_KEYWORD, State.EXPECTING_KEYWORD),
        MESSAGE(State.EXPECTING_MESSAGE, State.EXPECTING_MESSAGE),
        TIMEOUT(State.EXPECTING_TIMEOUT, State.EXPECTING_TIMEOUT),
        SHOW(State.EXPECTING_FORWARD);

        @Getter
        private int mask;
        @Getter
        private State initialState;

        Protocol(State initialState, State ... states) {
            this.mask = State.DEFAULT.value | Arrays.stream(states).map(state -> state.getValue()).reduce(0, (accumulator, value) -> accumulator | value) | State.EXPECTING_FORWARD.value;
            this.initialState = initialState;
        }
    }

    @Id
    private long userId;
    private State state;
    private String keyword;
    private String message;
    private int timeout;
    private Protocol protocol;

    public Session(long userId) {
        this.userId = userId;
        this.state = State.DEFAULT;
    }
}
