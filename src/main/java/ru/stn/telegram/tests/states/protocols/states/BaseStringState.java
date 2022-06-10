package ru.stn.telegram.tests.states.protocols.states;

import org.telegram.telegrambots.meta.api.objects.Message;
import ru.stn.telegram.tests.states.exceptions.InvalidInputException;
import ru.stn.telegram.tests.states.localization.Entry;

import java.util.ResourceBundle;
import java.util.function.BiConsumer;

public class BaseStringState<C> extends BaseValueState<C, String> {
    public BaseStringState(Entry entry, BiConsumer<C, String> consumer) {
        super(entry, consumer);
    }

    @Override
    protected String process(C context, Message message, ResourceBundle resourceBundle) {
        if (message.getText() == null) {
            throw new InvalidInputException();
        }
        return message.getText();
    }
}
