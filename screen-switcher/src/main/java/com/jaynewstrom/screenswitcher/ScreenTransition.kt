package com.jaynewstrom.screenswitcher

import android.view.View

/**
 * Performs the transition between screens.
 */
interface ScreenTransition {
    /**
     * The transition that occurs when adding a screen. See [ScreenSwitcher.push].
     *
     * @param foregroundView The view that will become the focus after than transition is complete.
     * @param backgroundView The view that was the focus before the transition started.
     * @param onTransitionCompleted A block of code that must be called after the transition is complete.
     */
    fun transitionIn(foregroundView: View, backgroundView: View, onTransitionCompleted: Runnable)

    /**
     * The transition that occurs when removing a screen. See [ScreenSwitcher.pop].
     *
     * @param foregroundView The view that was the focus before the transition started, and should be animated away.
     * @param backgroundView The view that will become the focus after the transition is complete.
     * @param onTransitionCompleted A block of code that must be called after the transition is complete.
     */
    fun transitionOut(foregroundView: View, backgroundView: View, onTransitionCompleted: Runnable)
}
