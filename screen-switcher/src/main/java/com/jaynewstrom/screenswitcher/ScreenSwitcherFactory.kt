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
        finishHandler: ScreenSwitcherFinishHandler
    ): ScreenSwitcher {
        return RealScreenSwitcher(state, ActivityScreenSwitcherHost(activity, finishHandler))
    }

    /**
     * Creates a [ScreenSwitcher] that uses [ViewGroup.addView] to display a [Screen].
     */
    @JvmStatic fun viewScreenSwitcher(
        viewGroup: ViewGroup,
        state: ScreenSwitcherState,
        finishHandler: ScreenSwitcherFinishHandler
    ): ScreenSwitcher {
        return RealScreenSwitcher(state, ViewScreenSwitcherHost(viewGroup, finishHandler))
    }
}
