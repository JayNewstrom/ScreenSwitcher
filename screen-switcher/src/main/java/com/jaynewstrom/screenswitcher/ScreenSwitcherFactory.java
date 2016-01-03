package com.jaynewstrom.screenswitcher;

import android.app.Activity;

public final class ScreenSwitcherFactory {

    private ScreenSwitcherFactory() {
    }

    public static ScreenSwitcher activityScreenSwitcher(Activity activity, ScreenSwitcherState state) {
        return new ActivityScreenSwitcher(activity, state);
    }
}
