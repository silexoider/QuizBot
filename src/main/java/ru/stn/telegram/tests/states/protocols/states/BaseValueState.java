package ru.stn.telegram.tests.states.protocols.states;

import org.telegram.telegrambots.meta.api.objects.Message;
import ru.stn.telegram.tests.states.entities.sessions.Session;
import ru.stn.telegram.tests.states.localization.Entry;
import ru.stn.telegram.tests.states.protocols.basics.BaseState;

import java.util.ResourceBundle;
import java.util.function.BiConsumer;

public abstract class BaseValueState<C, V> extends BaseState<C> {
    private final BiConsumer<C, V> consumer;

    public BaseValueState(Entry entry, BiConsumer<C, V> consumer) {
        super(entry);
        this.consumer = consumer;
    }

    @Override
    protected void process(Session session, C context, Message message, ResourceBundle resourceBundle) {
        V value = process(context, message, resourceBundle);
        consumer.accept(context, value);
        sessionService.save(session);
    }

    protected abstract V process(C context, Message message, ResourceBundle resourceBundle);
}
