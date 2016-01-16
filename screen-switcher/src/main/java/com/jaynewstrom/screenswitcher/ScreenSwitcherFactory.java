package com.jaynewstrom.screenswitcher;

import android.app.Activity;
import android.support.annotation.NonNull;

public final class ScreenSwitcherFactory {

    private ScreenSwitcherFactory() {
    }

    public static ScreenSwitcher activityScreenSwitcher(@NonNull Activity activity, @NonNull ScreenSwitcherState state) {
        return new ActivityScreenSwitcher(activity, state);
    }
}
