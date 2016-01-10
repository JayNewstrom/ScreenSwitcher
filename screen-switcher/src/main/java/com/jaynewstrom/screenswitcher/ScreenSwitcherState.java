package com.jaynewstrom.screenswitcher;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class ScreenSwitcherState {

    private final List<Screen> screens;
    private final Map<Screen, ScreenPopListener> popListenerMap;

    public ScreenSwitcherState(List<Screen> screens) {
        this.screens = new ArrayList<>(screens);
        popListenerMap = new LinkedHashMap<>();
    }

    public void registerPopListener(Screen screen, ScreenPopListener popListener) {
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
