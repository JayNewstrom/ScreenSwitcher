package com.jaynewstrom.screenswitcher

import android.view.View
import android.view.ViewGroup

internal class ViewScreenSwitcherHost(
    private val viewGroup: ViewGroup,
    private val finishHandler: ScreenSwitcherFinishHandler
) : ScreenSwitcherHost {
    override fun onScreenSwitcherFinished(finishCompleteHandler: ScreenSwitcherFinishHandler.FinishCompleteHandler) {
        finishHandler.onScreenSwitcherFinished(finishCompleteHandler)
    }

    override fun addView(view: View) = viewGroup.addView(view)

    override fun hostView() = viewGroup
}
