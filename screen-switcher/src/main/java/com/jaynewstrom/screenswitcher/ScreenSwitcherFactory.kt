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
        return RealScreenSwitcher(state, ActivityScreenSwitcherHost(activity, popHandler))
    }

    /**
     * Creates a [ScreenSwitcher] that uses [ViewGroup.addView] to display a [Screen].
     */
    @JvmStatic fun viewScreenSwitcher(
        viewGroup: ViewGroup,
        state: ScreenSwitcherState,
        popHandler: ScreenSwitcherPopHandler
    ): ScreenSwitcher {
        return RealScreenSwitcher(state, ViewScreenSwitcherHost(viewGroup, popHandler))
    }
}
