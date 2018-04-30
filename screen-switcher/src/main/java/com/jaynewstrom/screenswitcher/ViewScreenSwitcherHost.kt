package com.jaynewstrom.screenswitcher

import android.view.View
import android.view.ViewGroup

internal class ViewScreenSwitcherHost(
    private val viewGroup: ViewGroup,
    private val popHandler: ScreenSwitcherPopHandler
) : ScreenSwitcherHost {
    override fun onLastScreenPopped(popCompleteHandler: ScreenSwitcherPopHandler.PopCompleteHandler) {
        popHandler.onLastScreenPopped(popCompleteHandler)
    }

    override fun addView(view: View) = viewGroup.addView(view)

    override fun hostView() = viewGroup
}
