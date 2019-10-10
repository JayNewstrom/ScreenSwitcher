package com.jaynewstrom.screenswitcher

import android.content.Context
import android.view.View
import android.view.ViewGroup

/**
 * A dummy screen to prevent exceptions from being thrown for APIs that expect a screen.
 * See [ScreenSwitcherConfig.failSilentlyWhenPossible].
 */
object NoScreenFoundScreen : Screen {
    override fun createView(context: Context, hostView: ViewGroup): View {
        throw UnsupportedOperationException("createView should not be called on NoScreenFoundScreen.")
    }

    override fun destroyScreen(viewToDestroy: View) {
        throw UnsupportedOperationException("destroyScreen should not be called on NoScreenFoundScreen.")
    }

    override fun transition(): ScreenTransition {
        throw UnsupportedOperationException("transition should not be called on NoScreenFoundScreen.")
    }
}
