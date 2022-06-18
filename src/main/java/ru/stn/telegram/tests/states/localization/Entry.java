package ru.stn.telegram.tests.states.localization;

import lombok.Getter;

public enum Entry {
    FORWARD_PROMPT("forward_prompt"),
    KEYWORD_PROMPT("keyword_prompt"),
    MESSAGE_PROMPT("message_prompt"),
    TIMEOUT_PROMPT("timeout_prompt"),
    CORRECT_PROMPT("correct_prompt"),
    ATTEMPT_PROMPT("attempt_prompt"),
    MAXIMUM_PROMPT("maximum_prompt"),
    COMMENT_PROMPT("comment_prompt"),
    COMPLETED("completed"),
    CANCELLED("cancelled"),
    INVALID_INPUT("invalid_input"),
    QUESTION_ABSENT("question_absent"),
    QUESTION_FORMAT("question_format"),
    SINGULAR_PROMPT("singular_prompt"),
    DUAL_PROMPT("dual_prompt"),
    PLURAL_PROMPT("plural_prompt"),
    PAYMENT_PROMPT("payment_prompt"),
    PAYMENT_FORMAT("payment_format"),
    CURRENCY_FORMAT("currency_format"),
    UNEXPECTED_ERROR_FORMAT("unexpected_error_format"),
    INSUFFICIENT_PRIVILEGES("insufficient_privileges"),
    CORRECT_ANSWER_FORMAT("correct_answer_format"),
    HELP_FORMAT("help_format"),
    WHEREAMI_FORMAT("whereami_format"),
    WRONG_ANSWER_FORMAT("wrong_answer_format"),
    TIMEOUT_EXPIRED("timeout_expired"),
    ALREADY_CORRECT("already_correct"),
    OWN_BALANCE_FORMAT("own_balance_format"),
    USER_BALANCE_FORMAT("user_balance_format");

    @Getter
    private final String name;

    Entry(String name) {
        this.name = name;
    }
}
