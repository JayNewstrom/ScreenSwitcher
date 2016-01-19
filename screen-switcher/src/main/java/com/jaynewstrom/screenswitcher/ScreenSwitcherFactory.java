package com.jaynewstrom.screenswitcher;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import static com.jaynewstrom.screenswitcher.Preconditions.checkArgument;
import static com.jaynewstrom.screenswitcher.Preconditions.checkNotNull;

public final class ScreenSwitcherFactory {

    private ScreenSwitcherFactory() {
    }

    /**
     * Creates a {@link ScreenSwitcher} that uses {@link Activity#addContentView(View, LayoutParams)} to display a {@link Screen}.
     */
    public static ScreenSwitcher activityScreenSwitcher(@NonNull Activity activity, @NonNull ScreenSwitcherState state) {
        checkNotNull(activity, "activity == null");
        checkNotNull(state, "state == null");
        checkArgument(state.getScreens().size() >= 1, "state needs screens in order to initialize a ScreenSwitcher");
        return new ActivityScreenSwitcher(activity, state);
    }
}
