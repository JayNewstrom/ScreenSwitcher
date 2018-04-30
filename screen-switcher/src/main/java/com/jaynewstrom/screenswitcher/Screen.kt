package com.jaynewstrom.screenswitcher

import android.content.Context
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
    fun createView(context: Context, hostView: ViewGroup): View

    /**
     * Will be called when the [Screen] is removed from the [ScreenSwitcher] for good.
     * Note, this will only be called once.
     */
    fun destroyScreen(viewToDestroy: View)

    /**
     * The [ScreenTransition] to perform when calling [ScreenSwitcher] methods.
     * Note, this is only called when transitioning between two [Screen]s.
     */
    fun transition(): ScreenTransition
}