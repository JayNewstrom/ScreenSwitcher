package com.jaynewstrom.screenswitcher

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager

internal class ActivityScreenSwitcherHost(
    private val activity: Activity,
    override val finishHandler: ScreenSwitcherFinishHandler
) : ScreenSwitcherHost {
    override fun addView(view: View) = activity.addContentView(view, WindowManager.LayoutParams())

    override fun hostView(): ViewGroup = activity.findViewById(android.R.id.content)
}
