package ru.stn.telegram.quiz.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.stn.telegram.quiz.entities.Question;
import ru.stn.telegram.quiz.repositories.QuestionRepository;
import ru.stn.telegram.quiz.services.LocalizationService;
import ru.stn.telegram.quiz.services.QuestionService;

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
    private final QuestionRepository questionRepository;

    @Override
    public Question find(long chatId, int postId) {
        return questionRepository.findByChatIdAndPostId(chatId, postId).orElse(null);
    }

    @Override
    public Question setQuestion(long chatId, int postId, String keyword, String message, int timeout) {
        Question question = find(chatId, postId);
        if (question == null) {
            question = new Question(chatId, postId, keyword, message, timeout);
        } else {
            question.setKeyword(keyword);
            question.setMessage(message);
            question.setTimeout(timeout);
        }
        questionRepository.save(question);
        return question;
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
    public boolean checkKeyword(Question question, Message message) {
        Pattern pattern = Pattern.compile(String.format("\\b%s\\b", Pattern.quote(question.getKeyword())), Pattern.CASE_INSENSITIVE);
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
