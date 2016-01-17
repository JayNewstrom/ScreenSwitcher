package com.jaynewstrom.screenswitcher;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

public final class ScreenSwitcherFactory {

    private ScreenSwitcherFactory() {
    }

    /**
     * Creates a {@link ScreenSwitcher} that uses {@link Activity#addContentView(View, LayoutParams)} to display a {@link Screen}.
     */
    public static ScreenSwitcher activityScreenSwitcher(@NonNull Activity activity, @NonNull ScreenSwitcherState state) {
        return new ActivityScreenSwitcher(activity, state);
    }
}
