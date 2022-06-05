package ru.stn.telegram.quiz.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(indexes = @Index(name= "XChatPostIds", columnList = "chatId, postId"))
@NoArgsConstructor
@AllArgsConstructor
public class Question implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private long chatId;
    private int postId;
    private String keyword;
    private String message;
    private int timeout;

    public Question(long chatId, int postId, String keyword, String message, int timeout) {
        this.chatId = chatId;
        this.postId = postId;
        this.keyword = keyword;
        this.message = message;
        this.timeout = timeout;
    }
}
