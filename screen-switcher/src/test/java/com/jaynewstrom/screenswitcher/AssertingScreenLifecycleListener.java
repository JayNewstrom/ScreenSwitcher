package com.jaynewstrom.screenswitcher;

import java.util.ArrayList;
import java.util.List;

final class AssertingScreenLifecycleListener implements ScreenLifecycleListener {

    private Screen activeScreen;
    private final List<Screen> screens = new ArrayList<>();

    @Override public void onScreenAdded(Screen screen) {
        if (!screens.add(screen)) {
            throw new AssertionError(screen + " screen already existed in: " + screens);
        }
    }

    @Override public void onScreenRemoved(Screen screen) {
        if (!screens.remove(screen)) {
            throw new AssertionError(screen + " screen did not exist in: " + screens);
        }
    }

    @Override public void onScreenBecameActive(Screen screen) {
        if (activeScreen != null) {
            throw new AssertionError(activeScreen + " was already active.");
        }
        if (!screens.contains(screen)) {
            throw new AssertionError(screen + " does not exist in screens: " + screens);
        }
        activeScreen = screen;
    }

    @Override public void onScreenBecameInactive(Screen screen) {
        if (activeScreen != screen) {
            throw new AssertionError("Active screen: " + activeScreen + " did not align with screen becoming inactive: " + screen);
        }
        if (!screens.contains(screen)) {
            throw new AssertionError(screen + " does not exist in screens: " + screens);
        }
        activeScreen = null;
    }
}
