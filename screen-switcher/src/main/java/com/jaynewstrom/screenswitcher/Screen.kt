package com.jaynewstrom.screenswitcher

import android.view.View
import android.view.ViewGroup

/**
 * An object that describes what the user would see on the display.
 * This object is designed to persist in memory inside [ScreenSwitcherState] when an [android.app.Activity]
 * configuration change occurs.
 */
interface Screen {
    /**
     * Creates the view associated with the [Screen].
     * Note, this can be called multiple times.
     */
    fun createView(hostView: ViewGroup, screenSwitcherState: ScreenSwitcherState): View

    /**
     * Bind the view created via [createView] with the data needed for the screen.
     * Always called in symmetry with [createView].
     */
    fun bindView(view: View)

    /**
     * Will be called when the [Screen] is removed from the [ScreenSwitcher] for good.
     * Note, this will only be called once.
     */
    fun destroyScreen(associatedView: View)

    /**
     * The [Transition] to perform when calling [ScreenSwitcher] methods.
     * Note, this is only called when transitioning between two [Screen]s.
     */
    fun transition(): Transition

    /**
     * Performs the transition between screens.
     */
    interface Transition {
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
}
