package com.jaynewstrom.screenswitcher

/**
 * An object that listens for the last screen pop, and notifies the [FinishCompleteHandler] when the finish is complete.
 */
interface ScreenSwitcherFinishHandler {
    /**
     * In a nested screen switcher (ie, tab bar) it's useful to never finish the screen switcher.
     * This prevents the root screen from ever being removed.
     *
     * @return true if the screen switcher should finish.
     */
    val screenSwitcherShouldFinish: Boolean
        get() = true

    /**
     * The [FinishCompleteHandler] should be stored in a field until the pop is complete (ie, the activity has been destroyed).
     * Once the finish is complete, it should call [FinishCompleteHandler.finishComplete].
     */
    fun onScreenSwitcherFinished(finishCompleteHandler: FinishCompleteHandler)

    /**
     * A callback for notifying when the screen switcher finish is complete.
     * See [ScreenSwitcherFinishHandler.onScreenSwitcherFinished].
     */
    interface FinishCompleteHandler {
        fun finishComplete()
    }
}
