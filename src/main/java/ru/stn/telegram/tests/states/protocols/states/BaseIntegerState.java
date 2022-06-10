package ru.stn.telegram.tests.states.protocols.states;

import org.telegram.telegrambots.meta.api.objects.Message;
import ru.stn.telegram.tests.states.exceptions.InvalidInputException;
import ru.stn.telegram.tests.states.localization.Entry;

import java.util.ResourceBundle;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class BaseIntegerState<C> extends BaseValueState<C, Integer> {
    public BaseIntegerState(Entry entry, BiConsumer<C, Integer> consumer) {
        super(entry, consumer);
    }

    @Override
    protected Integer process(C context, Message message, ResourceBundle resourceBundle) {
        if (message.getText() == null) {
            throw new InvalidInputException();
        }
        int value;
        try {
            value = Integer.parseInt(message.getText());
        } catch (NumberFormatException e) {
            throw new InvalidInputException();
        }
        return value;
    }
}
