package com.jaynewstrom.screenswitcher;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.jaynewstrom.screenswitcher.Preconditions.checkArgument;
import static com.jaynewstrom.screenswitcher.Preconditions.checkNotNull;

/**
 * This object is designed to persist in memory when an {@link android.app.Activity} configuration change occurs.
 */
public final class ScreenSwitcherState {

    private final List<Screen> screens;
    private final Map<Screen, ScreenPopListener> popListenerMap;

    /**
     * @param screens The initial screens that the {@link ScreenSwitcher} should show.
     * @throws IllegalArgumentException if screens is empty
     */
    public ScreenSwitcherState(@NonNull List<Screen> screens) {
        checkNotNull(screens, "screens == null");
        checkArgument(!screens.isEmpty(), "screens must contain at least one screen");
        for (int i = 0, size = screens.size(); i < size; i++) {
            checkNotNull(screens.get(i), String.format("screen at index %d was null", i));
        }
        this.screens = new ArrayList<>(screens);
        popListenerMap = new LinkedHashMap<>();
    }

    /**
     * The pop listener will be automatically unregistered when the {@link Screen} is popped.
     * See documentation for {@link ScreenPopListener} for what objects should be registered.
     *
     * @param screen The {@link Screen} to be notified of before it gets popped.
     * @param popListener The {@link ScreenPopListener} to call when the {@link Screen} is trying to be popped.
     */
    public void registerPopListener(@NonNull Screen screen, @NonNull ScreenPopListener popListener) {
        checkNotNull(screen, "screen == null");
        checkNotNull(popListener, "popListener == null");
        popListenerMap.put(screen, popListener);
    }

    List<Screen> getScreens() {
        return screens;
    }

    boolean handlesPop(Screen screen) {
        ScreenPopListener popListener = popListenerMap.get(screen);
        return popListener != null && popListener.onScreenPop(screen);
    }
}
