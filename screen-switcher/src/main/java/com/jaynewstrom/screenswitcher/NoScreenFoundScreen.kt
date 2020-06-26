package com.jaynewstrom.screenswitcher

import android.view.View
import android.view.ViewGroup

/**
 * A dummy screen to prevent exceptions from being thrown for APIs that expect a screen.
 * See [ScreenSwitcherConfig.failSilentlyWhenPossible].
 */
internal object NoScreenFoundScreen : Screen {
    override fun createView(hostView: ViewGroup, screenSwitcherState: ScreenSwitcherState): View {
        throw UnsupportedOperationException("createView should not be called on NoScreenFoundScreen.")
    }

    override fun bindView(view: View) {
        throw UnsupportedOperationException("bindView should not be called on NoScreenFoundScreen.")
    }

    override fun destroyScreen(associatedView: View) {
        throw UnsupportedOperationException("destroyScreen should not be called on NoScreenFoundScreen.")
    }

    override fun transition(): Screen.Transition {
        throw UnsupportedOperationException("transition should not be called on NoScreenFoundScreen.")
    }
}
