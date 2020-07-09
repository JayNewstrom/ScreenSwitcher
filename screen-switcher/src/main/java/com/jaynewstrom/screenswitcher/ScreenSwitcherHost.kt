package com.jaynewstrom.screenswitcher

import android.view.View
import android.view.ViewGroup

internal interface ScreenSwitcherHost {
    val finishHandler: ScreenSwitcherFinishHandler

    fun addView(view: View)

    fun hostView(): ViewGroup
}
