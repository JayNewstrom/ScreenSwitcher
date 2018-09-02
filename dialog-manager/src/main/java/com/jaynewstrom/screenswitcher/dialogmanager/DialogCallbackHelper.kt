package com.jaynewstrom.screenswitcher.dialogmanager

import android.app.Dialog
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.Window

/**
 * Prevents touches from getting through to a dialog when the associated activity is transitioning.
 */
internal class DialogCallbackHelper(private val isTransitioning: () -> Boolean) {
    fun bootstrap(dialog: Dialog) {
        val callback = dialog.window.callback
        dialog.window.callback = DelegatingWindowCallback(callback, isTransitioning)
    }
}

private class DelegatingWindowCallback(
    private val delegate: Window.Callback,
    private val isTransitioning: () -> Boolean
) : Window.Callback by delegate {
    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        if (event.keyCode == KeyEvent.KEYCODE_BACK && isTransitioning()) {
            return true
        }
        return delegate.dispatchKeyEvent(event)
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        return isTransitioning() || delegate.dispatchTouchEvent(event)
    }
}
