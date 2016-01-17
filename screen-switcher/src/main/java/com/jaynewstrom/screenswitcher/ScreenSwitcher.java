package com.jaynewstrom.screenswitcher;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

/**
 * This object lives inside of the {@link android.app.Activity} lifecycle, and shouldn't be referenced outside of it to ensure leaks
 * don't occur.
 */
public interface ScreenSwitcher {

    /**
     * Add the given {@link Screen}.
     */
    void push(@NonNull Screen screen);

    /**
     * Will try to pop the top {@code numberToPop} {@link Screen}s.
     * If there is a {@link ScreenPopListener} that overrides one of the {@link Screen}s being popped, it will pop up until that point.
     *
     * @throws IllegalArgumentException if {@code numberToPop} is < 1.
     */
    void pop(@IntRange(from = 1) int numberToPop);

    /**
     * @return true if there is a transition being executed.
     */
    boolean isTransitioning();
}
