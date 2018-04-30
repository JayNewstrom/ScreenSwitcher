package com.jaynewstrom.screenswitcher

/**
 * It's important that this is only implemented on objects that live outside of the [android.app.Activity] lifecycle.
 */
interface ScreenPopListener {
    /**
     * @param screen The [Screen] that is trying to be popped.
     * @return true if the pop has been consumed and the [Screen] should not be popped.
     */
    fun onScreenPop(screen: Screen): Boolean
}
