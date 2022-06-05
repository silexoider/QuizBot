package ru.stn.telegram.quiz.services.impl;

import org.springframework.stereotype.Service;
import ru.stn.telegram.quiz.services.LocalizationService;

import java.util.ResourceBundle;

@Service
public class LocalizationServiceImpl implements LocalizationService {
    @Override
    public String getMessage(Message message, ResourceBundle resourceBundle) {
        return resourceBundle.getString(message.getText());
    }

    @Override
    public String getKeywordPrompt(ResourceBundle resourceBundle) {
        return getMessage(Message.KEYWORD_PROMPT, resourceBundle);
    }
    @Override
    public String getReplyPrompt(ResourceBundle resourceBundle) {
        return getMessage(Message.MESSAGE_PROMPT, resourceBundle);
    }
    @Override
    public String getTimeoutPrompt(ResourceBundle resourceBundle) {
        return getMessage(Message.TIMEOUT_PROMPT, resourceBundle);
    }
    @Override
    public String getForwardPrompt(ResourceBundle resourceBundle) {
        return getMessage(Message.FORWARD_PROMPT, resourceBundle);
    }
    @Override
    public String getInvalidInput(ResourceBundle resourceBundle) {
        return getMessage(Message.INVALID_INPUT, resourceBundle);
    }
    @Override
    public String getInsufficientPrivilege(ResourceBundle resourceBundle) {
        return getMessage(Message.INSUFFICIENT_PRIVILEGE, resourceBundle);
    }
    @Override
    public String getSuccess(ResourceBundle resourceBundle) {
        return getMessage(Message.SUCCESS, resourceBundle);
    }
    @Override
    public String getPartFailure(ResourceBundle resourceBundle) {
        return getMessage(Message.PART_FAILURE, resourceBundle);
    }
    @Override
    public String getCancelled(ResourceBundle resourceBundle) {
        return getMessage(Message.CANCELLED, resourceBundle);
    }
    @Override
    public String getNoQuestion(ResourceBundle resourceBundle) {
        return getMessage(Message.NO_QUESTION, resourceBundle);
    }
    @Override
    public String getQuestionFormat(ResourceBundle resourceBundle) {
        return getMessage(Message.QUESTION_FORMAT, resourceBundle);
    }
}
