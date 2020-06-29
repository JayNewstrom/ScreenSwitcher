package com.jaynewstrom.screenswitcher

import android.view.View
import android.view.ViewGroup

internal interface ScreenSwitcherHost {
    fun onScreenSwitcherFinished(finishCompleteHandler: ScreenSwitcherFinishHandler.FinishCompleteHandler)

    fun addView(view: View)

    fun hostView(): ViewGroup
}
