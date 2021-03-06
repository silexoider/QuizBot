package ru.stn.telegram.tests.states.processors;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.stn.telegram.tests.states.entities.Answer;
import ru.stn.telegram.tests.states.entities.Channel;
import ru.stn.telegram.tests.states.entities.Question;
import ru.stn.telegram.tests.states.entities.sessions.Session;
import ru.stn.telegram.tests.states.localization.Entry;
import ru.stn.telegram.tests.states.localization.Localizer;
import ru.stn.telegram.tests.states.services.*;

import java.util.AbstractMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.BiConsumer;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class MessageProcessor {
    private final SessionService sessionService;
    private final ProtocolService protocolService;
    private final DefaultCommandProcessor defaultCommandProcessor;

    @Autowired
    private Localizer localizer;
    @Autowired
    private BotService botService;
    @Autowired
    private AnswerService answerService;
    @Autowired
    private ChannelService channelService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private QuestionService questionService;

    private final List<AbstractMap.SimpleEntry<Function<Message, Boolean>, BiConsumer<Message, ResourceBundle>>> handlers = new LinkedList<>() {{
        add(new AbstractMap.SimpleEntry<>(MessageProcessor.this::checkPrivate, MessageProcessor.this::processPrivate));
        add(new AbstractMap.SimpleEntry<>(MessageProcessor.this::checkPublic, MessageProcessor.this::processPublic));
    }};

    private boolean checkPrivate(Message message) {
        return message.isUserMessage();
    }
    private boolean checkPublic(Message message) {
        return !message.isUserMessage();
    }

    private void processPrivate(Message message, ResourceBundle resourceBundle) {
        System.out.println("processPrivate");
        Session session = sessionService.find(message.getFrom().getId());
        if (session == null || session.getState() == -1) {
            defaultCommandProcessor.process(new DefaultCommandProcessor.Args(message, resourceBundle));
        } else {
            session.getProtocol().getConsumer().accept(protocolService, session, message, resourceBundle);
        }
    }
    private void processPublic(Message message, ResourceBundle resourceBundle) {
        System.out.println("processPublic");
        if (message.getText() == null || message.getReplyToMessage() == null) {
            System.out.println("processPublic: label01");
            return;
        }
        MessageService.Post post = messageService.getForwardedChannelPost(message.getReplyToMessage());
        if (post == null) {
            System.out.println("processPublic: label02");
            return;
        }
        Question question = questionService.find(post.getChatId(), post.getPostId());
        if (question == null) {
            System.out.println("processPublic: label03");
            return;
        }
        if (!questionService.checkTimeout(question, message)) {
            System.out.println("processPublic: label04");
            return;
        }
        Answer answer = answerService.get(post.getChatId(), post.getPostId(), message.getFrom().getId());
        Channel channel = channelService.get(post.getChatId());
        if (questionService.checkKeyword(question, message)) {
            if (answerService.processCorrect(answer, question)) {
                System.out.println(String.format("processPublic: label05; message.getFrom().getId() = %d; message.getFrom().getUserName() = %s", message.getFrom().getId(), message.getFrom().getUserName()));
                botService.sendMessage(
                        message.getFrom().getId(),
                        String.format(
                                localizer.localize(Entry.CORRECT_ANSWER_FORMAT, resourceBundle),
                                channelService.getValueInCurrency(channel, question.getCorrect()),
                                channelService.getValueInCurrency(channel, answerService.getChatBalance(answer)),
                                question.getMessage()
                        )
                );
            } else {
                System.out.println("processPublic: label06");
                botService.sendMessage(message.getFrom().getId(), localizer.localize(Entry.ALREADY_CORRECT, resourceBundle));
            }
        } else {
            botService.sendMessage(
                    message.getFrom().getId(),
                    String.format(
                            localizer.localize(Entry.WRONG_ANSWER_FORMAT, resourceBundle),
                            channelService.getValueInCurrency(channel, answerService.getChatBalance(answer))
                    )
            );
            answerService.processAttempt(answer, question);
        }
    }

    public void process(Message message, ResourceBundle resourceBundle) {
        for (var handler : handlers) {
            if (handler.getKey().apply(message)) {
                handler.getValue().accept(message, resourceBundle);
            }
        }
    }
}
