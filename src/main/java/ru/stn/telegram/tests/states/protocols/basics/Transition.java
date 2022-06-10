package ru.stn.telegram.tests.states.protocols.basics;

import lombok.Getter;

public class Transition<S, C extends S> {
    public enum Kind {
        NEXT,
        SELF,
        CANCEL,
        STANDBY,
        SPECIFIC
    }

    @Getter
    private final Kind kind;
    @Getter
    private final Navigator<S, C> navigator;

    private Transition(Kind kind, Navigator<S, C> navigator) {
        this.kind = kind;
        this.navigator = navigator;
    }
    public static <S, C extends S> Transition<S, C> next() {
        return new Transition<>(Kind.NEXT, null);
    }
    public static <S, C extends S> Transition<S, C> self() {
        return new Transition<>(Kind.SELF, null);
    }
    public static <S, C extends S> Transition<S, C> cancel() {
        return new Transition<>(Kind.CANCEL, null);
    }
    public static <S, C extends S> Transition<S, C> standby() {
        return new Transition<>(Kind.STANDBY, null);
    }
    public static <S, C extends S> Transition<S, C> specific(Navigator<S, C> navigator) {
        return new Transition<>(Kind.SPECIFIC, navigator);
    }
}
