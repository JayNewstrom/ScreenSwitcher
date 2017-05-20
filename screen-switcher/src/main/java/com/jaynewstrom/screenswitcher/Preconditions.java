package com.jaynewstrom.screenswitcher;

import javax.annotation.Nullable;

final class Preconditions {

    static <T> T checkNotNull(@Nullable T t, String message) {
        if (t == null) {
            throw new NullPointerException(message);
        }
        return t;
    }

    static void checkArgument(boolean expectedArgument, String message) {
        if (!expectedArgument) {
            throw new IllegalArgumentException(message);
        }
    }

    static void checkState(boolean expectedState, String message) {
        if (!expectedState) {
            throw new IllegalStateException(message);
        }
    }
}
