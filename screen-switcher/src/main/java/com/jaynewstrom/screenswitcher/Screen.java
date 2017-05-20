package com.jaynewstrom.screenswitcher;

import android.content.Context;
import android.view.View;

/**
 * An object that describes what the user would see on the display.
 * This object is designed to persist in memory inside {@link ScreenSwitcherState} when an {@link android.app.Activity}
 * configuration change occurs.
 */
public interface Screen {

    /**
     * Creates the view associated with the {@link Screen}.
     * Note, this can be called multiple times.
     */
    View createView(Context context);

    /**
     * Will be called when the {@link Screen} is removed from the {@link ScreenSwitcher} for good.
     * Note, this will only be called once.
     */
    void destroyScreen(View viewToDestroy);

    /**
     * The {@link ScreenTransition} to perform when calling {@link ScreenSwitcher} methods.
     * Note, this is only called when transitioning between two {@link Screen}s.
     */
    ScreenTransition transition();
}
