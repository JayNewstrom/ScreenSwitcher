package com.jaynewstrom.screenswitcher.dialogmanager

import android.app.Dialog
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.Window
import com.jaynewstrom.screenswitcher.screenmanager.ScreenManager

/**
 * Prevents touches from getting through to a dialog when the associated activity is transitioning.
 */
internal class DialogCallbackHelper(private val screenManager: ScreenManager) {
    fun bootstrap(dialog: Dialog) {
        val callback = dialog.window!!.callback
        dialog.window!!.callback = DelegatingWindowCallback(callback, screenManager)
    }
}

private class DelegatingWindowCallback(
    private val delegate: Window.Callback,
    private val screenManager: ScreenManager
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

    private fun isTransitioning() = screenManager.screenSwitcher?.isTransitioning == true
}
