package com.jaynewstrom.screenswitcher

import android.view.View
import android.view.ViewGroup

internal interface ScreenSwitcherHost {
    fun onLastScreenPopped(popCompleteHandler: ScreenSwitcherPopHandler.PopCompleteHandler)

    fun addView(view: View)

    fun hostView(): ViewGroup
}
