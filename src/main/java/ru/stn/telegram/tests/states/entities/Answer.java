package ru.stn.telegram.tests.states.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
@NoArgsConstructor
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private long chatId;
    private int postId;
    private long userId;
    private int attempts;
    private boolean correct;
    private int balance;

    public Answer(long chatId, int postId, long userId) {
        this.chatId = chatId;
        this.postId = postId;
        this.userId = userId;
        this.attempts = 0;
        this.correct = false;
        this.balance = 0;
    }
}
