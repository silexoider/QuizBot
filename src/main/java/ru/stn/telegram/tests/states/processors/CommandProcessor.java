package ru.stn.telegram.tests.states.processors;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public abstract class CommandProcessor<T, R> {
    private final R defaultResult;
    private final Map<String, Function<T, R>> handlers;

    public CommandProcessor(R defaultResult) {
        this.handlers = new HashMap<>();
        this.defaultResult = defaultResult;
    }

    public void addHandler(String command, Function<T, R> handler) {
        for (String variant : getCommandVariants(command)) {
            handlers.put(variant, handler);
        }
    }

    public R process(T value) {
        String text = getCommandText(value);
        if (text == null) {
            return defaultResult;
        }
        Function<T, R> handler = handlers.get(text);
        if (handler == null) {
            return defaultResult;
        }
        return handler.apply(value);
    }

    protected abstract List<String> getCommandVariants(String command);
    protected abstract String getCommandText(T value);
}
