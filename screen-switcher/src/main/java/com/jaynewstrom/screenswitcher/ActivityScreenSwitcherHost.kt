package com.jaynewstrom.screenswitcher

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager

internal class ActivityScreenSwitcherHost(
    private val activity: Activity,
    private val finishHandler: ScreenSwitcherFinishHandler
) : ScreenSwitcherHost {
    override fun onScreenSwitcherFinished(finishCompleteHandler: ScreenSwitcherFinishHandler.FinishCompleteHandler) {
        finishHandler.onScreenSwitcherFinished(finishCompleteHandler)
        activity.finish()
    }

    override fun addView(view: View) = activity.addContentView(view, WindowManager.LayoutParams())

    override fun hostView(): ViewGroup = activity.findViewById(android.R.id.content)
}
