package com.jaynewstrom.screenswitcher;

import static com.jaynewstrom.screenswitcher.Preconditions.checkArgument;
import static com.jaynewstrom.screenswitcher.Preconditions.checkNotNull;

final class Utils {

    private Utils() {
    }

    static void checkScreen(ScreenSwitcherState state, Screen screen) {
        checkNotNull(screen, "screen == null");
        checkArgument(!state.getScreens().contains(screen), "screen already exists");
    }
}
