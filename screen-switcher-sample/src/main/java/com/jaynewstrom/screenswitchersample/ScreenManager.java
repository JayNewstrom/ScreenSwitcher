package com.jaynewstrom.screenswitchersample;

import com.jaynewstrom.screenswitcher.Screen;
import com.jaynewstrom.screenswitcher.ScreenSwitcher;

public final class ScreenManager {

    private ScreenSwitcher screenSwitcher;

    ScreenManager() {
    }

    boolean isSameImplementation(ScreenSwitcher screenSwitcher) {
        return this.screenSwitcher == screenSwitcher;
    }

    void take(ScreenSwitcher screenSwitcher) {
        this.screenSwitcher = screenSwitcher;
    }

    void drop(ScreenSwitcher screenSwitcher) {
        if (isSameImplementation(screenSwitcher)) {
            this.screenSwitcher = null;
        }
    }

    public void pop(int numberToPop) {
        if (screenSwitcher != null) {
            screenSwitcher.pop(numberToPop);
        }
    }

    public void pop() {
        pop(1);
    }

    public void push(Screen screen) {
        if (screenSwitcher != null) {
            screenSwitcher.push(screen);
        }
    }
}
