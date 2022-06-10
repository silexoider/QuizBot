package ru.stn.telegram.tests.states.services.impl;

import lombok.RequiredArgsConstructor;
import org.checkerframework.common.util.report.qual.ReportOverride;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.stn.telegram.tests.states.entities.Question;
import ru.stn.telegram.tests.states.repositories.QuestionRepository;
import ru.stn.telegram.tests.states.services.QuestionService;
import ru.stn.telegram.tests.states.telegram.Config;

import java.time.Duration;
import java.time.Instant;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class QuestionServiceImpl extends BaseEntityServiceImpl<Question, Long, QuestionRepository> implements QuestionService {
    @Autowired
    private Config config;

    public QuestionServiceImpl(QuestionRepository repository) {
        super(repository);
    }

    @Override
    public Question find(long chatId, int postId) {
        return repository.findByChatIdAndPostId(chatId, postId).orElse(null);
    }

    @Override
    public void setFull(long chatId, int postId, String keyword, String message, int timeout, int correct, int attempt, int maximum) {
        Question question = find(chatId, postId);
        if (question == null) {
            question = new Question(chatId, postId);
        }
        question.setKeyword(keyword);
        question.setMessage(message);
        question.setTimeout(timeout);
        question.setCorrect(correct);
        question.setAttempt(attempt);
        question.setMaximum(maximum);
        repository.save(question);
    }

    @Override
    public void setBrief(long chatId, int postId, String keyword, String message, int timeout) {
        setFull(chatId, postId, keyword, message, timeout, config.getCorrect(), config.getAttempt(), config.getMaximum());
    }

    @Override
    public void setPost(Question question, long chatId, int postId) {
        question.setChatId(chatId);
        question.setPostId(postId);
        repository.save(question);
    }

    @Override
    public void setKeyword(Question question, String keyword) {
        setAttribute(question, Question::setKeyword, keyword);
    }

    @Override
    public void setMessage(Question question, String message) {
        setAttribute(question, Question::setMessage, message);
    }

    @Override
    public void setTimeout(Question question, int timeout) {
        setAttribute(question, Question::setTimeout, timeout);
    }

    @Override
    public void setCorrect(Question question, int correct) {
        setAttribute(question, Question::setCorrect, correct);
    }

    @Override
    public void setAttempt(Question question, int attempt) {
        setAttribute(question, Question::setAttempt, attempt);
    }

    @Override
    public void setMaximum(Question question, int maximum) {
        setAttribute(question, Question::setMaximum, maximum);
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
