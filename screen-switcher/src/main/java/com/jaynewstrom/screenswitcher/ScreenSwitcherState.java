package com.jaynewstrom.screenswitcher;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.jaynewstrom.screenswitcher.Preconditions.checkArgument;
import static com.jaynewstrom.screenswitcher.Preconditions.checkNotNull;

public final class ScreenSwitcherState {

    private final List<Screen> screens;
    private final Map<Screen, ScreenPopListener> popListenerMap;

    public ScreenSwitcherState(@NonNull List<Screen> screens) {
        checkNotNull(screens, "screens == null");
        checkArgument(!screens.isEmpty(), "screens must contain at least one screen");
        this.screens = new ArrayList<>(screens);
        popListenerMap = new LinkedHashMap<>();
    }

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
