package com.jaynewstrom.screenswitcher

import android.view.View

/**
 * It's important that this is only implemented on objects that live outside of the [android.app.Activity] lifecycle.
 */
interface ScreenPopListener {
    /**
     * @param view The [View] associated with the screen that is trying to be popped.
     * @param popContext An opaque object that is passed from the pop source to be used by pop listeners.
     *
     * @return true if the pop has been consumed and the [Screen] should not be popped.
     */
    fun onScreenPop(view: View, popContext: Any?): Boolean
}
