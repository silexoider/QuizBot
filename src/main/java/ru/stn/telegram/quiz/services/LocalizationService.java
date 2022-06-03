package ru.stn.telegram.quiz.services;

import lombok.Getter;

import java.util.ResourceBundle;

public interface LocalizationService {
    enum Message {
        GET_QUESTION_SUCCESS_REPLY("get_question_success_reply"),
        GET_QUESTION_FAILURE_REPLY("get_question_failure_reply"),
        GET_QUESTION_FAILURE_INVALID_MESSAGE("get_question_failure_reply_invalid_message"),
        GET_QUESTION_FAILURE_NO_QUESTION("get_question_failure_reply_no_question"),

        SET_QUESTION_SUCCESS_REPLY("set_question_success_reply"),
        SET_QUESTION_FAILURE_REPLY("set_question_failure_reply"),
        SET_QUESTION_FAILURE_REPLY_INVALID_MESSAGE("set_question_failure_reply_invalid_message"),
        SET_QUESTION_FAILURE_REPLY_INSUFFICIENT_PRIVILEGE("set_question_failure_reply_insufficient_privilege");

        @Getter
        private final String text;

        Message(String text) {
            this.text = text;
        }
    }

    String getGetQuestionSuccessReply(ResourceBundle resourceBundle);
    String getGetQuestionFailureReply(ResourceBundle resourceBundle);
    String getGetQuestionFailureReplyInvalidMessage(ResourceBundle resourceBundle);
    String getGetQuestionFailureReplyNoQuestion(ResourceBundle resourceBundle);

    String getSetQuestionSuccessReply(ResourceBundle resourceBundle);
    String getSetQuestionFailureReply(ResourceBundle resourceBundle);
    String getSetQuestionFailureReplyInvalidMessage(ResourceBundle resourceBundle);
    String getSetQuestionFailureReplyInsufficientPrivilege(ResourceBundle resourceBundle);
}
