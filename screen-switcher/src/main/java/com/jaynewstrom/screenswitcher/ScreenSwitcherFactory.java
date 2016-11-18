package com.jaynewstrom.screenswitcher;

import android.app.Activity;
import android.support.annotation.NonNull;
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
    public static ScreenSwitcher activityScreenSwitcher(@NonNull Activity activity, @NonNull ScreenSwitcherState state) {
        checkNotNull(activity, "activity == null");
        validateState(state);
        return new RealScreenSwitcher(activity, state, new ActivityScreenSwitcherHost(activity));
    }

    /**
     * Creates a {@link ScreenSwitcher} that uses {@link ViewGroup#addView(View)} to display a {@link Screen}.
     */
    public static ScreenSwitcher viewScreenSwitcher(@NonNull ViewGroup viewGroup, @NonNull ScreenSwitcherState state,
            @NonNull ViewScreenSwitcherHandler handler) {
        checkNotNull(viewGroup, "viewGroup == null");
        checkNotNull(handler, "handler == null");
        validateState(state);
        return new RealScreenSwitcher(viewGroup.getContext(), state, new ViewScreenSwitcherHost(viewGroup, handler));
    }

    private static void validateState(ScreenSwitcherState state) {
        checkNotNull(state, "state == null");
        checkArgument(state.getScreens().size() >= 1, "state needs screens in order to initialize a ScreenSwitcher");
    }
}
