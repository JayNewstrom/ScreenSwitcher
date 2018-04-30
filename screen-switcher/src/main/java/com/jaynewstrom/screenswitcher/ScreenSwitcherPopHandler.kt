package com.jaynewstrom.screenswitcher

/**
 * An object that listens for the last screen pop, and notifies the [PopCompleteHandler] when the last pop is complete.
 */
interface ScreenSwitcherPopHandler {
    /**
     * The PopCompleteHandler should be stored in a field until the pop is complete (ie, the activity has been destroyed).
     * Once the pop is complete, it should call [PopCompleteHandler.popComplete].
     */
    fun onLastScreenPopped(popCompleteHandler: PopCompleteHandler)

    /**
     * A callback for notifying when the last pop is complete.
     * See [ScreenSwitcherPopHandler.onLastScreenPopped].
     */
    interface PopCompleteHandler {
        fun popComplete()
    }
}
