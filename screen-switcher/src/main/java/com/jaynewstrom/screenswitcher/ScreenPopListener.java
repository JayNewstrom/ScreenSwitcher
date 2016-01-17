package com.jaynewstrom.screenswitcher;

import android.support.annotation.NonNull;

/**
 * It's important that this is only implemented on objects that live outside of the {@link android.app.Activity} lifecycle.
 */
public interface ScreenPopListener {

    /**
     * @param screen The {@link Screen} that is trying to be popped.
     * @return true if the pop has been consumed and the {@link Screen} should not be popped.
     */
    boolean onScreenPop(@NonNull Screen screen);
}
