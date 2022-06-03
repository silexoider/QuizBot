package ru.stn.telegram.quiz.services.impl;

import org.springframework.stereotype.Service;
import ru.stn.telegram.quiz.services.LocalizationService;

import java.util.ResourceBundle;

@Service
public class LocalizationServiceImpl implements LocalizationService {
    private String getMessage(ResourceBundle resourceBundle, Message message) {
        return resourceBundle.getString(message.getText());
    }

    @Override
    public String getGetQuestionSuccessReply(ResourceBundle resourceBundle) {
        return getMessage(resourceBundle, Message.GET_QUESTION_SUCCESS_REPLY);
    }

    @Override
    public String getGetQuestionFailureReply(ResourceBundle resourceBundle) {
        return getMessage(resourceBundle, Message.GET_QUESTION_FAILURE_REPLY);
    }

    @Override
    public String getGetQuestionFailureReplyInvalidMessage(ResourceBundle resourceBundle) {
        return getMessage(resourceBundle, Message.GET_QUESTION_FAILURE_INVALID_MESSAGE);
    }

    @Override
    public String getGetQuestionFailureReplyNoQuestion(ResourceBundle resourceBundle) {
        return getMessage(resourceBundle, Message.GET_QUESTION_FAILURE_NO_QUESTION);
    }

    @Override
    public String getSetQuestionSuccessReply(ResourceBundle resourceBundle) {
        return getMessage(resourceBundle, Message.SET_QUESTION_SUCCESS_REPLY);
    }

    @Override
    public String getSetQuestionFailureReply(ResourceBundle resourceBundle) {
        return getMessage(resourceBundle, Message.SET_QUESTION_FAILURE_REPLY);
    }

    @Override
    public String getSetQuestionFailureReplyInvalidMessage(ResourceBundle resourceBundle) {
        return getMessage(resourceBundle, Message.SET_QUESTION_FAILURE_REPLY_INVALID_MESSAGE);
    }

    @Override
    public String getSetQuestionFailureReplyInsufficientPrivilege(ResourceBundle resourceBundle) {
        return getMessage(resourceBundle, Message.SET_QUESTION_FAILURE_REPLY_INSUFFICIENT_PRIVILEGE);
    }
}
