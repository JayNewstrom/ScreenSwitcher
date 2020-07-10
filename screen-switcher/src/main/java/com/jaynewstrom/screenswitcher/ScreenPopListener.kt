package com.jaynewstrom.screenswitcher

import android.view.View

/**
 * It's important that this is only implemented on objects that live outside of the [android.app.Activity] lifecycle.
 */
interface ScreenPopListener {
    /**
     * @param view The [View] that is trying to be popped.
     * @param screen The [Screen] that is trying to be popped.
     * @param popContext An opaque object that is passed from the pop source to be used by pop listeners.
     *
     * @return true if the pop has been consumed and the [Screen] should not be popped.
     */
    fun onScreenPop(view: View, screen: Screen, popContext: Any?): Boolean
}
