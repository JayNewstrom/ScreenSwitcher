package com.jaynewstrom.screenswitcher;

import java.util.ArrayList;
import java.util.List;

public final class ScreenSwitcherState {

    private final List<Screen> screens;

    public ScreenSwitcherState(List<Screen> screens) {
        this.screens = new ArrayList<>(screens);
    }

    List<Screen> getScreens() {
        return screens;
    }
}
