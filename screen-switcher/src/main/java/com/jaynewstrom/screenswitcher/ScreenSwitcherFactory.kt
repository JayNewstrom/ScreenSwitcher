package com.jaynewstrom.screenswitcher

import android.app.Activity
import android.view.ViewGroup

object ScreenSwitcherFactory {
    /**
     * Creates a [ScreenSwitcher] that uses [Activity.addContentView] to display a [Screen].
     */
    @JvmStatic fun activityScreenSwitcher(
        activity: Activity,
        state: ScreenSwitcherState,
        popHandler: ScreenSwitcherPopHandler
    ): ScreenSwitcher {
        state.validate()
        return RealScreenSwitcher(activity, state, ActivityScreenSwitcherHost(activity, popHandler))
    }

    /**
     * Creates a [ScreenSwitcher] that uses [ViewGroup.addView] to display a [Screen].
     */
    @JvmStatic fun viewScreenSwitcher(
        viewGroup: ViewGroup,
        state: ScreenSwitcherState,
        popHandler: ScreenSwitcherPopHandler
    ): ScreenSwitcher {
        state.validate()
        return RealScreenSwitcher(viewGroup.context, state, ViewScreenSwitcherHost(viewGroup, popHandler))
    }

    @Suppress("NOTHING_TO_INLINE") // Deduplicate code, no need for a method jump.
    @JvmStatic private inline fun ScreenSwitcherState.validate() {
        checkArgument(screens.isNotEmpty()) { "state needs screens in order to initialize a ScreenSwitcher" }
    }
}
