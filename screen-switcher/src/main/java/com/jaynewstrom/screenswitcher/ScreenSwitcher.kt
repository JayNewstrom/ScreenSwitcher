package com.jaynewstrom.screenswitcher

import androidx.annotation.IntRange

/**
 * This object lives inside of the [android.app.Activity] lifecycle, and shouldn't be referenced outside of it to ensure leaks
 * don't occur.
 */
interface ScreenSwitcher {
    /**
     * @return true if there is a transition being executed.
     */
    val isTransitioning: Boolean

    /**
     * Add the given [Screen].
     */
    fun push(screen: Screen)

    /**
     * Will try to pop the top `numberToPop` [Screen]s.
     *
     * If there is a [ScreenPopListener] that overrides one of the [Screen]s being popped, it will pop up until that point.
     */
    fun pop(@IntRange(from = 1) numberToPop: Int)

    /**
     * Will try to pop the `numberToPop` [Screen]s, then transition to the last [Screen] in `screens`.
     *
     * If there is a [ScreenPopListener] that overrides one of the [Screen]s being popped, it will pop up until that point
     * without pushing the `screens`.
     */
    fun replaceScreensWith(@IntRange(from = 1) numberToPop: Int, newScreens: List<Screen>)
}
