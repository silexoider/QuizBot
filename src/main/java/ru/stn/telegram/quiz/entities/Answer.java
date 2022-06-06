package ru.stn.telegram.quiz.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table(indexes = @Index(name= "XChatUserPostIds", columnList = "chatId, userId, postId"))
@NoArgsConstructor
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private long chatId;
    private long userId;
    private int postId;
    private int attempts;
    private boolean correct;
    private int balance;

    public Answer(long chatId, long userId, int postId) {
        this.chatId = chatId;
        this.userId = userId;
        this.postId = postId;
        this.balance = 0;
        this.attempts = 0;
        this.correct = false;
    }
}
