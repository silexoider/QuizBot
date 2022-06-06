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
    public String getForwardPrompt(ResourceBundle resourceBundle) {
        return getMessage(Message.FORWARD_PROMPT, resourceBundle);
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
    public String getCorrectPrompt(ResourceBundle resourceBundle) {
        return getMessage(Message.CORRECT_PROMPT, resourceBundle);
    }
    @Override
    public String getAttemptPrompt(ResourceBundle resourceBundle) {
        return getMessage(Message.ATTEMPT_PROMPT, resourceBundle);
    }
    @Override
    public String getMaximumPrompt(ResourceBundle resourceBundle) {
        return getMessage(Message.MAXIMUM_PROMPT, resourceBundle);
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
    @Override
    public String getSuccessNotificationFormat(ResourceBundle resourceBundle) {
        return getMessage(Message.SUCCESS_NOTIFICATION_FORMAT, resourceBundle);
    }
    @Override
    public String getCurrencySingular(ResourceBundle resourceBundle) {
        return getMessage(Message.CURRENCY_SINGULAR, resourceBundle);
    }
    @Override
    public String getCurrencyDual(ResourceBundle resourceBundle) {
        return getMessage(Message.CURRENCY_DUAL, resourceBundle);
    }
    @Override
    public String getCurrencyPlural(ResourceBundle resourceBundle) {
        return getMessage(Message.CURRENCY_PLURAL, resourceBundle);
    }
    @Override
    public String getHelpFormat(ResourceBundle resourceBundle) {
        return getMessage(Message.HELP_FORMAT, resourceBundle);
    }
}
