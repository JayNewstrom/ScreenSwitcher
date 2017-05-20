package com.jaynewstrom.screenswitcher;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

import static com.jaynewstrom.screenswitcher.Preconditions.checkArgument;
import static com.jaynewstrom.screenswitcher.Preconditions.checkNotNull;

public final class ScreenSwitcherFactory {

    private ScreenSwitcherFactory() {
    }

    /**
     * Creates a {@link ScreenSwitcher} that uses {@link Activity#addContentView(View, LayoutParams)} to display a {@link Screen}.
     */
    public static ScreenSwitcher activityScreenSwitcher(Activity activity, ScreenSwitcherState state,
            ScreenSwitcherPopHandler popHandler) {
        checkNotNull(activity, "activity == null");
        validateState(state);
        checkNotNull(popHandler, "popHandler == null");
        return new RealScreenSwitcher(activity, state, new ActivityScreenSwitcherHost(activity, popHandler));
    }

    /**
     * Creates a {@link ScreenSwitcher} that uses {@link ViewGroup#addView(View)} to display a {@link Screen}.
     */
    public static ScreenSwitcher viewScreenSwitcher(ViewGroup viewGroup, ScreenSwitcherState state,
            ScreenSwitcherPopHandler popHandler) {
        checkNotNull(viewGroup, "viewGroup == null");
        checkNotNull(popHandler, "popHandler == null");
        validateState(state);
        return new RealScreenSwitcher(viewGroup.getContext(), state, new ViewScreenSwitcherHost(viewGroup, popHandler));
    }

    private static void validateState(ScreenSwitcherState state) {
        checkNotNull(state, "state == null");
        checkArgument(state.getScreens().size() >= 1, "state needs screens in order to initialize a ScreenSwitcher");
    }
}
