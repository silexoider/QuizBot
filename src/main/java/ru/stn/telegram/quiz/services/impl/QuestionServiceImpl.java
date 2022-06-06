package ru.stn.telegram.quiz.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.stn.telegram.quiz.entities.Question;
import ru.stn.telegram.quiz.repositories.QuestionRepository;
import ru.stn.telegram.quiz.services.LocalizationService;
import ru.stn.telegram.quiz.services.QuestionService;
import ru.stn.telegram.quiz.telegram.Config;

import java.time.Duration;
import java.time.Instant;
import java.time.Period;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {
    private final Config config;
    private final QuestionRepository questionRepository;

    @Override
    public Question find(long chatId, int postId) {
        return questionRepository.findByChatIdAndPostId(chatId, postId).orElse(null);
    }

    @Override
    public Question setQuestionFull(long chatId, int postId, String keyword, String message, int timeout, int correct, int attempt, int maximum) {
        Question question = find(chatId, postId);
        if (question == null) {
            question = new Question(chatId, postId, keyword, message, timeout, correct, attempt, maximum);
        } else {
            question.setKeyword(keyword);
            question.setMessage(message);
            question.setTimeout(timeout);
            question.setCorrect(correct);
            question.setAttempt(attempt);
            question.setMaximum(maximum);
        }
        questionRepository.save(question);
        return question;
    }

    @Override
    public Question setQuestionBrief(long chatId, int postId, String keyword, String message, int timeout) {
        return setQuestionFull(chatId, postId, keyword, message, timeout, config.getCorrect(), config.getAttempt(), config.getMaximum());
    }

    private <T> boolean setAttribute(long chatId, int postId, BiConsumer<Question, T> action, T value) {
        Question question = find(chatId, postId);
        if (question == null) {
            return false;
        }
        action.accept(question, value);
        questionRepository.save(question);
        return true;
    }

    @Override
    public boolean setKeyword(long chatId, int postId, String keyword) {
        return setAttribute(chatId, postId, Question::setKeyword, keyword);
    }

    @Override
    public boolean setMessage(long chatId, int postId, String message) {
        return setAttribute(chatId, postId, Question::setMessage, message);
    }

    @Override
    public boolean setTimeout(long chatId, int postId, int timeout) {
        return setAttribute(chatId, postId, Question::setTimeout, timeout);
    }

    @Override
    public boolean setCorrect(long chatId, int postId, int correct) {
        return setAttribute(chatId, postId, Question::setCorrect, correct);
    }

    @Override
    public boolean setAttempt(long chatId, int postId, int attempt) {
        return setAttribute(chatId, postId, Question::setAttempt, attempt);
    }

    @Override
    public boolean setMaximum(long chatId, int postId, int maximum) {
        return setAttribute(chatId, postId, Question::setMaximum, maximum);
    }

    @Override
    public boolean checkKeyword(Question question, Message message) {
        Pattern pattern = Pattern.compile(String.format("\\b%s\\b", Pattern.quote(question.getKeyword())), Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
        Matcher matcher = pattern.matcher(message.getText());
        return matcher.find();
    }

    @Override
    public boolean checkTimeout(Question question, Message message) {
        Duration questionDuration = Duration.ofSeconds(question.getTimeout());
        Duration calculatedDuration = Duration.between(Instant.ofEpochSecond(message.getReplyToMessage().getDate()), Instant.now());
        return questionDuration.compareTo(calculatedDuration) >= 0;
    }
}
