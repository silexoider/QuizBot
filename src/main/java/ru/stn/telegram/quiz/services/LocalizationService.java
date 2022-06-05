package ru.stn.telegram.quiz.services;

import lombok.Getter;

import javax.annotation.Resource;
import java.util.ResourceBundle;

public interface LocalizationService {
    enum Message {
        KEYWORD_PROMPT("keyword_prompt"),
        MESSAGE_PROMPT("message_prompt"),
        TIMEOUT_PROMPT("timeout_prompt"),
        FORWARD_PROMPT("forward_prompt"),
        INVALID_INPUT("invalid_input"),
        INSUFFICIENT_PRIVILEGE("insufficient_privilege"),
        SUCCESS("success"),
        PART_FAILURE("part_failure"),
        CANCELLED("cancelled"),
        NO_QUESTION("no_question"),
        QUESTION_FORMAT("question_format");

        @Getter
        private final String text;

        Message(String text) {
            this.text = text;
        }
    }

    String getMessage(Message message, ResourceBundle resourceBundle);
    String getKeywordPrompt(ResourceBundle resourceBundle);
    String getReplyPrompt(ResourceBundle resourceBundle);
    String getTimeoutPrompt(ResourceBundle resourceBundle);
    String getForwardPrompt(ResourceBundle resourceBundle);
    String getInvalidInput(ResourceBundle resourceBundle);
    String getInsufficientPrivilege(ResourceBundle resourceBundle);
    String getSuccess(ResourceBundle resourceBundle);
    String getPartFailure(ResourceBundle resourceBundle);
    String getCancelled(ResourceBundle resourceBundle);
    String getNoQuestion(ResourceBundle resourceBundle);
    String getQuestionFormat(ResourceBundle resourceBundle);
}
