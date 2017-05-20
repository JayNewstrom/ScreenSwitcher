package com.jaynewstrom.screenswitcher;

/**
 * An object that listens for the last screen pop, and notifies the {@link PopCompleteHandler} when the last pop is complete.
 */
public interface ScreenSwitcherPopHandler {
    /**
     * The PopCompleteHandler should be stored in a field until the pop is complete (ie, the activity has been destroyed).
     * Once the pop is complete, it should call {@link PopCompleteHandler#popComplete()}.
     */
    void onLastScreenPopped(PopCompleteHandler popCompleteHandler);

    /**
     * A callback for notifying when the last pop is complete.
     * See {@link ScreenSwitcherPopHandler#onLastScreenPopped(PopCompleteHandler)}.
     */
    interface PopCompleteHandler {
        void popComplete();
    }
}
