package com.jaynewstrom.screenswitcher

/**
 * A global screen switcher lifecycle listener.
 */
interface ScreenLifecycleListener {
    /**
     * Called when a screen is added. Symmetric with [onScreenRemoved].
     */
    fun onScreenAdded(screen: Screen)

    /**
     * Called when a screen is removed. Symmetric with [onScreenAdded].
     */
    fun onScreenRemoved(screen: Screen)

    /**
     * Called when a screen becomes active. Only a single screen is "active" at a time.
     * Symmetric with [onScreenBecameInactive].
     */
    fun onScreenBecameActive(screen: Screen)

    /**
     * Called when a screen becomes inactive. Only a single screen is "active" at a time.
     * Symmetric with [onScreenBecameActive].
     */
    fun onScreenBecameInactive(screen: Screen)
}
