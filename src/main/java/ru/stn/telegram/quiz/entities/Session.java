package ru.stn.telegram.quiz.entities;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.stn.telegram.quiz.services.LocalizationService;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlType;
import java.util.Arrays;

@Data
@Entity
@NoArgsConstructor
public class Session {
    public enum State {
        DEFAULT(null, null),

        CURRENCY_PLURAL(DEFAULT, LocalizationService.Message.CURRENCY_PLURAL),
        CURRENCY_DUAL(CURRENCY_PLURAL, LocalizationService.Message.CURRENCY_DUAL),
        CURRENCY_SINGULAR(CURRENCY_DUAL, LocalizationService.Message.CURRENCY_SINGULAR),

        PAY(DEFAULT, LocalizationService.Message.PAY_AMOUNT_PROMPT),
        COMMENT(PAY, LocalizationService.Message.COMMENT_PROMPT),

        MAXIMUM(DEFAULT, LocalizationService.Message.MAXIMUM_PROMPT),
        ATTEMPT(MAXIMUM, LocalizationService.Message.ATTEMPT_PROMPT),
        CORRECT(ATTEMPT, LocalizationService.Message.CORRECT_PROMPT),
        TIMEOUT(CORRECT, LocalizationService.Message.TIMEOUT_PROMPT),
        MESSAGE(TIMEOUT, LocalizationService.Message.MESSAGE_PROMPT),
        KEYWORD(MESSAGE, LocalizationService.Message.KEYWORD_PROMPT),
        FORWARD(KEYWORD, LocalizationService.Message.FORWARD_PROMPT);

        @Getter
        private State next;
        @Getter
        private LocalizationService.Message prompt;

        State(State next, LocalizationService.Message prompt) {
            this.next = next;
            this.prompt = prompt;
        }
    }

    public enum Protocol {
        FULL("Full"),
        BRIEF("Brief"),
        KEYWORD("Keyword"),
        MESSAGE("Message"),
        TIMEOUT("Timeout"),
        CORRECT("Correct"),
        ATTEMPT("Attempt"),
        MAXIMUM("Maximum"),
        SHOW("Show"),
        CURRENCY("Currency"),
        PAY("Pay");

        @Getter
        private String name;

        Protocol(String name) {
            this.name = name;
        }
    }

    @Id
    private long userId;
    private Long chatId;
    private Integer postId;
    private String keyword;
    private String message;
    private int timeout;
    private int correct;
    private int attempt;
    private int maximum;
    private String currencySingular;
    private String currencyDual;
    private String currencyPlural;
    private long commentUserId;
    private int amount;
    private State state;
    private Protocol protocol;

    public Session(long userId) {
        this.userId = userId;
        this.state = State.DEFAULT;
    }
}
