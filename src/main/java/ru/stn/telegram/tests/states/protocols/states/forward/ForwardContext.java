package ru.stn.telegram.tests.states.protocols.states.forward;

public interface ForwardContext {
    long getChatId();
    void setChatId(long chatId);

    int getPostId();
    void setPostId(int postId);
}
