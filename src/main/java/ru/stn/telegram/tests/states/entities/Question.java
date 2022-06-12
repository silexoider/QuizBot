package ru.stn.telegram.tests.states.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table(indexes = @Index(name = "XChatPost", columnList = "chatId, postId"))
@NoArgsConstructor
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private long chatId;
    private int postId;
    private String keyword;
    private String message;
    private int timeout;
    private int correct;
    private int attempt;
    private int maximum;

    public Question(long chatId, int postId) {
        this.chatId = chatId;
        this.postId = postId;
    }
    public Question(long chatId, int postId, String keyword, String message, int timeout, int correct, int attempt, int maximum) {
        this.chatId = chatId;
        this.postId = postId;
        this.keyword = keyword;
        this.message = message;
        this.timeout = timeout;
        this.correct = correct;
        this.attempt = attempt;
        this.maximum = maximum;
    }
}
