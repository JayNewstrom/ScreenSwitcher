package com.jaynewstrom.screenswitcher;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

import java.util.List;

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
     * <p/>
     * If there is a {@link ScreenPopListener} that overrides one of the {@link Screen}s being popped, it will pop up until that point.
     */
    void pop(@IntRange(from = 1) int numberToPop);

    /**
     * Will try to pop the {@code numberToPop} {@link Screen}s, then transition to the last {@link Screen} in {@code screens}.
     * <p/>
     * If there is a {@link ScreenPopListener} that overrides one of the {@link Screen}s being popped, it will pop up until that point
     * without pushing the {@code screens}.
     */
    void replaceScreensWith(@IntRange(from = 1) int numberToPop, @NonNull List<Screen> screens);

    /**
     * @return true if there is a transition being executed.
     */
    boolean isTransitioning();
}
