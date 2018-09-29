package com.jaynewstrom.screenswitcher

import android.view.View

/**
 * It's important that this is only implemented on objects that live outside of the [android.app.Activity] lifecycle.
 */
interface ScreenPopListener {
    /**
     * @param view The [View] that is trying to be popped.
     * @param screen The [Screen] that is trying to be popped.
     * @return true if the pop has been consumed and the [Screen] should not be popped.
     */
    fun onScreenPop(view: View, screen: Screen): Boolean
}
