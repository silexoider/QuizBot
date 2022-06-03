package ru.stn.telegram.quiz.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.stn.telegram.quiz.entities.Question;
import ru.stn.telegram.quiz.repositories.QuestionRepository;
import ru.stn.telegram.quiz.services.QuestionService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {
    private final QuestionRepository questionRepository;

    @Override
    public Question find(long chatId, int postId) {
        return questionRepository.findByChatIdAndPostId(chatId, postId).orElse(null);
    }

    @Override
    public Question setQuestion(long chatId, int postId, String text) {
        Question question = find(chatId, postId);
        if (question == null) {
            question = new Question(chatId, postId, text);
        } else {
            question.setText(text);
        }
        questionRepository.save(question);
        return question;
    }
}
