package com.jaynewstrom.screenswitcher;

/**
 * A global screen switcher lifecycle listener.
 */
public interface ScreenLifecycleListener {
    /**
     * Called when a screen is added. Symmetric with {@link #onScreenRemoved(Screen)}.
     */
    void onScreenAdded(Screen screen);

    /**
     * Called when a screen is removed. Symmetric with {@link #onScreenAdded(Screen)}.
     */
    void onScreenRemoved(Screen screen);

    /**
     * Called when a screen becomes active. Only a single screen is "active" at a time.
     * Symmetric with {@link #onScreenBecameInactive(Screen)}.
     */
    void onScreenBecameActive(Screen screen);

    /**
     * Called when a screen becomes inactive. Only a single screen is "active" at a time.
     * Symmetric with {@link #onScreenBecameActive(Screen)}.
     */
    void onScreenBecameInactive(Screen screen);
}
